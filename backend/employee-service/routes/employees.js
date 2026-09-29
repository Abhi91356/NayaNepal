const db = require('../database');
const auditLog = require('../auditLog');

// Get all employees for user
exports.getEmployees = async (req, res) => {
    try {
        const { search = '', employmentType = '' } = req.query;

        let query = `
            SELECT e.*, o.office_name, o.district, o.office_address,
                   a.company_name, a.country, a.is_verified, a.is_trusted, a.trust_score
            FROM employees e
            LEFT JOIN offices o ON e.id = o.employee_id
            LEFT JOIN abroad_companies a ON e.id = a.employee_id
            WHERE e.user_id = $1 AND e.deleted_at IS NULL
        `;

        const values = [req.user.userId];
        let placeholderIndex = 2;

        if (search) {
            query += ` AND (e.full_name ILIKE $${placeholderIndex} OR e.nepali_name ILIKE $${placeholderIndex} OR o.office_name ILIKE $${placeholderIndex})`;
            values.push(`%${search}%`);
            placeholderIndex++;
        }

        if (employmentType) {
            query += ` AND e.employment_type = $${placeholderIndex}`;
            values.push(employmentType);
            placeholderIndex++;
        }

        query += ' ORDER BY e.created_at DESC';

        const { rows: employees } = await db.query(query, values);

        // Format for response
        const formattedEmployees = employees.map(emp => {
            const formatted = { ...emp };
            if (emp.employment_type === 'abroad' && emp.company_name) {
                formatted.abroadCompany = {
                    companyName: emp.company_name,
                    country: emp.country,
                    isVerified: emp.is_verified,
                    isTrusted: emp.is_trusted,
                    trustScore: emp.trust_score
                };
            }
            if (emp.employment_type === 'domestic' && emp.office_name) {
                formatted.office = {
                    officeName: emp.office_name,
                    district: emp.district,
                    officeAddress: emp.office_address
                };
            }
            return formatted;
        });

        return res.status(200).json({
            success: true,
            data: formattedEmployees
        });
    } catch (error) {
        console.error('Get employees error:', error);
        return res.status(500).json({
            error: { code: 'SERVER_ERROR', message: 'Failed to fetch employees' }
        });
    }
};

// Create new employee
exports.createEmployee = async (req, res) => {
    const client = await db.pool.connect();
    try {
        const {
            fullName,
            nepaliName,
            email,
            phone,
            designation,
            department,
            employmentType,
            officeInfo,
            abroadCompanyInfo
        } = req.body;

        if (!fullName || !phone || !employmentType) {
            return res.status(400).json({
                error: { code: 'INVALID_INPUT', message: 'Full name, phone, and employment type required' }
            });
        }

        await client.query('BEGIN');

        const employeeQuery = `
            INSERT INTO employees (
                user_id, full_name, nepali_name, email, phone, designation, department,
                employment_type, is_verified
            ) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
            RETURNING id
        `;

        const employeeRes = await client.query(employeeQuery, [
            req.user.userId,
            fullName,
            nepaliName || null,
            email || null,
            phone,
            designation || null,
            department || null,
            employmentType,
            false
        ]);

        const employeeId = employeeRes.rows[0].id;

        if (employmentType === 'domestic' && officeInfo) {
            const officeQuery = `
                INSERT INTO offices (
                    employee_id, office_name, office_address, office_phone,
                    district, region, office_type, floor_number, building_name
                ) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
            `;

            await client.query(officeQuery, [
                employeeId,
                officeInfo.officeName,
                officeInfo.officeAddress || null,
                officeInfo.officePhone || null,
                officeInfo.district || null,
                officeInfo.region || null,
                officeInfo.officeType || 'private',
                officeInfo.floorNumber || null,
                officeInfo.buildingName || null
            ]);
        }

        if (employmentType === 'abroad' && abroadCompanyInfo) {
            const companyQuery = `
                INSERT INTO abroad_companies (
                    employee_id, company_name, company_address, country, company_phone,
                    company_website, industry, is_verified, is_trusted, trust_score,
                    verification_source
                ) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)
            `;

            await client.query(companyQuery, [
                employeeId,
                abroadCompanyInfo.companyName,
                abroadCompanyInfo.companyAddress || null,
                abroadCompanyInfo.country,
                abroadCompanyInfo.companyPhone || null,
                abroadCompanyInfo.website || null,
                abroadCompanyInfo.industry || null,
                abroadCompanyInfo.isVerified || false,
                abroadCompanyInfo.isTrusted || false,
                abroadCompanyInfo.trustScore || 0,
                abroadCompanyInfo.verificationSource || 'self_reported'
            ]);
        }

        await client.query('COMMIT');

        await auditLog.record({
            action: 'EMPLOYEE_CREATED',
            userId: req.user.userId,
            resourceType: 'employee',
            resourceId: employeeId,
            ip: req.ip,
            status: 'success'
        });

        return res.status(201).json({
            success: true,
            data: { id: employeeId, fullName, employmentType, createdAt: new Date().toISOString() }
        });
    } catch (error) {
        await client.query('ROLLBACK');
        console.error('Create employee error:', error);
        return res.status(500).json({
            error: { code: 'SERVER_ERROR', message: 'Failed to create employee' }
        });
    } finally {
        client.release();
    }
};

