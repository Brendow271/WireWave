-- Вставка данных в таблицу app_user
INSERT INTO app_user (username, email, hash_password, role)
VALUES ('ivan_ivanov', 'ivan.ivanov@example.com', '$2a$10$7L/1QbOIX6Oz6lM5Qie6fO5LE.o.O2Akxz3DZGty/aCOEBReI95Qm',
        'USER'),
       ('maria_petrova', 'maria.petrova@example.com', '$2a$10$T.kTQQZlfzB7G.C8L.N9h.M8f/WFY/9t1Fdc9x/9na5jMa7oyQy3m',
        'USER'),
       ('sergey_sidorov', 'sergey.sidorov@example.com', '$2a$10$yGhBXJ5B79fUN4NLXcNPXuvWxl4UjOjvY3DHQ3KyOZFW0cn8My5re',
        'USER'),
       ('anna_kuznetsova', 'anna.kuznetsova@example.com',
        '$2a$10$u/6NhjU/7yLzYo7cxOG8a.LQf1ZLxqJLeO1Bf3aPgyxZrE8z58lmu', 'USER')
ON CONFLICT (username) DO NOTHING;

-- Вставка данных в таблицу product
INSERT INTO product (product_name, description, price, discount_price)
VALUES
    -- Смартфоны Apple
    ('Смартфон Apple iPhone 14', 'Смартфон Apple iPhone 14 с экраном 6.1 дюймов, 128 ГБ памяти', 79990, 74990),
    ('Смартфон Apple iPhone 13', 'Смартфон Apple iPhone 13 с экраном 6.1 дюймов, 128 ГБ памяти', 69990, 64990),
    ('Смартфон Apple iPhone 12', 'Смартфон Apple iPhone 12 с экраном 6.1 дюймов, 64 ГБ памяти', 59990, 54990),

    -- Смартфоны Samsung
    ('Смартфон Samsung Galaxy S21', 'Смартфон Samsung Galaxy S21 с экраном 6.2 дюймов, 128 ГБ памяти', 74990, 69990),
    ('Смартфон Samsung Galaxy S20', 'Смартфон Samsung Galaxy S20 с экраном 6.2 дюймов, 128 ГБ памяти', 64990, 59990),
    ('Смартфон Samsung Galaxy S8', 'Смартфон Samsung Galaxy S8 с экраном 5.8 дюймов, 64 ГБ памяти', 19990, 17990),

    -- Сопутствующие товары - наушники
    ('Наушники Apple AirPods Pro', 'Беспроводные наушники Apple AirPods Pro с шумоподавлением', 19990, 17990),
    ('Наушники Samsung Galaxy Buds Pro', 'Беспроводные наушники Samsung Galaxy Buds Pro с шумоподавлением', 14990,
     12990),
    ('Наушники Sony WH-1000XM4', 'Беспроводные наушники Sony WH-1000XM4 с шумоподавлением', 24990, 22990),

    -- Чехлы для смартфонов
    ('Чехол для Huawei P50', 'Чехол для смартфона Huawei P50', 1490, 1290),
    ('Чехол для Samsung Galaxy S21', 'Чехол для смартфона Samsung Galaxy S21', 990, 890),
    ('Чехол для Apple iPhone 13', 'Чехол для смартфона Apple iPhone 13', 1990, 1790),

    -- Смартфоны Huawei
    ('Смартфон Huawei P50', 'Смартфон Huawei P50 с экраном 6.1 дюймов, 128 ГБ памяти', 59990, 54990),
    ('Смартфон Huawei P40', 'Смартфон Huawei P40 с экраном 6.1 дюймов, 128 ГБ памяти', 49990, 44990),
    ('Смартфон Huawei Mate 40', 'Смартфон Huawei Mate 40 с экраном 6.5 дюймов, 128 ГБ памяти', 69990, 64990),

    -- Аудиотехника - портативные колонки
    ('Умная колонка Яндекс Станция', 'Умная колонка Яндекс Станция с голосовым управлением', 9990, 8990),
    ('Портативная колонка JBL Charge 4', 'Портативная колонка JBL Charge 4 с водонепроницаемым корпусом', 7990, 6990),
    ('Портативная колонка Sony SRS-XB43', 'Портативная колонка Sony SRS-XB43 с Extra Bass', 14990, 12990);

