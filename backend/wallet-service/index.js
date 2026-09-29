const express = require('express');
const app = express();
app.use(express.json());

// Step 2: Break Monolith into Microservices
// Wallet Service Logic

app.get('/api/v1/wallet/balance', (req, res) => {
  // Logic to fetch balance from PostgreSQL
  res.json({
    success: true,
    data: { balance: 12450.00, currency: 'NPR' }
  });
});

app.post('/api/v1/wallet/pay', async (req, res) => {
  const { amount, recipientId } = req.body;

  // Logic to process payment

  // Step 3: Implement Inter-Service Communication (Event-Driven)
  console.log(`Publishing event: wallet.transaction.completed for ${amount}`);
  // await eventBus.publish('wallet.transaction.completed', { ... });

  res.json({
    success: true,
    data: { transactionId: 'txn_123', status: 'completed' }
  });
});

const PORT = process.env.PORT || 3001;
app.listen(PORT, () => console.log(`Wallet Service running on port ${PORT}`));
