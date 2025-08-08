-- Initialization script for Rick and Morty PostgreSQL database
-- This script runs when the container starts for the first time

-- Create additional schemas if needed
CREATE SCHEMA IF NOT EXISTS rickandmorty;

-- Set default schema
SET search_path TO rickandmorty, public;

-- Create sequences for auto-increment IDs
CREATE SEQUENCE IF NOT EXISTS character_seq START 1000;

-- Create indexes for better performance (will be created by JPA/Hibernate too)
-- These are examples for common queries

-- Grant permissions
GRANT ALL PRIVILEGES ON DATABASE rickandmorty TO postgres;
GRANT ALL PRIVILEGES ON SCHEMA rickandmorty TO postgres;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA rickandmorty TO postgres;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA rickandmorty TO postgres;

-- Log initialization
INSERT INTO pg_stat_statements_info (dealloc) VALUES (0) ON CONFLICT DO NOTHING;

COMMIT;
