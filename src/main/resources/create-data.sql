CREATE TABLE _user (
  id        BIGINT IDENTITY PRIMARY KEY,
  username  VARCHAR(100),
  password  VARCHAR(128)
);

CREATE TABLE _permission (
  id   BIGINT IDENTITY PRIMARY KEY ,
  name VARCHAR(30)
);

CREATE TABLE _user_permission (
  user_id INTEGER,
  permission_id INTEGER,
  FOREIGN KEY (user_id) REFERENCES _user(id),
  FOREIGN KEY (permission_id) REFERENCES _permission(id),
);