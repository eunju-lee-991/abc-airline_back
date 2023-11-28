INSERT INTO user(user_id, social_login_yn, email, sign_up_date, last_access_date)
VALUES (1, 0, 'abc@naver.com',current_date, current_date);
INSERT INTO user(user_id, social_login_yn, email, sign_up_date, last_access_date)
VALUES (2, 0, 'efg@gmail.com',current_date, current_date);


INSERT INTO airport(IATACode, name, country, continents, city)
VALUES ('ICN', 'Incheon International Airport', 'Korea', 'Asia', 'Seoul/Incheon');
INSERT INTO airport(IATACode, name, country, continents, city)
VALUES ('GMP', 'Gimpo International Airport', 'Korea', 'Asia', 'Seoul/Gimpo');
INSERT INTO airport(IATACode, name, country, continents, city)
VALUES ('PUS', 'Gimhae International Airport', 'Korea', 'Asia', 'Busan');
INSERT INTO airport(IATACode, name, country, continents, city)
VALUES ('ADL', 'Adelaide International Airport', 'Australia', 'Oceania', 'Adelaide');
INSERT INTO airport(IATACode, name, country, continents, city)
VALUES ('BNE', 'Brisbane International Airport', 'Australia', 'Oceania', 'Brisbane');
INSERT INTO airport(IATACode, name, country, continents, city)
VALUES ('NRT', 'Narita Airport', 'Japan', 'Asia', 'Tokyo');
INSERT INTO airport(IATACode, name, country, continents, city)
VALUES ('FUK', 'Fukuoka Airport', 'Japan', 'Asia', 'Fukuoka');
INSERT INTO airport(IATACode, name, country, continents, city)
VALUES ('DMK', 'Don Mueang International', 'Asia', 'Thailand', 'Bangkok');
INSERT INTO airport(IATACode, name, country, continents, city)
VALUES ('CNX', 'Chiang Mai International Airport', 'Asia', 'Thailand', 'Chiang Mai');


INSERT INTO  flight_route(route_id, departure_code, arrival_code)
VALUES (1, 'ICN', 'ADL');
INSERT INTO  flight_route(route_id, departure_code, arrival_code)
VALUES (10, 'ADL', 'ICN');
INSERT INTO  flight_route(route_id, departure_code, arrival_code)
VALUES (2, 'ICN', 'BNE');
INSERT INTO  flight_route(route_id, departure_code, arrival_code)
VALUES (3, 'BNE', 'ICN');
INSERT INTO  flight_route(route_id, departure_code, arrival_code)
VALUES (4, 'ICN', 'NRT');
INSERT INTO  flight_route(route_id, departure_code, arrival_code)
VALUES (5, 'NRT', 'ICN');
INSERT INTO  flight_route(route_id, departure_code, arrival_code)
VALUES (6, 'PUS', 'FUK');
INSERT INTO  flight_route(route_id, departure_code, arrival_code)
VALUES (7, 'FUK', 'PUS');
INSERT INTO  flight_route(route_id, departure_code, arrival_code)
VALUES (8, 'ICN', 'FUK');
INSERT INTO  flight_route(route_id, departure_code, arrival_code)
VALUES (9, 'FUK', 'ICN');


INSERT INTO  airplane(model, manufacturer, series, total_seat)
VALUES ('B787', 'BOEING', '9', 269);
INSERT INTO  airplane(model, manufacturer, series, total_seat)
VALUES ('B777', 'BOEING', '200ER', 261);
INSERT INTO  airplane(model, manufacturer, series, total_seat)
VALUES ('B747', 'BOEING', '8i', 368);
INSERT INTO  airplane(model, manufacturer, series, total_seat)
VALUES ('B737', 'BOEING', '900', 188);
INSERT INTO  airplane(model, manufacturer, series, total_seat)
VALUES ('A380', 'AIRBUS', '800', 407);
INSERT INTO  airplane(model, manufacturer, series, total_seat)
VALUES ('A330', 'AIRBUS', '200', 218);
INSERT INTO  airplane(model, manufacturer, series, total_seat)
VALUES ('A321', 'AIRBUS', 'neo', 182);
INSERT INTO  airplane(model, manufacturer, series, total_seat)
VALUES ('A220', 'AIRBUS', '300', 140);



