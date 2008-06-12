<?php
define('GAME_DIR', realpath('').'/');
define('SMARTY_DIR', realpath('').'/../Smarty/libs/');
include(GAME_DIR . 'libs/setup.php');

$game =& new Game;
$game->init($_GET);
$code = '14446';
if(isset($_COOKIE[$code]) && $code == $_COOKIE[$code] ) {
   $game->set('admin', 'admin');
}

$_action = isset($_REQUEST['action']) ? $_REQUEST['action'] : 'view';

switch($_action) {
    case 'new_game':
        $game->displayGameForm();
        break;
    case 'game':
        $game->mungeFormData($_POST);
        if($game->isValidGameForm($_POST)) {
            $game->update($_POST);
        }
        break;
    case 'edit':
        $game->edit($_GET);
        break;
    case 'delete':
        $game->deleteEntity($_GET);
        break;
    case 'data':
        $game->editEntity($_POST);
        break;
    case 'submit':
      	$game->mungeFormData($_POST);
        if($game->isValidForm($_POST)) {
            $game->addEntry($_POST);
        }
        break;
    case 'comment':
		    $game->mungeFormData($_POST);
        if($game->isValidCommentForm($_POST)) {
            $game->addComment($_POST);
        }
        break;
}

$game->displayGame($game->getEntries(), $game->getComments());

?>
