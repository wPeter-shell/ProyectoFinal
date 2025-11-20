package logic;

import java.util.ArrayList;

public class HistorialClinico {

	private ArrayList<Consulta> misConsultas;

	private HistorialClinico(ArrayList<Consulta> misConsultas) {
		super();
		this.misConsultas = misConsultas;
	}

	public ArrayList<Consulta> getMisConsultas() {
		return misConsultas;
	}

	public void setMisConsultas(ArrayList<Consulta> misConsultas) {
		this.misConsultas = misConsultas;
	}
}
