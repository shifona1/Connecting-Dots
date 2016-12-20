<?php

		 require_once('dbConnect.php');
		 try{
		 
		    $job = mysqli_real_escape_string($con,$_REQUEST['jobid']);

		 $sql="SELECT E.Profession,L.id,L.Username,L.PhoneNumber,L.Compressed_image, I.Longitude, I.Latitude, I.ZipCode FROM Employees E JOIN Login_Information L on E.IMEI = L.IMEI join ImeiZip I on L.IMEI=I.IMEI where E.Profession like '%.$job.%'";
		 $check=$con->query($sql);
		if(!$check) echo $con->error;

		$arr = array();			
		while($row = $check->fetch_assoc()) {
			$arr[] = array('name' => $row['Username'], 'phone' => $row['PhoneNumber'], 'prof' => $row['Profession'], 'img' => $row['Compressed_image'],'id' => $row['id'],'lon' => $row['Longitude'], 'lat' => $row['Latitude'], 'zip' => $row['ZipCode']);
		    }
		echo json_encode(array(0,$arr));

		mysqli_close($con);
		exit();
	
		}
			catch(Exception $e) {
			 	echo json_encode(array(-1,"Error in Accessing database"));
		 	
		 }
		 

		?>