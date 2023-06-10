--changeset lukesukhanov:v1.1-drop-and-create-id-sequence

SET search_path TO guess_number_game_dev;

-------- Sequences  --------

-- Single sequence for all 'id' columns
DROP SEQUENCE IF EXISTS common_id_seq CASCADE;
CREATE SEQUENCE common_id_seq
	AS bigint
	MINVALUE 1
	START 1
	INCREMENT BY 5
	CACHE 1;
