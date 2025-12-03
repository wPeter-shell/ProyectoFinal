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
import logic.Cita;
import logic.Validaciones;

public class ModificarMedico extends JDialog {

   private JPanel contentPane;
   private JTable tableMedicos;
   private DefaultTableModel modeloTabla;

   private JTextField txtFiltro;
   private JComboBox<String> cmbCriterio;

   private Medico m;
   private List<Medico> listaMedicos;   // lista original de médicos
   private Principal principal;         // referencia al principal (para actualizar cards si quieres)
   private JButton btnDesabilitar;  // Declarada como variable de instancia

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
      cmbCriterio.addItem("Cédula");
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
      
      // Listener para actualizar el texto del botón según el médico seleccionado
      tableMedicos.getSelectionModel().addListSelectionListener(e -> {
         if (!e.getValueIsAdjusting()) {
            int fila = tableMedicos.getSelectedRow();
            if (fila == -1) return;

            String cedula = (String) tableMedicos.getValueAt(fila, 2);

            for (Medico m : listaMedicos) {
               if (m.getCedula().equals(cedula)) {
                  if (m.getInhabilitado()) {
                     btnDesabilitar.setText("Habilitar");
                  } else {
                     btnDesabilitar.setText("Inhabilitar");
                  }
                  break;
               }
            }
         }
      });
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

      // CORREGIDO: Declarar btnDesabilitar una sola vez
      btnDesabilitar = new JButton("Inhabilitar");
      btnDesabilitar.setFont(new Font("Segoe UI", Font.PLAIN, 13));

      btnDesabilitar.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            int fila = tableMedicos.getSelectedRow();
            if (fila == -1) {
               JOptionPane.showMessageDialog(ModificarMedico.this,
                     "Debe seleccionar un médico.",
                     "Sin selección",
                     JOptionPane.WARNING_MESSAGE);
               return;
            }

            String cedulaSeleccionada = (String) tableMedicos.getValueAt(fila, 2);

            Medico medicoSeleccionado = null;
            for (Medico x : listaMedicos) {
               if (x.getCedula().equals(cedulaSeleccionada)) {
                  medicoSeleccionado = x;
                  break;
               }
            }

            if (medicoSeleccionado == null) {
               JOptionPane.showMessageDialog(ModificarMedico.this,
                     "No se pudo obtener el médico.",
                     "Error",
                     JOptionPane.ERROR_MESSAGE);
               return;
            }

            // 🔁 Si YA está INHABILITADO → ahora lo habilitamos
            if (medicoSeleccionado.getInhabilitado()) {
               medicoSeleccionado.habilitar();

               Hospital.getInstancia().guardarDatos();
               cargarMedicos();          // recarga lista + colores
               if (principal != null) {
                  principal.actualizarCards();
               }

               JOptionPane.showMessageDialog(ModificarMedico.this,
                     "El Dr. " + medicoSeleccionado.getApellido() +
                     " ha sido habilitado nuevamente.",
                     "Cambio exitoso",
                     JOptionPane.INFORMATION_MESSAGE);

               btnDesabilitar.setText("Inhabilitar");
               return;
            }

            // 🧮 Si está HABILITADO → verificar si tiene citas pendientes
            int pendientes = 0;
            if (medicoSeleccionado.getMisCitas() != null) {
               for (Cita c : medicoSeleccionado.getMisCitas()) {
                  if ("Pendiente".equalsIgnoreCase(c.getEstado())) {
                     pendientes++;
                  }
               }
            }

            if (pendientes > 0) {
               JOptionPane.showMessageDialog(ModificarMedico.this,
                     "No se puede inhabilitar al Dr. " + medicoSeleccionado.getApellido() +
                     ". Tiene " + pendientes + " cita(s) pendiente(s).",
                     "Acción no permitida",
                     JOptionPane.ERROR_MESSAGE);
               return;
            }

            // ✅ No tiene citas pendientes → se puede inhabilitar
            medicoSeleccionado.inhabilitar();

            Hospital.getInstancia().guardarDatos();
            cargarMedicos();
            if (principal != null) {
               principal.actualizarCards();
            }

            JOptionPane.showMessageDialog(ModificarMedico.this,
                  "El Dr. " + medicoSeleccionado.getApellido() +
                  " ha sido inhabilitado.",
                  "Cambio exitoso",
                  JOptionPane.INFORMATION_MESSAGE);

