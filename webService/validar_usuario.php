<?php
include 'conexion.php';
$nick_u=$_REQUEST['nick'];
$pass_u=$_REQUEST['password'];
#$nick_u="pedro2";
#$pass_u="pedro2";

$sentencia=$conexion->query("select * from user where nick='$nick_u'");



$contraBD = mysqli_fetch_array($sentencia);

$datos=array();

$comp = $contraBD[4];


foreach ($sentencia as $row) {
    
    if (crypt($pass_u, $comp) == $comp){
        $datos[]=$row;
    }else{
        echo 'contra no coincide';
    }
    
   
}
echo json_encode($datos);




?>