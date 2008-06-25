<?php

function load() {
    $map = array();
	$lines = file('a');
	$current = "";
	$c = array();
	foreach ($lines as $line_num => $line) {
         $p = strpos ( $line , "=");
         if($p !== FALSE) {
               $map[$current] = $c;
               $current = substr($line, 0, $p);
               $c = array();
         } else {
           $c[] = $line;
         }
	}
    $map[$current] = $c;
	return $map;
 }
 
 
  function print_word($str, $map, $im, $y, $w, $h, $v = false, $stX = 1, $stY = 1, $d = 3) {
    for($i =0; $i < strlen($str); $i++) {
           $c = $map[substr($str, $i, 1)];
           $mX = 0;
           if(isset($c)) {
             foreach ($c as $j => $line) {
                 for($l =0; $l < strlen($line); $l++) {
                        if(substr($line, $l, 1) == "*") {
                           if($v) {
                        	imagecopy($im,$y,$stX + $d * $l ,$stY + $j* $d,0,0,$w,$h);
                           }
                        	$mX = max($mX, $l);
                        }
                 }
             }
             $stX = $stX + $d * ($mX + 2);
           }
           if(substr($str, $i, 1) == "_") {
             $stX = $stX + $d * 5;
           }
    }
    return $stX - 2*$d;
 }
	
	$map = load();
    header("Content-Type: image/png");
	$im = @imagecreatefrompng("hockey.png");
	$y = @imagecreatefrompng("y.png");
	$w = imagesx($y);
	$h = imagesy($y);
    $d = 3;
    
    $str = $_GET["word"];

    $stX = 1 + $d * floor((imagesx($im) - print_word($str,$map, $im, $y, $w, $h, false, 1, 1, $d)) / (2*$d));
    $stY = 1 + 12 * $d;
    print_word($str,$map, $im, $y, $w, $h, true, $stX, $stY, $d);
	imagepng($im);
?>
