<?php
	require 'connect.php';
	
	if (isset($_GET['username']) && isset($_GET['password'])) {
		
		$username = ($_GET['username']);
		$password = ($_GET['password']);
		
		$query = "SELECT username
			  FROM parent
			  WHERE username = '$username' AND password = '$password'";
		
		$result = mysql_query($query);
		
		if (mysql_num_rows($result) == 1) { // Person who is trying to log in is a parent
			$arr = array('login' => 'parent');
		} 
		else {
			$query = "SELECT username
			          FROM child
			          WHERE username = '$username' AND password = '$password'";
		
			$result = mysql_query($query);
		
			if (mysql_num_rows($result) == 1) { // Person who is trying to log in is a child
				$arr = array('login' => 'child');
			}
			else {  // Invalid login credentials
				$arr = array('login' => 'invalid');
			}
		}
		
		echo json_encode($arr);			
	}
	
	mysql_close($con);

?>