
<?PHP

include 'conexion.php';


$json=array();
				
		$consulta="select * from user";
		
		$resultado=mysqli_query($conexion,$consulta);
		
		while($registro=mysqli_fetch_array($resultado)){
			$json['user'][]=$registro;
		}
		mysqli_close($conexion);
		echo json_encode($json);
?>