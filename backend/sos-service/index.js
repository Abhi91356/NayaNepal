const express = require('express');
const { Pool } = require('pg');
require('dotenv').config();

const app = express();
app.use(express.json());

const pool = new Pool({
  connectionString: process.env.DB_URL,
});

// Middleware to simulate auth (as shown in Gateway)
const authMiddleware = (req, res, next) => {
  req.user = { userId: req.headers['x-user-id'] || 'test-user' };
  next();
};

app.post('/api/v1/sos/alert', authMiddleware, async (req, res) => {
  const { type, latitude, longitude } = req.body;

  try {
    // 1. Record metadata in Core DB (simulated here, in reality would use event bus or internal API)
    // 2. Record geospatial location in PostGIS
    const query = `
      INSERT INTO user_locations (user_id, alert_type, location, created_at)
      VALUES ($1, $2, ST_SetSRID(ST_MakePoint($3, $4), 4326), NOW())
      RETURNING id
    `;

    const result = await pool.query(query, [req.user.userId, type, longitude, latitude]);

    console.log(`SOS Alert triggered: ${type} at ${latitude}, ${longitude}`);

    res.status(201).json({
      success: true,
      data: {
        alertId: result.rows[0].id,
        status: 'DISPATCHING_HELP'
      }
    });
  } catch (error) {
    console.error('SOS Alert Error:', error);
    res.status(500).json({ error: 'Failed to process SOS alert' });
  }
});

const PORT = process.env.PORT || 3004;
app.listen(PORT, () => console.log(`SOS Service running on port ${PORT}`));
