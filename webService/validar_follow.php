<?php
include 'conexion.php';

$idUser_u=$_REQUEST['idUser'];
$following=$_REQUEST['following'];


$sentencia=$conexion->query("select * from follow where idUser='$idUser_u' and following = '$following'");


$datos=array();



foreach ($sentencia as $row) {
    
        $datos[]=$row;

   
}
echo json_encode($datos);




?>