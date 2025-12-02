package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import logic.Medico;
import logic.Paciente;
import logic.Administrador;
import logic.Enfermedad;
import logic.Vacuna;
import logic.Secretaria;
import logic.Hospital;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent; 

public class Principal extends JFrame {

   private JPanel contentPane;
   private Dimension dim;

   private Object usuarioLogueado;

   private JMenuItem itemRegistrarMedico;
   private JMenuItem itemRegistrarSecretaria;
   private JMenuItem itemAgregarEnfermedad;
   private JMenuItem itemAgregarVacuna;
   private JMenuItem itemDefinirNumCitas;
   private JMenuItem itemHacerCita;
   private JMenuItem menuListar;
   private JMenuItem menuArchivos;
   private JMenuItem itemListarPacientes;
   private JMenuItem itemGuardarArchivos;
   private JMenuItem itemRespaldo;
   private JMenu menuCitas;
   private JMenu menuAdmin;
   private JMenu menuSistema;
   static Socket sfd = null;
   static DataInputStream EntradaSocket;
   static DataOutputStream SalidaSocket;

   // Labels para las cantidades en los cards del dashboard
   private JLabel lblCantMedicos;
   private JLabel lblCantPacientes;
   private JLabel lblCantVacunas;
   private JLabel lblCantEnfermedades;

