const express = require('express');
const { createClient } = require('redis');
require('dotenv').config();

const app = express();
app.use(express.json());

const client = createClient({
    url: process.env.REDIS_URL || 'redis://localhost:6379'
});

client.on('error', err => console.log('Redis Client Error', err));

(async () => {
    await client.connect();
    console.log('Connected to Redis for Tracking');
})();

app.post('/api/v1/tracking/update', async (req, res) => {
    const { userId, latitude, longitude } = req.body;

    try {
        // Use Redis Geo sets for high-performance real-time tracking
        await client.geoAdd('active_users', {
            longitude,
            latitude,
            member: userId
        });

        // Also store as a simple key with TTL for "last seen" status
        await client.set(`user:${userId}:last_seen`, JSON.stringify({ latitude, longitude, timestamp: Date.now() }), {
            EX: 300 // 5 minutes TTL
        });

        res.json({ success: true, message: 'Position updated' });
    } catch (error) {
        console.error('Tracking Error:', error);
        res.status(500).json({ error: 'Failed to update position' });
    }
});

const PORT = process.env.PORT || 3005;
app.listen(PORT, () => console.log(`Tracking Service running on port ${PORT}`));
