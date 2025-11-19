package logic;

public class Enfermedad {

	private String nombre;
	private boolean bajoVigilancia;
	private boolean tieneVacuna;
	
	private Enfermedad(String nombre, boolean bajoVigilancia, boolean tieneVacuna) {
		super();
		this.nombre = nombre;
		this.bajoVigilancia = bajoVigilancia;
		this.tieneVacuna = tieneVacuna;
	}

	public String getNombre() {
		return nombre;
	}

	public boolean isBajoVigilancia() {
		return bajoVigilancia;
	}

	public boolean isTieneVacuna() {
		return tieneVacuna;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setBajoVigilancia(boolean bajoVigilancia) {
		this.bajoVigilancia = bajoVigilancia;
	}

	public void setTieneVacuna(boolean tieneVacuna) {
		this.tieneVacuna = tieneVacuna;
	}
}