-- Создание таблицы warehouse
CREATE TABLE IF NOT EXISTS warehouse
(
    id         SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL,
    quantity   INTEGER NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product (id)
);

-- Вставка данных о количестве товаров в таблицу warehouse
INSERT INTO warehouse (id_product, quantity)
VALUES
    (1, 50),   -- Смартфон Apple iPhone 14
    (2, 40),   -- Смартфон Apple iPhone 13
    (3, 30),   -- Смартфон Apple iPhone 12
    (4, 20),   -- Смартфон Samsung Galaxy S21
    (5, 25),   -- Смартфон Samsung Galaxy S20
    (6, 15),   -- Смартфон Samsung Galaxy S8
    (7, 60),   -- Наушники Apple AirPods Pro
    (8, 50),   -- Наушники Samsung Galaxy Buds Pro
    (9, 45),   -- Наушники Sony WH-1000XM4
    (10, 70),  -- Чехол для Huawei P50
    (11, 80),  -- Чехол для Samsung Galaxy S21
    (12, 75),  -- Чехол для Apple iPhone 13
    (13, 35),  -- Смартфон Huawei P50
    (14, 40),  -- Смартфон Huawei P40
    (15, 30),  -- Смартфон Huawei Mate 40
    (16, 90),  -- Умная колонка Яндекс Станция
    (17, 65),  -- Портативная колонка JBL Charge 4
    (18, 55);  -- Портативная колонка Sony SRS-XB43

-- Вставка данных в таблицу categories
INSERT INTO categories (category_name)
VALUES ('Смартфоны'),
       ('Apple'),
       ('Samsung'),
       ('Huawei'),
       ('Сопутствующие товары'),
       ('Наушники'),
       ('Чехлы'),
       ('Аудиотехника'),
       ('Портативные колонки');

-- Вставка данных в таблицу order_position
INSERT INTO categories_subcategories (id_categories, id_subcategories)
VALUES (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (5, 6),
       (5, 7),
       (8, 6),
       (8, 9);

-- Вставка данных в таблицу product_categories
INSERT INTO product_categories (id_categories, id_product)
VALUES
    -- Смартфоны Apple
    (2, 1),
    (2, 2),
    (2, 3),

    -- Смартфоны Samsung
    (3, 4),
    (3, 5),
    (3, 6),

    -- Смартфоны Huawei
    (4, 13),
    (4, 14),
    (4, 15),

    -- Сопутствующие товары - наушники
    (6, 7),
    (6, 8),
    (6, 9),

    -- Сопутствующие товары - чехлы
    (7, 10),
    (7, 11),
    (7, 12),

    -- Аудиотехника - портативные колонки
    (9, 16),
    (9, 17),
    (9, 18);


-- Вставка данных в таблицу order_position
INSERT INTO order_position (id_product, quantity)
VALUES (1, 2),
       (2, 1),
       (3, 5);

-- Вставка данных в таблицу comment
INSERT INTO comment (id_product, id_user, estimation, description, photo)
VALUES (1, 1, 5, 'Отличный товар, рекомендую!', NULL),
       (2, 2, 4, 'Хороший товар, но мог бы быть лучше.', NULL),
       (3, 3, 3, 'Средний продукт, ожидал большего.', NULL),
       (1, 4, 5, 'Просто супер! Всем понравилось.', NULL),
       (2, 1, 2, 'Не подошло, много недостатков.', NULL),
       (3, 2, 1, 'Ужасное качество, не советую.', NULL);
