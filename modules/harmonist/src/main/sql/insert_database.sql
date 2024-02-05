INSERT INTO users (nickname, email, password, phone, role)
    VALUES  ('IThror', 'thror.dainson@ya.ru', '$2a$10$svvtLuz.vJ5HHhN6B9dAT.FaRYWVVwbQFUNvj1Zc/hrvx54WAD7GC', '8-800', 'ROLE_ADMIN'),
            ('Alien', 'some.adress@ya.ru', '$2a$10$svvtLuz.vJ5HHhN6B9dAT.FaRYWVVwbQFUNvj1Zc/hrvx54WAD7GC', '8-700', 'ROLE_USER'),
            ('Tutor', 'third.adress@yu.ru', '$2a$10$svvtLuz.vJ5HHhN6B9dAT.FaRYWVVwbQFUNvj1Zc/hrvx54WAD7GC', '8-600', 'ROLE_USER');

INSERT INTO groups (leader, name, description)
    VALUES  (1, 'Itmo SE Students', '2023/2025 Itmo SE Students group'),
            (3, 'Itmo SE Tutors', 'ITMO SE Tutors');

INSERT INTO participants (user_id, group_id, status)
    VALUES  (1, 1, 0), -- Editor
            (2, 1, 1), -- User
            (3, 2, 0);