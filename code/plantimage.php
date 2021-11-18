<?php
    header("Content-Type:text/html; charset=utf-8");
    $conn= mysqli_connect("localhost","root","","bookmark");
 
    $conn->query("SET NAMES 'UTF-8'");
     
    $femail = $_POST["femail"]?? "";
    $sql = "select * from bmakt where femail='$femail'";
    $result=mysqli_query($conn, $sql);
 
    $rowCnt= mysqli_num_rows($result);
 
    $arr= array(); //빈 배열 생성
 
    for($i=0;$i<$rowCnt;$i++){
        $row= mysqli_fetch_array($result, MYSQLI_ASSOC);
        //각 각의 row를 $arr에 추가
        $arr[$i]= $row;
        
    }
 
    //배열을 json으로 변환하는 함수가 있음.
        $jsonData=json_encode($arr); //json배열로 만들어짐.
        echo "$jsonData";
 
    mysqli_close($conn);
 
?>