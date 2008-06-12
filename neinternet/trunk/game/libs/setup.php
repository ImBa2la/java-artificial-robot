<?php

require(GAME_DIR . 'libs/sql.lib.php');
require(GAME_DIR . 'libs/game.lib.php');
require(SMARTY_DIR . 'Smarty.class.php');
require('DB.php'); // PEAR DB

// database configuration
class Game_SQL extends SQL {
    function Game_SQL() {
        $dsn = "mysql://neinternet_drupa:z1x2c3v4@localhost/neinternet_drupa";
        $this->connect($dsn) || die('could not connect to database');
    }       
}

// smarty configuration
class Game_Smarty extends Smarty {
    function Game_Smarty() {
        $this->template_dir = GAME_DIR . 'templates';
        $this->compile_dir = GAME_DIR . 'templates_c';
        $this->config_dir = GAME_DIR . 'configs';
        $this->cache_dir = GAME_DIR . 'cache';
    }
}
      
?>
