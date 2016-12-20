<?php
 define('HOST','localhost');
 define('USER','user');
 define('PASS','pass');
 define('DB','database');
 
 $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');

?>
