-- Вставка данных в таблицу app_user
INSERT INTO app_user (username, email, hash_password, role)
VALUES
    ('ivan_ivanov', 'ivan.ivanov@example.com', '$2a$10$7L/1QbOIX6Oz6lM5Qie6fO5LE.o.O2Akxz3DZGty/aCOEBReI95Qm', 'USER'),
    ('maria_petrova', 'maria.petrova@example.com', '$2a$10$T.kTQQZlfzB7G.C8L.N9h.M8f/WFY/9t1Fdc9x/9na5jMa7oyQy3m', 'USER'),
    ('sergey_sidorov', 'sergey.sidorov@example.com', '$2a$10$yGhBXJ5B79fUN4NLXcNPXuvWxl4UjOjvY3DHQ3KyOZFW0cn8My5re', 'USER'),
    ('anna_kuznetsova', 'anna.kuznetsova@example.com', '$2a$10$u/6NhjU/7yLzYo7cxOG8a.LQf1ZLxqJLeO1Bf3aPgyxZrE8z58lmu', 'USER')
ON CONFLICT (username) DO NOTHING;

-- Вставка данных в таблицу product
INSERT INTO product (product_name, description, price, discount_price)
VALUES
    -- Смартфоны Apple
    ('Смартфон Apple iPhone 14', 'Смартфон Apple iPhone 14 с экраном 6.1 дюймов, 128 ГБ памяти', 999.99, 949.99),
    ('Смартфон Apple iPhone 13', 'Смартфон Apple iPhone 13 с экраном 6.1 дюймов, 128 ГБ памяти', 899.99, 849.99),
    ('Смартфон Apple iPhone 12', 'Смартфон Apple iPhone 12 с экраном 6.1 дюймов, 64 ГБ памяти', 799.99, 749.99),

    -- Смартфоны Samsung
    ('Смартфон Samsung Galaxy S8', 'Смартфон Samsung Galaxy S8 с экраном 5.8 дюймов, 64 ГБ памяти', 499.99, 449.99),
    ('Смартфон Samsung Galaxy S21', 'Смартфон Samsung Galaxy S21 с экраном 6.2 дюймов, 128 ГБ памяти', 799.99, 749.99),
    ('Смартфон Samsung Galaxy S20', 'Смартфон Samsung Galaxy S20 с экраном 6.2 дюймов, 128 ГБ памяти', 699.99, 649.99),

    -- Сопутствующие товары - наушники
    ('Наушники Apple AirPods Pro', 'Беспроводные наушники Apple AirPods Pro с шумоподавлением', 249.99, 229.99),
    ('Наушники Samsung Galaxy Buds Pro', 'Беспроводные наушники Samsung Galaxy Buds Pro с шумоподавлением', 199.99, 179.99),
    ('Наушники Sony WH-1000XM4', 'Беспроводные наушники Sony WH-1000XM4 с шумоподавлением', 349.99, 329.99),

    -- Сопутствующие товары - чехлы
    ('Чехол для Huawei P50', 'Чехол для смартфона Huawei P50', 29.99, 24.99),
    ('Чехол для Samsung Galaxy S21', 'Чехол для смартфона Samsung Galaxy S21', 19.99, 17.99),
    ('Чехол для Apple iPhone 13', 'Чехол для смартфона Apple iPhone 13', 34.99, 29.99),

    -- Смартфон Huawei
    ('Смартфон Huawei P50', 'Смартфон Huawei P50 с экраном 6.1 дюймов, 128 ГБ памяти', 699.99, 649.99),
    ('Смартфон Huawei P40', 'Смартфон Huawei P40 с экраном 6.1 дюймов, 128 ГБ памяти', 599.99, 549.99),
    ('Смартфон Huawei Mate 40', 'Смартфон Huawei Mate 40 с экраном 6.5 дюймов, 128 ГБ памяти', 799.99, 749.99),

    -- Аудиотехника - портативные колонки
    ('Умная колонка Яндекс Станция', 'Умная колонка Яндекс Станция с голосовым управлением', 199.99, 179.99),
    ('Портативная колонка JBL Charge 4', 'Портативная колонка JBL Charge 4 с водонепроницаемым корпусом', 149.99, 129.99),
    ('Портативная колонка Sony SRS-XB43', 'Портативная колонка Sony SRS-XB43 с Extra Bass', 299.99, 269.99);

-- Вставка данных в таблицу categories
INSERT INTO categories (category_name)
VALUES
    ('Смартфоны Apple'),
    ('Смартфоны Samsung'),
    ('Сопутствующие товары - наушники'),
    ('Сопутствующие товары - чехлы'),
    ('Смартфоны Huawei'),
    ('Аудиотехника - портативные колонки');

-- Вставка данных в таблицу product_categories
INSERT INTO product_categories (id_categories, id_product)
VALUES
    -- Смартфоны Apple
    (1, 1),
    (1, 2),
    (1, 3),

    -- Смартфоны Samsung
    (2, 4),
    (2, 5),
    (2, 6),

    -- Сопутствующие товары - наушники
    (3, 7),
    (3, 8),
    (3, 9),

    -- Сопутствующие товары - чехлы
    (4, 10),
    (4, 11),
    (4, 12),

    -- Смартфоны Huawei
    (5, 13),
    (5, 14),
    (5, 15),

    -- Аудиотехника - портативные колонки
    (6, 16),
    (6, 17),
    (6, 18);


-- Вставка данных в таблицу order_position
INSERT INTO order_position (id_product, quantity)
VALUES
    (1, 2),
    (2, 1),
    (3, 5);

-- Вставка данных в таблицу comment
INSERT INTO comment (id_product, id_user, estimation, description, photo)
VALUES
    (1, 1, 5, 'Отличный товар, рекомендую!', NULL),
    (2, 2, 4, 'Хороший товар, но мог бы быть лучше.', NULL),
    (3, 3, 3, 'Средний продукт, ожидал большего.', NULL),
    (1, 4, 5, 'Просто супер! Всем понравилось.', NULL),
    (2, 1, 2, 'Не подошло, много недостатков.', NULL),
    (3, 2, 1, 'Ужасное качество, не советую.', NULL);
