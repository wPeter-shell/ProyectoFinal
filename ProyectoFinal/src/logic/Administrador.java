package logic;

import java.io.Serializable;

public class Administrador implements Serializable {

	private String usuario;
	private String password;
	Hospital hospital;

	public Administrador(String usuario, String password, Hospital hospital) {
		super();
		this.usuario = usuario;
		this.password = password;
		this.hospital = hospital;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}
	
	public void registrarMedico(Medico medico) {
	    hospital.getMisMedicos().add(medico);
	}

	public void eliminarMedico(Medico medico) {
	    hospital.getMisMedicos().remove(medico);
	}
	
	public void registrarEspecialidad(String especialidad) {
		hospital.getEspecialidades().add(especialidad);
	}


	public void definirCantCitas(Medico medico, int cantCitas) {
	    medico.setCitasPorDia(cantCitas);
	}
	
	public void registrarSecretaria(Secretaria s) {
		Hospital.getInstancia().registrarSecretaria(s);
    }

	public void reemplazarSecretaria(Secretaria secretaria) {
	    Hospital.getInstancia().reemplazarSecretaria(secretaria);
	}

	public void agregarVacuna(Vacuna vacuna) {
	    hospital.getControlVacunas().add(vacuna);
	}

	public void eliminarVacuna(Vacuna vacuna) {
	    hospital.getControlVacunas().remove(vacuna);
	}
	
	public void agregarEnfermedad(Enfermedad enfermedad) {
	    hospital.getMisEnfermedades().add(enfermedad);
	}
	
	public void eliminarEnfermedad(Enfermedad enfermedad) {
	    hospital.getMisEnfermedades().remove(enfermedad);
	}

	public void marcarComoVigilada(Enfermedad enfermedad) {
	    hospital.getEnfermedadesVigiladas().add(enfermedad);
	}

	public void desmarcarComoVigilada(Enfermedad enfermedad) {
	    hospital.getEnfermedadesVigiladas().remove(enfermedad);
	}

	public void cambiarPassword(String nuevaPassword) {
	    this.password = nuevaPassword;
	}

}
