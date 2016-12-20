<?php
	 require_once('dbConnect.php');
		 try{
				$index=mysqli_real_escape_string($con,$_REQUEST['index']);

				
				$word = "";

				if($index=="1")
					$word = "Image_One";
				else if($index=="2")
					$word = "Image_Two";
				else if($index=="3")
					$word = "Image_Three";
				else die();

				if(isset(  $_GET['imei'])) {
					$imei = mysqli_real_escape_string($con,$_GET['imei']);
			  		$sql="SELECT $word as w FROM Employees where IMEI='$imei'";
				} else if(isset(  $_GET['id'])) {
					$eid = mysqli_real_escape_string($con,$_GET['id']);
			  		$sql="SELECT $word as w FROM Employees E JOIN Login_Information L on E.IMEI=L.IMEI where id='$eid'";
				} else {
					die();	
				}  
				
				$check=$con->query($sql);
				if(!$check) echo $con->error;
				$row =  $check->fetch_assoc();
				header("Content-type: image/jpeg");
				echo base64_decode($row['w']);
				mysqli_close($con);
		
				
				exit();				
       		    }
		catch(Exception $e) {
			 	echo json_encode(array(-1,"Error in Accessing database"));
		 	
		 }	
?>
