CREATE TABLE IF NOT EXISTS payment_transactions (
                                                    id SERIAL PRIMARY KEY,
                                                    order_id BIGINT NOT NULL,
                                                    amount INT NOT NULL,
                                                    transaction_status VARCHAR(50) NOT NULL,
    transaction_time TIMESTAMP NOT NULL
    );