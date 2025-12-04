package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import logic.Administrador;
import logic.Enfermedad;
import logic.Hospital;

public class AgregarEnfermedad extends JDialog {

   private final JPanel contentPanel = new JPanel();
   private JTextField txtNombre;
   private JCheckBox chkVigilancia;
   private Principal principal;

   /**
    * Launch the application.
    */
   public static void main(String[] args) {
      try {
         AgregarEnfermedad dialog = new AgregarEnfermedad(null);
         dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
         dialog.setVisible(true);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }


   public AgregarEnfermedad(Principal principal) {
      this.principal = principal;

      setTitle("Agregar Enfermedad");
      setModal(true);
      setSize(540, 249);
      setLocationRelativeTo(null);
      getContentPane().setLayout(new BorderLayout(0, 0));

      JPanel headerPanel = new JPanel();
      headerPanel.setBackground(new Color(0, 128, 128));
      headerPanel.setBorder(new EmptyBorder(12, 18, 12, 18));
      headerPanel.setLayout(new BorderLayout());
      getContentPane().add(headerPanel, BorderLayout.NORTH);

      JLabel lblTitulo = new JLabel("Registro de Enfermedad");
      lblTitulo.setForeground(Color.WHITE);
      lblTitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
      lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
      headerPanel.add(lblTitulo, BorderLayout.WEST);

      JLabel lblSubtitulo = new JLabel("Complete los datos de la enfermedad");
      lblSubtitulo.setForeground(new Color(225, 240, 250));
      lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
      lblSubtitulo.setHorizontalAlignment(SwingConstants.LEFT);
      headerPanel.add(lblSubtitulo, BorderLayout.SOUTH);

      contentPanel.setBackground(Color.WHITE);
      contentPanel.setBorder(new EmptyBorder(20, 30, 10, 30));
      getContentPane().add(contentPanel, BorderLayout.CENTER);
      contentPanel.setLayout(new GridLayout(2, 2, 12, 15));

      Font labelFont = new Font("Segoe UI", Font.PLAIN, 13);

      JLabel lblNombre = new JLabel("Nombre de la enfermedad:");
      lblNombre.setFont(labelFont);
      contentPanel.add(lblNombre);

      txtNombre = new JTextField();
      contentPanel.add(txtNombre);

      JLabel lblVigilancia = new JLabel("Bajo vigilancia:");
      lblVigilancia.setFont(labelFont);
      contentPanel.add(lblVigilancia);

      chkVigilancia = new JCheckBox("Marcar enfermedad bajo vigilancia");
      chkVigilancia.setBackground(Color.WHITE);
      chkVigilancia.setFont(new Font("Segoe UI", Font.PLAIN, 12));
      contentPanel.add(chkVigilancia);

      JPanel buttonPane = new JPanel();
      buttonPane.setBorder(new EmptyBorder(10, 10, 10, 10));
      buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
      getContentPane().add(buttonPane, BorderLayout.SOUTH);

      JButton okButton = new JButton("Agregar");
      okButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
      okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            agregarEnfermedad();
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

   private void agregarEnfermedad() {
      try {
         String nombre = txtNombre.getText().trim();

         if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                  "El nombre de la enfermedad es obligatorio.",
                  "Error de validación",
                  JOptionPane.ERROR_MESSAGE);
            return;
         }

         for (Enfermedad enfermedadExistente : Hospital.getInstancia().getMisEnfermedades()) {
            if (enfermedadExistente.getNombre().equalsIgnoreCase(nombre)) {
               JOptionPane.showMessageDialog(this,
                     "Ya existe una enfermedad con este nombre.",
                     "Error de registro",
                     JOptionPane.ERROR_MESSAGE);
               return;
            }
         }

         boolean bajoVigilancia = chkVigilancia.isSelected();

        	 Enfermedad nuevaEnfermedad = new Enfermedad(nombre, bajoVigilancia);
	         Administrador admin = Hospital.getInstancia().getAdministrador();
	         admin.agregarEnfermedad(nuevaEnfermedad);

         if (bajoVigilancia) {
            admin.marcarComoVigilada(nuevaEnfermedad);
         }

         Hospital.getInstancia().guardarDatos();

         if (principal != null) {
            principal.actualizarCards();
         }

         String mensaje = "Enfermedad agregada exitosamente:\n" +
               "Nombre: " + nombre + "\n" +
               "Bajo vigilancia: " + (bajoVigilancia ? "Sí" : "No");

         JOptionPane.showMessageDialog(this,
               mensaje,
               "Registro exitoso",
               JOptionPane.INFORMATION_MESSAGE);

         limpiarCampos();
         dispose();

      } catch (Exception e) {
         JOptionPane.showMessageDialog(this,
               "Error al agregar enfermedad: " + e.getMessage(),
               "Error del sistema",
               JOptionPane.ERROR_MESSAGE);
         e.printStackTrace();
      }
   }

   private void limpiarCampos() {
      txtNombre.setText("");
      chkVigilancia.setSelected(false);
   }
}
