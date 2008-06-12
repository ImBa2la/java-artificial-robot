<?php
$code = '14446';
if(isset($_COOKIE[$code]) && $_COOKIE[$code] == $code) {
                             setcookie('14446', '0');
} else {
  setcookie('14446', '14446');
}
?>
