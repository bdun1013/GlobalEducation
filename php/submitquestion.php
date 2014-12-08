<?php
	require 'connect.php';
	
	if (isset($_GET['username']) && isset($_GET['questionID']) && isset($_GET['isCorrect'])) {
		
		$username = $_GET['username'];
		$questionID = intval($_GET['questionID']);
		$isCorrect = $_GET['isCorrect'];
		
		$query = "INSERT INTO questionsAnswered 
		          VALUES('$username', '$questionID', '$isCorrect')";
		$result = mysql_query($query);
		
	}
	
	mysql_close($con);

?>