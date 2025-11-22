package logic;

import java.util.ArrayList;

public class Hospital {
	
	private Administrador admin;
	private ArrayList<Paciente> misPacientes;
	private ArrayList<Medico> misMedicos;
	private ArrayList<Secretaria> misSecretarias;
	private ArrayList<Cita> misCitas;
	private ArrayList<Vacuna> controlVacunas;
	private ArrayList<Enfermedad> misEnfermedades;
	private ArrayList<Enfermedad> enfermedadesVigiladas;
	
	public Hospital(Administrador admin, ArrayList<Paciente> misPacientes, ArrayList<Medico> misMedicos,
			ArrayList<Secretaria> misSecretarias, ArrayList<Cita> misCitas, ArrayList<Vacuna> controlVacunas,
			ArrayList<Enfermedad> misEnfermedades, ArrayList<Enfermedad> enfermedadesVigiladas) {
		super();
		this.admin = admin;
		this.misPacientes = misPacientes;
		this.misMedicos = misMedicos;
		this.misSecretarias = misSecretarias;
		this.misCitas = misCitas;
		this.controlVacunas = controlVacunas;
		this.misEnfermedades = misEnfermedades;
		this.enfermedadesVigiladas = enfermedadesVigiladas;
	}

	public Administrador getAdmin() {
		return admin;
	}

	public ArrayList<Paciente> getMisPacientes() {
		return misPacientes;
	}

	public ArrayList<Medico> getMisMedicos() {
		return misMedicos;
	}

	public ArrayList<Secretaria> getMisSecretarias() {
		return misSecretarias;
	}

	public ArrayList<Cita> getMisCitas() {
		return misCitas;
	}

	public ArrayList<Vacuna> getControlVacunas() {
		return controlVacunas;
	}

	public ArrayList<Enfermedad> getMisEnfermedades() {
		return misEnfermedades;
	}

	public ArrayList<Enfermedad> getEnfermedadesVigiladas() {
		return enfermedadesVigiladas;
	}

	public void setAdmin(Administrador admin) {
		this.admin = admin;
	}

	public void setMisPacientes(ArrayList<Paciente> misPacientes) {
		this.misPacientes = misPacientes;
	}

	public void setMisMedicos(ArrayList<Medico> misMedicos) {
		this.misMedicos = misMedicos;
	}

	public void setMisSecretarias(ArrayList<Secretaria> misSecretarias) {
		this.misSecretarias = misSecretarias;
	}

	public void setMisCitas(ArrayList<Cita> misCitas) {
		this.misCitas = misCitas;
	}

	public void setControlVacunas(ArrayList<Vacuna> controlVacunas) {
		this.controlVacunas = controlVacunas;
	}

	public void setMisEnfermedades(ArrayList<Enfermedad> misEnfermedades) {
		this.misEnfermedades = misEnfermedades;
	}

	public void setEnfermedadesVigiladas(ArrayList<Enfermedad> enfermedadesVigiladas) {
		this.enfermedadesVigiladas = enfermedadesVigiladas;
	}
	
	public boolean buscarPacientePorCedula(String cedula) {
		for(Paciente p : misPacientes) {
			if(p.getCedula().equals(cedula)) {
				return true;
			}
		}
		return false;
	}

	
}
	