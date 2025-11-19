package logic;

import java.util.ArrayList;

public class HistorialClinico {

	private ArrayList<Consulta> misConsultas;

	private HistorialClinico(Arraylist<Consulta> misConsultas) {
		super();
		this.misConsultas = misConsultas;
	}

	public Arraylist<Consulta> getMisConsultas() {
		return misConsultas;
	}

	public void setMisConsultas(Arraylist<Consulta> misConsultas) {
		this.misConsultas = misConsultas;
	}
}