// Get employee details
exports.getEmployeeDetails = async (req, res) => {
    try {
        const { employeeId } = req.params;

        const query = `
            SELECT e.*, o.office_name, o.district, o.office_address, o.office_phone,
                   o.region, o.office_type, o.floor_number, o.building_name,
                   a.company_name, a.country, a.company_address, a.company_phone,
                   a.company_website, a.industry, a.is_verified, a.is_trusted,
                   a.trust_score, a.verification_source, a.verification_date
            FROM employees e
            LEFT JOIN offices o ON e.id = o.employee_id
            LEFT JOIN abroad_companies a ON e.id = a.employee_id
            WHERE e.id = $1 AND e.user_id = $2 AND e.deleted_at IS NULL
        `;

        const { rows: employees } = await db.query(query, [employeeId, req.user.userId]);

        if (employees.length === 0) {
            return res.status(404).json({
                error: { code: 'EMPLOYEE_NOT_FOUND', message: 'Employee not found' }
            });
        }

        const employee = employees[0];

        if (employee.employment_type === 'domestic' && employee.office_name) {
            employee.office = {
                officeName: employee.office_name,
                officeAddress: employee.office_address,
                district: employee.district,
                region: employee.region,
                officeType: employee.office_type,
                floorNumber: employee.floor_number,
                buildingName: employee.building_name,
                officePhone: employee.office_phone
            };
        }

        if (employee.employment_type === 'abroad' && employee.company_name) {
            employee.abroadCompany = {
                companyName: employee.company_name,
                country: employee.country,
                companyAddress: employee.company_address,
                companyPhone: employee.company_phone,
                companyWebsite: employee.company_website,
                industry: employee.industry,
                isVerified: employee.is_verified,
                isTrusted: employee.is_trusted,
                trustScore: employee.trust_score,
                verificationSource: employee.verification_source,
                verificationDate: employee.verification_date
            };
        }

        return res.status(200).json({ success: true, data: employee });
    } catch (error) {
        console.error('Get employee details error:', error);
        return res.status(500).json({
            error: { code: 'SERVER_ERROR', message: 'Failed to fetch employee details' }
        });
    }
};

// Update employee
exports.updateEmployee = async (req, res) => {
    try {
        const { employeeId } = req.params;
        const { fullName, nepaliName, designation, department } = req.body;

        const query = `
            UPDATE employees
            SET full_name = $1, nepali_name = $2, designation = $3, department = $4, updated_at = NOW()
            WHERE id = $5 AND user_id = $6 AND deleted_at IS NULL
        `;

        const result = await db.query(query, [fullName, nepaliName, designation, department, employeeId, req.user.userId]);

        if (result.rowCount === 0) {
            return res.status(404).json({
                error: { code: 'EMPLOYEE_NOT_FOUND', message: 'Employee not found' }
            });
        }

        await auditLog.record({
            action: 'EMPLOYEE_UPDATED',
            userId: req.user.userId,
            resourceType: 'employee',
            resourceId: employeeId,
            ip: req.ip,
            status: 'success'
        });

        return res.status(200).json({ success: true, message: 'Employee updated successfully' });
    } catch (error) {
        console.error('Update employee error:', error);
        return res.status(500).json({
            error: { code: 'SERVER_ERROR', message: 'Failed to update employee' }
        });
    }
};

// Delete employee
exports.deleteEmployee = async (req, res) => {
    try {
        const { employeeId } = req.params;

        const query = `
            UPDATE employees
            SET deleted_at = NOW()
            WHERE id = $1 AND user_id = $2
        `;

        const result = await db.query(query, [employeeId, req.user.userId]);

        if (result.rowCount === 0) {
            return res.status(404).json({
                error: { code: 'EMPLOYEE_NOT_FOUND', message: 'Employee not found' }
            });
        }

        await auditLog.record({
            action: 'EMPLOYEE_DELETED',
            userId: req.user.userId,
            resourceType: 'employee',
            resourceId: employeeId,
            ip: req.ip,
            status: 'success'
        });

        return res.status(200).json({ success: true, message: 'Employee deleted successfully' });
    } catch (error) {
        console.error('Delete employee error:', error);
        return res.status(500).json({
            error: { code: 'SERVER_ERROR', message: 'Failed to delete employee' }
        });
    }
};

// Verify abroad company (admin only)
exports.verifyAbroadCompany = async (req, res) => {
    const client = await db.pool.connect();
    try {
        const { companyId } = req.params;
        const { isVerified, isTrusted, trustScore, verificationSource } = req.body;

        await client.query('BEGIN');

        const query = `
            UPDATE abroad_companies
            SET is_verified = $1, is_trusted = $2, trust_score = $3,
                verification_source = $4, verification_date = NOW(), updated_at = NOW()
            WHERE id = $5
            RETURNING id
        `;

        const result = await client.query(query, [
            isVerified,
            isTrusted,
            trustScore,
            verificationSource,
            companyId
        ]);

        if (result.rowCount === 0) {
            await client.query('ROLLBACK');
            return res.status(404).json({
                error: { code: 'COMPANY_NOT_FOUND', message: 'Company not found' }
            });
        }

        // Log verification
        await client.query(
            'INSERT INTO verification_logs (company_id, verified_by, verification_type, verification_method, notes, status) VALUES ($1, $2, $3, $4, $5, $6)',
            [companyId, req.user.userId, isVerified ? 're_verification' : 'initial', 'document_check', `Verified with trust score: ${trustScore}`, 'approved']
        );

        await client.query('COMMIT');

        return res.status(200).json({ success: true, message: 'Company verification updated successfully' });
    } catch (error) {
        await client.query('ROLLBACK');
        console.error('Verify company error:', error);
        return res.status(500).json({ error: 'Failed to verify company' });
    } finally {
        client.release();
    }
};
