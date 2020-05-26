<?php

include 'conexion.php';

$idUser=$_POST['idUser'];

$consulta = "Delete from user where idUser =  '".$idUser."'";


echo $consulta;
mysqli_query($conexion,$consulta) or die (mysqli_error());
mysqli_close($conexion);

?>