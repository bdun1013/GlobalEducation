<?php
	require 'connect.php';
	
	if (isset($_GET['username']) && isset($_GET['password']) &&
	    isset($_GET['level']) && isset($_GET['city']) &&
	    isset($_GET['state']) && isset($_GET['country']) &&
	    isset($_GET['name'])) {
		
		$username = ($_GET['username']);
		$password = ($_GET['password']);
		$level = ($_GET['level']);
		$city = ($_GET['city']);
		$state = ($_GET['state']);
		$country = ($_GET['country']);
		$name = ($_GET['name']);
		
		$query = "INSERT INTO child 
			  VALUES('$username', '$password', '$level', '$city', '$state', '$country', '$name')";
		$result = mysql_query($query);
		
	}
	
	mysql_close($con);

?>