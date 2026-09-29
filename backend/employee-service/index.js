const express = require('express');
const morgan = require('morgan');
const employeesRouter = require('./routes/employees');

const app = express();
app.use(express.json());
app.use(morgan('dev'));

// Auth middleware - in production this would verify JWT from Gateway
const authMiddleware = (req, res, next) => {
    const userId = req.headers['x-user-id'] || req.headers['authorization']?.split(' ')[1];
    if (!userId) {
        return res.status(401).json({ error: 'Unauthorized: Missing user identification' });
    }
    req.user = { userId };
    next();
};

app.use(authMiddleware);

// Employee Routes (prefix matches Gateway)
app.get('/api/v1/employees', employeesRouter.getEmployees);
app.post('/api/v1/employees', employeesRouter.createEmployee);
app.get('/api/v1/employees/:employeeId', employeesRouter.getEmployeeDetails);
app.put('/api/v1/employees/:employeeId', employeesRouter.updateEmployee);
app.delete('/api/v1/employees/:employeeId', employeesRouter.deleteEmployee);

// Admin / Verification Routes
app.post('/api/v1/employees/company/:companyId/verify', employeesRouter.verifyAbroadCompany);

const PORT = process.env.PORT || 3006;
app.listen(PORT, () => {
    console.log(`Employee Service running on port ${PORT}`);
});
