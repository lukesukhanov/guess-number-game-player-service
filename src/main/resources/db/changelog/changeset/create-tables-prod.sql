--changeset lukesukhanov:create-tables-prod

CREATE SCHEMA IF NOT EXISTS guess_my_number_game;

SET search_path TO guess_my_number_game;

-------- Sequences  --------

-- Single sequence for all 'id' columns
CREATE SEQUENCE IF NOT EXISTS common_id_seq
	AS bigint
	MINVALUE 1
	START 1
	INCREMENT BY 5
	CACHE 1;

-------- Trigger functions  --------

-- Trigger function for synchronizing 'created' column during INSERT
CREATE OR REPLACE FUNCTION sync_insert_created_column() RETURNS trigger AS
$$
BEGIN
	NEW.created := now();
	RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Trigger function for synchronizing 'created' column during UPDATE
CREATE OR REPLACE FUNCTION sync_update_created_column() RETURNS trigger AS
$$
BEGIN
	NEW.created := OLD.created;
	RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Trigger function for synchronizing 'last_modified' column during INSERT and UPDATE
CREATE OR REPLACE FUNCTION sync_update_last_modified_column() RETURNS trigger AS
$$
BEGIN
	NEW.last_modified := now();
	RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Trigger function for synchronizing 'version' column during INSERT
CREATE OR REPLACE FUNCTION sync_insert_version_column() RETURNS trigger AS
$$
BEGIN
	NEW.version := 1;
	RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Trigger function for synchronizing 'version' column during UPDATE
CREATE OR REPLACE FUNCTION sync_update_version_column() RETURNS trigger AS
$$
BEGIN
	NEW.version := OLD.version + 1;
	RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-------- 'player' table  --------

-- Table
CREATE TABLE IF NOT EXISTS player (
	id bigint NOT NULL PRIMARY KEY DEFAULT nextval('common_id_seq'),
	username varchar NOT NULL UNIQUE,
	best_attempts_count integer CHECK(best_attempts_count > 0),
	created timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
	last_modified timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
	version integer
);

-- Trigger for inserting the 'created' column
DROP TRIGGER IF EXISTS sync_insert_created_column ON player;
CREATE TRIGGER sync_insert_created_column
	BEFORE INSERT
	ON player
	FOR EACH ROW 
	EXECUTE FUNCTION sync_insert_created_column();

-- Trigger for updating the 'created' column
DROP TRIGGER IF EXISTS sync_update_created_column ON player;
CREATE TRIGGER sync_update_created_column
	BEFORE UPDATE
	ON player
	FOR EACH ROW 
	EXECUTE FUNCTION sync_update_created_column();

-- Trigger for inserting the 'last_modified' column
DROP TRIGGER IF EXISTS sync_update_last_modified_column ON player;
CREATE TRIGGER sync_update_last_modified_column
	BEFORE INSERT
	ON player
	FOR EACH ROW 
	EXECUTE FUNCTION sync_update_last_modified_column();

-- Trigger for updating the 'last_modified' column
DROP TRIGGER IF EXISTS sync_update_last_modified_column ON player;
CREATE TRIGGER sync_update_last_modified_column
	BEFORE UPDATE
	ON player
	FOR EACH ROW 
	EXECUTE FUNCTION sync_update_last_modified_column();

-- Trigger for synchronizing 'version' column
DROP TRIGGER IF EXISTS sync_insert_version_column ON player;
CREATE TRIGGER sync_insert_version_column
	BEFORE INSERT
	ON player
	FOR EACH ROW 
	EXECUTE FUNCTION sync_insert_version_column();

-- Trigger for synchronizing 'version' column
DROP TRIGGER IF EXISTS sync_update_version_column ON player;
CREATE TRIGGER sync_update_version_column
	BEFORE UPDATE
	ON player
	FOR EACH ROW 
	EXECUTE FUNCTION sync_update_version_column();

-------- 'users' table  --------

-- Table
CREATE TABLE IF NOT EXISTS users (
	username varchar NOT NULL PRIMARY KEY,
	password varchar NOT NULL,
	enabled boolean NOT NULL,
	created timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
	last_modified timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Trigger for inserting the 'created' column
DROP TRIGGER IF EXISTS sync_insert_created_column ON users;
CREATE TRIGGER sync_insert_created_column
	BEFORE INSERT
	ON users
	FOR EACH ROW 
	EXECUTE FUNCTION sync_insert_created_column();

-- Trigger for updating the 'created' column
DROP TRIGGER IF EXISTS sync_update_created_column ON users;
CREATE TRIGGER sync_update_created_column
	BEFORE UPDATE
	ON users
	FOR EACH ROW 
	EXECUTE FUNCTION sync_update_created_column();

-- Trigger for inserting the 'last_modified' column
DROP TRIGGER IF EXISTS sync_update_last_modified_column ON users;
CREATE TRIGGER sync_update_last_modified_column
	BEFORE INSERT
	ON users
	FOR EACH ROW 
	EXECUTE FUNCTION sync_update_last_modified_column();

-- Trigger for updating the 'last_modified' column
DROP TRIGGER IF EXISTS sync_update_last_modified_column ON users;
CREATE TRIGGER sync_update_last_modified_column
	BEFORE UPDATE
	ON users
	FOR EACH ROW 
	EXECUTE FUNCTION sync_update_last_modified_column();

-------- 'authorities' table  --------

-- Table
CREATE TABLE IF NOT EXISTS authorities (
	username varchar NOT NULL REFERENCES users(username) ON DELETE CASCADE,
	authority varchar NOT NULL,
	created timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
	last_modified timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (username, authority)
);

-- Trigger for inserting the 'created' column
DROP TRIGGER IF EXISTS sync_insert_created_column ON authorities;
CREATE TRIGGER sync_insert_created_column
	BEFORE INSERT
	ON authorities
	FOR EACH ROW 
	EXECUTE FUNCTION sync_insert_created_column();

-- Trigger for updating the 'created' column
DROP TRIGGER IF EXISTS sync_update_created_column ON authorities;
CREATE TRIGGER sync_update_created_column
	BEFORE UPDATE
	ON authorities
	FOR EACH ROW 
	EXECUTE FUNCTION sync_update_created_column();

-- Trigger for inserting the 'last_modified' column
DROP TRIGGER IF EXISTS sync_update_last_modified_column ON authorities;
CREATE TRIGGER sync_update_last_modified_column
	BEFORE INSERT
	ON authorities
	FOR EACH ROW 
	EXECUTE FUNCTION sync_update_last_modified_column();

-- Trigger for updating the 'last_modified' column
DROP TRIGGER IF EXISTS sync_update_last_modified_column ON authorities;
CREATE TRIGGER sync_update_last_modified_column
	BEFORE UPDATE
	ON authorities
	FOR EACH ROW 
	EXECUTE FUNCTION sync_update_last_modified_column();