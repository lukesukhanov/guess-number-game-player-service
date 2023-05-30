--changeset lukesukhanov:add-players-dev

SET search_path TO guess_my_number_game_dev;

INSERT INTO player
	(username, best_attempts_count)
	VALUES ('vasya99', 8);
		