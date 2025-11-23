package logic;

public class Cita {

    private Paciente paciente;
    private Medico medico;
    private String dia;
    private String estado;

    public Cita(Paciente paciente, Medico medico, String dia) {
        this.paciente = paciente;
        this.medico = medico;
        this.dia = dia;
        this.estado = "Pendiente";
    }

    public Paciente getPaciente() {
		return paciente;
	}


	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}


	public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
