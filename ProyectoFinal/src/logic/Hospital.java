package logic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;

public class Hospital implements Serializable {
	
	private static Hospital instancia;
	private ArrayList<Paciente> misPacientes;
	private ArrayList<Medico> misMedicos;
	private Administrador administrador;
	private Secretaria secretaria;
	private ArrayList<Cita> misCitas;
	private ArrayList<Vacuna> controlVacunas;
	private ArrayList<Enfermedad> misEnfermedades;
	private ArrayList<Enfermedad> enfermedadesVigiladas;
	private ArrayList<String> especialidades;

	
	private Hospital() {
        misPacientes = new ArrayList<>();
        misMedicos = new ArrayList<>();
        secretaria = null;
        misCitas = new ArrayList<>();
        controlVacunas = new ArrayList<>();
        misEnfermedades = new ArrayList<>();
        enfermedadesVigiladas = new ArrayList<>();
        especialidades = new ArrayList<>();
        cargarEspecialidadesPredeterminadas();
        
        administrador = new Administrador("Admin", "123", this);

    }
	
	public static Hospital getInstancia() {
		
        if (instancia == null) {
        	
        	Hospital h = cargarDatos();
        	
        	if(h != null) {
        		
        		instancia = h;
        	}else {
        		
        		instancia = new Hospital();
        	}
        }
        return instancia;
    }

	public ArrayList<Paciente> getMisPacientes() {
		return misPacientes;
	}

	public ArrayList<Medico> getMisMedicos() {
		return misMedicos;
	}
	
	public Administrador getAdministrador() {
		return administrador;
	}

	public Secretaria getSecretaria() { 
        return secretaria;
    }

	public ArrayList<Cita> getMisCitas() {
		return misCitas;
	}

	public ArrayList<Vacuna> getControlVacunas() {
		return controlVacunas;
	}

	public ArrayList<Enfermedad> getMisEnfermedades() {
		return misEnfermedades;
	}

	public ArrayList<Enfermedad> getEnfermedadesVigiladas() {
		return enfermedadesVigiladas;
	}
	
	public ArrayList<String> getEspecialidades() {
		return especialidades;
	}

	public static void setInstancia(Hospital instancia) {
		Hospital.instancia = instancia;
	}

	public void setMisPacientes(ArrayList<Paciente> misPacientes) {
		this.misPacientes = misPacientes;
	}

	public void setMisMedicos(ArrayList<Medico> misMedicos) {
		this.misMedicos = misMedicos;
	}

	public void setMisCitas(ArrayList<Cita> misCitas) {
		this.misCitas = misCitas;
	}

	public void setControlVacunas(ArrayList<Vacuna> controlVacunas) {
		this.controlVacunas = controlVacunas;
	}

	public void setMisEnfermedades(ArrayList<Enfermedad> misEnfermedades) {
		this.misEnfermedades = misEnfermedades;
	}

	public void setEnfermedadesVigiladas(ArrayList<Enfermedad> enfermedadesVigiladas) {
		this.enfermedadesVigiladas = enfermedadesVigiladas;
	}
	
	public void setAgregarEspecialidad(ArrayList<String> especialidades) {
	    this.especialidades = especialidades;
	}
	
	public void cargarEspecialidadesPredeterminadas() {
		String[] lista = {
		        "Cardiolog�a", "Pediatr�a", "Dermatolog�a", "Ginecolog�a",
		        "Obstetricia", "Neurolog�a", "Psiquiatr�a", "Oftalmolog�a",
		        "Otorrinolaringolog�a", "Neumolog�a", "Gastroenterolog�a",
		        "Endocrinolog�a", "Urolog�a", "Nefrolog�a", "Hematolog�a",
		        "Oncolog�a", "Reumatolog�a", "Traumatolog�a", "Ortopedia",
		        "Medicina Interna", "Medicina General", "Cirug�a General",
		        "Cirug�a Pl�stica", "Cirug�a Cardiovascular",
		        "Cirug�a Pedi�trica", "Cirug�a Oncol�gica",
		        "Cirug�a Maxilofacial", "Anestesiolog�a", "Radiolog�a",
		        "Medicina Nuclear", "Rehabilitaci�n y Fisiatr�a",
		        "Nutrici�n Cl�nica", "Infectolog�a", "Alergolog�a",
		        "Inmunolog�a", "Geriatr�a", "Neonatolog�a",
		        "Gen�tica M�dica", "Urgencias M�dicas",
		        "Terapia Intensiva", "Medicina Familiar",
		        "Neurocirug�a", "Proctolog�a",
		        "Coloproctolog�a", "Angiolog�a",
		        "Podolog�a M�dica", "Sexolog�a Cl�nica",
		        "Toxicolog�a", "Medicina del Trabajo"
		    };

		    for (String esp : lista) {
		        this.especialidades.add(esp);
		    }

	}


	public void agregarPaciente(Paciente p) {
	    misPacientes.add(p);
	}

	public void agregarMedico(Medico m) {
	    misMedicos.add(m);
	}
	
	public void agregarCita(Cita cita) {
	    misCitas.add(cita);
	}
	
	public void agregarControlVacuna(Vacuna vacuna) {
	    controlVacunas.add(vacuna);
	}
	
