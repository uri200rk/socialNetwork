<?php

include 'conexion.php';

$idPublication=$_POST['idPublication'];
$likes = $_POST['likes'];


$consulta = "Update publication set likes = '".$likes."' + 1 where idPublication =  '".$idPublication."'";


echo $consulta;
mysqli_query($conexion,$consulta) or die (mysqli_error());
mysqli_close($conexion);

?>
