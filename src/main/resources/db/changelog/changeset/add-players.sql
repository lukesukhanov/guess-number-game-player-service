--changeset Luke Sukhanov:add-players

SET search_path TO guess_my_number_game;

INSERT INTO player
	(username, best_attempts_count)
	VALUES ('vasya99', 8);
		