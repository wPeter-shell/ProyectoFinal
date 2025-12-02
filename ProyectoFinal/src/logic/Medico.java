package logic;

import java.io.Serializable;
import java.util.ArrayList;

public class Medico extends Persona implements Serializable {

	private String usuario;
	private String password;
    private String especialidad;
    private int citasPorDia;
    private ArrayList<Consulta> misConsultas;
    private ArrayList<Cita> misCitas;
    private boolean disponible;
    

	public Medico(String usuario, String password, String nombre, String apellido, String cedula, char genero, int edad, String telefono,
			String direccion, String especialidad, int citasPorDia) {
		super(nombre, apellido, cedula, genero, edad, telefono, direccion);
		this.usuario = usuario;
		this.password = password;
		this.especialidad = especialidad;
		this.citasPorDia = citasPorDia;
		this.misConsultas = new ArrayList<>();
		this.misCitas = new ArrayList<>();
		this.disponible = true;
	}

	public String getUsuario() {
		return usuario;
	}

	public String getPassword() {
		return password;
	}
	
	public boolean getDisponible() {
		return disponible;
	}
	
	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}
	

	public void setMisConsultas(ArrayList<Consulta> misConsultas) {
		this.misConsultas = misConsultas;
	}

	public void setMisCitas(ArrayList<Cita> misCitas) {
		this.misCitas = misCitas;
	}

	public String getEspecialidad() {
		return especialidad;
	}

	public int getCitasPorDia() {
		return citasPorDia;
	}

	public ArrayList<Consulta> getMisConsultas() {
		return misConsultas;
	}

	public ArrayList<Cita> getMisCitas() {
		return misCitas;
	}

	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	public void setCitasPorDia(int citasPorDia) {
		this.citasPorDia = citasPorDia;
	}

	public void agregarCita(Cita cita) {
	    misCitas.add(cita);
	}
	
	public void agregarConsulta(Consulta consulta) {
	    misConsultas.add(consulta);
	}

	public boolean tieneDisponibilidad(String dia) {
	    int ocupadas = 0;

	    for (Cita c : misCitas) {
	        if (c.getDia().equalsIgnoreCase(dia)
	            && !c.getEstado().equalsIgnoreCase("Cancelada")) {
	            ocupadas++;
	        }
	    }

	    return ocupadas < citasPorDia;
	}
	
	public ArrayList<Paciente> getMisPacientes() {

	    ArrayList<Paciente> pacientes = new ArrayList<>();

	    for (Cita c : misCitas) {
	        Paciente p = c.getPaciente();
	        if (!pacientes.contains(p)) {
	            pacientes.add(p);
	        }
	    }

	    for (Consulta consulta : misConsultas) {
	        Paciente p = consulta.getCita().getPaciente();
	        if (!pacientes.contains(p)) {
	            pacientes.add(p);
	        }
	    }

	    return pacientes;
	}

	public int cantCitasDisp(String dia) {
	    if (misCitas == null) {
	        return citasPorDia;
	    }

	    int ocupadas = 0;

	    for (Cita c : misCitas) {
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
	
	public int saberCantCitas() {
		return this.getMisCitas().size();
	}


}
