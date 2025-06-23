
ALTER TABLE request 
ADD COLUMN last_status_id BIGINT;

ALTER TABLE request
ADD CONSTRAINT fk_request_last_status
    FOREIGN KEY (last_status_id)
    REFERENCES request_status (id)
    ON DELETE SET NULL;

CREATE INDEX idx_request_last_status_id ON request (last_status_id);