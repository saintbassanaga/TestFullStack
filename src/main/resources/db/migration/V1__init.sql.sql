CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    price NUMERIC(10,2),
    variants JSONB,
    created_at TIMESTAMP DEFAULT NOW()
);
