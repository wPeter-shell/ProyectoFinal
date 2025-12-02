package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import logic.Cita;
import logic.Hospital;
import logic.Medico;
import logic.Paciente;
import logic.Secretaria;
import logic.Validaciones;

public class RegistrarCita extends JDialog {

   private final JPanel contentPanel = new JPanel();
   private JTextField txtNombre;
   private JTextField txtApellido;
   private JTextField txtCedula;
   private JTextField txtEdad;
   private JTextField txtTelefono;
   private JTextField txtDireccion;
   private JComboBox<Character> cmbGenero;
   private JComboBox<String> cmbMedicos;
   private JComboBox<String> cmbDias;

   public static void main(String[] args) {
      try {
         RegistrarCita dialog = new RegistrarCita();
         dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
         dialog.setVisible(true);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public RegistrarCita() {
      setTitle("Registrar Cita");
      setModal(true);
      setSize(500, 500);
      setLocationRelativeTo(null);
      getContentPane().setLayout(new BorderLayout(0, 0));

      JPanel headerPanel = new JPanel();
      headerPanel.setBackground(new Color(0, 128, 128));
      headerPanel.setBorder(new EmptyBorder(12, 18, 12, 18));
      headerPanel.setLayout(new BorderLayout());
      getContentPane().add(headerPanel, BorderLayout.NORTH);

      JLabel lblTitulo = new JLabel("Registro de Cita");
      lblTitulo.setForeground(Color.WHITE);
      lblTitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
      lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
      headerPanel.add(lblTitulo, BorderLayout.WEST);

      JLabel lblSubtitulo = new JLabel("Complete los datos del paciente para agendar una cita");
      lblSubtitulo.setForeground(new Color(225, 240, 250));
      lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
      lblSubtitulo.setHorizontalAlignment(SwingConstants.LEFT);
      headerPanel.add(lblSubtitulo, BorderLayout.SOUTH);

      contentPanel.setBorder(new EmptyBorder(20, 30, 10, 30));
      contentPanel.setBackground(new Color(245, 247, 250));
      getContentPane().add(contentPanel, BorderLayout.CENTER);
      contentPanel.setLayout(new GridLayout(9, 2, 10, 12));

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

      JLabel lblCedula = new JLabel("C�dula:");
      lblCedula.setFont(labelFont);
      contentPanel.add(lblCedula);
      txtCedula = new JTextField();
      contentPanel.add(txtCedula);

      JLabel lblGenero = new JLabel("G�nero:");
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

      JLabel lblTelefono = new JLabel("Tel�fono:");
      lblTelefono.setFont(labelFont);
      contentPanel.add(lblTelefono);
      txtTelefono = new JTextField();
      contentPanel.add(txtTelefono);

      JLabel lblDireccion = new JLabel("Direcci�n:");
      lblDireccion.setFont(labelFont);
      contentPanel.add(lblDireccion);
      txtDireccion = new JTextField();
      contentPanel.add(txtDireccion);

      JLabel lblMedico = new JLabel("M�dico:");
      lblMedico.setFont(labelFont);
      contentPanel.add(lblMedico);
      cmbMedicos = new JComboBox<>();
      cargarMedicos();
      contentPanel.add(cmbMedicos);

      JLabel lblDia = new JLabel("D�a:");
      lblDia.setFont(labelFont);
      contentPanel.add(lblDia);
      cmbDias = new JComboBox<String>();
      cmbDias.addItem("Lunes");
      cmbDias.addItem("Martes");
      cmbDias.addItem("Mi�rcoles");
      cmbDias.addItem("Jueves");
      cmbDias.addItem("Viernes");
      cmbDias.addItem("S�bado");
      cmbDias.addItem("Domingo");
      contentPanel.add(cmbDias);

      JPanel buttonPane = new JPanel();
      buttonPane.setBorder(new EmptyBorder(10, 18, 10, 18));
      buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
      getContentPane().add(buttonPane, BorderLayout.SOUTH);

      JButton cancelButton = new JButton("Cancelar");
      cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
      cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            dispose();
         }
      });
      
