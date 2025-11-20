ackage logic;

import java.util.ArrayList;

public class Secretaria extends Persona {

    private ArrayList<Medico> medicos;

	public Secretaria(String nombre, String apellido, String cedula, char genero, int edad, String telefono,
			String direccion, ArrayList<Medico> medicos) {
		super(nombre, apellido, cedula, genero, edad, telefono, direccion);
		this.medicos = medicos;
	}

	public ArrayList<Medico> getMedicos() {
		return medicos;
	}

	public void setMedicos(ArrayList<Medico> medicos) {
		this.medicos = medicos;
	}

    
}
