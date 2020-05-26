<?php

include 'conexion.php';


$idUser=$_POST['idUser'];
$following = $_POST['following'];
$idFollowing="";



$consulta="insert into follow values('".$idUser."','".$following."','".$idFollowing."')";
echo $consulta;
mysqli_query($conexion,$consulta) or die (mysqli_error());
mysqli_close($conexion);

?>