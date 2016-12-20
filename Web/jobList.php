<?php
	require_once('dbConnect.php');

		 try{

			$sql="SELECT * FROM JobList ";
			$check=$con->query($sql);
			if(!$check) echo $con->error;
			$arr = array();			
		while($row = $check->fetch_assoc()) 
			{
				$arr[$row['jid']] = $row['jName'];
			}
			echo json_encode($arr);

			mysqli_close($con);
			exit();
		 }

		catch(Exception $e) {
			 	echo json_encode("Error in Accessing database");	
		}
?>
