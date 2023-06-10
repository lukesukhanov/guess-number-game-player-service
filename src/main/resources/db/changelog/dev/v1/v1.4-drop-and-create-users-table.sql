--changeset lukesukhanov:v1.4-drop-and-create-users-table

SET search_path TO guess_number_game_dev;

-------- 'users' table  --------

-- Table
DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users (
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
