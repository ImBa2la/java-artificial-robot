<?php

/**
 * Project: Guestbook Sample Smarty Application
 * Author: Monte Ohrt <monte [AT] ohrt [DOT] com>
 * Date: March 14th, 2005
 * File: guestbook.lib.php
 * Version: 1.0
 */

/**
 * guestbook application library
 *
 */
class Game {

    var $sql = null;
    var $tpl = null;
    var $error = null;
    var $gameId = 0;
    var $name = "";
    var $score = "";
    var $start = null;
    var $period = 0; 
    var $archive = 0;
    
    function Game() {
        $this->sql =& new Game_SQL;
        $this->tpl =& new Game_Smarty;
    }
    
    function init($formvars) {
	$formvars = array_change_key_case($formvars, CASE_LOWER);
        $this->gameId = $formvars['gameid'];
        $this->sql->query(sprintf("select * from game where game_id = %s", $this->gameId), SQL_INIT, SQL_ASSOC);
        $game = $this->sql->record;
        // Create game if authorized
        $this->name = $game["name"];
        $this->score = $game["score"];
        $this->start = $game["start_time"];
        $this->period = $game["period"];
        $this->archive = $game["archive"];
    }
    
    function update($formvars) {
        $_query = sprintf("update game set name = '%s', score = '%s' where game_id = %s",
                mysql_escape_string($formvars['nameValue']), mysql_escape_string($formvars['score']), $this->gameId);
        return $this->sql->query($_query);
    }
    
    function deleteEntity($formvars) {
        $_query = sprintf("delete from online where game_id = %s and id = %s", $this->gameId, $formvars['id']);
        return $this->sql->query($_query);
    }
    
    function edit($formvars) {
        $this->tpl->assign('editId', $formvars['id']);
    }
    
    function editEntity($formvars) {
        $_query = sprintf("update online set val = '%s' where game_id = %s and id = %s", mysql_escape_string($formvars['val']), $this->gameId, $formvars['editId']);
        return $this->sql->query($_query);
    }

    function mungeFormData(&$formvars) {
        $formvars['Comment'] = trim($formvars['Comment']);
        $formvars['userComment'] = trim($formvars['userComment']);
        $formvars['userName'] = trim($formvars['userName']);
        $formvars['nameValue'] = trim($formvars['nameValue']);
        $formvars['score'] = trim($formvars['score']);
	}

    function isValidForm($formvars) {
        $this->error = null;
        if(strlen($formvars['Comment']) == 0) {
            $this->error = 'comment_empty';
            return false; 
        }
        return true;
    }
    
    function isValidCommentForm($formvars) {
        $this->error = null;
        if(strlen($formvars['userName']) == 0) {
            $this->error = 'user_name_empty';
            return false;
        }
        if(strlen($formvars['userComment']) == 0) {
            $this->error = 'user_comment_empty';
            return false; 
        }
        return true;
    }
    
    
    function isValidGameForm($formvars) {
        $this->error = null;
        if(strlen($formvars['nameValue']) == 0) {
            $this->error = 'name_empty';
            return false;
        }
        if(strlen($formvars['score']) == 0) {
            $this->error = 'score_empty';
            echo "score_empty";
            return false;
        }
        return true;
    }
    
    function addEntry($formvars) {
        $_query = sprintf("insert into online(val, game_id, mnt) select '%s' as val,%s as game_id , (TIMESTAMPDIFF(MINUTE,start_time, NOW()) + 45*%s ) as mnt from game where game_id = %s",
                mysql_escape_string($formvars['Comment']), $this->gameId, $this->period, $this->gameId);
        return $this->sql->query($_query);
    }
    
    function addComment($formvars) {
        $_query = sprintf("insert into comment(name, val, game_id) values('%s', '%s', %s)",
                mysql_escape_string($formvars['userName']), mysql_escape_string($formvars['userComment']), $this->gameId);
        return $this->sql->query($_query);
    }
    
    
    function getEntries() {
        $this->sql->query(sprintf("select * from online where game_id = %s order by add_time ". ($this->archive == 0 ? "DESC": ""), $this->gameId),
			     SQL_ALL,
			     SQL_ASSOC
		    );
        return $this->sql->record;
    }
    
    function getComments() {
        $this->sql->query(sprintf("select * from comment where game_id = %s order by add_time", $this->gameId),
  			SQL_ALL,
	   		SQL_ASSOC
		);
        return $this->sql->record;
    }    
    
    function displayGame($data = array(), $comments = array()) {
        $this->tpl->assign('data', $data);
        $this->tpl->assign('comments', $comments);
        $this->tpl->assign('game', $this);
        $this->tpl->display('game2.tpl');
    }
    
    function set($key, $value) {
        $this->tpl->assign($key, $value);
    }
}

?>