	public void agregarEnfermedad(Enfermedad enfermedad) {
	    misEnfermedades.add(enfermedad);
	}
	
	public void agregarEnfermedadVigilada(Enfermedad enfermedad) {
	    enfermedadesVigiladas.add(enfermedad);
	}
	
	public void agregarEspecialidad(String especialidad) {
	    especialidades.add(especialidad);
	}
	
	public Paciente buscarPacientePorCedula(String cedula) {
		for(Paciente p : misPacientes) {
			if(p.getCedula().equals(cedula)) {
				return p;
			}
		}
		return null;
	}	
	
	public void registrarSecretaria(Secretaria s) {
	    if (s == null) {
	        throw new IllegalArgumentException("La secretaria no puede ser null.");
	    }
	    if (this.secretaria != null) {
	        throw new IllegalStateException("Ya hay una secretaria registrada en el hospital.");
	    }
	    this.secretaria = s;
	}

	public void reemplazarSecretaria(Secretaria nueva) {
	    if (nueva == null) {
	        throw new IllegalArgumentException("La secretaria no puede ser null.");
	    }
	    this.secretaria = nueva;
	}
	
	
	public Object LogIn(String user, String password) {
		
		if(user == null || password == null) {
			throw new IllegalArgumentException("Usuario y/o Contraseña no pueden ser nulos");
		}
		
		if(user.equalsIgnoreCase(administrador.getUsuario()) && password.equals(administrador.getPassword()) ) {
			return administrador;
		}
		
		if (secretaria != null && user.equalsIgnoreCase(secretaria.getUsuario()) && password.equals(secretaria.getPassword())) {
			return secretaria;
		}
		
		for(Medico m : misMedicos) {
			if(user.equalsIgnoreCase(m.getUsuario() ) && password.equals(m.getPassword()) ) {
				return m;
			}
		}
		
		return null;
	}

	public String crearUsuarioMedico() {
		int numeroMedico = misMedicos.size() + 1;
		return String.format("Med-%06d", numeroMedico);
	}

	public String crearUsuarioSecretaria() {
		return "Secre-1";
	}

	private String generarPasswordAleatoria() {
		String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder password = new StringBuilder();
		
		for (int i = 0; i < 8; i++) {
			int index = (int) (Math.random() * caracteres.length());
			password.append(caracteres.charAt(index));
		}
		
		return password.toString();
	}

	public String crearPasswordMedico() {
		return generarPasswordAleatoria();
	}

	public String crearPasswordSecretaria() {
		return generarPasswordAleatoria();
	}
	
	
	public void guardarDatos() {
		try {
			ObjectOutputStream datos = new ObjectOutputStream(new FileOutputStream("hospital.dat"));
			datos.writeObject(this);
			datos.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Hospital cargarDatos() {
		try {
			ObjectInputStream datos = new ObjectInputStream(new FileInputStream("hospital.dat"));
			Hospital h = (Hospital) datos.readObject();
			datos.close();
			return h;
		}catch(Exception e) {
			return null;
		}
	}
	
	
	public ArrayList<String> getResumenEnfermedadesVigiladas() {
		
	   ArrayList<String> resumen = new ArrayList<String>();

	   for (Enfermedad enfermedad : enfermedadesVigiladas) {

	      int cantPacientes = 0;

	      for (Paciente paciente : misPacientes) {

	         if (paciente != null && paciente.getHistorial() != null) {

	            boolean pacienteTieneEsaEnfermedad = false;
	            HistorialClinico historial = paciente.getHistorial();
	            ArrayList<Consulta> consultas = historial.getMisConsultas();

	            for (Consulta consulta : consultas) {

	               if (consulta != null && consulta.getDatos() != null) {

	                  DatosConsulta datos = consulta.getDatos();
	                  Enfermedad diag = datos.getDiagnostico();

	                  if (!pacienteTieneEsaEnfermedad
	                        && diag != null
	                        && diag.getNombre() != null
	                        && enfermedad.getNombre() != null
	                        && diag.getNombre().equalsIgnoreCase(enfermedad.getNombre())) {

	                     pacienteTieneEsaEnfermedad = true;
	                  }
	               }
	            }

	            if (pacienteTieneEsaEnfermedad) {
	               cantPacientes++;
	            }
	         }
	      }

	      String linea = enfermedad.getNombre()
	            + " - Pacientes en vigilancia: "
	            + cantPacientes;

	      resumen.add(linea);
	   }

	   return resumen;
	}

	
	
	public void generarArchivoEnfermedadesVigiladas(String nombreArchivo) {
	   ArrayList<String> resumen = getResumenEnfermedadesVigiladas();

	   PrintWriter writer = null;

	   try {
	      writer = new PrintWriter(new FileWriter(nombreArchivo));

	      writer.println("REPORTE DE ENFERMEDADES BAJO VIGILANCIA");
	      writer.println("=======================================");
	      writer.println();

	      for (String linea : resumen) {
	         writer.println(linea);
	      }

	   } catch (IOException e) {
	      e.printStackTrace();
	   } finally {
	      if (writer != null) {
	         writer.close();
	      }
	   }
	}
	
}