INSERT INTO users (nickname, email, password, phone)
    VALUES  ('IThror', 'thror.dainson@ya.ru', '12345', '8-800'),
            ('Alien', 'some.adress@ya.ru', 'qwerty', '8-700'),
            ('Tutor', 'third.adress@yu.ru', '1q2w3e', '8-600');

INSERT INTO groups (leader, name, description)
    VALUES  (1, 'Itmo SE Students', '2023/2025 Itmo SE Students group'),
            (3, 'Itmo SE Tutors', 'ITMO SE Tutors');

INSERT INTO participants (user_id, group_id, status)
    VALUES  (1, 1, 0), -- Editor
            (2, 1, 1), -- User
            (3, 2, 0);