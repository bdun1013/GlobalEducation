<?php
	require 'connect.php';
	
	if (isset($_GET['username'])) {
		
		$username = ($_GET['username']);
		
		$query = "SELECT childUsername
			  FROM parentChild
			  WHERE parentUsername = '$username'";
		
		$result = mysql_query($query);
		$rows = mysql_num_rows($result);
		
		$arr = array();
		
		for ($i = 0; $i < $rows; ++$i) {
			$row = mysql_fetch_row($result);
			$childUsername = $row[0];
			$childNumber = $i + 1;
			$childKey = 'child' . $childNumber;
			$arr[$childKey] = $childUsername;
		}
		
		echo json_encode($arr);			
	}
	
	mysql_close($con);

?>