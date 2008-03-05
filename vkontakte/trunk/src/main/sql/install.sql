DROP TABLE IF EXISTS `vkontakte_status`;

CREATE TABLE `vkontakte_status` (
  `id` int(11) NOT NULL,
  `last_seen_date` DATETIME default NULL,
  `last_session_date` DATETIME default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM;


DROP TABLE IF EXISTS `vkontakte_session`;

CREATE TABLE `vkontakte_session` (
  `id` int(11) NOT NULL,
  `start_session` DATETIME default NULL,
  `end_session` DATETIME default NULL
) ENGINE=MyISAM;

DROP TABLE IF EXISTS `user_online`;

CREATE TABLE `user_online` (
  `id` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM;


DROP TABLE IF EXISTS `vkontakte_info`;

CREATE TABLE `vkontakte_info` (
  `id` int(11) NOT NULL,
  `name` varchar(255) default NULL,
  `picture` text default NULL,
  `last_check` DATETIME default NULL,
   PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=cp1251;

DROP TABLE IF EXISTS `vkontakte_info_history`;

CREATE TABLE `vkontakte_info_history` (
  `id` int(11) NOT NULL,
  `name` varchar(255) default NULL,
  `picture` text default NULL,
  `check_time` DATETIME default NULL,
   KEY  `id`(`id`),
   KEY  `check_time`(`check_time`)
) ENGINE=MyISAM;


DROP TABLE IF EXISTS `vkontakte_interest`;

CREATE TABLE `vkontakte_interest` (
  `id` int(11) NOT NULL,
  `f` varchar(10) default NULL,
   PRIMARY KEY  (`id`)
) ENGINE=MyISAM;


DROP TABLE IF EXISTS `vkontakte_queue`;

CREATE TABLE `vkontakte_queue` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`uid` varchar(50),
	`path` text,
	`done` int default 0,
     PRIMARY KEY (`id`),
	KEY `done` (`done`)
) ENGINE=MyISAM;