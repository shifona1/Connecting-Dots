<?php

	 require_once('dbConnect.php');
		 try{
			$sql="SELECT count(*) as c FROM JobList";
			$check=$con->query($sql);
			if(!$check) echo $con->error;

			$MAX_JOBS = $check->fetch_assoc()['c'];
		}
		catch(Exception $e) {
			 	echo json_encode(array(-1,"Error in Accessing database"));
		 		die();
		 }		



	$fname = "data.json";
	if(!isset($_REQUEST['jobs'])) {
		echo "Error!";	
		die();	
	} 
	$jdata = json_decode($_REQUEST['jobs']);
	//File Locking
		$data = file_get_contents($fname);
		$arr = json_decode($data);
	
		//init		

		if(count($arr)<$MAX_JOBS)	
		 {
			
			for($i=0;$i<$MAX_JOBS;$i++)
			for($j=0;$j<$MAX_JOBS;$j++)
			$arr[$i][$j]=20;
			file_put_contents($fname,json_encode($arr));
		}
	

	
	$added = null;
	if(isset($_REQUEST['newjobs'])) {
		$added = json_decode($_REQUEST['newjobs']);
	}
	if($added!=null)	{
	
			foreach($added as $njob)
			if($njob>=0 && $njob<$MAX_JOBS)
			foreach($jdata as $job)
			if($job>=0 && $job<$MAX_JOBS){
				$arr[$job][$njob]++;
			}
			$data = json_encode($arr);
			file_put_contents($fname,$data);
			echo "DONE";
		
		
	}
	else
	{
		$out = array();
		$jdata =  array_unique($jdata);
		$left = count($arr)-count($jdata);

		if($left == 0) {
			echo "No Suggestions!";
			die();		
		}

		foreach($jdata as $job)
		if($job>=0 && $job<$MAX_JOBS)
		{
			$jobothers = $arr[$job];
			$tot = 0;
			for($i=0;$i<count($jobothers);$i++)			
			{
			if(!in_array($i,$jdata))		
				$tot+=$jobothers[$i];
			}
			$avg = $tot/count($jobothers);
			$newa = array();
			$s=0;
			for($i=0;$i<count($jobothers);$i++)
			if(!in_array($i,$jdata))		
			{
				$val = $jobothers[$i]/$tot/count($jdata);
				if(isset($out[$i]))
					$out[$i]+=$val;
				else $out[$i] = $val;
				$s+=$val;
			}
		
		}
		$j = -1;
		$r = mt_rand() / mt_getrandmax();
		foreach($out as $jid => $p) {
			//echo $p." ".$jid.";";
			if($p>=$r) {
				$j = $jid;
				break; 		
			}		
			$r-=$p;
		}
		echo $j;
	}
	
?>
