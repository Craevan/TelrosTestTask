INSERT INTO USERS (SURNAME, FIRSTNAME, PATRONYMIC, PASSWORD, EMAIL, PHONE_NUMBER, BIRTHDATE)
VALUES ('Adminskiy', 'Admin', 'Adminovich', '{noop}admin', 'admin@gmail.com', '79001112233', '1988-08-26'),
       ('Userov', 'User', 'Userovich', '{noop}password', 'user@gmail.com', '79002223344', '2000-02-02');

INSERT INTO USER_ROLE (role, user_id)
VALUES ('ADMIN', 1),
       ('USER', 2);