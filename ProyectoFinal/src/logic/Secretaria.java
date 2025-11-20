package logic;

import java.util.ArrayList;

public class Secretaria extends Persona {

    private ArrayList<Medico> medico;
    private Hospital hospital;

	public Secretaria(String nombre, String apellido, String cedula, char genero, int edad, String telefono,
			String direccion, ArrayList<Medico> medico, Hospital hospital) {
		super(nombre, apellido, cedula, genero, edad, telefono, direccion);
		this.medico = medico;
	}

	public ArrayList<Medico> getMedico() {
		return medico;
	}

	public void setMedicos(ArrayList<Medico> medicos) {
		this.medico = medicos;
	}

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

    
}
