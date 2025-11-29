package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;

import logic.Administrador;
import logic.Hospital;
import logic.Secretaria;

public class RegistrarSecretaria extends JDialog {

   private final JPanel contentPanel = new JPanel();
   private JTextField txtNombre;
   private JTextField txtApellido;
   private JTextField txtCedula;
   private JTextField txtEdad;
   private JTextField txtTelefono;
   private JTextField txtDireccion;
   private JComboBox<Character> cmbGenero;

   /**
    * Launch the application (para probar solo esta ventana).
    */
   public static void main(String[] args) {
      try {
         RegistrarSecretaria dialog = new RegistrarSecretaria();
         dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
         dialog.setVisible(true);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   /**
    * Constructor: crea el dialogo y la interfaz.
    */
   public RegistrarSecretaria() {
      setTitle("Registrar Secretaria/o");
      setModal(true); // opcional, para que bloquee hasta terminar
      setSize(500, 450);
      setLocationRelativeTo(null);
      getContentPane().setLayout(new BorderLayout(0, 0));

      // =========================
      // HEADER LINDO ARRIBA
      // =========================
      JPanel headerPanel = new JPanel();
      headerPanel.setBackground(new Color(0, 128, 128));
      headerPanel.setBorder(new EmptyBorder(12, 18, 12, 18));
      headerPanel.setLayout(new BorderLayout());
      getContentPane().add(headerPanel, BorderLayout.NORTH);

      JLabel lblTitulo = new JLabel("Registro de Secretaria/o");
      lblTitulo.setForeground(Color.WHITE);
      lblTitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
      lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
      headerPanel.add(lblTitulo, BorderLayout.WEST);

      JLabel lblSubtitulo = new JLabel("Complete los datos para agregar una Secretaria/o");
      lblSubtitulo.setForeground(new Color(225, 240, 250));
      lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
      lblSubtitulo.setHorizontalAlignment(SwingConstants.LEFT);
      headerPanel.add(lblSubtitulo, BorderLayout.SOUTH);

      // =========================
      // PANEL CENTRAL CON FORM
      // =========================
      contentPanel.setBorder(new EmptyBorder(20, 30, 10, 30));
      contentPanel.setBackground(new Color(245, 247, 250));
      getContentPane().add(contentPanel, BorderLayout.CENTER);
      contentPanel.setLayout(new GridLayout(7, 2, 10, 12));

      Font labelFont = new Font("Segoe UI", Font.PLAIN, 13);

      // Nombre
      JLabel lblNombre = new JLabel("Nombre:");
      lblNombre.setFont(labelFont);
      contentPanel.add(lblNombre);
      txtNombre = new JTextField();
      contentPanel.add(txtNombre);

      // Apellido
      JLabel lblApellido = new JLabel("Apellido:");
      lblApellido.setFont(labelFont);
      contentPanel.add(lblApellido);
      txtApellido = new JTextField();
      contentPanel.add(txtApellido);

      // Cedula
      JLabel lblCedula = new JLabel("Cedula:");
      lblCedula.setFont(labelFont);
      contentPanel.add(lblCedula);
      txtCedula = new JTextField();
      contentPanel.add(txtCedula);

      // Genero
      JLabel lblGenero = new JLabel("Genero:");
      lblGenero.setFont(labelFont);
      contentPanel.add(lblGenero);
      cmbGenero = new JComboBox<Character>();
      cmbGenero.addItem('M');
      cmbGenero.addItem('F');
      contentPanel.add(cmbGenero);

      // Edad
      JLabel lblEdad = new JLabel("Edad:");
      lblEdad.setFont(labelFont);
      contentPanel.add(lblEdad);
      txtEdad = new JTextField();
      contentPanel.add(txtEdad);

      // Telefono
      JLabel lblTelefono = new JLabel("Telefono:");
      lblTelefono.setFont(labelFont);
      contentPanel.add(lblTelefono);
      txtTelefono = new JTextField();
      contentPanel.add(txtTelefono);

      // Direccion
      JLabel lblDireccion = new JLabel("Direccion:");
      lblDireccion.setFont(labelFont);
      contentPanel.add(lblDireccion);
      txtDireccion = new JTextField();
      contentPanel.add(txtDireccion);

      // =========================
      // PANEL DE BOTONES ABAJO
      // =========================
      JPanel buttonPane = new JPanel();
      buttonPane.setBorder(new EmptyBorder(10, 18, 10, 18));
      buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
      getContentPane().add(buttonPane, BorderLayout.SOUTH);

      JButton okButton = new JButton("Registrar");
      okButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
      okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            registrarSecretaria();
         }
      });
      buttonPane.add(okButton);
      getRootPane().setDefaultButton(okButton);

