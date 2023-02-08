#!/bin/bash
psql -v ON_ERROR_STOP=1 -U postgres <<-EOSQL
  \connect user_db
  BEGIN;
    CREATE TABLE IF NOT EXISTS users (
	  id serial NOT NULL ,
	  username VARCHAR(50) UNIQUE,
	  password VARCHAR(50)
	);
	CREATE TABLE IF NOT EXISTS roles (
	  id serial NOT NULL,
	  value VARCHAR(50) UNIQUE
	);
	CREATE TABLE IF NOT EXISTS users_roles (
	  id serial NOT NULL,
	  user_id INTEGER NOT NULL,
	  role_id INTEGER NOT NULL
	);
	CREATE TABLE IF NOT EXISTS token(
		id serial NOT NULL,
		token VARCHAR(2048),
		valid boolean NOT NULL,
		timestamp VARCHAR(50)
	);
	INSERT INTO ROLES (VALUE) VALUES ('admin');
	INSERT INTO ROLES (VALUE) VALUES ('content_manager');
	INSERT INTO ROLES (VALUE) VALUES ('user');
	INSERT INTO USERS (USERNAME, PASSWORD) VALUES ('admin', 'admin');
	INSERT INTO USERS_ROLES (USER_ID, ROLE_ID) VALUES (1, 1);
	INSERT INTO USERS_ROLES (USER_ID, ROLE_ID) VALUES (1, 3);
  COMMIT;
EOSQL