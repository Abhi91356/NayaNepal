const express = require('express');
const morgan = require('morgan');
const accountsRouter = require('./routes/accounts');

const app = express();
app.use(express.json());

if (process.env.NODE_ENV !== 'test') {
    app.use(morgan('dev'));
}

// Auth middleware - in production this would verify JWT from Gateway
const authMiddleware = (req, res, next) => {
    // For testing and simple auth, we look for x-user-id or a Bearer token
    let userId = req.headers['x-user-id'];

    if (!userId && req.headers['authorization']) {
        // Simplified: any bearer token is 'test-user-id' for now
        userId = 'test-user-id';
    }

    if (!userId) {
        return res.status(401).json({
            error: { code: 'UNAUTHORIZED', message: 'Missing user identification' }
        });
    }

    req.user = { userId };
    next();
};

app.use(authMiddleware);

// Routes with prefix to match Gateway routing
app.post('/api/v1/accounting/accounts', accountsRouter.createAccount);
app.get('/api/v1/accounting/accounts', accountsRouter.getAccounts);
app.put('/api/v1/accounting/accounts/:accountId', accountsRouter.updateAccount);
app.delete('/api/v1/accounting/accounts/:accountId', accountsRouter.deleteAccount);

app.post('/api/v1/accounting/transactions', accountsRouter.recordTransaction);

app.get('/api/v1/accounting/reports/balance-sheet', accountsRouter.getBalanceSheet);
app.get('/api/v1/accounting/reports/income-statement', accountsRouter.getIncomeStatement);

module.exports = app;
