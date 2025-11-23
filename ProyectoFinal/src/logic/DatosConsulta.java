package logic;

import java.util.ArrayList;

public class DatosConsulta {

    private String sintomas;
    private String analisisHistorial;
    private Enfermedad diagnostico;
    private boolean bajoVigilancia;
    private boolean importante;
    private String tratamiento;
    private ArrayList<Vacuna> vacunasAplicadas;

    public DatosConsulta(String sintomas, String analisisHistorial, Enfermedad diagnostico,
                         boolean bajoVigilancia, boolean importante, String tratamiento,
                         ArrayList<Vacuna> vacunasAplicadas) {

        this.sintomas = sintomas;
        this.analisisHistorial = analisisHistorial;
        this.diagnostico = diagnostico;
        this.bajoVigilancia = bajoVigilancia;
        this.importante = importante;
        this.tratamiento = tratamiento;
        this.vacunasAplicadas = vacunasAplicadas;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public String getAnalisisHistorial() {
        return analisisHistorial;
    }

    public void setAnalisisHistorial(String analisisHistorial) {
        this.analisisHistorial = analisisHistorial;
    }

    public Enfermedad getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(Enfermedad diagnostico) {
        this.diagnostico = diagnostico;
    }

    public boolean isBajoVigilancia() {
        return bajoVigilancia;
    }

    public void setBajoVigilancia(boolean bajoVigilancia) {
        this.bajoVigilancia = bajoVigilancia;
    }

    public boolean isImportante() {
        return importante;
    }

    public void setImportante(boolean importante) {
        this.importante = importante;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public ArrayList<Vacuna> getVacunasAplicadas() {
        return vacunasAplicadas;
    }

    public void setVacunasAplicadas(ArrayList<Vacuna> vacunasAplicadas) {
        this.vacunasAplicadas = vacunasAplicadas;
    }    
    
    
}
