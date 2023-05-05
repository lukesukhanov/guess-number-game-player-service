--changeset Luke Sukhanov:create-schema

-- Create a new schema.
DROP SCHEMA IF EXISTS game CASCADE;
CREATE SCHEMA game
	AUTHORIZATION postgres;

-----------------------------------
	
-- Create a single sequence for all 'id' columns.
DROP SEQUENCE IF EXISTS game.common_id_seq CASCADE;
CREATE SEQUENCE game.common_id_seq
	AS bigint
	MINVALUE 1
	START 1
	INCREMENT BY 5
	CACHE 1;
	
-----------------------------------
	
-- Create a trigger function for synchronizing 'last_modified' column during UPDATE.
DROP FUNCTION IF EXISTS game.sync_last_modified CASCADE;
CREATE FUNCTION game.sync_last_modified() RETURNS trigger AS
$$
BEGIN
	NEW.last_modified := NOW();
	RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Create a trigger function for synchronizing 'version' column during INSERT.
DROP FUNCTION IF EXISTS game.sync_version_insert CASCADE;
CREATE FUNCTION game.sync_version_insert() RETURNS trigger AS
$$
BEGIN
	NEW.version := 1;
	RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Create a trigger function for synchronizing 'version' column during UPDATE.
DROP FUNCTION IF EXISTS game.sync_version_update CASCADE;
CREATE FUNCTION game.sync_version_update() RETURNS trigger AS
$$
BEGIN
	NEW.version := OLD.version + 1;
	RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-----------------------------------

-- Create a table called 'player'.
DROP TABLE IF EXISTS game.player CASCADE;
CREATE TABLE game.player (
	id bigint NOT NULL PRIMARY KEY DEFAULT nextval('game.common_id_seq'),
	username varchar NOT NULL UNIQUE,
	min_attempts_count integer CHECK(min_attempts_count > 0),
	created timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
	last_modified timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
	version integer
);

ALTER TABLE game.player
	OWNER to postgres;

-- Create a trigger for updating the 'last_modified' column.
DROP TRIGGER IF EXISTS sync_last_modified ON game.player CASCADE;
CREATE TRIGGER sync_last_modified
	BEFORE UPDATE
	ON game.player
	FOR EACH ROW 
	EXECUTE FUNCTION game.sync_last_modified();
	
-- Create a trigger for synchronizing 'version' column during INSERT.
DROP TRIGGER IF EXISTS sync_version_insert ON game.player CASCADE;
CREATE TRIGGER sync_version_insert
	BEFORE INSERT
	ON game.player
	FOR EACH ROW 
	EXECUTE FUNCTION game.sync_version_insert();
	
-- Create a trigger for synchronizing 'version' column during UPDATE.
DROP TRIGGER IF EXISTS sync_version_update ON game.player CASCADE;
CREATE TRIGGER sync_version_update
	BEFORE UPDATE
	ON game.player
	FOR EACH ROW 
	EXECUTE FUNCTION game.sync_version_update();