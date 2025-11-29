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
import java.rmi.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import logic.Administrador;
import logic.Medico;
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
   private JMenu menuConsulta;
   private JMenuItem itemAtenderConsultas;
   
   

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

      JMenuItem itemCerrarSesion = new JMenuItem("Cerrar sesión");
      itemCerrarSesion.addActionListener(e -> {
         Login_Inicial login = new Login_Inicial();
         login.setVisible(true);
         dispose();
      });
      menuSistema.add(itemCerrarSesion);

      JMenuItem itemSalir = new JMenuItem("Salir");
      itemSalir.addActionListener(e -> System.exit(0));
      menuSistema.add(itemSalir);

      menuAdmin = new JMenu("Administración");
      menuAdmin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      menuBar.add(menuAdmin);

      itemRegistrarMedico = new JMenuItem("Registrar médico");
      itemRegistrarMedico.addActionListener(e -> {
         new RegistrarMedico().setVisible(true);
      });
      menuAdmin.add(itemRegistrarMedico);

      itemRegistrarSecretaria = new JMenuItem("Registrar secretaria");
      itemRegistrarSecretaria.addActionListener(e -> {
         new RegistrarSecretaria().setVisible(true);
      });
      menuAdmin.add(itemRegistrarSecretaria);
      
      itemAgregarEnfermedad = new JMenuItem("Agregar enfermedad");
      itemAgregarEnfermedad.addActionListener(e -> {
         new AgregarEnfermedad().setVisible(true);
      });
      menuAdmin.add(itemAgregarEnfermedad);

      itemAgregarVacuna = new JMenuItem("Agregar vacuna");
      itemAgregarVacuna.addActionListener(e -> {
         new AgregarVacuna().setVisible(true);
      });
      menuAdmin.add(itemAgregarVacuna);

      itemDefinirNumCitas = new JMenuItem("Modificar nº de citas por día");
      itemDefinirNumCitas.addActionListener(e -> {
         new DefinirNumCitas().setVisible(true);
      });
      menuAdmin.add(itemDefinirNumCitas);

      menuCitas = new JMenu("Citas");
      menuCitas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      menuBar.add(menuCitas);

      itemHacerCita = new JMenuItem("Registrar cita");
      menuCitas.add(itemHacerCita);
      
      menuConsulta = new JMenu("Consultas");
      menuConsulta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      menuBar.add(menuConsulta);
      
      itemAtenderConsultas = new JMenuItem("Atender Consultas");
      menuConsulta.add(itemAtenderConsultas);
      
      menuListar = new JMenu("Listar");
      menuListar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      menuBar.add(menuListar);
      
      itemListarPacientes = new JMenuItem("ListarPacientes");
      menuListar.add(itemListarPacientes);
      
      menuArchivos = new JMenu("Archivos");
      menuArchivos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      menuBar.add(menuArchivos);
      
      itemGuardarArchivos = new JMenuItem("Guardar Archivos");
      itemGuardarArchivos.addActionListener(new ActionListener() {
      	public void actionPerformed(ActionEvent e) {
      		Hospital.getInstancia().guardarDatos();
      		JOptionPane.showMessageDialog(null, "Guardado Correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
      	}
      });
      menuArchivos.add(itemGuardarArchivos);
      
      itemRespaldo = new JMenuItem("Respaldo");
      itemRespaldo.addActionListener(new ActionListener() {
      	public void actionPerformed(ActionEvent e) {
      		try {
      			sfd = new Socket("127.0.0.1", 7000);
      			DataInputStream aux = new DataInputStream(new FileInputStream(new File("hospital_respaldo.dat")));
      			SalidaSocket = new DataOutputStream((sfd.getOutputStream()));
      			int unByte;
      			try {
      				while((unByte = aux.read()) != - 1) {
      					SalidaSocket.write(unByte);
      					SalidaSocket.flush();
      				}
      			}catch(IOException ioe) {
      				System.out.println("Error: "+ioe);
      			}
      		}catch(UnknownHostException uhe) {
      			System.out.println("No se puede acceder al servidor.");
      			System.exit(1);
      		}catch(IOException ioe) {
      			System.out.println("Comunicación rechazada.");
      			System.exit(1);
      		}
      	}
      });
      menuArchivos.add(itemRespaldo);
   }


   //   DASHBOARD

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

	   JLabel lblSubtitulo = new JLabel("Sistema de gestión clínica");
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

	   Hospital h = Hospital.getInstancia();
	   int cantPacientes = h.getMisPacientes().size();
	   int cantMedicos = h.getMisMedicos().size();
	   int cantEnfermedades = h.getMisEnfermedades().size();
	   int cantVacunas = h.getControlVacunas().size();

	   JPanel cardPacientes = crearCardEstadistica(
	      "Pacientes",
	      cantPacientes,
	      new Color(76, 175, 80) // verde
	   );
	   gbc.gridx = 0;
	   gbc.gridy = 0;
	   panelCentro.add(cardPacientes, gbc);

	   JPanel cardMedicos = crearCardEstadistica(
	      "Médicos",
	      cantMedicos,
	      new Color(33, 150, 243) // azul
	   );
	   gbc.gridx = 1;
	   gbc.gridy = 0;
	   panelCentro.add(cardMedicos, gbc);

	   JPanel cardEnfermedades = crearCardEstadistica(
	      "Enfermedades",
	      cantEnfermedades,
	      new Color(255, 152, 0) // naranja
	   );
	   gbc.gridx = 0;
	   gbc.gridy = 1;
	   panelCentro.add(cardEnfermedades, gbc);

	   JPanel cardVacunas = crearCardEstadistica(
	      "Vacunas",
	      cantVacunas,
	      new Color(156, 39, 176) // morado
	   );
	   gbc.gridx = 1;
	   gbc.gridy = 1;
	   panelCentro.add(cardVacunas, gbc);
	}

   /**
    * Crea una tarjeta blanca con borde suave y título + texto.
    */
   private JPanel crearCardEstadistica(String titulo, int cantidad, Color colorSuperior) {
	   JPanel card = new JPanel();
	   card.setBackground(Color.WHITE);
	   card.setBorder(new EmptyBorder(16, 18, 16, 18));
	   card.setLayout(new BorderLayout(8, 10));

	   JPanel barraColor = new JPanel();
	   barraColor.setBackground(colorSuperior);
	   barraColor.setPreferredSize(new Dimension(10, 6));
	   card.add(barraColor, BorderLayout.NORTH);

	   // Título
	   JLabel lblTitulo = new JLabel(titulo);
	   lblTitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
	   lblTitulo.setForeground(new Color(60, 60, 60));
	   lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
	   card.add(lblTitulo, BorderLayout.WEST);

	   JLabel lblCantidad = new JLabel(String.valueOf(cantidad));
	   lblCantidad.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 32));
	   lblCantidad.setForeground(new Color(40, 40, 40));
	   lblCantidad.setHorizontalAlignment(SwingConstants.RIGHT);
	   card.add(lblCantidad, BorderLayout.EAST);

	   return card;
	}

   
   private String obtenerTextoUsuario() {
      if (usuarioLogueado == null) {
         return "Modo demostración";
      }

      if (usuarioLogueado instanceof Administrador) {
         return "Sesión: Administrador";
      }

      if (usuarioLogueado instanceof Secretaria) {
         Secretaria s = (Secretaria) usuarioLogueado;
         return "Sesión: Secretaria - " + s.getNombre() + " " + s.getApellido();
      }

      if (usuarioLogueado instanceof Medico) {
         Medico m = (Medico) usuarioLogueado;
         return "Sesión: Médico - " + m.getNombre() + " " + m.getApellido();
      }

      return "Sesión activa";
   }


   //   PERMISOS POR ROL

   private void configurarPermisos() {

      itemRegistrarMedico.setEnabled(false);
      itemRegistrarSecretaria.setEnabled(false);
      itemAgregarEnfermedad.setEnabled(false);
      itemAgregarVacuna.setEnabled(false);
      itemDefinirNumCitas.setEnabled(false);
      itemHacerCita.setEnabled(false);
      itemListarPacientes.setEnabled(false);
      itemGuardarArchivos.setEnabled(false);
      itemRespaldo.setEnabled(false);
      

      if (usuarioLogueado instanceof Administrador) {
         itemRegistrarMedico.setEnabled(true);
         itemRegistrarSecretaria.setEnabled(true);
         itemAgregarEnfermedad.setEnabled(true);
         itemAgregarVacuna.setEnabled(true);
         itemDefinirNumCitas.setEnabled(true);
         itemHacerCita.setEnabled(true);
         itemGuardarArchivos.setEnabled(true);
         itemListarPacientes.setEnabled(true);
         itemRespaldo.setEnabled(true);

      } else if (usuarioLogueado instanceof Secretaria) {

         itemHacerCita.setEnabled(true);
         itemListarPacientes.setEnabled(true);

      } else if (usuarioLogueado instanceof Medico) {
    	  itemListarPacientes.setEnabled(true);
      }
   }
}
