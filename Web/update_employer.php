<?php
	require_once('dbConnect.php');
		 try{
		 
		       $imei= mysqli_real_escape_string($con,$_REQUEST['IMEI']);	
 		       $USERNAME=mysqli_real_escape_string($con,$_REQUEST['name']);
			$PHONE=mysqli_real_escape_string($con,$_REQUEST['phone']);		
		     $profession= mysqli_real_escape_string($con,$_REQUEST['job']);
			
		   
		 $sql="SELECT * FROM Login_Information where IMEI='$imei'";
		 $check=$con->query($sql);
		if(!$check) echo $con->error;

	 	if(mysqli_num_rows($check)>=1)
   		  {
			 $sql="UPDATE Login_Information SET Username='$USERNAME',PhoneNumber='$PHONE' where IMEI='$imei'";
			 
			 $check=$con->query($sql);
			 if(!$check) echo $con->error;

			 $sql="SELECT * FROM Employers where IMEI='$imei'";
			  $check2=$con->query($sql);
			 if(mysqli_num_rows($check2)>=1)
			 {
				$sql="UPDATE Employers SET Profession='$profession' where IMEI='$imei'";
				$check=$con->query($sql);
			 }
			 else
			{
				$sql="Insert into Employers(IMEI,Profession) values('$imei','$profession')";
				$check=$con->query($sql);
			}

   		  }
			else{	 
			 $sql = "INSERT INTO Login_Information(IMEI,Username,PhoneNumber,Profession) VALUES ('$imei','$USERNAME','$PHONE')";		
			$check=$con->query($sql);
			 if(!$check) echo $con->error;
			}
		if($check)echo "Added to Database Succesfully"; else echo $con->error;
		 mysqli_close($con);
		}	
		
		catch(Exception $e) {
		 	echo "Error in Uploading database";
		}


?>
