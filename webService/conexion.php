<?php

$hostname='mysql-uri200rk.alwaysdata.net';
$database='uri200rk_snapshot';
$username='uri200rk';
$password='Martinez1Xispa1';

$conexion=new mysqli($hostname,$username,$password,$database);
if($conexion->connect_errno){
    echo "error de php";
}

?>
