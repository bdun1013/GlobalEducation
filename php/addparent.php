<?php
	require 'connect.php';
	
	if (isset($_GET['username']) && isset($_GET['password'])) {
		
		$username = ($_GET['username']);
		$password = ($_GET['password']);
		
		$query = "INSERT INTO parent
		          VALUES('$username', '$password')";
		$result = mysql_query($query);
		
	}
	
	mysql_close($con);

?>
