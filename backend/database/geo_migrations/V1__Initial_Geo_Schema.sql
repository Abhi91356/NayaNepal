-- Enable PostGIS
CREATE EXTENSION IF NOT EXISTS postgis;

-- Table for SOS Locations
CREATE TABLE user_locations (
    id SERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    alert_type VARCHAR(50),
    location GEOGRAPHY(POINT, 4326),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_user_locations_gist ON user_locations USING GIST (location);
