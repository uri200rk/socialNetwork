<?php
include 'conexion.php';

$nick_u=$_REQUEST['nick'];


$sentencia=$conexion->query("select * from user where nick='$nick_u'");


$datos=array();



foreach ($sentencia as $row) {
    
        $datos[]=$row;
   
}
echo json_encode($datos);



?>