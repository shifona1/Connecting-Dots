<?php
	 require_once('dbConnect.php');

	if(isset(  $_GET['IMEI'])) {
		$imei = mysqli_real_escape_string($con,$_GET['IMEI']);
  		$sql = "SELECT Profile_Pic FROM Login_Information WHERE IMEI='$imei'";
	} else if(isset(  $_GET['id'])) {
		$eid = mysqli_real_escape_string($con,$_GET['id']);
  		$sql = "SELECT Profile_Pic FROM Login_Information WHERE id='$eid'";
	} else {
		die();	
	}  
	// do some validation here to ensure id is safe

	$check=$con->query($sql);
	if(!$check) echo $con->error;
	$row =  $check->fetch_assoc();
	 mysqli_close($con);
	
  header("Content-type: image/jpeg");
  echo base64_decode($row['Profile_Pic']);
?>