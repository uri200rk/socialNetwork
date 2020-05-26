<?php

include 'conexion.php';
$idUser="";
$fullName=$_POST['fullName'];
$nick=$_POST['nick'];
$mail=$_POST['mail'];
$password=$_POST['password'];



$consulta="insert into user values('".$idUser."','".$fullName."','".$nick."','".$mail."','".password_hash($password, PASSWORD_DEFAULT, ['cost'=> 5])."')";
mysqli_query($conexion,$consulta) or die (mysqli_error());
mysqli_close($conexion);

?>