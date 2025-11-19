package logic;

import java.util.ArrayList;

public class Hospital {
	
	private Administrador admin;
	private ArrayList<Paciente> misPacientes;
	private ArrayList<Secretaria> misMedicos;
	private Arraylist<Cita> misCitas;
	private Arraylist<Vacunas> controlVacunas;
	private Arraylist<Enfermadades> misEnfermedades;
	private Arraylist<Enfermedades> enfermedadesVigiladas;
	
	private Hospital(Administrador admin, ArrayList<Paciente> misPacientes, ArrayList<Secretaria> misMedicos,
			Arraylist<Cita> misCitas, Arraylist<Vacunas> controlVacunas, Arraylist<Enfermadades> misEnfermedades,
			Arraylist<Enfermedades> enfermedadesVigiladas) {
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

	public Arraylist<Cita> getMisCitas() {
		return misCitas;
	}

	public Arraylist<Vacunas> getControlVacunas() {
		return controlVacunas;
	}

	public Arraylist<Enfermadades> getMisEnfermedades() {
		return misEnfermedades;
	}

	public Arraylist<Enfermedades> getEnfermedadesVigiladas() {
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

	public void setMisCitas(Arraylist<Cita> misCitas) {
		this.misCitas = misCitas;
	}

	public void setControlVacunas(Arraylist<Vacunas> controlVacunas) {
		this.controlVacunas = controlVacunas;
	}

	public void setMisEnfermedades(Arraylist<Enfermadades> misEnfermedades) {
		this.misEnfermedades = misEnfermedades;
	}

	public void setEnfermedadesVigiladas(Arraylist<Enfermedades> enfermedadesVigiladas) {
		this.enfermedadesVigiladas = enfermedadesVigiladas;
	}
	
	
}
