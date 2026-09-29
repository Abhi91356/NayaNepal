const express = require('express');
const rateLimit = require('express-rate-limit');
const jwt = require('jsonwebtoken');

const app = express();
app.use(express.json());

// --- Step 1: Design API Gateway & Routing ---

// Rate limiting: 100 requests per 15 minutes per IP
const limiter = rateLimit({
  windowMs: 15 * 60 * 1000,
  max: 100,
  message: {
    success: false,
    error: {
      code: 'TOO_MANY_REQUESTS',
      message: 'Too many requests from this IP, please try again later.'
    }
  },
});

app.use(limiter);

// JWT Middleware for Centralized Auth
const authMiddleware = (req, res, next) => {
  const authHeader = req.headers.authorization;
  if (!authHeader || !authHeader.startsWith('Bearer ')) {
    return res.status(401).json({
      success: false,
      error: { code: 'UNAUTHORIZED', message: 'No token provided' }
    });
  }

  const token = authHeader.split(' ')[1];
  try {
    const decoded = jwt.verify(token, process.env.JWT_SECRET || 'naya_nepal_secret');
    req.user = decoded;
    next();
  } catch (err) {
    return res.status(401).json({
      success: false,
      error: { code: 'UNAUTHORIZED', message: 'Invalid or expired token' }
    });
  }
};

// Route to microservices (Proxy logic would go here in production)
app.use('/api/v1/auth', (req, res) => res.json({ message: 'Routing to Auth Service' }));
app.use('/api/v1/wallet', authMiddleware, (req, res) => res.json({ message: 'Routing to Wallet Service' }));
app.use('/api/v1/marketplace', authMiddleware, (req, res) => res.json({ message: 'Routing to Marketplace Service' }));
app.use('/api/v1/accounting', authMiddleware, (req, res) => res.json({ message: 'Routing to Accounting Service' }));

// --- New Government & Police Portal Routes ---
app.use('/api/v1/employees', authMiddleware, (req, res) => res.json({ message: 'Routing to Employee Directory Service' }));
app.use('/api/v1/salary', authMiddleware, (req, res) => res.json({ message: 'Routing to Salary Management Service' }));
app.use('/api/v1/complaints', authMiddleware, (req, res) => res.json({ message: 'Routing to Complaint System Service' }));
app.use('/api/v1/police-messages', authMiddleware, (req, res) => res.json({ message: 'Routing to Police Messaging Service' }));
app.use('/api/v1/trusted-voice', authMiddleware, (req, res) => res.json({ message: 'Routing to Trusted Voice Service' }));
app.use('/api/v1/ideas', authMiddleware, (req, res) => res.json({ message: 'Routing to Ideas & Suggestions Service' }));
app.use('/api/v1/cybercrime', authMiddleware, (req, res) => res.json({ message: 'Routing to Cybercrime Reporting Service' }));
app.use('/api/v1/sos', authMiddleware, (req, res) => res.json({ message: 'Routing to SOS Emergency Service' }));
app.use('/api/v1/tracking', authMiddleware, (req, res) => res.json({ message: 'Routing to Live Tracking Service' }));

// Step 4: Unified Error Handling
app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(500).json({
    success: false,
    error: {
      code: 'INTERNAL_SERVER_ERROR',
      message: 'An unexpected error occurred.',
      details: process.env.NODE_ENV === 'development' ? err.message : undefined
    },
    timestamp: new Date().toISOString(),
    requestId: req.id // assuming a request ID middleware is used
  });
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`NayaNepal API Gateway running on port ${PORT}`));
