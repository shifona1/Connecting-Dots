<?php
	require_once('dbConnect.php');
		 try{
		 
		       $imei= mysqli_real_escape_string($con,$_REQUEST['imei']);
			$prof=mysqli_real_escape_string($con,$_REQUEST['request']);
			
			$sql="INSERT INTO Requests(IMEI,Jobs) values('$imei','$prof')";
			$check=$con->query($sql);
			if(!$check) echo $con->error;
			else echo "Successfully submitted the request";
					
		}
		catch(Exception $e) {
			 	echo "The request could not be submitted";
		 	
		 }		
?>
