--changeset lukesukhanov:v1.1-create-id-sequence

SET search_path TO guess_number_game;

-------- Sequences  --------

-- Single sequence for all 'id' columns
CREATE SEQUENCE IF NOT EXISTS common_id_seq
	AS bigint
	MINVALUE 1
	START 1
	INCREMENT BY 5
	CACHE 1;
