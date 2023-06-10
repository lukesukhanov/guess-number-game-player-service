--changeset lukesukhanov:v1.3-create-player-table

SET search_path TO guess_number_game;

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
