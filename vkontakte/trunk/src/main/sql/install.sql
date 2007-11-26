DROP TABLE IF EXISTS `vkontakte_status`;

CREATE TABLE `vkontakte_status` (
  `id` int(11) NOT NULL,
  `name` varchar(255) default NULL,
  `last_seen_date` DATETIME default NULL,
  `last_session_date` DATETIME default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=cp1251;


DROP TABLE IF EXISTS `vkontakte_session`;

CREATE TABLE `vkontakte_session` (
  `id` int(11) NOT NULL,
  `start_session` DATETIME default NULL,
  `end_session` DATETIME default NULL
) ENGINE=MyISAM DEFAULT CHARSET=cp1251;

DROP TABLE IF EXISTS `user_online`;

CREATE TABLE `user_online` (
  `id` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=cp1251;