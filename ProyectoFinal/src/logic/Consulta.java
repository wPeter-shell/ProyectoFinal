package logic;

public class Consulta {

    private Paciente paciente;
    private Medico medico;
    private Cita cita;
    private DatosConsulta datos;

    public Consulta(Paciente paciente, Cita cita, DatosConsulta datos) {
        this.paciente = paciente;
        this.cita = cita;
        this.datos = datos;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public DatosConsulta getDatos() {
        return datos;
    }

    public void setDatos(DatosConsulta datos) {
        this.datos = datos;
    }

	public Medico getMedico() {
		return medico;
	}

	public void setMedico(Medico medico) {
		this.medico = medico;
	}
}
