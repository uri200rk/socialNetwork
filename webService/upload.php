<?php

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $imagen = $_POST['foto'];
    $nombre = $_POST['nombre'];

    //Ruta donde se guardaran las imagenes
    $path = "upload/$nombre.png";
    //Crear la imagen enviada
    file_put_contents($path, base64_decode($imagen));

    echo "Se subio exitosamente!";
}