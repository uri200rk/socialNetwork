
<?PHP

include 'conexion.php';


$json=array();
				
		$consulta="select * from publication order by idPublication DESC";
		$resultado=mysqli_query($conexion,$consulta);
		
		while($registro=mysqli_fetch_array($resultado)){
			$json['publication'][]=$registro;
		}
		mysqli_close($conexion);
		echo json_encode($json);
?>
