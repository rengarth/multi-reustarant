CREATE TABLE IF NOT EXISTS categories (
                                          id SERIAL PRIMARY KEY,
                                          name VARCHAR(255) NOT NULL,
    is_deleted BOOLEAN NOT NULL,
    parent_id BIGINT,
    CONSTRAINT fk_parent FOREIGN KEY (parent_id) REFERENCES categories(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS dishes (
                                      id SERIAL PRIMARY KEY,
                                      name VARCHAR(255) NOT NULL,
    description TEXT,
    price_per_one INT NOT NULL,
    stock_quantity INT NOT NULL,
    is_deleted BOOLEAN NOT NULL,
    is_available BOOLEAN NOT NULL,
    category_id BIGINT NOT NULL,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS administrators (
                                              id SERIAL PRIMARY KEY,
                                              first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_deleted BOOLEAN NOT NULL
    );

CREATE TABLE IF NOT EXISTS waiters (
                                       id SERIAL PRIMARY KEY,
                                       first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_deleted BOOLEAN NOT NULL
    );

CREATE TABLE IF NOT EXISTS orders (
                                      id SERIAL PRIMARY KEY,
                                      waiter_id BIGINT NOT NULL,
                                      status VARCHAR(50) NOT NULL,
    payment_status VARCHAR(50) NOT NULL,
    total_amount INT NOT NULL,
    create_time TIMESTAMP NOT NULL,
    lead_time TIMESTAMP,
    CONSTRAINT fk_waiter FOREIGN KEY (waiter_id) REFERENCES waiters(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS order_details (
                                             id SERIAL PRIMARY KEY,
                                             order_id BIGINT NOT NULL,
                                             dish_id BIGINT NOT NULL,
                                             quantity INT NOT NULL,
                                             total_amount INT NOT NULL,
                                             CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_dish FOREIGN KEY (dish_id) REFERENCES dishes(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS tables (
                                           id SERIAL PRIMARY KEY,
                                           number INT UNIQUE NOT NULL,
                                           waiter_id BIGINT,
                                           total_amount INT,
                                           CONSTRAINT fk_waiter FOREIGN KEY (waiter_id) REFERENCES waiters(id) ON DELETE SET NULL
    );

CREATE TABLE IF NOT EXISTS table_items (
                                           id SERIAL PRIMARY KEY,
                                           dish_id BIGINT NOT NULL,
                                           quantity INT NOT NULL
);

CREATE TABLE IF NOT EXISTS table_items_list (
                                                table_id BIGINT NOT NULL,
                                                table_item_id BIGINT NOT NULL,
                                                CONSTRAINT fk_table FOREIGN KEY (table_id) REFERENCES tables(id) ON DELETE CASCADE,
    CONSTRAINT fk_table_item FOREIGN KEY (table_item_id) REFERENCES table_items(id) ON DELETE CASCADE,
    PRIMARY KEY (table_id, table_item_id)
    );


COPY categories(id, is_deleted, name, parent_id)
    FROM '/docker-entrypoint-initdb.d/fulfillment/categories.csv'
    DELIMITER ','
    CSV HEADER;

COPY dishes(id, description, is_deleted, name, price_per_one, stock_quantity, category_id, is_available)
    FROM '/docker-entrypoint-initdb.d/fulfillment/dishes.csv'
    DELIMITER ','
    CSV HEADER;

COPY tables (id, number, waiter_id, total_amount)
    FROM '/docker-entrypoint-initdb.d/fulfillment/tables.csv'
    DELIMITER ','
    CSV HEADER;

COPY administrators(id, first_name, is_deleted, last_name, password, phone_number)
    FROM '/docker-entrypoint-initdb.d/fulfillment/administrators.csv'
    DELIMITER ','
    CSV HEADER;

COPY waiters(id, first_name, is_deleted, last_name, password, phone_number)
    FROM '/docker-entrypoint-initdb.d/fulfillment/waiters.csv'
    DELIMITER ','
    CSV HEADER;