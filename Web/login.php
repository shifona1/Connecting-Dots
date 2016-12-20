<?php
		 require_once('dbConnect.php');
		 try{
		 
		       $imei= mysqli_real_escape_string($con,$_REQUEST['IMEI']);
	
		 $sql = "SELECT * FROM Login_Information where IMEI='$imei' ";

		 $check=$con->query($sql);
		if(!$check) echo $con->error;

	 if(mysqli_num_rows($check)>=1)
   		  {
			$row =  $check->fetch_assoc();
			$data = array();
			$data["username"]=$row['Username'];
			$data["type"]=$row['Type'];
			$data["phone"]=$row['PhoneNumber'];
			if($row['Type']=='Employer')
			{
				$sql="SELECT * FROM Employers where IMEI='$imei'";
				$check=$con->query($sql);
				if(!$check) echo $con->error;
				$row2 =  $check->fetch_assoc();
				$data["profes"]=$row2['Profession'];
			}
			else
			{
				$sql="SELECT * FROM Employees where IMEI='$imei'";
				$check=$con->query($sql);
				if(!$check) echo $con->error;
				$row2 =  $check->fetch_assoc();
				$data["profes"]=$row2['Profession'];
			}
    			 echo json_encode(array(0,$data));
   		  }
		else
			echo json_encode(array(1,"Not Registered"));
  
		 mysqli_close($con);
		exit();

		}
		catch(Exception $e) {
		 	
		 }
echo json_encode(array(-1,"Error"));
		 

		?>   
