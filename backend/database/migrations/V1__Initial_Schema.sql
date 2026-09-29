-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- === PILLAR A: IDENTITY & TRUST ===

CREATE TABLE users (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  full_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  phone_number VARCHAR(20) UNIQUE NOT NULL,
  password_hash VARCHAR(512) NOT NULL,
  preferred_language VARCHAR(10) DEFAULT 'en',
  is_verified BOOLEAN DEFAULT false,
  kyc_status VARCHAR(50) DEFAULT 'pending', -- 'pending', 'approved', 'rejected'
  is_active BOOLEAN DEFAULT true,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP WITH TIME ZONE NULL -- Soft delete
);

-- === PILLAR A: PAYMENTS & WALLET ===

CREATE TABLE wallets (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL UNIQUE,
  balance DECIMAL(15, 2) DEFAULT 0.00 CHECK (balance >= 0),
  currency VARCHAR(3) DEFAULT 'NPR',
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE wallet_transactions (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  wallet_id UUID NOT NULL,
  type VARCHAR(20) NOT NULL, -- 'credit', 'debit'
  amount DECIMAL(15, 2) NOT NULL CHECK (amount > 0),
  description TEXT,
  status VARCHAR(20) DEFAULT 'completed', -- 'pending', 'completed', 'failed'
  metadata JSONB, -- Store flexible data (merchant_id, p2p_sender_id, etc.)
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (wallet_id) REFERENCES wallets(id) ON DELETE CASCADE
);

CREATE INDEX idx_wallet_id ON wallet_transactions(wallet_id);
CREATE INDEX idx_transaction_created_at ON wallet_transactions(created_at);

-- === PILLAR B: MARKETPLACE ===

CREATE TABLE marketplace_items (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  seller_id UUID NOT NULL,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  price DECIMAL(15, 2) NOT NULL CHECK (price >= 0),
  category VARCHAR(100) NOT NULL,
  image_url TEXT,
  stock_quantity INTEGER DEFAULT 0 CHECK (stock_quantity >= 0),
  is_active BOOLEAN DEFAULT true,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (seller_id) REFERENCES users(id) ON DELETE CASCADE
);

-- === AUDIT & COMPLIANCE LAYER ===

CREATE TABLE audit_logs (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  actor_id UUID NOT NULL, -- user or admin
  actor_type VARCHAR(20) NOT NULL, -- 'user', 'admin'
  action VARCHAR(100) NOT NULL,
  resource_type VARCHAR(50), -- 'wallet', 'document', 'marketplace_item'
  resource_id UUID,
  changes JSONB, -- Before/after state snapshot
  ip_address VARCHAR(45),
  status VARCHAR(20), -- 'success', 'failure'
  error_message TEXT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_actor_id ON audit_logs(actor_id);
CREATE INDEX idx_audit_action ON audit_logs(action);
CREATE INDEX idx_audit_created_at ON audit_logs(created_at);

-- Trigger to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_users_modtime BEFORE UPDATE ON users FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();
CREATE TRIGGER update_wallets_modtime BEFORE UPDATE ON wallets FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();
CREATE TRIGGER update_marketplace_items_modtime BEFORE UPDATE ON marketplace_items FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();
