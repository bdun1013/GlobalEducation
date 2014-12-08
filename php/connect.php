<?php
# Enter appropriate hostname, username, and password for your local mysql setup
# Make sure to call mysql_close($con) after your database calls
$hostname = "localhost";
$username = "cmsc436_cmsc436";
$dbname = "cmsc436_globalEducation";
$password = "jM4zX6QbY75z426V";

//connection to the database
$con = mysql_connect($hostname, $username, $password) 
  or die("Unable to connect to MySQL");


mysql_select_db($dbname) 
  or die("Unable to select database: " . mysql_error());

?>