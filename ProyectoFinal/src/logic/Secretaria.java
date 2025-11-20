package logic;

import java.util.ArrayList;

public class Secretaria extends Persona {

    private ArrayList<Medico> medico;

    public Secretaria(String nombre, String apellido, String cedula, char genero, int edad, 
    				  String direccion, ArrayList<Medico> medico) {

        super(nombre, apellido, cedula, genero, edad, direccion);
        this.medico = medico;
    }

    public ArrayList<Medico> getMedicos() {
        return medico;
    }

    public void setMedicos(ArrayList<Medico> medicos) {
        this.medico = medicos;
    }
}
