<?php
    header("Content-Type:text/html; charset=utf-8");
    $conn= mysqli_connect("localhost","root","","bookmark");

    $femail = $_POST["femail"];
    $fname = $_POST["fname"];

    $statement = mysqli_prepare($conn, "INSERT INTO bmakt VALUES (?,?)");
    mysqli_stmt_bind_param($statement, "ss", $femail, $fname);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;
    
    echo json_encode($response);
?>