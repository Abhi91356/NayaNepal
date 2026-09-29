const db = require('./database');

exports.record = async ({ action, userId, resourceType, resourceId, ip, status, errorMessage = null, changes = null }) => {
    try {
        const query = `
            INSERT INTO audit_logs (
                actor_id, actor_type, action, resource_type, resource_id, ip_address, status, error_message, changes
            ) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
        `;

        await db.query(query, [
            userId,
            'user',
            action,
            resourceType,
            resourceId,
            ip,
            status,
            errorMessage,
            changes ? JSON.stringify(changes) : null
        ]);
    } catch (error) {
        console.error('Failed to record audit log:', error);
    }
};
