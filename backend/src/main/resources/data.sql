-- Вставка данных в таблицу app_user
INSERT INTO app_user (first_name, last_name, email, hashed_password)
VALUES
    ('Иван', 'Иванов', 'ivan.ivanov@example.com', '$2a$10$7L/1QbOIX6Oz6lM5Qie6fO5LE.o.O2Akxz3DZGty/aCOEBReI95Qm'),
    ('Мария', 'Петрова', 'maria.petrova@example.com', '$2a$10$T.kTQQZlfzB7G.C8L.N9h.M8f/WFY/9t1Fdc9x/9na5jMa7oyQy3m'),
    ('Сергей', 'Сидоров', 'sergey.sidorov@example.com', '$2a$10$yGhBXJ5B79fUN4NLXcNPXuvWxl4UjOjvY3DHQ3KyOZFW0cn8My5re'),
    ('Анна', 'Кузнецова', 'anna.kuznetsova@example.com', '$2a$10$u/6NhjU/7yLzYo7cxOG8a.LQf1ZLxqJLeO1Bf3aPgyxZrE8z58lmu')
ON CONFLICT (email) DO NOTHING;

-- Вставка данных в таблицу product
INSERT INTO product (product_name, description, price, discount_price)
VALUES
    ('Товар 1', 'Описание товара 1', 100.00, 80.00),
    ('Товар 2', 'Описание товара 2', 200.00, 150.00),
    ('Товар 3', 'Описание товара 3', 300.00, 250.00);

-- Вставка данных в таблицу categories
INSERT INTO categories (category_name)
VALUES
    ('Категория 1'),
    ('Категория 2'),
    ('Категория 3');

-- Вставка данных в таблицу categories_subcategories
INSERT INTO categories_subcategories (id_categories, id_subcategories)
VALUES
    (1, 2),
    (2, 3);

-- Вставка данных в таблицу product_categories
INSERT INTO product_categories (id_categories, id_product)
VALUES
    (1, 1),
    (2, 2),
    (3, 3);

-- Вставка данных в таблицу order_position
INSERT INTO order_position (id_product, quantity)
VALUES
    (1, 2),
    (2, 1),
    (3, 5);