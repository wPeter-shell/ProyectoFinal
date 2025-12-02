package visual;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import logic.Hospital;
import logic.Medico;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ModificarMedico extends JDialog {

   private JPanel contentPane;
   private JTable tableMedicos;
   private DefaultTableModel modeloTabla;

   private JTextField txtFiltro;
   private JComboBox<String> cmbCriterio;

   private Medico m;
   private List<Medico> listaMedicos;   // lista original de médicos
   private Principal principal;         // referencia al principal (para actualizar cards si quieres)

   // Constructor principal (lo usas desde Principal)
   public ModificarMedico(Principal principal) {
      this.principal = principal;

      setTitle("Definir número de citas por día");
      setModal(true);
      setSize(750, 450);
      setLocationRelativeTo(null);

      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
      contentPane.setLayout(new BorderLayout());
      setContentPane(contentPane);

      crearHeader();
      crearCentro();
      crearBotonera();

      cargarMedicos();
   }

   // Constructor para probar esta ventana sola (opcional)
   public static void main(String[] args) {
      SwingUtilities.invokeLater(() -> {
    	  ModificarMedico dialog = new ModificarMedico(null);
         dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
         dialog.setVisible(true);
      });
   }

   // ====== HEADER TURQUESA (título bonito) ======
   private void crearHeader() {
      JPanel header = new JPanel();
      header.setBackground(new Color(0, 128, 128));
      header.setLayout(new BorderLayout());
      header.setBorder(new EmptyBorder(12, 18, 12, 18));

      JLabel lblTitulo = new JLabel("Definir número de citas por día");
      lblTitulo.setForeground(Color.WHITE);
      lblTitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
      lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
      header.add(lblTitulo, BorderLayout.NORTH);

      JLabel lblSub = new JLabel("Seleccione un médico y modifique su cantidad máxima de citas diarias");
      lblSub.setForeground(new Color(225, 240, 250));
      lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
      lblSub.setHorizontalAlignment(SwingConstants.LEFT);
      header.add(lblSub, BorderLayout.SOUTH);

      contentPane.add(header, BorderLayout.NORTH);
   }

   // ====== PANEL CENTRAL: filtro + tabla ======
   private void crearCentro() {
      JPanel panelCentro = new JPanel(new BorderLayout());
      panelCentro.setBorder(new EmptyBorder(15, 20, 15, 20));
      panelCentro.setBackground(new Color(245, 247, 250));

      // -------- PANEL FILTRO ARRIBA --------
      JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
      panelFiltro.setOpaque(false);

      JLabel lblBuscar = new JLabel("Buscar por:");
      lblBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
      panelFiltro.add(lblBuscar);

      cmbCriterio = new JComboBox<String>();
      cmbCriterio.addItem("Nombre");
      cmbCriterio.addItem("Apellido");
      cmbCriterio.addItem("Especialidad");
      cmbCriterio.setFont(new Font("Segoe UI", Font.PLAIN, 13));
      panelFiltro.add(cmbCriterio);

      txtFiltro = new JTextField();
      txtFiltro.setColumns(20);
      panelFiltro.add(txtFiltro);

      JButton btnFiltrar = new JButton("Filtrar");
      btnFiltrar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
      btnFiltrar.addActionListener(e -> aplicarFiltro());
      panelFiltro.add(btnFiltrar);

      JButton btnLimpiar = new JButton("Limpiar");
      btnLimpiar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
      btnLimpiar.addActionListener(e -> {
         txtFiltro.setText("");
         cargarTabla(listaMedicos); // recarga todos
      });
      panelFiltro.add(btnLimpiar);

      // Que filtre mientras escribes
      txtFiltro.addKeyListener(new KeyAdapter() {
         @Override
         public void keyReleased(KeyEvent e) {
            aplicarFiltro();
         }
      });

      panelCentro.add(panelFiltro, BorderLayout.NORTH);

      // -------- TABLA DE MÉDICOS --------
      String[] columnas = { "Nombre", "Apellido", "Cédula", "Especialidad", "Citas por día" };
      modeloTabla = new DefaultTableModel(columnas, 0) {
         @Override
         public boolean isCellEditable(int row, int column) {
            return false; // no editable desde la tabla
         }
      };

      tableMedicos = new JTable(modeloTabla);
      tableMedicos.setRowHeight(22);
      tableMedicos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      tableMedicos.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
      tableMedicos.setFont(new Font("Segoe UI", Font.PLAIN, 13));

      JScrollPane scroll = new JScrollPane(tableMedicos);
      panelCentro.add(scroll, BorderLayout.CENTER);

      contentPane.add(panelCentro, BorderLayout.CENTER);
   }

   // ====== BOTONES ABAJO ======
   private void crearBotonera() {
      JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
      panelBotones.setBorder(new EmptyBorder(10, 18, 10, 18));
      panelBotones.setBackground(Color.WHITE);

      JButton btnModificar = new JButton("Modificar");
      btnModificar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
      btnModificar.addActionListener(e -> modificarCitas());
      panelBotones.add(btnModificar);

      JButton btnCerrar = new JButton("Cerrar");
      btnCerrar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
      btnCerrar.addActionListener(e -> dispose());
      
      JButton btnDesabilitar = new JButton("Inhabilitar");
      btnDesabilitar.addActionListener(e -> inhabilitarMedico());
   
      btnDesabilitar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
      panelBotones.add(btnDesabilitar);
      panelBotones.add(btnCerrar);

      contentPane.add(panelBotones, BorderLayout.SOUTH);
   }

   // ====== Cargar médicos desde Hospital ======
   private void cargarMedicos() {
      listaMedicos = new ArrayList<>(Hospital.getInstancia().getMisMedicos());
      cargarTabla(listaMedicos);
   }

   private void cargarTabla(List<Medico> datos) {
      modeloTabla.setRowCount(0); // limpia

      for (Medico m : datos) {
         Object[] fila = new Object[] {
               m.getNombre(),
               m.getApellido(),
               m.getCedula(),
               m.getEspecialidad(),
               m.getCitasPorDia()
         };
         modeloTabla.addRow(fila);
      }
   }

   // ====== Filtro por nombre / cédula / especialidad ======
   private void aplicarFiltro() {
      if (listaMedicos == null) return;

      String texto = txtFiltro.getText().trim().toLowerCase();
      if (texto.isEmpty()) {
         cargarTabla(listaMedicos);
         return;
      }

      String criterio = (String) cmbCriterio.getSelectedItem();
      List<Medico> filtrados = new ArrayList<>();

      for (Medico m : listaMedicos) {
         String valor = "";

         if ("Nombre".equals(criterio)) {
            valor = (m.getNombre() + " " + m.getApellido()).toLowerCase();
         } else if ("Cédula".equals(criterio)) {
            valor = m.getCedula() != null ? m.getCedula().toLowerCase() : "";
         } else if ("Especialidad".equals(criterio)) {
            valor = m.getEspecialidad() != null ? m.getEspecialidad().toLowerCase() : "";
         }

         if (valor.contains(texto)) {
            filtrados.add(m);
         }
      }

      cargarTabla(filtrados);
   }

   // ====== Acción del botón MODIFICAR ======
   private void modificarCitas() {
      int fila = tableMedicos.getSelectedRow();
      if (fila == -1) {
         JOptionPane.showMessageDialog(this,
               "Debe seleccionar un médico de la tabla.",
               "Sin selección",
               JOptionPane.WARNING_MESSAGE);
         return;
      }

      // Identificar el médico según su cédula
      String cedulaSeleccionada = (String) tableMedicos.getValueAt(fila, 2);
      Medico medicoSeleccionado = m;
      for (Medico m : listaMedicos) {
         if (m.getCedula().equals(cedulaSeleccionada)) {
            medicoSeleccionado = m;
            break;
         }
      }

      if (medicoSeleccionado == null) {
         JOptionPane.showMessageDialog(this,
               "No se pudo identificar el médico seleccionado.",
               "Error",
               JOptionPane.ERROR_MESSAGE);
         return;
      }

      String actual = String.valueOf(medicoSeleccionado.getCitasPorDia());

      // Pedir nuevo valor
      String input = JOptionPane.showInputDialog(
            this,
            "Ingrese el nuevo número de citas por día para:\n" +
            medicoSeleccionado.getNombre() + " " + medicoSeleccionado.getApellido() +
            "\nActual: " + actual,
            actual
      );

      if (input == null) {
         return; // cancelado
      }

      input = input.trim();

      // Validar que sea numérico
      if (!input.matches("\\d+")) {
         JOptionPane.showMessageDialog(this,
               "Debe ingresar un número válido.",
               "Error de formato",
               JOptionPane.ERROR_MESSAGE);
         return;
      }

      int nuevoValor = Integer.parseInt(input);

      // Validar rango (ajusta si quieres otro)
      if (nuevoValor <= 0 || nuevoValor > 20) {
         JOptionPane.showMessageDialog(this,
               "Las citas por día deben estar entre 1 y 20.",
               "Valor fuera de rango",
               JOptionPane.ERROR_MESSAGE);
         return;
      }

      // Actualizar el médico
      medicoSeleccionado.setCitasPorDia(nuevoValor);

      // Guardar
      Hospital.getInstancia().guardarDatos();

      // Refrescar tabla
      cargarMedicos();

      // Refrescar cards del dashboard (si quieres)
      if (principal != null) {
         principal.actualizarCards();
      }

      JOptionPane.showMessageDialog(this,
            "Citas por día actualizadas correctamente.",
            "Actualización exitosa",
            JOptionPane.INFORMATION_MESSAGE);
   }
   
   private void inhabilitarMedico() {
	   
	   int fila = tableMedicos.getSelectedRow();
	      if (fila == -1) {
	         JOptionPane.showMessageDialog(this,
	               "Debe seleccionar un médico de la tabla.",
	               "Sin selección",
	               JOptionPane.WARNING_MESSAGE);
	         return;
	      }
	   
	   
	   if(m.saberCantCitas() == 0) {
 			m.setDisponible(true);
 			JOptionPane.showMessageDialog(
 	               ModificarMedico.this,
 	               "El Dr. " + m.getApellido() + " ha sido inhabilitado.",
 	               "Cambio Exitoso",
 	               JOptionPane.INFORMATION_MESSAGE
 	         );

             return;
 		}
 		else {
 			JOptionPane.showMessageDialog(
 				   ModificarMedico.this,
 	               "El Dr. "+m.getApellido()+"tiene citas pendientes",
 	               "Error de formato",
 	               JOptionPane.ERROR_MESSAGE);
 		}
   }
   
   
}