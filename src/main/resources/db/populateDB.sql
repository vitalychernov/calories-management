DELETE
FROM users;
DELETE
FROM user_roles;
DELETE
FROM meals;

ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100000, '2020-01-30 07:00', 'User Breakfast', 700),
       (100000, '2020-01-30 12:00', 'User lunch', 1000),
       (100000, '2020-01-30 19:00', 'User dinner', 300),
       (100000, '2020-01-31 00:00', 'User border meal', 300),
       (100001, '2020-01-31 07:00', 'Admin breakfast', 300),
       (100001, '2020-01-31 14:00', 'Admin lunch', 600),
       (100001, '2020-01-31 22:00', 'Admin dinner', 500);
