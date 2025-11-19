package logic;

import java.util.ArrayList;

public class Hospital {
	
	private Administrador admin;
	private ArrayList<Paciente> misPacientes;
	private ArrayList<Secretaria> misMedicos;
	private ArrayList<Cita> misCitas;
	private ArrayList<Vacunas> controlVacunas;
	private ArrayList<Enfermadades> misEnfermedades;
	private ArrayList<Enfermedades> enfermedadesVigiladas;
	
	private Hospital(Administrador admin, ArrayList<Paciente> misPacientes, ArrayList<Secretaria> misMedicos,
			ArrayList<Cita> misCitas, ArrayList<Vacunas> controlVacunas, ArrayList<Enfermadades> misEnfermedades,
			ArrayList<Enfermedades> enfermedadesVigiladas) {
		super();
		this.admin = admin;
		this.misPacientes = misPacientes;
		this.misMedicos = misMedicos;
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

	public ArrayList<Secretaria> getMisMedicos() {
		return misMedicos;
	}

	public ArrayList<Cita> getMisCitas() {
		return misCitas;
	}

	public ArrayList<Vacunas> getControlVacunas() {
		return controlVacunas;
	}

	public ArrayList<Enfermadades> getMisEnfermedades() {
		return misEnfermedades;
	}

	public ArrayList<Enfermedades> getEnfermedadesVigiladas() {
		return enfermedadesVigiladas;
	}

	public void setAdmin(Administrador admin) {
		this.admin = admin;
	}

	public void setMisPacientes(ArrayList<Paciente> misPacientes) {
		this.misPacientes = misPacientes;
	}

	public void setMisMedicos(ArrayList<Secretaria> misMedicos) {
		this.misMedicos = misMedicos;
	}

	public void setMisCitas(ArrayList<Cita> misCitas) {
		this.misCitas = misCitas;
	}

	public void setControlVacunas(ArrayList<Vacunas> controlVacunas) {
		this.controlVacunas = controlVacunas;
	}

	public void setMisEnfermedades(ArrayList<Enfermadades> misEnfermedades) {
		this.misEnfermedades = misEnfermedades;
	}

	public void setEnfermedadesVigiladas(ArrayList<Enfermedades> enfermedadesVigiladas) {
		this.enfermedadesVigiladas = enfermedadesVigiladas;
	}
	
	
}
