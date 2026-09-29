const db = require('../database');
const auditLog = require('../auditLog');
const { body, validationResult } = require('express-validator');

// Create new ledger account
exports.createAccount = async (req, res) => {
    try {
        const { name, type, description, initialBalance } = req.body;

        // Validate input
        if (!name || !type) {
            return res.status(400).json({
                error: { code: 'INVALID_INPUT', message: 'Name and type required' }
            });
        }

        const validTypes = ['CASH', 'BANK', 'ASSET', 'LIABILITY', 'INCOME', 'EXPENSE'];
        if (!validTypes.includes(type)) {
            return res.status(400).json({
                error: { code: 'INVALID_TYPE', message: 'Invalid account type' }
            });
        }

        // Create account
        const query = `
            INSERT INTO accounts (
                user_id, name, type, description, initial_balance, balance
            ) VALUES ($1, $2, $3, $4, $5, $6)
            RETURNING id
        `;

        const result = await db.query(query, [
            req.user.userId,
            name,
            type,
            description || '',
            initialBalance || 0,
            initialBalance || 0
        ]);

        const accountId = result.rows[0].id;

        // Log audit
        await auditLog.record({
            action: 'ACCOUNT_CREATED',
            userId: req.user.userId,
            resourceType: 'account',
            resourceId: accountId,
            ip: req.ip,
            status: 'success'
        });

        return res.status(201).json({
            success: true,
            data: {
                id: accountId,
                name,
                type,
                description,
                initialBalance: initialBalance || 0,
                balance: initialBalance || 0,
                createdAt: new Date().toISOString()
            }
        });
    } catch (error) {
        console.error('Create account error:', error);
        return res.status(500).json({
            error: { code: 'SERVER_ERROR', message: 'Failed to create account' }
        });
    }
};

// Get all accounts for user
exports.getAccounts = async (req, res) => {
    try {
        const query = `
            SELECT id, name, type, description, initial_balance as "initialBalance", balance, created_at as "createdAt"
            FROM accounts
            WHERE user_id = $1
            ORDER BY created_at DESC
        `;

        const { rows } = await db.query(query, [req.user.userId]);

        return res.status(200).json({
            success: true,
            data: rows
        });
    } catch (error) {
        console.error('Get accounts error:', error);
        return res.status(500).json({
            error: { code: 'SERVER_ERROR', message: 'Failed to fetch accounts' }
        });
    }
};

// Update account
exports.updateAccount = async (req, res) => {
    try {
        const { accountId } = req.params;
        const { name, type, description } = req.body;

        const query = `
            UPDATE accounts
            SET name = $1, type = $2, description = $3, updated_at = NOW()
            WHERE id = $4 AND user_id = $5
        `;

        const result = await db.query(query, [
            name,
            type,
            description || '',
            accountId,
            req.user.userId
        ]);

        if (result.rowCount === 0) {
            return res.status(404).json({
                error: { code: 'ACCOUNT_NOT_FOUND', message: 'Account not found' }
            });
        }

        // Log audit
        await auditLog.record({
            action: 'ACCOUNT_UPDATED',
            userId: req.user.userId,
            resourceType: 'account',
            resourceId: accountId,
            ip: req.ip,
            status: 'success'
        });

        return res.status(200).json({
            success: true,
            message: 'Account updated successfully'
        });
    } catch (error) {
        console.error('Update account error:', error);
        return res.status(500).json({
            error: { code: 'SERVER_ERROR', message: 'Failed to update account' }
        });
    }
};

// Delete account
exports.deleteAccount = async (req, res) => {
    try {
        const { accountId } = req.params;

        const query = `
            DELETE FROM accounts
            WHERE id = $1 AND user_id = $2
        `;

        const result = await db.query(query, [accountId, req.user.userId]);

        if (result.rowCount === 0) {
            return res.status(404).json({
                error: { code: 'ACCOUNT_NOT_FOUND', message: 'Account not found' }
            });
        }

        // Log audit
        await auditLog.record({
            action: 'ACCOUNT_DELETED',
            userId: req.user.userId,
            resourceType: 'account',
            resourceId: accountId,
            ip: req.ip,
            status: 'success'
        });

        return res.status(200).json({
            success: true,
            message: 'Account deleted successfully'
        });
    } catch (error) {
        console.error('Delete account error:', error);
        return res.status(500).json({
            error: { code: 'SERVER_ERROR', message: 'Failed to delete account' }
        });
    }
};

