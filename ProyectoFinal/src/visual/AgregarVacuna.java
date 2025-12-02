package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import logic.Vacuna;
import logic.Enfermedad;
import logic.Hospital;
import logic.Administrador;


public class AgregarVacuna extends JDialog {

   private final JPanel contentPanel = new JPanel();
   private JTextField txtNombre;
   private JComboBox<Enfermedad> cmbEnfermedad;

   /**
    * Launch the application.
    */
   public static void main(String[] args) {
      try {
         AgregarVacuna dialog = new AgregarVacuna();
         dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
         dialog.setVisible(true);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   /**
    * Create the dialog.
    */
   public AgregarVacuna() {
      setTitle("Agregar Vacuna");
      setModal(true);
      setSize(480, 260);
      setLocationRelativeTo(null);
      getContentPane().setLayout(new BorderLayout(0, 0));

      // ====== HEADER LINDO ARRIBA ======
      JPanel headerPanel = new JPanel();
      headerPanel.setBackground(new Color(0, 128, 128));
      headerPanel.setBorder(new EmptyBorder(12, 18, 12, 18));
      headerPanel.setLayout(new BorderLayout());
      getContentPane().add(headerPanel, BorderLayout.NORTH);

      JLabel lblTitulo = new JLabel("Registro de Vacuna");
      lblTitulo.setForeground(Color.WHITE);
      lblTitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
      lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
      headerPanel.add(lblTitulo, BorderLayout.WEST);

      JLabel lblSubtitulo = new JLabel("Complete los datos de la vacuna");
      lblSubtitulo.setForeground(new Color(225, 240, 250));
      lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
      lblSubtitulo.setHorizontalAlignment(SwingConstants.LEFT);
      headerPanel.add(lblSubtitulo, BorderLayout.SOUTH);

      // ====== PANEL CENTRAL ======
      contentPanel.setBorder(new EmptyBorder(20, 30, 10, 30));
      getContentPane().add(contentPanel, BorderLayout.CENTER);
      contentPanel.setLayout(new GridLayout(2, 2, 12, 15));

      Font labelFont = new Font("Segoe UI", Font.PLAIN, 13);

      // Nombre vacuna
      JLabel lblNombre = new JLabel("Nombre de la vacuna:");
      lblNombre.setFont(labelFont);
      contentPanel.add(lblNombre);

      txtNombre = new JTextField();
      contentPanel.add(txtNombre);

      // Enfermedad que previene
      JLabel lblEnfermedad = new JLabel("Enfermedad que previene:");
      lblEnfermedad.setFont(labelFont);
      contentPanel.add(lblEnfermedad);

      cmbEnfermedad = new JComboBox<Enfermedad>();
      // Cargar todas las enfermedades registradas en el Hospital
      for (Enfermedad enf : Hospital.getInstancia().getMisEnfermedades()) {
         cmbEnfermedad.addItem(enf);
      }
      contentPanel.add(cmbEnfermedad);

      // ====== PANEL DE BOTONES ======
      JPanel buttonPane = new JPanel();
      buttonPane.setBorder(new EmptyBorder(10, 18, 10, 18));
      buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
      getContentPane().add(buttonPane, BorderLayout.SOUTH);

      JButton okButton = new JButton("Agregar");
      okButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
      okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            agregarVacuna();
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
    * Lógica para registrar la vacuna
    */
   private void agregarVacuna() {
      try {
         String nombre = txtNombre.getText().trim();

         // Validar nombre
         if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                  "El nombre de la vacuna es obligatorio.",
                  "Error de validación",
                  JOptionPane.ERROR_MESSAGE);
            return;
         }

         // Validar que exista al menos una enfermedad
         if (cmbEnfermedad.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this,
                  "No hay enfermedades registradas. Registre una enfermedad primero.",
                  "Error de datos",
                  JOptionPane.ERROR_MESSAGE);
            return;
         }

         Enfermedad enfermedadSeleccionada =
               (Enfermedad) cmbEnfermedad.getSelectedItem();

         if (enfermedadSeleccionada == null) {
            JOptionPane.showMessageDialog(this,
                  "Debe seleccionar una enfermedad.",
                  "Error de validación",
                  JOptionPane.ERROR_MESSAGE);
            return;
         }

         // Validar que no exista vacuna con el mismo nombre
         for (Vacuna v : Hospital.getInstancia().getControlVacunas()) {
            if (v.getNombre().equalsIgnoreCase(nombre)) {
               JOptionPane.showMessageDialog(this,
                     "Ya existe una vacuna con este nombre.",
                     "Error de registro",
                     JOptionPane.ERROR_MESSAGE);
               return;
            }
         }

         // Crear la vacuna
         Vacuna nuevaVacuna = new Vacuna(nombre, enfermedadSeleccionada);

         // Registrar en el sistema
         Administrador admin = Hospital.getInstancia().getAdministrador();
         admin.agregarVacuna(nuevaVacuna);

         Hospital.getInstancia().guardarDatos();

         JOptionPane.showMessageDialog(this,
               "Vacuna registrada correctamente.",
               "Registro exitoso",
               JOptionPane.INFORMATION_MESSAGE);

         limpiarCampos();
         dispose();

      } catch (Exception e) {
         JOptionPane.showMessageDialog(this,
               "Error al agregar vacuna: " + e.getMessage(),
               "Error del sistema",
               JOptionPane.ERROR_MESSAGE);
         e.printStackTrace();
      }
   }

   private void limpiarCampos() {
      txtNombre.setText("");
      if (cmbEnfermedad.getItemCount() > 0) {
         cmbEnfermedad.setSelectedIndex(0);
      }
   }
}
