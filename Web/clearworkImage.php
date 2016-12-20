<?php
	require_once('dbConnect.php');
		 try{
			$imei=mysqli_real_escape_string($con,$_REQUEST['imei']);
			$index=mysqli_real_escape_string($con,$_REQUEST['index']);
			$word="";
			
				if($index=="1")
					$word = "Image_One";
				else if($index=="2")
					$word = "Image_Two";
				else if($index=="3")
					$word = "Image_Three";
				else die();

			$sql="UPDATE Employees SET $word=NULL where IMEI='$imei'";
		 	$check=$con->query($sql);
			if(!$check) echo $con->error;
			else echo "Successfully Deleted";
		   }
		catch(Exception $e) {
		 	echo "Error in Deleting The Picture";
		}		
?>
