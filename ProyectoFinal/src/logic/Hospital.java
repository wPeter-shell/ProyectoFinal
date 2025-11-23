package logic;

import java.util.ArrayList;

public class Hospital {
	
	private static Hospital instancia;
	private ArrayList<Paciente> misPacientes;
	private ArrayList<Medico> misMedicos;
	private ArrayList<Secretaria> misSecretarias;
	private ArrayList<Cita> misCitas;
	private ArrayList<Vacuna> controlVacunas;
	private ArrayList<Enfermedad> misEnfermedades;
	private ArrayList<Enfermedad> enfermedadesVigiladas;
	
	private Hospital() {
        misPacientes = new ArrayList<>();
        misMedicos = new ArrayList<>();
        misSecretarias = new ArrayList<>();
        misCitas = new ArrayList<>();
        controlVacunas = new ArrayList<>();
        misEnfermedades = new ArrayList<>();
        enfermedadesVigiladas = new ArrayList<>();
    }

	public static Hospital getInstancia() {
        if (instancia == null) {
            instancia = new Hospital();
        }
        return instancia;
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

	public static void setInstancia(Hospital instancia) {
		Hospital.instancia = instancia;
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

	public void agregarPaciente(Paciente p) {
	    misPacientes.add(p);
	}

	public void agregarMedico(Medico m) {
	    misMedicos.add(m);
	}
	
	public void agregarSecretaria(Secretaria s) {
	    misSecretarias.add(s);
	}
	
	public void agregarCita(Cita cita) {
	    misCitas.add(cita);
	}
	
	public void agregarControlVacuna(Vacuna vacuna) {
	    controlVacunas.add(vacuna);
	}
	
	public void agregarEnfermedad(Enfermedad enfermedad) {
	    misEnfermedades.add(enfermedad);
	}
	
	public void agregarEnfermedadVigilada(Enfermedad enfermedad) {
	    enfermedadesVigiladas.add(enfermedad);
	}
	
	public Paciente buscarPacientePorCedula(String cedula) {
		for(Paciente p : misPacientes) {
			if(p.getCedula().equals(cedula)) {
				return p;
			}
		}
		return null;
	}
	

}
	
	
	

	