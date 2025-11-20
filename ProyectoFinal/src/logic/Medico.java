package logic;

import java.util.ArrayList;

public class Medico extends Persona {

    private Secretaria secretaria;
    private String especialidad;
    private int citasPorDia;
    private ArrayList<Consulta> misConsulta;
    private ArrayList<Cita> misCita;

    public Medico(String nombre, String apellido, String cedula, char genero, int edad, String direccion,
                  Secretaria secretaria, String especialidad, int citasPorDia,
                  ArrayList<Consulta> misConsulta, ArrayList<Cita> misCita) {

        super(nombre, apellido, cedula, genero, edad, direccion);

        this.secretaria = secretaria;
        this.especialidad = especialidad;
        this.citasPorDia = citasPorDia;
        this.misConsulta = misConsulta;
        this.misCita = misCita;
    }

    public Secretaria getSecretaria() {
        return secretaria;
    }

    public void setSecretaria(Secretaria secretaria) {
        this.secretaria = secretaria;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public int getCitasPorDia() {
        return citasPorDia;
    }

    public void setCitasPorDia(int citasPorDia) {
        this.citasPorDia = citasPorDia;
    }

    public ArrayList<Consulta> getMisConsulta() {
        return misConsulta;
    }

    public void setMisConsultas(ArrayList<Consulta> misConsulta) {
        this.misConsulta = misConsulta;
    }

    public ArrayList<Cita> getMisCita() {
        return misCita;
    }

    public void setMisCitas(ArrayList<Cita> misCita) {
        this.misCita = misCita;
    }
}
