--changeset lukesukhanov:v1.5-create-authorities-table

SET search_path TO guess_number_game;

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
