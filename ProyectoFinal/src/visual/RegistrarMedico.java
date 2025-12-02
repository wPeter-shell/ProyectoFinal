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
import logic.Medico;
import logic.Validaciones;

import java.awt.Window.Type;

public class RegistrarMedico extends JDialog {

   private final JPanel contentPanel = new JPanel();
   private JTextField txtNombre;
   private JTextField txtApellido;
   private JTextField txtCedula;
   private JTextField txtEdad;
   private JTextField txtTelefono;
   private JTextField txtDireccion;
   private JTextField txtEspecialidad;
   private JTextField txtCitasPorDia;
   private JComboBox<Character> cmbGenero;
   private Principal principal;

   public static void main(String[] args) {
      try {
         RegistrarMedico dialog = new RegistrarMedico(null);
         dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
         dialog.setVisible(true);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public RegistrarMedico(Principal principal) {
      this.principal = principal;
      setTitle("Registrar Médico");
      setModal(true);
      setSize(500, 450);
      setLocationRelativeTo(null);
      getContentPane().setLayout(new BorderLayout(0, 0));

      JPanel headerPanel = new JPanel();
      headerPanel.setBackground(new Color(0, 128, 128));
      headerPanel.setBorder(new EmptyBorder(12, 18, 12, 18));
      headerPanel.setLayout(new BorderLayout());
      getContentPane().add(headerPanel, BorderLayout.NORTH);

      JLabel lblTitulo = new JLabel("Registro de médico");
      lblTitulo.setForeground(Color.WHITE);
      lblTitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
      lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
      headerPanel.add(lblTitulo, BorderLayout.WEST);

      JLabel lblSubtitulo = new JLabel("Complete los datos para agregar un nuevo médico");
      lblSubtitulo.setForeground(new Color(225, 240, 250));
      lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
      lblSubtitulo.setHorizontalAlignment(SwingConstants.LEFT);
      headerPanel.add(lblSubtitulo, BorderLayout.SOUTH);

      contentPanel.setBackground(Color.WHITE);
      contentPanel.setBorder(new EmptyBorder(20, 40, 10, 40));
      getContentPane().add(contentPanel, BorderLayout.CENTER);
      contentPanel.setLayout(new GridLayout(9, 2, 12, 10));

      Font labelFont = new Font("Segoe UI", Font.PLAIN, 13);

      JLabel lblNombre = new JLabel("Nombre:");
      lblNombre.setFont(labelFont);
      contentPanel.add(lblNombre);
      txtNombre = new JTextField();
      contentPanel.add(txtNombre);

      JLabel lblApellido = new JLabel("Apellido:");
      lblApellido.setFont(labelFont);
      contentPanel.add(lblApellido);
      txtApellido = new JTextField();
      contentPanel.add(txtApellido);

      JLabel lblCedula = new JLabel("Cédula:");
      lblCedula.setFont(labelFont);
      contentPanel.add(lblCedula);
      txtCedula = new JTextField();
      contentPanel.add(txtCedula);

      JLabel lblGenero = new JLabel("Género:");
      lblGenero.setFont(labelFont);
      contentPanel.add(lblGenero);

      cmbGenero = new JComboBox<Character>();
      cmbGenero.addItem('M');
      cmbGenero.addItem('F');
      contentPanel.add(cmbGenero);

      JLabel lblEdad = new JLabel("Edad:");
      lblEdad.setFont(labelFont);
      contentPanel.add(lblEdad);
      txtEdad = new JTextField();
      contentPanel.add(txtEdad);

      JLabel lblTelefono = new JLabel("Teléfono:");
      lblTelefono.setFont(labelFont);
      contentPanel.add(lblTelefono);
      txtTelefono = new JTextField();
      contentPanel.add(txtTelefono);

      JLabel lblDireccion = new JLabel("Dirección:");
      lblDireccion.setFont(labelFont);
      contentPanel.add(lblDireccion);
      txtDireccion = new JTextField();
      contentPanel.add(txtDireccion);

      JLabel lblEspecialidad = new JLabel("Especialidad:");
      lblEspecialidad.setFont(labelFont);
      contentPanel.add(lblEspecialidad);
      txtEspecialidad = new JTextField();
      contentPanel.add(txtEspecialidad);

      JLabel lblCitasPorDia = new JLabel("Citas por día:");
      lblCitasPorDia.setFont(labelFont);
      contentPanel.add(lblCitasPorDia);
      txtCitasPorDia = new JTextField();
      contentPanel.add(txtCitasPorDia);

      JPanel buttonPane = new JPanel();
      buttonPane.setBorder(new EmptyBorder(10, 10, 10, 10));
      buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
      getContentPane().add(buttonPane, BorderLayout.SOUTH);

      JButton okButton = new JButton("Registrar");
      okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            registrarMedico();
         }
      });
      buttonPane.add(okButton);
      getRootPane().setDefaultButton(okButton);