// Record transaction
exports.recordTransaction = async (req, res) => {
    const client = await db.pool.connect();
    try {
        const { fromAccountId, toAccountId, amount, type, description, date } = req.body;

        // Validate
        if (!fromAccountId || !toAccountId || !amount || amount <= 0) {
            return res.status(400).json({
                error: { code: 'INVALID_INPUT', message: 'Invalid transaction data' }
            });
        }

        const transactionTypes = ['SALE', 'PURCHASE', 'PAYMENT', 'RECEIPT', 'EXPENSE', 'TRANSFER'];
        if (!transactionTypes.includes(type)) {
            return res.status(400).json({
                error: { code: 'INVALID_TYPE', message: 'Invalid transaction type' }
            });
        }

        await client.query('BEGIN');

        // Verify accounts exist
        const fromAccountRes = await client.query(
            'SELECT * FROM accounts WHERE id = $1 AND user_id = $2 FOR UPDATE',
            [fromAccountId, req.user.userId]
        );

        const toAccountRes = await client.query(
            'SELECT * FROM accounts WHERE id = $1 AND user_id = $2 FOR UPDATE',
            [toAccountId, req.user.userId]
        );

        if (fromAccountRes.rowCount === 0 || toAccountRes.rowCount === 0) {
            await client.query('ROLLBACK');
            return res.status(404).json({
                error: { code: 'ACCOUNT_NOT_FOUND', message: 'One or both accounts not found' }
            });
        }

        // Calculate tax
        const taxRate = type === 'SALE' || type === 'PURCHASE' ? 0.13 : 0; // 13% VAT
        const taxAmount = amount * taxRate;
        const netAmount = amount - taxAmount;

        // Update account balances
        const updateQuery = `
            UPDATE accounts
            SET balance = balance + $1, updated_at = NOW()
            WHERE id = $2 AND user_id = $3
        `;

        // Debit from account logic
        let fromUpdateAmount, toUpdateAmount;
        if (type === 'SALE' || type === 'RECEIPT' || type === 'TRANSFER') {
            fromUpdateAmount = netAmount;
        } else {
            fromUpdateAmount = -netAmount;
        }

        // Credit to account logic
        if (type === 'PURCHASE' || type === 'PAYMENT' || type === 'EXPENSE') {
            toUpdateAmount = netAmount;
        } else {
            toUpdateAmount = -netAmount;
        }

        await client.query(updateQuery, [fromUpdateAmount, fromAccountId, req.user.userId]);
        await client.query(updateQuery, [toUpdateAmount, toAccountId, req.user.userId]);

        // Create transaction record
        const transactionQuery = `
            INSERT INTO transactions (
                user_id, from_account_id, to_account_id, amount, tax_amount, total_amount,
                type, description, date
            ) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
            RETURNING id
        `;

        const transactionResult = await client.query(transactionQuery, [
            req.user.userId,
            fromAccountId,
            toAccountId,
            netAmount,
            taxAmount,
            amount,
            type,
            description || '',
            date || new Date()
        ]);

        const transactionId = transactionResult.rows[0].id;

        await client.query('COMMIT');

        // Audit log
        await auditLog.record({
            action: 'TRANSACTION_RECORD',
            userId: req.user.userId,
            resourceType: 'transaction',
            resourceId: transactionId,
            ip: req.ip,
            status: 'success'
        });

        return res.status(201).json({
            success: true,
            data: {
                id: transactionId,
                fromAccountId,
                toAccountId,
                amount: netAmount,
                taxAmount: taxAmount,
                totalAmount: amount,
                type,
                description: description || '',
                date: date || new Date().toISOString(),
                createdAt: new Date().toISOString()
            }
        });
    } catch (error) {
        await client.query('ROLLBACK');
        console.error('Record transaction error:', error);
        return res.status(500).json({
            error: { code: 'SERVER_ERROR', message: 'Failed to record transaction' }
        });
    } finally {
        client.release();
    }
};

// Get financial reports
exports.getBalanceSheet = async (req, res) => {
    try {
        const query = `
            SELECT
                SUM(CASE WHEN type IN ('ASSET', 'CASH', 'BANK') THEN balance ELSE 0 END) as "totalAssets",
                SUM(CASE WHEN type IN ('LIABILITY') THEN balance ELSE 0 END) as "totalLiabilities",
                SUM(CASE WHEN type IN ('INCOME') THEN balance ELSE 0 END) as "totalIncome",
                SUM(CASE WHEN type IN ('EXPENSE') THEN balance ELSE 0 END) as "totalExpenses"
            FROM accounts
            WHERE user_id = $1
        `;

        const { rows } = await db.query(query, [req.user.userId]);
        const report = rows[0];

        const netIncome = (parseFloat(report.totalIncome) || 0) - (parseFloat(report.totalExpenses) || 0);
        const equity = (parseFloat(report.totalAssets) || 0) - (parseFloat(report.totalLiabilities) || 0);

        return res.status(200).json({
            success: true,
            data: {
                totalAssets: parseFloat(report.totalAssets) || 0,
                totalLiabilities: parseFloat(report.totalLiabilities) || 0,
                netIncome: netIncome,
                equity: equity,
                generatedAt: new Date().toISOString()
            }
        });
    } catch (error) {
        console.error('Balance sheet error:', error);
        return res.status(500).json({
            error: { code: 'SERVER_ERROR', message: 'Failed to generate balance sheet' }
        });
    }
};

exports.getIncomeStatement = async (req, res) => {
    try {
        const query = `
            SELECT
                SUM(CASE WHEN type IN ('INCOME') THEN balance ELSE 0 END) as "totalIncome",
                SUM(CASE WHEN type IN ('EXPENSE') THEN balance ELSE 0 END) as "totalExpenses"
            FROM accounts
            WHERE user_id = $1
        `;

        const { rows } = await db.query(query, [req.user.userId]);
        const report = rows[0];

        const netIncome = (parseFloat(report.totalIncome) || 0) - (parseFloat(report.totalExpenses) || 0);

        return res.status(200).json({
            success: true,
            data: {
                totalIncome: parseFloat(report.totalIncome) || 0,
                totalExpenses: parseFloat(report.totalExpenses) || 0,
                netIncome: netIncome,
                generatedAt: new Date().toISOString()
            }
        });
    } catch (error) {
        console.error('Income statement error:', error);
        return res.status(500).json({
            error: { code: 'SERVER_ERROR', message: 'Failed to generate income statement' }
        });
    }
};
