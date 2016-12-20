<?php
	require_once('dbConnect.php');
		 try{
			$imei=mysqli_real_escape_string($con,$_REQUEST['imei']);
			$img=mysqli_real_escape_string($con,$_REQUEST['img']);
			$sql="SELECT * FROM Login_Information where IMEI='$imei'";
		 	$check=$con->query($sql);
			if(!$check) echo $con->error;
			if(mysqli_num_rows($check)>=1)
   		  	{	
				$sql="UPDATE Login_Information SET Profile_Pic='$img' where IMEI='$imei'";				 
				$check=$con->query($sql);
				if(!$check) echo $con->error;
				else		
					echo "Picture Uploaded Successfully";
		 	}

			 mysqli_close($con);	
		}
		catch(Exception $e) {
		 	echo "Error in Uploading The Picture";
		}		
?>
