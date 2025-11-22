package logic;

public class Cita {

    private Persona persona;
    private Medico medico;
    private String dia;
    private String estado;

    public Cita(Persona persona, Medico medico, String dia) {
        this.persona = persona;
        this.medico = medico;
        this.dia = dia;
        this.estado = "Pendiente";
    }

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
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
