<?PHP

include 'conexion.php';


$json=array();
				
		$consulta="select user.nick, follow.idUser from follow INNER JOIN user on follow.following = user.idUser";
		$resultado=mysqli_query($conexion,$consulta);
		
		while($registro=mysqli_fetch_array($resultado)){
			$json['follow'][]=$registro;
		}
		mysqli_close($conexion);
		echo json_encode($json);
?>