      JButton cancelButton = new JButton("Cancelar");
      cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            dispose();
         }
      });
      buttonPane.add(cancelButton);
   }

   private void registrarMedico() {
      try {
         if (txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty()
               || txtCedula.getText().isEmpty() || txtEspecialidad.getText().isEmpty()
               || txtCitasPorDia.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                  "Todos los campos obligatorios deben estar llenos",
                  "Error de validación",
                  JOptionPane.ERROR_MESSAGE);
            return;
         }

         String cedula = txtCedula.getText();
         for (Medico medicoExistente : Hospital.getInstancia().getMisMedicos()) {
            if (medicoExistente.getCedula().equals(cedula)) {
               JOptionPane.showMessageDialog(this,
                     "Ya existe un médico con esta cédula",
                     "Error de registro",
                     JOptionPane.ERROR_MESSAGE);
               return;
            }
         }
         
         String edadTxt = txtEdad.getText().trim();

         if (!edadTxt.matches("\\d+")) {
             JOptionPane.showMessageDialog(this,
                 "La edad solo debe tener números.",
                 "Error de formato",
                 JOptionPane.ERROR_MESSAGE);
             return;
         }


         String nombre = txtNombre.getText();
         String apellido = txtApellido.getText();
         char genero = (Character) cmbGenero.getSelectedItem();
         int edad = Integer.parseInt(txtEdad.getText());
         String telefono = txtTelefono.getText();
         String direccion = txtDireccion.getText();
         String especialidad = txtEspecialidad.getText();
         int citasPorDia = Integer.parseInt(txtCitasPorDia.getText());

         if (edad < 18 || edad > 100) {
            JOptionPane.showMessageDialog(this,
                  "La edad debe estar entre 18 y 100 años",
                  "Error de validación",
                  JOptionPane.ERROR_MESSAGE);
            return;
         }

         if (citasPorDia <= 0 || citasPorDia > 20) {
            JOptionPane.showMessageDialog(this,
                  "Las citas por día deben estar entre 1 y 20",
                  "Error de validación",
                  JOptionPane.ERROR_MESSAGE);
            return;
         }
         
         if(Validaciones.tieneNumero(nombre)) {
        	 JOptionPane.showMessageDialog(this,
                     "En nombre no se permiten números",
                     "Error de validación",
                     JOptionPane.ERROR_MESSAGE);
               return;

         }
         
         if(Validaciones.tieneNumero(apellido)) {
        	 JOptionPane.showMessageDialog(this,
                     "En apellido no se permiten números",
                     "Error de validación",
                     JOptionPane.ERROR_MESSAGE);
               return;

         }
         
         if(Validaciones.tieneLetra(telefono)) {
        	 JOptionPane.showMessageDialog(this,
                     "El teléfono solo debe tener números",
                     "Error de validación",
                     JOptionPane.ERROR_MESSAGE);
               return;

         }
         
         if(Validaciones.tieneLetra(cedula)) {
        	 JOptionPane.showMessageDialog(this,
                     "La cédula solo debe tener números",
                     "Error de validación",
                     JOptionPane.ERROR_MESSAGE);
               return;

         }
         
        
         

         Medico nuevoMedico = new Medico(
               Hospital.getInstancia().crearUsuarioMedico(),
               Hospital.getInstancia().crearPasswordMedico(),
               nombre, apellido, cedula,
               genero, edad, telefono, direccion,
               especialidad, citasPorDia
         );

         Hospital.getInstancia().getAdministrador().registrarMedico(nuevoMedico);
         Hospital.getInstancia().guardarDatos();

         if (principal != null) {
            principal.actualizarCards();
         }

         JOptionPane.showMessageDialog(this,
               "Médico registrado exitosamente:\n" +
               "Nombre: " + nombre + " " + apellido + "\n" +
               "Especialidad: " + especialidad + "\n" +
               "Cédula: " + cedula + "\n" +
               "Usuario: " + nuevoMedico.getUsuario(),
               "Registro Exitoso",
               JOptionPane.INFORMATION_MESSAGE);

         limpiarCampos();
         dispose();

      } catch (NumberFormatException e) {
         JOptionPane.showMessageDialog(this,
               "Edad y citas por día deben ser números válidos",
               "Error de formato",
               JOptionPane.ERROR_MESSAGE);
      } catch (Exception e) {
         JOptionPane.showMessageDialog(this,
               "Error al registrar médico: " + e.getMessage(),
               "Error del sistema",
               JOptionPane.ERROR_MESSAGE);
         e.printStackTrace();
      }
   }

   private void limpiarCampos() {
      txtNombre.setText("");
      txtApellido.setText("");
      txtCedula.setText("");
      cmbGenero.setSelectedIndex(0);
      txtEdad.setText("");
      txtTelefono.setText("");
      txtDireccion.setText("");
      txtEspecialidad.setText("");
      txtCitasPorDia.setText("");
   }
}
