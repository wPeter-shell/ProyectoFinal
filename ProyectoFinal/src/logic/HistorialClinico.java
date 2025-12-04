package logic;

import java.io.Serializable;
import java.util.ArrayList;

public class HistorialClinico implements Serializable {

    private ArrayList<Consulta> misConsultas;

    public HistorialClinico() {
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

    public Consulta getUltimaConsulta() {
        if (misConsultas == null || misConsultas.isEmpty()) {
            return null;
        }
        return misConsultas.get(misConsultas.size() - 1);
    }
}
