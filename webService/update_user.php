<?php

include 'conexion.php';

$idUser=$_POST['idUser'];
$fullName=$_POST['fullName'];
$nick=$_POST['nick'];
$mail=$_POST['mail'];


$consulta = "Update user set fullName = '".$fullName."', nick = '".$nick."', mail = '".$mail."' where idUser =  '".$idUser."'";


echo $consulta;
mysqli_query($conexion,$consulta) or die (mysqli_error());
mysqli_close($conexion);

?>