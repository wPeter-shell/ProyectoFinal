package logic;

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
	
    public void registrarCita(Persona persona) {

        if (!(persona instanceof Paciente)) {
            System.out.println("Error: Solo se pueden registrar citas para pacientes.");
            return;
        }

        Paciente p = (Paciente) persona;

        if (medicos == null || medicos.isEmpty()) {
            System.out.println("Error: No hay médicos asignados a esta secretaria.");
            return;
        }

        Medico medicoAsignado = medicos.get(0);

        String dia = "Hoy";

        if (!verificarDisponibilidad(medicoAsignado, dia)) {
            System.out.println("El médico no tiene disponibilidad ese día.");
            return;
        }

        Cita cita = new Cita(p, medicoAsignado, dia, "Pendiente");

        medicoAsignado.getMisCita().add(cita);

        System.out.println("Cita registrada exitosamente para " + p.getNombre());
    }

    public void modificarCita(Cita cita, String nuevoDia) {

        if (cita == null) {
            System.out.println("Error: La cita no existe.");
            return;
        }

        Medico medico = cita.getMedico();

        if (!verificarDisponibilidad(medico, nuevoDia)) {
            System.out.println("El médico no tiene disponibilidad en ese día.");
            return;
        }

        cita.setDia(nuevoDia);

        System.out.println("Cita modificada exitosamente a " + nuevoDia);
    }

    public void cancelarCita(Cita cita) {

        if (cita == null) {
            System.out.println("Error: La cita no existe.");
            return;
        }

        Medico medico = cita.getMedico();

        cita.setEstado("Cancelada");

        medico.getMisCita().remove(cita);

        System.out.println("Cita cancelada exitosamente.");
    }

    public boolean verificarDisponibilidad(Medico m, String dia) {

        if (m == null) return false;

        int ocupadas = 0;

        for (Cita c : m.getMisCita()) {
            if (c.getDia().equalsIgnoreCase(dia) && !c.getEstado().equals("Cancelada")) {
                ocupadas++;
            }
        }

        return ocupadas < m.getCitasPorDia();
    }

}

    
