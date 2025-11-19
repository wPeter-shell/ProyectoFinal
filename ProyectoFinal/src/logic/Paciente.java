package logic;

public class Paciente extends Persona {
	
	private float peso;
	private float altura;
	private String tipoSangre;
	private String alergias;
	private String defectoAlNacer;
	
	public Paciente(String nombre, String apellido, String cedula, char genero, int edad, String direccion, float peso,
			float altura, String tipoSangre, String alergias, String defectoAlNacer) {
		super(nombre, apellido, cedula, genero, edad, direccion);
		this.peso = peso;
		this.altura = altura;
		this.tipoSangre = tipoSangre;
		this.alergias = alergias;
		this.defectoAlNacer = defectoAlNacer;
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
