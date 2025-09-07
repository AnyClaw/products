DROP TABLE IF EXISTS orders, basket, products, users;
DROP TYPE IF EXISTS order_status, user_role;

CREATE TABLE IF NOT EXISTS products (
    id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name varchar(100) NOT NULL UNIQUE,
    amount int NOT NULL,
    cost money NOT NULL,

    CONSTRAINT products_check_name_not_empty CHECK (trim(name) <> ''),
    CONSTRAINT products_check_amount_positive CHECK (amount >= 0),
    CONSTRAINT products_check_cost_positive CHECK (cost > 0::money)
);

CREATE TABLE IF NOT EXISTS users (
    id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    login varchar(100) NOT NULL UNIQUE,
    password varchar(100) NOT NULL,
    name varchar(100) NOT NULL,
    phone varchar(100) NOT NULL,
    balance money NOT NULL DEFAULT 0,

    CONSTRAINT users_check_login_not_empty CHECK (trim(login) <> ''),
    CONSTRAINT users_check_password_not_empty CHECK (trim(password) <> ''),
    CONSTRAINT users_check_name_not_empty CHECK (trim(name) <> ''),
    CONSTRAINT users_check_phone_not_empty CHECK (trim(phone) <> ''),
    CONSTRAINT users_check_balance_positive CHECK (balance >= 0::money)
);

CREATE TABLE IF NOT EXISTS basket (
    id_user int NOT NULL,
    id_product int NOT NULL,
    amount int NOT NULL,

    CONSTRAINT basket_pk PRIMARY KEY (id_user, id_product),
    CONSTRAINT basket_users_fk FOREIGN KEY (id_user) REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT basket_products_fk FOREIGN KEY (id_product) REFERENCES products(id) ON DELETE RESTRICT,

    CONSTRAINT basket_check_amount_positive CHECK (amount > 0)
);

--#################################################

INSERT 
INTO users(login, password, name, phone) 
VALUES ('emoskalenko947@gmail.com', 'e4UxDu77', 'Egor', '+79255843709')
RETURNING *;

INSERT 
INTO products(name, amount, cost) 
VALUES ('Tomato', 10, 200::money);

INSERT 
INTO products(name, amount, cost) 
VALUES ('Potato', 10, 200::money);

INSERT 
INTO products(name, amount, cost) 
VALUES ('Kivi', 10, 200::money);

INSERT 
INTO products(name, amount, cost) 
VALUES ('Milk', 10, 200::money);

INSERT 
INTO products(name, amount, cost) 
VALUES ('Chocolate', 10, 200::money);

--#################################################

SELECT * FROM products;

SELECT * FROM basket WHERE id_user=?;

INSERT INTO basket
VALUES (?, ?, ?)
ON CONFLICT (id_user, id_product)
DO UPDATE SET amount = ?;

DELETE FROM basket WHERE id_user = ? AND id_product = ?;

DELETE FROM basket WHERE id_user = ?;

UPDATE products SET amount = amount - ? WHERE id = ?;

UPDATE users SET balance = ?::money WHERE id = ? RETURNING *;

SELECT * FROM products 
WHERE LOWER(name) LIKE CONCAT('%', LOWER(?), '%');

SELECT * FROM products WHERE id=? LIMIT 1;

INSERT 
INTO users(login, password, name, phone, role) 
VALUES (?, ?, ?, ?)
RETURNING *;

SELECT * FROM users WHERE login = ? LIMIT 1;

UPDATE users SET balance = balance + ?::money WHERE id = ? RETURNING *;