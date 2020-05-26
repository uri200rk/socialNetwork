<?php

$hostname='localhost';
$database='snapshot_db';
$username='root';
$password='';

$conexion=new mysqli($hostname,$username,$password,$database);
if($conexion->connect_errno){
    echo "error de php";
}

?>