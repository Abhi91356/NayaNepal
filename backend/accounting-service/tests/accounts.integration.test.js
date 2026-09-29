const request = require('supertest');
const app = require('../app');
const db = require('../database');

describe('Account Integration Tests', () => {
    const authToken = 'Bearer mock-token';
    const testUserId = 'test-user-id';

    beforeAll(async () => {
        // Clean up any existing test data for the test user
        // We use try-catch in case tables don't exist yet in the test environment
        try {
            await db.query('DELETE FROM transactions WHERE user_id = $1', [testUserId]);
            await db.query('DELETE FROM accounts WHERE user_id = $1', [testUserId]);
        } catch (e) {
            console.log('Cleanup failed, tables might not exist yet');
        }
    });

    afterAll(async () => {
        try {
            await db.query('DELETE FROM transactions WHERE user_id = $1', [testUserId]);
            await db.query('DELETE FROM accounts WHERE user_id = $1', [testUserId]);
        } catch (e) {
            // ignore
        }
        await db.pool.end();
    });

    test('Create account successfully', async () => {
        const res = await request(app)
            .post('/api/v1/accounting/accounts')
            .set('Authorization', authToken)
            .send({
                name: 'Test Cash Account',
                type: 'CASH',
                description: 'Test account',
                initialBalance: 1000
            });

        expect(res.status).toBe(201);
        expect(res.body.success).toBe(true);
        expect(res.body.data.name).toBe('Test Cash Account');
        expect(res.body.data.type).toBe('CASH');
        expect(res.body.data.balance).toBe(1000);
    });

    test('Get accounts list', async () => {
        const res = await request(app)
            .get('/api/v1/accounting/accounts')
            .set('Authorization', authToken);

        expect(res.status).toBe(200);
        expect(res.body.success).toBe(true);
        expect(res.body.data).toBeInstanceOf(Array);
    });

    test('Record transaction successfully', async () => {
        // Create two accounts
        const account1Res = await request(app)
            .post('/api/v1/accounting/accounts')
            .set('Authorization', authToken)
            .send({
                name: 'Account 1',
                type: 'CASH',
                initialBalance: 1000
            });

        const account2Res = await request(app)
            .post('/api/v1/accounting/accounts')
            .set('Authorization', authToken)
            .send({
                name: 'Account 2',
                type: 'BANK',
                initialBalance: 0
            });

        const account1Id = account1Res.body.data.id;
        const account2Id = account2Res.body.data.id;

        // Record transaction
        const transactionRes = await request(app)
            .post('/api/v1/accounting/transactions')
            .set('Authorization', authToken)
            .send({
                fromAccountId: account1Id,
                toAccountId: account2Id,
                amount: 500,
                type: 'TRANSFER',
                description: 'Test transfer'
            });

        expect(transactionRes.status).toBe(201);
        expect(transactionRes.body.success).toBe(true);
        expect(transactionRes.body.data.type).toBe('TRANSFER');
        expect(transactionRes.body.data.totalAmount).toBe(500);
    });
});
