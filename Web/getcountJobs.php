<?php
	require_once('dbConnect.php');
		 try{
			$sql="SELECT count(*) as c FROM JobList";
			$check=$con->query($sql);
			if(!$check) echo $con->error;

			$MAX_JOBS = $check->fetch_assoc()['c']; 
			echo $MAX_JOBS;
			mysqli_close($con);
		}
		catch(Exception $e) {

		}		
			
?>
