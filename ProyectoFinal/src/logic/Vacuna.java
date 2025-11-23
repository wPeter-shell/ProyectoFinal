	package logic;

public class Vacuna {
	
	private String nombre;
	private Enfermedad enfermedadPrevenida;
	
	private Vacuna(String nombre, Enfermedad enfermedadPrevenida) {
		super();
		this.nombre = nombre;
		this.enfermedadPrevenida = enfermedadPrevenida;
	}

	public String getNombre() {
		return nombre;
	}

	public Enfermedad getEnfermedadPrevenida() {
		return enfermedadPrevenida;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setEnfermedadPrevenida(Enfermedad enfermedadPrevenida) {
		this.enfermedadPrevenida = enfermedadPrevenida;
	}
}
