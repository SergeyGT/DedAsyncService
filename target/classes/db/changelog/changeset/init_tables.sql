CREATE TABLE IF NOT EXISTS request (
    id BIGSERIAL PRIMARY KEY,
    request_data JSONB NOT NULL,
    duplicate_count INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS request_status (
    id BIGSERIAL PRIMARY KEY,
    request_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    
    CONSTRAINT fk_request_status_request 
        FOREIGN KEY (request_id) 
        REFERENCES request (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_request_status_request_id_created_at 
    ON request_status (request_id, created_at DESC)