package logic;

import java.util.ArrayList;

public class Medico extends Persona {

    private Secretaria secretaria;
    private String especialidad;
    private int citasPorDia;
    private ArrayList<Consulta> misConsulta;
    private ArrayList<Cita> misCita;
    private Hospital hospital;
    
	public Medico(String nombre, String apellido, String cedula, char genero, int edad, String telefono,
			String direccion, Secretaria secretaria, String especialidad, int citasPorDia,
			ArrayList<Consulta> misConsulta, ArrayList<Cita> misCita) {
		super(nombre, apellido, cedula, genero, edad, telefono, direccion);
		this.secretaria = secretaria;
		this.especialidad = especialidad;
		this.citasPorDia = citasPorDia;
		this.misConsulta = misConsulta;
		this.misCita = misCita;
	}
	
	public Secretaria getSecretaria() {
		return secretaria;
	}
	
	public void setSecretaria(Secretaria secretaria) {
		this.secretaria = secretaria;
	}
	
	public String getEspecialidad() {
		return especialidad;
	}
	
	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}
	
	public int getCitasPorDia() {
		return citasPorDia;
	}
	
	public void setCitasPorDia(int citasPorDia) {
		this.citasPorDia = citasPorDia;
	}
	
	public ArrayList<Consulta> getMisConsulta() {
		return misConsulta;
	}
	
	public void setMisConsulta(ArrayList<Consulta> misConsulta) {
		this.misConsulta = misConsulta;
	}
	
	public ArrayList<Cita> getMisCita() {
		return misCita;
	}
	
	public void setMisCita(ArrayList<Cita> misCita) {
		this.misCita = misCita;
	}
	
	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	public void registrarPaciente(Persona persona) {

	    if (persona instanceof Paciente) {
	        Paciente p = (Paciente) persona;

	        if (hospital.getMisPacientes() == null) {
	            hospital.setMisPacientes(new ArrayList<>());
	        }

	        if (!hospital.getMisPacientes().contains(p)) {
	            hospital.getMisPacientes().add(p);
	        }

	    } else {
	        System.out.println("Error: Solo se pueden registrar objetos de tipo Paciente.");
	    }
	}


	
	public int cantCitasDisp(String dia) {
	    if (misCita == null) {
	        return citasPorDia;
	    }

	    int ocupadas = 0;

	    for (Cita c : misCita) {
	        if (c.getDia().equalsIgnoreCase(dia) && 
	            c.getMedico().equals(this) &&
	            c.getEstado().equalsIgnoreCase("Pendiente")) 
	        {
	            ocupadas++;
	        }
	    }

	    int disponibles = citasPorDia - ocupadas;
	    return disponibles;
	}

}
