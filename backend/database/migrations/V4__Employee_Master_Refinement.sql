-- === REFINING EMPLOYEE MASTER SYSTEM ===

-- Update employees table with more comprehensive fields
ALTER TABLE employees
ADD COLUMN full_name VARCHAR(255),
ADD COLUMN email VARCHAR(255) UNIQUE,
ADD COLUMN phone VARCHAR(20),
ADD COLUMN nepali_name VARCHAR(255),
ADD COLUMN profile_image_url TEXT,
ADD COLUMN employment_type VARCHAR(50) DEFAULT 'domestic', -- 'domestic', 'abroad'
ADD COLUMN deleted_at TIMESTAMP WITH TIME ZONE NULL;

CREATE INDEX idx_employees_phone ON employees(phone);
CREATE INDEX idx_employees_employment_type ON employees(employment_type);

-- Abroad Company Information
CREATE TABLE abroad_companies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    employee_id UUID NOT NULL,
    company_name VARCHAR(255) NOT NULL,
    company_address TEXT,
    country VARCHAR(100) NOT NULL,
    company_phone VARCHAR(20),
    company_website TEXT,
    industry VARCHAR(100),
    is_verified BOOLEAN DEFAULT false,
    is_trusted BOOLEAN DEFAULT false,
    verification_date TIMESTAMP WITH TIME ZONE,
    trust_score INTEGER DEFAULT 0, -- 0-100 scale
    verification_source VARCHAR(100), -- 'government', 'third_party', 'self_reported'
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
);

CREATE INDEX idx_abroad_companies_country ON abroad_companies(country);
CREATE INDEX idx_abroad_companies_verified ON abroad_companies(is_verified);
CREATE INDEX idx_abroad_companies_trusted ON abroad_companies(is_trusted);
CREATE INDEX idx_abroad_companies_trust_score ON abroad_companies(trust_score);

-- Company Verification Logs
CREATE TABLE verification_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID NOT NULL,
    verified_by UUID NOT NULL, -- admin/user ID
    verification_type VARCHAR(50), -- 'initial', 're_verification', 'trust_update'
    verification_method VARCHAR(50), -- 'document_check', 'phone_verification', 'site_visit'
    notes TEXT,
    status VARCHAR(20), -- 'pending', 'approved', 'rejected'
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (company_id) REFERENCES abroad_companies(id) ON DELETE CASCADE
);

CREATE INDEX idx_verification_logs_company_id ON verification_logs(company_id);
CREATE INDEX idx_verification_logs_status ON verification_logs(status);

-- Trust Score Events
CREATE TABLE trust_score_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID NOT NULL,
    event_type VARCHAR(50), -- 'positive_review', 'negative_review', 'complaint', 'resolution'
    score_change INTEGER NOT NULL, -- +10 or -10
    reason TEXT,
    reported_by UUID,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (company_id) REFERENCES abroad_companies(id) ON DELETE CASCADE
);

CREATE INDEX idx_trust_events_company_id ON trust_score_events(company_id);
