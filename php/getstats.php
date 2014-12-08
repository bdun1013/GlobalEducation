<?php
	require 'connect.php';
	
	
	if (isset($_GET['username']) && isset($_GET['location'])) {
		
		$childUsername = $_GET['username'];
		$locationType = strtolower($_GET['location']);
		
		$allStats = array();
		
		$query = "SELECT SUM(answeredCorrect)
			  FROM questionsAnswered
			  WHERE childUsername = '$childUsername'";
		$result = mysql_query($query);
		if (!$result) die ("Database access failed: " . mysql_error());
		$row = mysql_fetch_row($result);
		$childCorrect = $row[0];
		
		$query = "SELECT COUNT(*)
			  FROM questionsAnswered
			  WHERE childUsername = '$childUsername'";
		$result = mysql_query($query);
		if (!$result) die ("Database access failed: " . mysql_error());
		$row = mysql_fetch_row($result);
		$childTotal = $row[0];
		
		$stats = array();
		$stats['correct'] = $childCorrect;
		$stats['total'] = $childTotal;
		
		$allStats[0] = $stats;
		
		if ($locationType != 'global') {  //either city state or country
		
			// query to get child's location
			$query = "SELECT $locationType
				  FROM child
				  WHERE username = '$childUsername'";
			
			$result = mysql_query($query);
			if (!$result) die ("Database access failed: " . mysql_error());
			$row = mysql_fetch_row($result);
			$location = $row[0];
			
			// get all usernames of other children in same region
			$query = "SELECT username
				  FROM child
				  WHERE $locationType = '$location' AND username != '$childUsername'";
				  
			
			$result = mysql_query($query);
			if (!$result) die ("Database access failed: " . mysql_error());
			$rows = mysql_num_rows($result);
			
			for ($i = 0; $i < $rows; ++$i) {
				$row = mysql_fetch_row($result);
				$childUsername = $row[0];
				$query1 = "SELECT SUM(answeredCorrect)
					  FROM questionsAnswered
					  WHERE childUsername = '$childUsername'";
				$result1 = mysql_query($query1);
				if (!$result1) die ("Database access failed: " . mysql_error());
				$row1 = mysql_fetch_row($result1);
				$childCorrect = $row1[0];
				
				$query1 = "SELECT COUNT(*)
					  FROM questionsAnswered
					  WHERE childUsername = '$childUsername'";
				$result1 = mysql_query($query1);
				if (!$result1) die ("Database access failed: " . mysql_error());
				$row1 = mysql_fetch_row($result1);
				$childTotal = $row1[0];
				
				$stats = array();
				$stats['correct'] = $childCorrect;
				$stats['total'] = $childTotal;
				
				
				$allStats[$i + 1] = $stats;
			}
				
		} 
		else {  
		
			$query = "SELECT username
				  FROM child WHERE username != '$childUsername'";
				  
			$result = mysql_query($query);
			if (!$result) die ("Database access failed: " . mysql_error());
			$rows = mysql_num_rows($result);
			
			for ($i = 0; $i < $rows; ++$i) {
				$row = mysql_fetch_row($result);
				$childUsername = $row[0];
				$query1 = "SELECT SUM(answeredCorrect)
					  FROM questionsAnswered
					  WHERE childUsername = '$childUsername'";
				$result1 = mysql_query($query1);
				if (!$result1) die ("Database access failed: " . mysql_error());
				$row1 = mysql_fetch_row($result1);
				$childCorrect = $row1[0];
				
				$query1 = "SELECT COUNT(*)
					  FROM questionsAnswered
					  WHERE childUsername = '$childUsername'";
				$result1 = mysql_query($query1);
				if (!$result1) die ("Database access failed: " . mysql_error());
				$row1 = mysql_fetch_row($result1);
				$childTotal = $row1[0];
				
				$stats = array();
				$stats['correct'] = $childCorrect;
				$stats['total'] = $childTotal;
				
				
				$allStats[$i + 1] = $stats;
			}
			
		}
		
		echo json_encode($allStats);			
	}
	
	mysql_close($con);

?>