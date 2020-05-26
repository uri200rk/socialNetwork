<?php

include 'conexion.php';

$idPublication="";
$idUser=$_POST['idUser'];
$nick = $_POST['nick'];
$title=$_POST['title'];
$description=$_POST['description'];
$idMedia=$_POST['idMedia'];
$likes=$_POST['likes'];
$date=$_POST['date'];
/*
$idPublication="";
$idUser=$_POST['idUser'];
$title=$_POST['title'];
$description=$_POST['description'];
$idMedia=$_POST['idMedia'];
$likes=$_POST['likes'];
$date=$_POST['date'];
*/

$consulta="insert into publication values('".$idPublication."','".$idUser."','".$nick."','".$title."','".$description."','".$idMedia."','".$likes."','".$date."')";
echo $consulta;
mysqli_query($conexion,$consulta) or die (mysqli_error());
mysqli_close($conexion);

?>