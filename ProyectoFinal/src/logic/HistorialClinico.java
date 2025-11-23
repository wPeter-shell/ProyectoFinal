package logic;

import java.util.ArrayList;

public class HistorialClinico {

	private ArrayList<Consulta> misConsultas;

	public HistorialClinico() {
		super();
		this.misConsultas = new ArrayList<>();
	}

	public ArrayList<Consulta> getMisConsultas() {
		return misConsultas;
	}

	public void setMisConsultas(ArrayList<Consulta> misConsultas) {
		this.misConsultas = misConsultas;
	}

	public void agregarConsulta(Consulta consulta) {
        misConsultas.add(consulta);
    }
}
