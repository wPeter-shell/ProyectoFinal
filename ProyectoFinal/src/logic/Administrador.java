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
	
	// REGISTRAR M�DICO
	public void registrarMedico(Medico medico) {
	    hospital.getMisMedicos().add(medico);
	}

	// ELIMINAR M�DICO
	public void eliminarMedico(Medico medico) {
	    hospital.getMisMedicos().remove(medico);
	}
	
	public void registrarEspecialidad(String especialidad) {
		hospital.getEspecialidades().add(especialidad);
	}


	// DEFINIR CU�NTAS CITAS POR D�A PUEDE ATENDER UN M�DICO
	public void definirCantCitas(Medico medico, int cantCitas) {
	    medico.setCitasPorDia(cantCitas);
	}
	
	// REGISTRAR SECRETARIA
	public void registrarSecretaria(Secretaria s) {
		Hospital.getInstancia().registrarSecretaria(s);
    }

	// REEMPLAZAR SECRETARIA
	public void reemplazarSecretaria(Secretaria secretaria) {
	    Hospital.getInstancia().reemplazarSecretaria(secretaria);
	}

	// AGREGAR VACUNA AL CONTROL DEL HOSPITAL
	public void agregarVacuna(Vacuna vacuna) {
	    hospital.getControlVacunas().add(vacuna);
	}

	// ELIMINAR VACUNA
	public void eliminarVacuna(Vacuna vacuna) {
	    hospital.getControlVacunas().remove(vacuna);
	}
	
	// AGREGAR ENFERMEDAD AL HOSPITAL
	public void agregarEnfermedad(Enfermedad enfermedad) {
	    hospital.getMisEnfermedades().add(enfermedad);
	}
	
	// ELIMINAR ENFERMEDAD
	public void eliminarEnfermedad(Enfermedad enfermedad) {
	    hospital.getMisEnfermedades().remove(enfermedad);
	}

	// MARCAR COMO VIGILADA
	public void marcarComoVigilada(Enfermedad enfermedad) {
	    hospital.getEnfermedadesVigiladas().add(enfermedad);
	}

	// DESMARCAR COMO VIGILADA
	public void desmarcarComoVigilada(Enfermedad enfermedad) {
	    hospital.getEnfermedadesVigiladas().remove(enfermedad);
	}

	public void cambiarPassword(String nuevaPassword) {
	    this.password = nuevaPassword;
	}

}
