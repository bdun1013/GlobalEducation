<?php

	require 'connect.php';
	
	if (isset($_GET['username'])) {
		// retrieve username
		$childUsername = $_GET['username'];
		
		// get level of child
		$query = "SELECT level 
			  FROM child 
			  WHERE username='$childUsername'";
		$result = mysql_query($query);
		$row = mysql_fetch_row($result);
		$level = $row[0];
		
		// Query to find all questions at the child's current level that they have not answered
		$query =  "SELECT q.questionText, q.choiceOne, q.choiceTwo, q.choiceThree, q.choiceFour, q.correctChoice, q.category, q.questionID
			   FROM question q
			   WHERE q.level = '$level' AND q.questionID NOT IN 
			   ( SELECT qA.questionID
			     FROM questionsAnswered qA
			     WHERE qA.childUsername = '$childUsername'
			   )";
			   
		$result = mysql_query($query);
		$rows = mysql_num_rows($result);
		
		if ($rows != 0) {  //There are available questions at this level
			$randomRow = rand(1,$rows);
			//Advance result by random number of rows to get a random question
			while ($randomRow > 0) {
				$row = mysql_fetch_row($result);
				$randomRow--;
			}
			
			$questionText = $row[0];
			$choiceOne = $row[1];
			$choiceTwo = $row[2];
			$choiceThree = $row[3];
			$choiceFour = $row[4];
			$correctChoice = $row[5];
			$category = $row[6];
			$questionID = $row[7];
			
			$arr = array('questionText' => $questionText, 'choiceOne' => $choiceOne,
				     'choiceTwo' => $choiceTwo, 'choiceThree' => $choiceThree, 
				     'choiceFour' => $choiceFour, 'correctChoice' => $correctChoice,
				     'category' => $category, 'questionID' => $questionID, 
				     'username' => $childUsername);
			echo json_encode($arr);
			
		} else {  //There are not available questions at this level
			// promote child to higher level 
			$level = $level + 1;
			
			$query = "UPDATE child
				  SET level = '$level'
				  WHERE username='$childUsername'";
				  
			$result = mysql_query($query);
			
				// Query to find all questions at the child's current level that they have not answered
			$query =  "SELECT q.questionText, q.choiceOne, q.choiceTwo, q.choiceThree, q.choiceFour, q.correctChoice,
			           	q.category, q.questionID
				   FROM question q
				   WHERE q.level = '$level' AND q.questionID NOT IN 
				   ( SELECT qA.questionID
				     FROM questionsAnswered qA
				     WHERE qA.childUsername = '$childUsername'
				   )";
				   
			$result = mysql_query($query);
			$rows = mysql_num_rows($result);
			
			$randomRow = rand(1,$rows);
			//Advance result by random number of rows to get a random question
			while ($randomRow > 0) {
				$row = mysql_fetch_row($result);
				$randomRow--;
			}
			
			$questionText = $row[0];
			$choiceOne = $row[1];
			$choiceTwo = $row[2];
			$choiceThree = $row[3];
			$choiceFour = $row[4];
			$correctChoice = $row[5];
			$category = $row[6];
			$questionID = $row[7];
			
			$arr = array('questionText' => $questionText, 'choiceOne' => $choiceOne,
				     'choiceTwo' => $choiceTwo, 'choiceThree' => $choiceThree, 
				     'choiceFour' => $choiceFour, 'correctChoice' => $correctChoice,
				     'category' => $category, 'questionID' => $questionID, 
				     'username' => $childUsername);
			echo json_encode($arr);
			}
		
	}
	
	mysql_close($con);

?>