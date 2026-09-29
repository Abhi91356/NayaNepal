const { Pool } = require('pg');
require('dotenv').config();

const pool = new Pool({
  connectionString: process.env.DB_URL || 'postgresql://user:pass@localhost:5432/naya_nepal',
});

module.exports = {
  query: (text, params) => pool.query(text, params),
  pool
};
