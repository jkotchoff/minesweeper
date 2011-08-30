<?php

$filename = "top_scores.xml";

$fp = fopen($filename, "r");
$contents = fread($fp, filesize($filename));
fclose($fp);

printf($contents);

?>
