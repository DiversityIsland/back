INSERT INTO city
VALUES ('1', 'city1');
INSERT INTO city
VALUES ('2', 'city2');
INSERT INTO city
VALUES ('3', 'city3');

INSERT INTO country
VALUES ('1', 'country1');
INSERT INTO country
VALUES ('2', 'country2');
INSERT INTO country
VALUES ('3', 'country3');

INSERT INTO address
VALUES (1, 'city_index1', 'house1', 'street1', 1, 1);
INSERT INTO address
VALUES (2, 'city_index2', 'house2', 'street2', 2, 2);
INSERT INTO address
VALUES (3, 'city_index3', 'house3', 'street3', 3, 3);


INSERT INTO platform.user (id, activate, activation_code, age, birthday, email, first_name, gender, is_using_two_factor_auth, last_name, password, phone, secret, username, address_id, favorite_id, image_id) VALUES (3, true, '32e32d23d23', 44, '2021-05-07 17:44:56', 'ddd@mail.ru', 'dddddddd', null, false, null, '123456', '89110249729', null, 'petr', null, null, null);
INSERT INTO platform.item (id) VALUES (1);
INSERT INTO platform.shop (id) VALUES (1);


INSERT INTO role
VALUES ('3', 'USER');

INSERT INTO user_role
VALUES ('3', '3');

INSERT INTO country_city
VALUES (3, 3);


