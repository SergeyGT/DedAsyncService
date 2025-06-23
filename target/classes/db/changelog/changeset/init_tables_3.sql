-- Добавляем новое поле last_status_id в таблицу request
ALTER TABLE request 
ADD COLUMN last_status_id BIGINT;

-- Добавляем внешний ключ для связи с request_status
ALTER TABLE request
ADD CONSTRAINT fk_request_last_status
    FOREIGN KEY (last_status_id)
    REFERENCES request_status (id)
    ON DELETE SET NULL;

-- Создаем индекс для ускорения поиска по last_status_id
CREATE INDEX idx_request_last_status_id ON request (last_status_id);