package logic;

import java.util.ArrayList;

public class Paciente extends Persona {
	
	private HistorialClinico historial;
	private ArrayList<Vacuna> misVacunas;
	private float peso;
	private float altura;
	private String tipoSangre;
	private String alergias;
	private String defectoAlNacer;
	
	public Paciente(String nombre, String apellido, String cedula, char genero, int edad, String telefono,
			String direccion, float peso, float altura,
			String tipoSangre, String alergias, String defectoAlNacer) {
		super(nombre, apellido, cedula, genero, edad, telefono, direccion);
		this.historial = new HistorialClinico();
		this.misVacunas = new  ArrayList<>();
		this.peso = peso;
		this.altura = altura;
		this.tipoSangre = tipoSangre;
		this.alergias = alergias;
		this.defectoAlNacer = defectoAlNacer;
	}

	public Paciente(Persona p) {
		super(p.getNombre(), p.getApellido(), p.getCedula(), p.getGenero(),
		      p.getEdad(), p.getTelefono(), p.getDireccion());
		
		this.historial = new HistorialClinico();
		this.misVacunas = new ArrayList<>();
	}
	
	public HistorialClinico getHistorial() {
		return historial;
	}

	public ArrayList<Vacuna> getMisVacunas() {
		return misVacunas;
	}

	public float getPeso() {
		return peso;
	}

	public void setPeso(float peso) {
		this.peso = peso;
	}

	public float getAltura() {
		return altura;
	}

	public void setAltura(float altura) {
		this.altura = altura;
	}

	public String getTipoSangre() {
		return tipoSangre;
	}

	public void setTipoSangre(String tipoSangre) {
		this.tipoSangre = tipoSangre;
	}

	public String getAlergias() {
		return alergias;
	}

	public void setAlergias(String alergias) {
		this.alergias = alergias;
	}

	public String getDefectoAlNacer() {
		return defectoAlNacer;
	}

	public void setDefectoAlNacer(String defectoAlNacer) {
		this.defectoAlNacer = defectoAlNacer;
	}
	
	
	

}
