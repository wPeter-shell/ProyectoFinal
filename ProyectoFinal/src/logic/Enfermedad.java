package logic;

import java.io.Serializable;

public class Enfermedad implements Serializable {

	private String nombre;
	private boolean bajoVigilancia;
	private boolean tieneVacuna;
	
	public Enfermedad(String nombre, boolean bajoVigilancia) {
		super();
		this.nombre = nombre;
		this.bajoVigilancia = bajoVigilancia;
		this.tieneVacuna = false;
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
	
	@Override
	public String toString() {
	   return nombre;
	}

	
	
}
