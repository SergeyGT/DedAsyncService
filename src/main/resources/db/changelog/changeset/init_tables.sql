CREATE TABLE request (
    id BIGSERIAL PRIMARY KEY,
    normalized_request_data TEXT NOT NULL DEFAULT '',
    request_hash VARCHAR(64) NOT NULL DEFAULT '',
    duplicate_count INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT uk_request_hash UNIQUE (request_hash)
);

CREATE TABLE request_status (
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
    ON request_status (request_id, created_at DESC);