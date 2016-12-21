<?php
	require_once('dbConnect.php');
	
		try{
			$imei= mysqli_real_escape_string($con,$_REQUEST['IMEI']);
			$zip= mysqli_real_escape_string($con,$_REQUEST['zip']);
			$lat= mysqli_real_escape_string($con,$_REQUEST['lat']);
			$long= mysqli_real_escape_string($con,$_REQUEST['lon']);
			$sql="SELECT * FROM ImeiZip where IMEI='$imei'";
		 	$check=$con->query($sql);
			if(!$check) echo $con->error;

		 	if(mysqli_num_rows($check)>=1)
   		  	{
				$sql = "UPDATE ImeiZip SET ZipCode='$zip',Latitude='$lat',Longitude='$long' where IMEI='$imei'"; 				    				 
				$check=$con->query($sql);					 
				//echo $con->error;
   		  	}
			else
			{
				$sql = "INSERT INTO ImeiZip (IMEI,ZipCode,Latitude,Longitude) VALUES ('$imei','$zip','$lat','$long')";
				$check=$con->query($sql);
				if($check)
				echo "Added to Database Succesfully"; 
			}
			
		 mysqli_close($con);
		
       	  	  }
	catch(Exception $e) {
		 	echo "Error";
		 	
		 }


?>