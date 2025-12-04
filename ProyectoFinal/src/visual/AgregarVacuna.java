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
   private Principal principal;

   /**
    * Launch the application.
    */
   public static void main(String[] args) {
      try {
         AgregarVacuna dialog = new AgregarVacuna(null);
         dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
         dialog.setVisible(true);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   /**
    * Create the dialog.
    */
   public AgregarVacuna(Principal principal) {
      this.principal = principal;
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

      // ====== PANEL CENTRAL =====
      contentPanel.setBackground(Color.WHITE);
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

      cmbEnfermedad = new JComboBox<>();
      contentPanel.add(cmbEnfermedad);

      // Cargar enfermedades al combo
      cargarEnfermedades();

      // ====== PANEL DE BOTONES ======
      JPanel buttonPane = new JPanel();
      buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
      buttonPane.setBorder(new EmptyBorder(10, 10, 10, 10));
      getContentPane().add(buttonPane, BorderLayout.SOUTH);

      JButton okButton = new JButton("Registrar");
      okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            agregarVacuna();
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

   private void cargarEnfermedades() {
      cmbEnfermedad.removeAllItems();
      for (Enfermedad enf : Hospital.getInstancia().getMisEnfermedades()) {
         cmbEnfermedad.addItem(enf);
      }
   }

   private void agregarVacuna() {
      try {
         String nombre = txtNombre.getText().trim();

         if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                  "El nombre de la vacuna es obligatorio.",
                  "Error de validación",
                  JOptionPane.ERROR_MESSAGE);
            return;
         }

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

         // Validar vacuna repetida
         for (Vacuna v : Hospital.getInstancia().getControlVacunas()) {
            if (v.getNombre().equalsIgnoreCase(nombre)
                  && v.getEnfermedadPrevenida().equals(enfermedadSeleccionada)) {
               JOptionPane.showMessageDialog(this,
                     "Ya existe una vacuna con ese nombre para esa enfermedad.",
                     "Registro duplicado",
                     JOptionPane.ERROR_MESSAGE);
               return;
            }
         }

         Vacuna nuevaVacuna = new Vacuna(nombre, enfermedadSeleccionada);

         Administrador admin = Hospital.getInstancia().getAdministrador();
         admin.agregarVacuna(nuevaVacuna);

         Hospital.getInstancia().guardarDatos();

         if (principal != null) {
            principal.actualizarCards();
         }

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