INSERT INTO  flight (flight_id, route_id, airplane_model, flight_number, departure_date, arrival_date, price)
VALUES (1, 1, 'A220', 'AA703', '2023-12-12 00:00:00.000000', '2023-12-12 02:00:00.000000', 300000);
INSERT INTO  flight (flight_id, route_id, airplane_model, flight_number, departure_date, arrival_date, price)
VALUES (2, 1, 'A220', 'AA771', '2023-12-12 08:00:00.000000', '2023-12-12 10:00:00.000000', 300000);

INSERT INTO  flight (flight_id, route_id, airplane_model, flight_number, departure_date, arrival_date, price)
VALUES (3, 10, 'A321', 'AA711', '2023-12-15 08:00:00.000000', '2023-12-15 10:00:00.000000', 250000);
INSERT INTO  flight (flight_id, route_id, airplane_model, flight_number, departure_date, arrival_date, price)
VALUES (4, 10, 'A321', 'AA725', '2023-12-15 18:00:00.000000', '2023-12-15 20:00:00.000000', 250000);

INSERT INTO  flight (flight_id, route_id, airplane_model, flight_number, departure_date, arrival_date, price)
VALUES (5, 2, 'A220', 'AA781', '2023-12-12 12:00:00.000000', '2023-12-12 14:00:00.000000', 400000);
INSERT INTO  flight (flight_id, route_id, airplane_model, flight_number, departure_date, arrival_date, price)
VALUES (6, 3, 'A220', 'AA781', '2023-12-15 12:00:00.000000', '2023-12-15 14:00:00.000000', 400000);

INSERT INTO  flight (flight_id, route_id, airplane_model, flight_number, departure_date, arrival_date, price)
VALUES (7, 4, 'A220', 'AA331', '2023-12-15 10:00:00.000000', '2023-12-15 12:00:00.000000', 200000);
INSERT INTO  flight (flight_id, route_id, airplane_model, flight_number, departure_date, arrival_date, price)
VALUES (8, 4, 'A220', 'AA337', '2023-12-16 10:00:00.000000', '2023-12-16 12:00:00.000000', 250000);

INSERT INTO  flight (flight_id, route_id, airplane_model, flight_number, departure_date, arrival_date, price)
VALUES (9, 6, 'B787', 'AA931', '2023-12-12 10:00:00.000000', '2023-12-12 12:00:00.000000', 270000);
INSERT INTO  flight (flight_id, route_id, airplane_model, flight_number, departure_date, arrival_date, price)
VALUES (10, 7, 'B777', 'AA391', '2023-12-15 14:00:00.000000', '2023-12-15 16:00:00.000000', 220000);

INSERT INTO  flight (flight_id, route_id, airplane_model, flight_number, departure_date, arrival_date, price)
VALUES (11, 5, 'A220', 'AA331', '2023-12-17 15:00:00.000000', '2023-12-17 17:00:00.000000', 350000);
INSERT INTO  flight (flight_id, route_id, airplane_model, flight_number, departure_date, arrival_date, price)
VALUES (12, 5, 'A220', 'AA351', '2023-12-18 15:00:00.000000', '2023-12-18 17:00:00.000000', 300000);


INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (1, 1, 'A1', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (2, 1, 'A2', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (3, 1, 'A3', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (4, 1, 'A4', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (5, 1, 'A5', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (6, 1, 'A6', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (7, 1, 'B1', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (8, 1, 'B2', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (9, 1, 'B3', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (10, 1, 'B4', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (11, 1, 'B5', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (12, 1, 'B6', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (13, 1, 'C1', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (14, 1, 'C2', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (15, 1, 'C3', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (16, 1, 'C4', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (17, 1, 'C5', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (18, 1, 'C6', 1);


INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (19, 11, 'A1', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (20, 11, 'A2', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (21, 11, 'A3', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (22, 11, 'A4', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (23, 11, 'A5', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (24, 11, 'A6', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (25, 11, 'B1', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (26, 11, 'B2', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (27, 11, 'B3', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (28, 11, 'B4', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (29, 11, 'B5', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (30, 11, 'B6', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (31, 11, 'C1', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (32, 11, 'C2', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (33, 11, 'C3', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (34, 11, 'C4', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (35, 11, 'C5', 1);
INSERT INTO seat (id, flight_id, seat_number, is_available) VALUES (36, 11, 'C6', 1);

INSERT INTO reservation (id, user_id, flight_id, reservation_price, seat_number, status)
VALUES (10000, 1, 11, 250000, 'A1', 'CONFIRMED');