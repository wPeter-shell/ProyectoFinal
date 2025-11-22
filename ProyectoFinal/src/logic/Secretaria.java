package logic;

public class Secretaria extends Persona {

	public Secretaria(String nombre, String apellido, String cedula, char genero, int edad, String telefono,
			String direccion) {
		super(nombre, apellido, cedula, genero, edad, telefono, direccion);
	}
	
	 public Cita registrarCita(Paciente paciente, Medico medico, String dia) {

	        if (paciente == null)
	            throw new IllegalArgumentException("Debe proporcionar un paciente.");

	        if (medico == null)
	            throw new IllegalArgumentException("Debe seleccionar un médico.");

	        if (dia == null)
	            throw new IllegalArgumentException("Debe seleccionar un día.");

	        if (!verificarDisponibilidad(medico, dia))
	            throw new IllegalStateException("El médico no tiene disponibilidad ese día.");

	        Cita cita = new Cita(paciente, medico, dia);

	        Hospital.getInstancia().agregarCita(cita);

	        medico.getMisCita().add(cita);

	        return cita;
	    }

    public boolean modificarCita(Cita cita, String nuevoDia) {

        if (cita == null) {
            throw new IllegalArgumentException("Debe seleccionar una cita");
        }
        
        if(nuevoDia == null) {
        	throw new IllegalArgumentException("Debe seleccionar un dia");
        }

        Medico medico = cita.getMedico();

        if (!verificarDisponibilidad(medico, nuevoDia)) {
        	throw new IllegalStateException("El medico no tiene disponibiladad ese dia");
        }
        
        cita.setDia(nuevoDia);
        
        return true;
    }

    public boolean cancelarCita(Cita cita) {
    	
        if (cita == null) {
        	throw new IllegalArgumentException("Debe seleccionar una cita");
        }

        cita.setEstado("Cancelada");
        
        return true;
    }

    public boolean verificarDisponibilidad(Medico m, String dia) {

        if (m == null)
        	return false;

        int ocupadas = 0;

        for (Cita c : m.getMisCita()) {
            if (c.getDia().equalsIgnoreCase(dia) && !c.getEstado().equals("Cancelada")) {
                ocupadas++;
            }
        }

        return ocupadas < m.getCitasPorDia();
    }

}