package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Normalizer;

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

public class AgregarEspecialidad extends JDialog {

   private final JPanel contentPanel = new JPanel();
   private JTextField txtNombre;
   private Principal principal;

   private String normalizarNombreEspecialidad(String texto) {
      if (texto == null) {
         return "";
      }

      String resultado = texto.trim().toLowerCase();
      resultado = Normalizer.normalize(resultado, Normalizer.Form.NFD);
      resultado = resultado.replaceAll("\\p{M}+", "");

      return resultado;
   }

   /**
    * Launch the application.
    */
   public static void main(String[] args) {
      try {
         AgregarEspecialidad dialog = new AgregarEspecialidad();
         dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
         dialog.setVisible(true);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void setPrincipal(Principal principal) {
      this.principal = principal;
   }

   /**
    * Create the dialog.
    */
   public AgregarEspecialidad() {
      //this.principal = principal;

      setTitle("Agregar Especialidad");
      setModal(true);
      setSize(540, 249);
      setLocationRelativeTo(null);
      getContentPane().setLayout(new BorderLayout(0, 0));

      // ====== HEADER LINDO ARRIBA ======
      JPanel headerPanel = new JPanel();
      headerPanel.setBackground(new Color(0, 128, 128));
      headerPanel.setBorder(new EmptyBorder(12, 18, 12, 18));
      headerPanel.setLayout(new BorderLayout());
      getContentPane().add(headerPanel, BorderLayout.NORTH);

      JLabel lblTitulo = new JLabel("Registro de Especialidad M\u00E9dica");
      lblTitulo.setForeground(Color.WHITE);
      lblTitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
      lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
      headerPanel.add(lblTitulo, BorderLayout.WEST);

      // ====== PANEL CENTRAL ======
      contentPanel.setBackground(Color.WHITE);
      contentPanel.setBorder(new EmptyBorder(20, 30, 10, 30));
      getContentPane().add(contentPanel, BorderLayout.CENTER);
      contentPanel.setLayout(new GridLayout(2, 2, 12, 15));

      Font labelFont = new Font("Segoe UI", Font.PLAIN, 13);

      // Nombre
      JLabel lblNombre = new JLabel("Nombre de la especialidad:");
      lblNombre.setFont(labelFont);
      contentPanel.add(lblNombre);

      txtNombre = new JTextField();
      contentPanel.add(txtNombre);

      // Espacio de relleno
      JLabel lblDummy1 = new JLabel("");
      contentPanel.add(lblDummy1);

      JLabel lblDummy2 = new JLabel("");
      contentPanel.add(lblDummy2);

      // ====== PANEL DE BOTONES ======
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

   /**
    * Método para agregar una especialidad al sistema
    */
   private void agregarEnfermedad() {
      try {
         String nombre = txtNombre.getText().trim();

         // Validar campo obligatorio
         if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                  "El nombre de la especialidad es obligatorio.",
                  "Error de validación",
                  JOptionPane.ERROR_MESSAGE);
            return;
         }

         // Normalizar el nombre para comparar sin acentos ni mayúsculas
         String nombreNormalizado = normalizarNombreEspecialidad(nombre);

         // Validar que no exista una especialidad con el mismo nombre (ignorando acentos)
         for (String esp : Hospital.getInstancia().getEspecialidades()) {
            String espNormalizada = normalizarNombreEspecialidad(esp);

            if (espNormalizada.equals(nombreNormalizado)) {
               JOptionPane.showMessageDialog(this,
                     "Ya existe una especialidad con este nombre.",
                     "Error de registro",
                     JOptionPane.ERROR_MESSAGE);
               return;
            }
         }

         Administrador admin = Hospital.getInstancia().getAdministrador();
         admin.registrarEspecialidad(nombre);

         // Guardar cambios en archivo
         Hospital.getInstancia().guardarDatos();

         // Actualizar cards en Principal
         if (principal != null) {
            principal.actualizarCards();
         }

         String mensaje = "Especialidad agregada exitosamente:\n" +
               "Nombre: " + nombre + "\n";

         JOptionPane.showMessageDialog(this,
               mensaje,
               "Registro exitoso",
               JOptionPane.INFORMATION_MESSAGE);

         limpiarCampos();
         dispose();

      } catch (Exception e) {
         JOptionPane.showMessageDialog(this,
               "Error al agregar especialidad: " + e.getMessage(),
               "Error del sistema",
               JOptionPane.ERROR_MESSAGE);
         e.printStackTrace();
      }
   }

   /**
    * Método para limpiar todos los campos del formulario
    */
   private void limpiarCampos() {
      txtNombre.setText("");
   }
}
