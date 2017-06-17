DROP TABLE IF EXISTS game;
CREATE TABLE game (
  id SERIAL,
  name VARCHAR(255) NOT NULL,
  big_blind INTEGER NOT NULL,
  max_players_num INTEGER NOT NULL,
  current_players_num INTEGER,
  PRIMARY KEY (id)
);

INSERT INTO game (name, big_blind, max_players_num, current_players_num)
VALUES ('Ramins favourite table', '500', '6', '2');
VALUES ('Lucky Gabors winning table', '200', '4', NULL);
VALUES ('Norberts table', '100', '2', '1');