DROP TABLE IF EXISTS game;

CREATE TABLE game (
  game_id int(11) NOT NULL auto_increment,
  name text,
  score text,
  start_time timestamp,
  period int default 1,
  PRIMARY KEY  (game_id)
) ENGINE=InnoDB;


DROP TABLE IF EXISTS online;

CREATE TABLE online (
  id int(11) NOT NULL auto_increment,
  val text,
  add_time timestamp NOT NULL default CURRENT_TIMESTAMP,
  game_id int(11) default NULL,
  PRIMARY KEY  (id)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS comment;

CREATE TABLE comment (
  id int(11) NOT NULL auto_increment,
  name text,
  val text,
  add_time timestamp NOT NULL default CURRENT_TIMESTAMP,
  game_id int(11) default NULL,
  PRIMARY KEY  (id)
) ENGINE=InnoDB;