            JButton okButton = new JButton("Registrar Cita");
            okButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            okButton.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                  registrarCita();
               }
            });
            buttonPane.add(okButton);
            getRootPane().setDefaultButton(okButton);
      buttonPane.add(cancelButton);
   }

   private void cargarMedicos() {
      ArrayList<Medico> medicos = Hospital.getInstancia().getMisMedicos();
      DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
      
      for (Medico medico : medicos) {
         model.addElement("Dr. " + medico.getApellido() + " - " + medico.getEspecialidad());
      }
      
      cmbMedicos.setModel(model);
   }

   private void registrarCita() {
      try {
         if (txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty() ||
             txtCedula.getText().isEmpty() || txtDireccion.getText().isEmpty() ||
             txtEdad.getText().isEmpty() || txtTelefono.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                  "Todos los campos son obligatorios",
                  "Error de validaci�n",
                  JOptionPane.ERROR_MESSAGE);
            return;
         }

         String nombre = txtNombre.getText();
         String apellido = txtApellido.getText();
         char genero = (Character) cmbGenero.getSelectedItem();
         int edad = Integer.parseInt(txtEdad.getText());
         String telefono = txtTelefono.getText();
         String direccion = txtDireccion.getText();
         String cedula = txtCedula.getText();

         if (edad < 0 || edad > 120) {
            JOptionPane.showMessageDialog(this,
                  "La edad debe estar entre 0 y 120 a�os",
                  "Error de validación",
                  JOptionPane.ERROR_MESSAGE);
            return;
         }

         String diaSeleccionado = (String) cmbDias.getSelectedItem();
         if (diaSeleccionado == null || diaSeleccionado.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                  "Debe seleccionar un d�a",
                  "Error de validaci�n",
                  JOptionPane.ERROR_MESSAGE);
            return;
         }
         
         if(Validaciones.tieneNumero(nombre)) {
        	 JOptionPane.showMessageDialog(this,
                     "En nombre no se permiten n�meros",
                     "Error de validaci�n",
                     JOptionPane.ERROR_MESSAGE);
               return;

         }
         
         if(Validaciones.tieneNumero(apellido)) {
        	 JOptionPane.showMessageDialog(this,
                     "En apellido no se permiten n�meros",
                     "Error de validaci�n",
                     JOptionPane.ERROR_MESSAGE);
               return;

         }
         
         if(Validaciones.tieneLetra(telefono)) {
        	 JOptionPane.showMessageDialog(this,
                     "El tel�fono solo debe tener n�meros",
                     "Error de validaci�n",
                     JOptionPane.ERROR_MESSAGE);
               return;

         }
         
         if(Validaciones.tieneLetra(cedula)) {
        	 JOptionPane.showMessageDialog(this,
                     "La c�dula solo debe tener n�meros",
                     "Error de validaci�n",
                     JOptionPane.ERROR_MESSAGE);
               return;

         }
         

         int selectedIndex = cmbMedicos.getSelectedIndex();
         if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(this,
                  "Debe seleccionar un m�dico",
                  "Error de validaci�n",
                  JOptionPane.ERROR_MESSAGE);
            return;
         }

         ArrayList<Medico> medicos = Hospital.getInstancia().getMisMedicos();
         Medico medico = medicos.get(selectedIndex);

         Secretaria secretaria = Hospital.getInstancia().getSecretaria();
         
         Paciente paciente = Hospital.getInstancia().buscarPacientePorCedula(cedula);
         if (paciente == null) {
            paciente = new Paciente(nombre, apellido, cedula, genero, edad, telefono, direccion, 0, 0, "", "", "");
            Hospital.getInstancia().agregarPaciente(paciente);
         }

         if (!secretaria.verificarDisponibilidad(medico, diaSeleccionado)) {
            JOptionPane.showMessageDialog(this,
                  "El m�dico no tiene disponibilidad para el d�a seleccionado",
                  "No hay disponibilidad",
                  JOptionPane.WARNING_MESSAGE);
            return;
         }
         
         

         Cita nuevaCita = secretaria.registrarCita(paciente, medico, diaSeleccionado);
         Hospital.getInstancia().guardarDatos();

         JOptionPane.showMessageDialog(this,
               "Cita registrada exitosamente:\n" +
               "Paciente: " + nombre + " " + apellido + "\n" +
               "M�dico: Dr. " + medico.getApellido() + " (" + medico.getEspecialidad() + ")\n" +
               "D�a: " + diaSeleccionado + "\n" +
               "Estado: " + nuevaCita.getEstado(),
               "Registro Exitoso",
               JOptionPane.INFORMATION_MESSAGE);
         
         limpiarCampos();
         dispose();

      } catch (NumberFormatException e) {
         JOptionPane.showMessageDialog(this,
               "La edad debe ser un n�mero v�lido",
               "Error de formato",
               JOptionPane.ERROR_MESSAGE);
      } catch (Exception e) {
         JOptionPane.showMessageDialog(this,
               "Error al registrar cita: " + e.getMessage(),
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
      cmbMedicos.setSelectedIndex(0);
      cmbDias.setSelectedIndex(0);
   }
}