            btnDesabilitar.setText("Habilitar");
         }
      });

      panelBotones.add(btnDesabilitar);

      JButton btnCerrar = new JButton("Cerrar");
      btnCerrar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
      btnCerrar.addActionListener(e -> dispose());
      panelBotones.add(btnCerrar);

      contentPane.add(panelBotones, BorderLayout.SOUTH);
   }

   // ====== Cargar médicos desde Hospital ======
   private void cargarMedicos() {
      listaMedicos = new ArrayList<>(Hospital.getInstancia().getMisMedicos());
      cargarTabla(listaMedicos);
      aplicarColoresTabla();
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

      Medico medicoSeleccionado = null;
      for (Medico med : listaMedicos) {
         if (med.getCedula().equals(cedulaSeleccionada)) {
            medicoSeleccionado = med;
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

      // ================== CAMBIAR EDAD ==================
      String edadActual = String.valueOf(medicoSeleccionado.getEdad());

      String inputEdad = JOptionPane.showInputDialog(
            this,
            "Ingrese la nueva edad para:\n" +
            medicoSeleccionado.getNombre() + " " + medicoSeleccionado.getApellido() +
            "\nEdad actual: " + edadActual,
            edadActual
      );

      if (inputEdad == null) {
         return; // cancelado
      }

      inputEdad = inputEdad.trim();

      if (!inputEdad.matches("\\d+")) {
         JOptionPane.showMessageDialog(this,
               "La edad debe ser un número válido.",
               "Error de formato",
               JOptionPane.ERROR_MESSAGE);
         return;
      }

      int nuevaEdad = Integer.parseInt(inputEdad);

      if (nuevaEdad < 18 || nuevaEdad > 100) { // ajusta el rango si quieres
         JOptionPane.showMessageDialog(this,
               "La edad debe estar entre 18 y 100 años.",
               "Edad fuera de rango",
               JOptionPane.ERROR_MESSAGE);
         return;
      }

      // ================== CAMBIAR CITAS POR DÍA ==================
      String actualCitas = String.valueOf(medicoSeleccionado.getCitasPorDia());

      String inputCitas = JOptionPane.showInputDialog(
            this,
            "Ingrese el nuevo número de citas por día para:\n" +
            medicoSeleccionado.getNombre() + " " + medicoSeleccionado.getApellido() +
            "\nActual: " + actualCitas,
            actualCitas
      );

      if (inputCitas == null) {
         return; // cancelado
      }

      inputCitas = inputCitas.trim();

      if (Validaciones.tieneLetra(inputCitas)) {
         JOptionPane.showMessageDialog(this,
               "Las citas por día deben ser un número válido.",
               "Error de formato",
               JOptionPane.ERROR_MESSAGE);
         return;
      }

      int nuevoValor = Integer.parseInt(inputCitas);

      if (nuevoValor < 1 || nuevoValor > 20) { // el rango que ya usabas
         JOptionPane.showMessageDialog(this,
               "Las citas por día deben estar entre 1 y 20.",
               "Valor fuera de rango",
               JOptionPane.ERROR_MESSAGE);
         return;
      }

      // ================== APLICAR CAMBIOS ==================
      medicoSeleccionado.setEdad(nuevaEdad);
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
            "Edad y citas por día actualizadas correctamente.",
            "Actualización exitosa",
            JOptionPane.INFORMATION_MESSAGE);
   }

   private void aplicarColoresTabla() {
      tableMedicos.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
         @Override
         public java.awt.Component getTableCellRendererComponent(
               javax.swing.JTable table,
               Object value,
               boolean isSelected,
               boolean hasFocus,
               int row,
               int column) {

            java.awt.Component comp = super.getTableCellRendererComponent(
                  table, value, isSelected, hasFocus, row, column);

            String cedula = (String) table.getValueAt(row, 2);

            Medico med = null;
            for (Medico x : listaMedicos) {
               if (x.getCedula().equals(cedula)) {
                  med = x;
                  break;
               }
            }

            if (med != null && med.getInhabilitado()) {
               // Fila sombreada si está inhabilitado
               comp.setBackground(new java.awt.Color(220, 220, 220));
               comp.setForeground(java.awt.Color.DARK_GRAY);
            } else {
               comp.setBackground(java.awt.Color.WHITE);
               comp.setForeground(java.awt.Color.BLACK);
            }

            if (isSelected) {
               comp.setBackground(new java.awt.Color(153, 204, 255));
               comp.setForeground(java.awt.Color.BLACK);
            }

            return comp;
         }
      });

      tableMedicos.repaint();
   }
}