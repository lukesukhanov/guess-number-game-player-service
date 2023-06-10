--changeset lukesukhanov:v1.2-create-trigger-functions

SET search_path TO guess_number_game;

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
