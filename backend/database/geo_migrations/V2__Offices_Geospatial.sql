-- === OFFICE GEOSPATIAL DATA ===

CREATE TABLE offices (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    employee_id UUID NOT NULL, -- Reference to Core DB Employee
    office_name VARCHAR(255) NOT NULL,
    office_address TEXT,
    office_phone VARCHAR(20),
    district VARCHAR(100),
    region VARCHAR(100), -- 'Kathmandu Valley', 'Hilly Region', 'Terai'
    office_type VARCHAR(50), -- 'government', 'private', 'ngo'
    floor_number INTEGER,
    building_name VARCHAR(100),
    location GEOGRAPHY(POINT, 4326), -- PostGIS for exact location
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_offices_district ON offices(district);
CREATE INDEX idx_offices_type ON offices(office_type);
CREATE INDEX idx_offices_location ON offices USING GIST (location);
