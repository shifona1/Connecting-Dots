<?php
	require_once('dbConnect.php');
		 try{
		 
		      $imei= mysqli_real_escape_string($con,$_REQUEST['imei']);	
 		       $USERNAME=mysqli_real_escape_string($con,$_REQUEST['username']);
			$PHONE=mysqli_real_escape_string($con,$_REQUEST['phone']);		
		     $profession= mysqli_real_escape_string($con,$_REQUEST['Profession']);

		   //$image = $_REQUEST['image'];	
		   
		 $sql="SELECT * FROM Login_Information where IMEI='$imei'";
		 $check=$con->query($sql);
		if(!$check) echo $con->error;

	 	if(mysqli_num_rows($check)>=1)
   		  {
			 $sql="UPDATE Login_Information SET Username='$USERNAME',PhoneNumber='$PHONE' where IMEI='$imei'";
			 
			 $check=$con->query($sql);
			 if(!$check) echo $con->error;
			 $sql2="SELECT * FROM Employees where IMEI='$imei'";
			$check2=$con->query($sql2);
			 if(!$check2) echo $con->error;
			 if(mysqli_num_rows($check2)>=1)
			 {
	    			 $sql="UPDATE Employees SET image_skill='', Profession='$profession' where IMEI='$imei'";
			 
				$check=$con->query($sql);
			 }
			 else
			{
				$sql="Insert into Employees(IMEI,image_skill,Profession) values('$imei','','$profession')";
				$check=$con->query($sql);
			}
			if(!$check) echo $con->error;	
   		  }
		else{	 
			echo "Unexpected Error!";
			die();		
		}
		if($check)echo "Updated Successfully";
		 else echo $con->error;
		 mysqli_close($con);
	}	
		catch(Exception $e) {
		 	echo "Error in Uploading database";
			}


?>
