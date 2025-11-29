package logic;

import java.io.Serializable;

public class Consulta implements Serializable{

    private Cita cita;
    private DatosConsulta datos;
    
	public Consulta(Cita cita, DatosConsulta datos) {
		super();
		this.cita = cita;
		this.datos = datos;
		
		cita.getPaciente().getHistorial().agregarConsulta(this);
	}
	
	public Cita getCita() {
		return cita;
	}
	public DatosConsulta getDatos() {
		return datos;
	}

	public void setDatos(DatosConsulta datos) {
		this.datos = datos;
	}

	public boolean esVigilada() {
		return datos.getDiagnostico().isBajoVigilancia();

    }

    public boolean esImportante() {
        return datos.isImportante();
    }

}
