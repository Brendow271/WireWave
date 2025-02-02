-- Вставка данных в таблицу app_user
INSERT INTO app_user (first_name, last_name, email, hash_password, role)
VALUES ('Ivan', 'Ivanov', 'ivan.ivanov@example.com', '$2a$10$7L/1QbOIX6Oz6lM5Qie6fO5LE.o.O2Akxz3DZGty/aCOEBReI95Qm',
        'USER'),
       ('Maria', 'Petrova', 'maria.petrova@example.com', '$2a$10$T.kTQQZlfzB7G.C8L.N9h.M8f/WFY/9t1Fdc9x/9na5jMa7oyQy3m',
        'USER'),
       ('Sergey', 'Sidorov', 'sergey.sidorov@example.com',
        '$2a$10$yGhBXJ5B79fUN4NLXcNPXuvWxl4UjOjvY3DHQ3KyOZFW0cn8My5re', 'USER'),
       ('Anna', 'Kuznetsova', 'anna.kuznetsova@example.com',
        '$2a$10$u/6NhjU/7yLzYo7cxOG8a.LQf1ZLxqJLeO1Bf3aPgyxZrE8z58lmu', 'USER')
ON CONFLICT (email) DO NOTHING;


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


-- Вставка данных о количестве товаров в таблицу warehouse
INSERT INTO warehouse (id_product, quantity)
VALUES (1, 50),  -- Смартфон Apple iPhone 14
       (2, 40),  -- Смартфон Apple iPhone 13
       (3, 30),  -- Смартфон Apple iPhone 12
       (4, 20),  -- Смартфон Samsung Galaxy S21
       (5, 25),  -- Смартфон Samsung Galaxy S20
       (6, 15),  -- Смартфон Samsung Galaxy S8
       (7, 60),  -- Наушники Apple AirPods Pro
       (8, 50),  -- Наушники Samsung Galaxy Buds Pro
       (9, 45),  -- Наушники Sony WH-1000XM4
       (10, 70), -- Чехол для Huawei P50
       (11, 80), -- Чехол для Samsung Galaxy S21
       (12, 75), -- Чехол для Apple iPhone 13
       (13, 35), -- Смартфон Huawei P50
       (14, 40), -- Смартфон Huawei P40
       (15, 30), -- Смартфон Huawei Mate 40
       (16, 90), -- Умная колонка Яндекс Станция
       (17, 65), -- Портативная колонка JBL Charge 4
       (18, 55) -- Портативная колонка Sony SRS-XB43
ON CONFLICT DO NOTHING;

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
       ('Портативные колонки')
ON CONFLICT DO NOTHING;

-- Вставка данных в таблицу order_position
INSERT INTO categories_subcategories (id_categories, id_subcategories)
VALUES (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (5, 6),
       (5, 7),
       (8, 6),
       (8, 9)
ON CONFLICT DO NOTHING;

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
    (9, 18)
ON CONFLICT DO NOTHING;

-- Вставка данных в таблицу basket
INSERT INTO basket (id, id_user)
VALUES
    (1, 1),  -- Для пользователя Ivan Ivanov
    (2, 2),  -- Для пользователя Maria Petrova
    (3, 3),  -- Для пользователя Sergey Sidorov
    (4, 4)   -- Для пользователя Anna Kuznetsova
ON CONFLICT DO NOTHING;

-- -- Вставка данных в таблицу order_position
INSERT INTO order_position (id_basket, id_product, quantity)
VALUES
    -- Корзина 1 (iPhone 14 и AirPods Pro для Ivan Ivanov)
    (1, 1, 1),  -- iPhone 14
    (1, 7, 1),  -- AirPods Pro

    -- Корзина 2 (Galaxy S21 и чехол для Samsung Galaxy S21 для Maria Petrova)
    (2, 4, 1),  -- Galaxy S21
    (2, 11, 2), -- Чехол для Galaxy S21

    -- Корзина 3 (Huawei P50 для Sergey Sidorov)
    (3, 13, 1), -- Huawei P50

    -- Корзина 4 (Наушники Sony для Anna Kuznetsova)
    (4, 9, 1)   -- Sony WH-1000XM4
ON CONFLICT DO NOTHING;


-- Вставка данных в таблицу basket
INSERT INTO basket (id, id_user)
VALUES
    (1, 1),  -- Для пользователя Ivan Ivanov
    (2, 2),  -- Для пользователя Maria Petrova
    (3, 3),  -- Для пользователя Sergey Sidorov
    (4, 4)   -- Для пользователя Anna Kuznetsova
ON CONFLICT DO NOTHING;

INSERT INTO order_position (id_basket, id_product, quantity)
VALUES
    -- Корзина 1 (iPhone 14 и AirPods Pro для Ivan Ivanov)
    (1, 1, 1),  -- iPhone 14
    (1, 7, 1),  -- AirPods Pro

    -- Корзина 2 (Galaxy S21 и чехол для Samsung Galaxy S21 для Maria Petrova)
    (2, 4, 1),  -- Galaxy S21
    (2, 11, 2), -- Чехол для Galaxy S21

    -- Корзина 3 (Huawei P50 для Sergey Sidorov)
    (3, 13, 1), -- Huawei P50

    -- Корзина 4 (Наушники Sony для Anna Kuznetsova)
    (4, 9, 1)   -- Sony WH-1000XM4
ON CONFLICT DO NOTHING;


-- Вставка данных в таблицу comment
INSERT INTO comment (id_product, id_user, estimation, description, photo)
VALUES (1, 1, 5, 'Отличный товар, рекомендую!', NULL),
       (2, 2, 4, 'Хороший товар, но мог бы быть лучше.', NULL),
       (3, 3, 3, 'Средний продукт, ожидал большего.', NULL),
       (1, 4, 5, 'Просто супер! Всем понравилось.', NULL),
       (2, 1, 2, 'Не подошло, много недостатков.', NULL),
       (3, 2, 1, 'Ужасное качество, не советую.', NULL);

INSERT INTO attribute (id, name, type)
VALUES
    (1, 'Цвет', 'string'),
    (2, 'Поддержка 5G', 'boolean'),
    (3, 'Материал корпуса', 'string')
ON CONFLICT DO NOTHING;

INSERT INTO product_attribute (product_id, attribute_id, value)
VALUES
    -- iPhone 14
    (1, 1, 'Черный'),  -- Цвет
    (1, 2, 'true'),    -- Поддержка 5G
    (1, 3, 'Стекло'),  -- Материал корпуса

    -- iPhone 13
    (2, 1, 'Белый'),
    (2, 2, 'true'),
    (2, 3, 'Стекло'),

    -- Samsung Galaxy S21
    (4, 1, 'Синий'),
    (4, 2, 'true'),
    (4, 3, 'Металл'),

    -- Huawei P50
    (13, 1, 'Золотой'),
    (13, 2, 'false'),
    (13, 3, 'Керамика'),

    -- JBL Charge 4
    (17, 1, 'Черный'),
    (17, 2, 'false'),
    (17, 3, 'Керамика')
ON CONFLICT DO NOTHING;


UPDATE app_user
SET role = 'ADMIN'
WHERE email = 'bobbrown@example.com';