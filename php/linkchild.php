<?php
	require 'connect.php';
	
	if (isset($_GET['parentUsername']) && isset($_GET['childUsername'])) {
		
		$parentUsername = ($_GET['parentUsername']);
		$childUsername = ($_GET['childUsername']);
		
		$query = "INSERT INTO parentChild 
		          VALUES('$parentUsername', '$childUsername')";
		$result = mysql_query($query);
		
	}
	
	mysql_close($con);

?>