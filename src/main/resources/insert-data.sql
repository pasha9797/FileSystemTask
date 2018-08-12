INSERT INTO _user VALUES (1, 'admin', 'admin');
INSERT INTO _user VALUES (2, 'test', 'test');

INSERT INTO _permission VALUES (1, 'read');
INSERT INTO _permission VALUES (2, 'write');
INSERT INTO _permission VALUES (3, 'admin');

INSERT INTO _user_permission VALUES (1, 1, 1);
INSERT INTO _user_permission VALUES (2, 1, 2);
INSERT INTO _user_permission VALUES (3, 1, 3);
INSERT INTO _user_permission VALUES (4, 2, 1);
INSERT INTO _user_permission VALUES (5, 2, 2);