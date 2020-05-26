<?PHP

include 'conexion.php';
$nick_u=$_REQUEST['nick'];


$json=array();
				
		$consulta="select * from user where nick='$nick_u'";
		$resultado=mysqli_query($conexion,$consulta);
		
		while($registro=mysqli_fetch_array($resultado)){
			$json['user'][]=$registro;
		}
		mysqli_close($conexion);
		echo json_encode($json);
?>