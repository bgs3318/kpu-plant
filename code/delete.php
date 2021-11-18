<?php

   $conn= mysqli_connect("localhost","root","","bookmark");
   $fname = $_POST["fname"];

   $sql = "delete from bmakt where fname = '$fname' ";
   $result = mysqli_query($conn, $sql);

   if( $result ) echo "성공";
   else echo "실패";

   mysqli_close($conn);

?>