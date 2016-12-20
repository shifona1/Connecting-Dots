<?php
		 require_once('dbConnect.php');
		 try{
		 
		    $name = mysqli_real_escape_string($con,$_REQUEST['username']);
		       $imei= mysqli_real_escape_string($con,$_REQUEST['IMEI']);	 
		     $type= mysqli_real_escape_string($con,$_REQUEST['Type']);
		    $phone= mysqli_real_escape_string($con,$_REQUEST['phone']);
			if(!($type=='Employee' || $type=='Employer')) {
				echo "Invalid Type";
				die();		
			}
				//echo $name." ".$password." ".$id." ".$type." ";
		 
		 $sql="SELECT * FROM Login_Information where IMEI='$imei'";
		 $check=$con->query($sql);
		if(!$check) echo $con->error;

	 	if(mysqli_num_rows($check)>=1)
   		  {
			 echo "The IMEI is already Registered.";
		 	    			 
			 //echo $con->error;
   		  }
		else{	
		 $image = mysqli_real_escape_string($con,$_REQUEST['image']);	
		$com_img= mysqli_real_escape_string($con,$_REQUEST['image_small']);
		if($type=='Employee')
		 $sql = "INSERT INTO Employees (IMEI,Profession) VALUES ('$imei','.')";
 		else
		 $sql = "INSERT INTO Employers (IMEI,Profession) VALUES ('$imei','.')";

		 $check=$con->query($sql);
			if(!$check)
			{
				echo "Error in Registration ". $con->error;; die();
			}
		$sql = "INSERT INTO Login_Information (Username,IMEI,Profile_Pic,Type,PhoneNumber,Compressed_image) VALUES ('$name','$imei','$image','$type','$phone','$com_img')";
 
		 $check=$con->query($sql);
			if(!$check)
			{
				echo "Error in Registration". $con->error ; die();
			}
		echo "Added to Database Successfully"; 
		}
		 mysqli_close($con);

		 
		}
		catch(Exception $e) {
		 	echo "Error in Uploading database";
		 	
		 }
		 

		?>