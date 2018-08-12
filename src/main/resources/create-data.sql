CREATE TABLE _user (
  id        INTEGER PRIMARY KEY,
  username  VARCHAR(30),
  password  VARCHAR(50)
);

CREATE TABLE _permission (
  id   INTEGER PRIMARY KEY,
  name VARCHAR(30)
);

CREATE TABLE _user_permission (
  id   INTEGER PRIMARY KEY,
  user_id INTEGER,
  permission_id INTEGER,
  FOREIGN KEY (user_id) REFERENCES _user(id),
  FOREIGN KEY (permission_id) REFERENCES _permission(id),
);