   private JMenu menuConsulta;
   private JMenuItem itemAtenderConsultas;
   private JMenuItem itemVerCuentas;
   private JMenuItem itemModificarCita;
   private JMenuItem itemEliminarCita;
   
   

   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            try {
               Principal frame = new Principal(null);
               frame.setVisible(true);
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      });
   }

   public Principal(Object usuarioLogueado) {
      this.usuarioLogueado = usuarioLogueado;

      dim = getToolkit().getScreenSize();
      setResizable(false);
      setTitle("Sistema Hospitalario");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(dim.width + 10, dim.height - 38);
      setLocationRelativeTo(null);

      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
      contentPane.setLayout(new BorderLayout());
      setContentPane(contentPane);

      crearMenu();
      crearDashboardMinimal();
      configurarPermisos();
   }

   public Principal() {
      this(null);
   }

   private void crearMenu() {

      JMenuBar menuBar = new JMenuBar();
      menuBar.setBorder(new EmptyBorder(2, 10, 2, 10));
      setJMenuBar(menuBar);

      menuSistema = new JMenu("Sistema");
      menuSistema.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      menuBar.add(menuSistema);

      JMenuItem itemCerrarSesion = new JMenuItem("Cerrar sesi\u00f3n");
      itemCerrarSesion.addActionListener(e -> {
         dispose();
         Login_Inicial login = new Login_Inicial();
         login.setVisible(true);
      });
      menuSistema.add(itemCerrarSesion);

      JMenuItem itemSalir = new JMenuItem("Salir");
      itemSalir.addActionListener(e -> System.exit(0));
      menuSistema.add(itemSalir);

      menuAdmin = new JMenu("Administraci\u00f3n");
      menuAdmin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      menuBar.add(menuAdmin);

      itemRegistrarMedico = new JMenuItem("Registrar m\u00e9dico");
      itemRegistrarMedico.addActionListener(e -> {
         new RegistrarMedico(this).setVisible(true);
      });
      menuAdmin.add(itemRegistrarMedico);

      itemRegistrarSecretaria = new JMenuItem("Registrar secretaria");
      itemRegistrarSecretaria.addActionListener(e -> {
         new RegistrarSecretaria().setVisible(true);
      });
      menuAdmin.add(itemRegistrarSecretaria);
      
      itemAgregarEnfermedad = new JMenuItem("Agregar enfermedad");
      itemAgregarEnfermedad.addActionListener(e -> {
         new AgregarEnfermedad(this).setVisible(true);
      });
      menuAdmin.add(itemAgregarEnfermedad);

      itemAgregarVacuna = new JMenuItem("Agregar vacuna");
      itemAgregarVacuna.addActionListener(e -> {
         new AgregarVacuna(this).setVisible(true);
      });
      menuAdmin.add(itemAgregarVacuna);

      itemDefinirNumCitas = new JMenuItem("Modificar n\u00ba de citas por d\u00eda");
      itemDefinirNumCitas.addActionListener(e -> {
         new DefinirNumCitas().setVisible(true);
      });
      menuAdmin.add(itemDefinirNumCitas);

      menuCitas = new JMenu("Citas");
      menuCitas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      menuBar.add(menuCitas);

      itemHacerCita = new JMenuItem("Hacer cita");
      itemHacerCita.addActionListener(e -> {
         new RegistrarCita().setVisible(true);
      });
      menuCitas.add(itemHacerCita);
      
      itemModificarCita = new JMenuItem("Modificar Cita");
      itemModificarCita.setEnabled(false);
      menuCitas.add(itemModificarCita);
      
      itemEliminarCita = new JMenuItem("Eliminar Cita");
      itemEliminarCita.setEnabled(false);
      menuCitas.add(itemEliminarCita);

      menuConsulta = new JMenu("Consultas");
      menuConsulta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      menuBar.add(menuConsulta);

      itemAtenderConsultas = new JMenuItem("Atender consultas");
      
      menuConsulta.add(itemAtenderConsultas);

    

      menuArchivos = new JMenu("Archivos");
      menuArchivos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      menuBar.add(menuArchivos);

      itemListarPacientes = new JMenuItem("Listar pacientes");
      menuArchivos.add(itemListarPacientes);

      itemGuardarArchivos = new JMenuItem("Guardar Archivos");
      itemGuardarArchivos.addActionListener(new ActionListener() {
      	public void actionPerformed(ActionEvent e) {
      		Hospital.getInstancia().guardarDatos();
      		JOptionPane.showMessageDialog(null, "Guardado Correctamente.", "Guardado", JOptionPane.INFORMATION_MESSAGE);
      	}
      });
      menuArchivos.add(itemGuardarArchivos);

      itemRespaldo = new JMenuItem("Hacer Respaldo");
      itemRespaldo.addActionListener(new ActionListener() {

  	   public void actionPerformed(ActionEvent e) {
  	      Socket sfd = null;
  	      DataInputStream inFile = null;
  	      DataOutputStream outSocket = null;

  	      try {
  	         // 1. Conectarse al servidor
  	         sfd = new Socket("127.0.0.1", 7000);

  	         // 2. Abrir el archivo ORIGINAL que quieres respaldar
  	         File archivoHospital = new File("hospital.dat");   // OJO: hospital.dat
  	         if (!archivoHospital.exists()) {
  	            JOptionPane.showMessageDialog(
  	               Principal.this,
  	               "El archivo hospital.dat no existe. Primero guarda los datos.",
  	               "Error de respaldo",
  	               JOptionPane.ERROR_MESSAGE
  	            );
  	            return;
  	         }

  	         // 3. Streams
  	         inFile = new DataInputStream(new FileInputStream(archivoHospital));
  	         outSocket = new DataOutputStream(sfd.getOutputStream());

  	         int unByte;
  	         int contador = 0;
  	         while ((unByte = inFile.read()) != -1) {
  	            outSocket.write(unByte);
  	            contador++;
  	         }
  	         outSocket.flush();
  	         System.out.println("Enviados " + contador + " bytes al servidor.");

  	         JOptionPane.showMessageDialog(
  	            Principal.this,
  	            "Respaldo enviado correctamente al servidor.",
  	            "Respaldo",
  	            JOptionPane.INFORMATION_MESSAGE
  	         );

  	      } catch (IOException ioe) {
  	         ioe.printStackTrace();
  	         JOptionPane.showMessageDialog(
  	            Principal.this,
  	            "Error en la comunicación con el servidor.",
  	            "Error de conexión",
  	            JOptionPane.ERROR_MESSAGE
  	         );
  	      } finally {
  	         try { if (inFile != null) inFile.close(); } catch (IOException ex) {}
  	         try { if (outSocket != null) outSocket.close(); } catch (IOException ex) {}
  	         try { if (sfd != null) sfd.close(); } catch (IOException ex) {}
  	      }
  	   }
  	});
      menuArchivos.add(itemRespaldo);

   }

   private void crearDashboardMinimal() {

      JPanel panelFondo = new JPanel();
      panelFondo.setBackground(new Color(245, 247, 250));
      panelFondo.setLayout(new BorderLayout());
      contentPane.add(panelFondo, BorderLayout.CENTER);

      JPanel header = new JPanel();
      header.setBackground(new Color(0, 128, 128));
      header.setLayout(new BorderLayout());
      header.setBorder(new EmptyBorder(12, 20, 12, 20));

      JLabel lblLogo = new JLabel();
      lblLogo.setHorizontalAlignment(SwingConstants.LEFT);
      try {
         ImageIcon icon = new ImageIcon(getClass().getResource("/img/hospital.png"));
         lblLogo.setIcon(icon);
      } catch (Exception e) {
      }
      header.add(lblLogo, BorderLayout.WEST);

      JPanel panelTitulos = new JPanel();
      panelTitulos.setOpaque(false);
      panelTitulos.setLayout(new BorderLayout());

      JLabel lblTitulo = new JLabel("Hospital Plus+");
      lblTitulo.setForeground(Color.WHITE);
      lblTitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 26));
      lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
      panelTitulos.add(lblTitulo, BorderLayout.NORTH);

      JLabel lblSubtitulo = new JLabel("Sistema de gesti\u00f3n cl\u00ednica");
      lblSubtitulo.setForeground(new Color(230, 240, 250));
      lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      lblSubtitulo.setHorizontalAlignment(SwingConstants.LEFT);
      panelTitulos.add(lblSubtitulo, BorderLayout.SOUTH);

      header.add(panelTitulos, BorderLayout.CENTER);

      JLabel lblUsuarioInfo = new JLabel();
      lblUsuarioInfo.setForeground(Color.WHITE);
      lblUsuarioInfo.setHorizontalAlignment(SwingConstants.RIGHT);
      lblUsuarioInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      lblUsuarioInfo.setText(obtenerTextoUsuario());
      header.add(lblUsuarioInfo, BorderLayout.EAST);

      panelFondo.add(header, BorderLayout.NORTH);

      JPanel panelCentro = new JPanel();
      panelCentro.setOpaque(false);
      panelCentro.setBorder(new EmptyBorder(40, 40, 40, 40));
      panelCentro.setLayout(new GridBagLayout());
      panelFondo.add(panelCentro, BorderLayout.CENTER);

      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(15, 15, 15, 15);
      gbc.fill = GridBagConstraints.BOTH;
      gbc.weightx = 0.5;
      gbc.weighty = 0.5;

      // Inicializar labels de cantidades
      lblCantPacientes = new JLabel("0");
      lblCantMedicos = new JLabel("0");
      lblCantEnfermedades = new JLabel("0");
      lblCantVacunas = new JLabel("0");

      JPanel cardPacientes = crearCardEstadistica(
         "Pacientes",
         lblCantPacientes,
         new Color(76, 175, 80)
      );
      gbc.gridx = 0;
      gbc.gridy = 0;
      panelCentro.add(cardPacientes, gbc);

      JPanel cardMedicos = crearCardEstadistica(
         "M\u00e9dicos",
         lblCantMedicos,
         new Color(33, 150, 243)
      );
      gbc.gridx = 1;
      gbc.gridy = 0;
      panelCentro.add(cardMedicos, gbc);

      JPanel cardEnfermedades = crearCardEstadistica(
         "Enfermedades",
         lblCantEnfermedades,
         new Color(255, 152, 0)
      );
      gbc.gridx = 0;
      gbc.gridy = 1;
      panelCentro.add(cardEnfermedades, gbc);

      JPanel cardVacunas = crearCardEstadistica(
         "Vacunas",
         lblCantVacunas,
         new Color(156, 39, 176)
      );
      gbc.gridx = 1;
      gbc.gridy = 1;
      panelCentro.add(cardVacunas, gbc);

      // Cargar valores reales desde Hospital
      actualizarCards();
   }

   private JPanel crearCardEstadistica(String titulo, JLabel lblCantidad, Color colorSuperior) {
      JPanel card = new JPanel();
      card.setBackground(Color.WHITE);
      card.setBorder(new EmptyBorder(16, 18, 16, 18));
      card.setLayout(new BorderLayout(8, 10));

      JPanel barraColor = new JPanel();
      barraColor.setBackground(colorSuperior);
      barraColor.setPreferredSize(new Dimension(10, 6));
      card.add(barraColor, BorderLayout.NORTH);

      JLabel lblTitulo = new JLabel(titulo);
      lblTitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
      lblTitulo.setForeground(new Color(60, 60, 60));
      lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
      card.add(lblTitulo, BorderLayout.WEST);

      lblCantidad.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 32));
      lblCantidad.setForeground(new Color(40, 40, 40));
      lblCantidad.setHorizontalAlignment(SwingConstants.RIGHT);
      card.add(lblCantidad, BorderLayout.EAST);

      return card;
   }

   public void actualizarCards() {
      Hospital h = Hospital.getInstancia();
      if (lblCantPacientes != null) {
         lblCantPacientes.setText(String.valueOf(h.getMisPacientes().size()));
      }
      if (lblCantMedicos != null) {
         lblCantMedicos.setText(String.valueOf(h.getMisMedicos().size()));
      }
      if (lblCantEnfermedades != null) {
         lblCantEnfermedades.setText(String.valueOf(h.getMisEnfermedades().size()));
      }
      if (lblCantVacunas != null) {
         lblCantVacunas.setText(String.valueOf(h.getControlVacunas().size()));
      }
   }
   
   public boolean tieneNumero(String cadena) {
	    return cadena.matches(".*\\d.*");
	}

   
   public boolean tieneLetra(String cadena) {
	    return cadena.matches(".*[a-zA-Z].*");
	}


   private String obtenerTextoUsuario() {

      if (usuarioLogueado == null) {
         return "Sesi\u00f3n sin usuario";
      }

      if (usuarioLogueado instanceof Administrador) {
         return "Administrador: " + ((Administrador) usuarioLogueado).getUsuario();
      } else if (usuarioLogueado instanceof Medico) {
         return "M\u00e9dico: " + ((Medico) usuarioLogueado).getNombre();
      } else if (usuarioLogueado instanceof Secretaria) {
         return "Secretaria: " + ((Secretaria) usuarioLogueado).getNombre();
      }

      return "Sesi\u00f3n activa";
   }

   private void configurarPermisos() {

      itemRegistrarMedico.setEnabled(false);
      itemRegistrarSecretaria.setEnabled(false);
      itemAgregarEnfermedad.setEnabled(false);
      itemAgregarVacuna.setEnabled(false);
      itemDefinirNumCitas.setEnabled(false);
      itemHacerCita.setEnabled(false);
      itemListarPacientes.setEnabled(false);
      
      if (usuarioLogueado instanceof Administrador) {
         itemRegistrarMedico.setEnabled(true);
         itemRegistrarSecretaria.setEnabled(true);
         itemAgregarEnfermedad.setEnabled(true);
         itemAgregarVacuna.setEnabled(true);
         itemDefinirNumCitas.setEnabled(true);
         itemListarPacientes.setEnabled(true);
         itemAtenderConsultas.setEnabled(true);
         itemHacerCita.setEnabled(true);
         
      } else if (usuarioLogueado instanceof Secretaria) {
         itemHacerCita.setEnabled(true);
      } else if (usuarioLogueado instanceof Medico) {
         itemHacerCita.setEnabled(false);
      }
   }
}