      JButton cancelButton = new JButton("Cancelar");
      cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
      cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            dispose();
         }
      });
      buttonPane.add(cancelButton);
   }

   /**
    * Metodo para registrar una secretaria en el sistema
    */
   private void registrarSecretaria() {
      try {
         // Validar campos obligatorios
         if (txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty() ||
             txtCedula.getText().isEmpty() || txtDireccion.getText().isEmpty() ||
             txtEdad.getText().isEmpty() || txtTelefono.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Todos los campos son obligatorios",
                    "Error de validacion",
                    JOptionPane.ERROR_MESSAGE);
            return;
         }

         // Validar que no exista YA una secretaria
         if (Hospital.getInstancia().getSecretaria() != null) {
            JOptionPane.showMessageDialog(this,
                    "Ya existe una Secretaria/o asignada en el sistema.",
                    "Error de registro",
                    JOptionPane.ERROR_MESSAGE);
            return;
         }

         // Obtener valores
         String nombre = txtNombre.getText();
         String apellido = txtApellido.getText();
         char genero = (Character) cmbGenero.getSelectedItem();
         int edad = Integer.parseInt(txtEdad.getText());
         String telefono = txtTelefono.getText();
         String direccion = txtDireccion.getText();
         String cedula = txtCedula.getText();

         // Validar edad
         if (edad < 18 || edad > 70) {
            JOptionPane.showMessageDialog(this,
                    "La edad debe estar entre 18 y 70 años",
                    "Error de validacion",
                    JOptionPane.ERROR_MESSAGE);
            return;
         }

         // Crear secretaria
         Secretaria nuevaSecretaria = new Secretaria(
               Hospital.getInstancia().crearUsuarioSecretaria(),
               Hospital.getInstancia().crearPasswordSecretaria(),
               nombre, apellido, cedula, genero, edad, telefono, direccion
         );

         // Registrar secretaria
         Hospital.getInstancia().getAdministrador().registrarSecretaria(nuevaSecretaria);
         Hospital.getInstancia().guardarDatos();

         // Mensaje final
         JOptionPane.showMessageDialog(this,
               "Secretaria/o registrada exitosamente:\n" +
               "Nombre: " + nombre + " " + apellido + "\n" +
               "Cedula: " + cedula + "\n" +
               "Usuario: " + nuevaSecretaria.getUsuario() + "\n" +
               "Password: " + nuevaSecretaria.getPassword(),
               "Registro Exitoso",
               JOptionPane.INFORMATION_MESSAGE);

         limpiarCampos();
         dispose();

      } catch (NumberFormatException e) {
         JOptionPane.showMessageDialog(this,
               "Edad debe ser un numero valido.",
               "Error de formato",
               JOptionPane.ERROR_MESSAGE);
      } catch (Exception e) {
         JOptionPane.showMessageDialog(this,
               "Error al registrar secretaria: " + e.getMessage(),
               "Error del sistema",
               JOptionPane.ERROR_MESSAGE);
         e.printStackTrace();
      }
   }

   /**
    * Metodo para limpiar todos los campos del formulario
    */
   private void limpiarCampos() {
      txtNombre.setText("");
      txtApellido.setText("");
      txtCedula.setText("");
      cmbGenero.setSelectedIndex(0);
      txtEdad.setText("");
      txtTelefono.setText("");
      txtDireccion.setText("");
   }
}
