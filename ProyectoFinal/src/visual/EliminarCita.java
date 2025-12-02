package visual;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import logic.*;

public class EliminarCita extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTable tablaCitas;
    private DefaultTableModel modeloTabla;
    private JTextField txtFiltroCedula;
    private JComboBox<String> cmbFiltroEstado;
    private Principal principal;
    private Object usuarioLogueado;

    public static void main(String[] args) {
        try {
            EliminarCita dialog = new EliminarCita(null, null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EliminarCita(Principal principal, Object usuarioLogueado) {
        this.principal = principal;
        this.usuarioLogueado = usuarioLogueado;
        
        setTitle("Eliminar Cita");
        setModal(true);
        setSize(900, 550);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        // ====== HEADER ======
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 128, 128));
        headerPanel.setBorder(new EmptyBorder(12, 18, 12, 18));
        headerPanel.setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Eliminar Citas");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
        lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
        headerPanel.add(lblTitulo, BorderLayout.WEST);

        JLabel lblSubtitulo = new JLabel("Seleccione una o más citas para eliminar");
        lblSubtitulo.setForeground(new Color(225, 240, 250));
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setHorizontalAlignment(SwingConstants.LEFT);
        headerPanel.add(lblSubtitulo, BorderLayout.SOUTH);

        getContentPane().add(headerPanel, BorderLayout.NORTH);

        // ====== PANEL DE FILTRO ======
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFiltro.setBackground(new Color(245, 247, 250));
        panelFiltro.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Filtro por cédula
        JLabel lblFiltroCedula = new JLabel("Cédula paciente:");
        lblFiltroCedula.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelFiltro.add(lblFiltroCedula);

        txtFiltroCedula = new JTextField(12);
        txtFiltroCedula.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelFiltro.add(txtFiltroCedula);

        // Filtro por estado
        JLabel lblFiltroEstado = new JLabel("Estado:");
        lblFiltroEstado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelFiltro.add(lblFiltroEstado);

        cmbFiltroEstado = new JComboBox<>(new String[]{"Todos", "Pendiente", "Atendida", "Cancelada"});
        cmbFiltroEstado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelFiltro.add(cmbFiltroEstado);

        // Botones de filtro
        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnFiltrar.addActionListener(e -> filtrarCitas());
        panelFiltro.add(btnFiltrar);

        JButton btnLimpiar = new JButton("Limpiar filtros");
        btnLimpiar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnLimpiar.addActionListener(e -> {
            txtFiltroCedula.setText("");
            cmbFiltroEstado.setSelectedIndex(0);
            cargarTodasLasCitas();
        });
        panelFiltro.add(btnLimpiar);

        getContentPane().add(panelFiltro, BorderLayout.NORTH);

        // ====== TABLA DE CITAS ======
        String[] columnas = {"", "Paciente", "Cédula", "Médico", "Día", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class;
                }
                return String.class;
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // Solo la columna de checkbox es editable
            }
        };

        tablaCitas = new JTable(modeloTabla);
        tablaCitas.setRowHeight(30);
        tablaCitas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaCitas.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
        
        // Configurar ancho de columnas
        tablaCitas.getColumnModel().getColumn(0).setPreferredWidth(30); // Checkbox
        tablaCitas.getColumnModel().getColumn(1).setPreferredWidth(150); // Paciente
        tablaCitas.getColumnModel().getColumn(2).setPreferredWidth(100); // Cédula
        tablaCitas.getColumnModel().getColumn(3).setPreferredWidth(180); // Médico
        tablaCitas.getColumnModel().getColumn(4).setPreferredWidth(100); // Día
        tablaCitas.getColumnModel().getColumn(5).setPreferredWidth(100); // Estado

        JScrollPane scrollPane = new JScrollPane(tablaCitas);
        scrollPane.setBorder(new EmptyBorder(10, 20, 10, 20));
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // ====== PANEL DE INFORMACIÓN ======
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInfo.setBackground(new Color(255, 255, 200));
        panelInfo.setBorder(new EmptyBorder(5, 20, 5, 20));
        
        JLabel lblInfo = new JLabel("Seleccione las citas que desea eliminar. Las citas ya atendidas solo pueden ser eliminadas por el administrador.");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setForeground(new Color(139, 0, 0));
        panelInfo.add(lblInfo);
        
        getContentPane().add(panelInfo, BorderLayout.SOUTH);

        // ====== PANEL DE BOTONES ======
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBotones.setBorder(new EmptyBorder(10, 18, 10, 18));
        panelBotones.setBackground(Color.WHITE);

        // Botón para seleccionar/deseleccionar todos
        JButton btnSeleccionarTodos = new JButton("Seleccionar todas");
        btnSeleccionarTodos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnSeleccionarTodos.addActionListener(e -> seleccionarTodasCitas());
        panelBotones.add(btnSeleccionarTodos);

        // Botón para eliminar seleccionadas
        JButton btnEliminar = new JButton("Eliminar Seleccionadas");
        btnEliminar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setBackground(new Color(220, 53, 69)); // Rojo Bootstrap
        btnEliminar.addActionListener(e -> eliminarCitasSeleccionadas());
        panelBotones.add(btnEliminar);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnCerrar.addActionListener(e -> dispose());
        panelBotones.add(btnCerrar);

        getContentPane().add(panelBotones, BorderLayout.SOUTH);

        // Cargar citas iniciales
        cargarTodasLasCitas();
    }

    private void cargarTodasLasCitas() {
        modeloTabla.setRowCount(0);
        ArrayList<Cita> citas = Hospital.getInstancia().getMisCitas();

        for (Cita cita : citas) {
            modeloTabla.addRow(new Object[]{
                false, // Checkbox desmarcado por defecto
                cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellido(),
                cita.getPaciente().getCedula(),
                "Dr. " + cita.getMedico().getApellido() + " (" + cita.getMedico().getEspecialidad() + ")",
                cita.getDia(),
                cita.getEstado()
            });
        }
        
        aplicarColoresPorEstado();
    }

    private void aplicarColoresPorEstado() {
        tablaCitas.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {
                
                Component comp = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                
                String estado = (String) table.getValueAt(row, 5); // Columna de estado
                
                if (!isSelected) {
                    if ("Atendida".equalsIgnoreCase(estado)) {
                        comp.setBackground(new Color(220, 255, 220)); // Verde claro
                    } else if ("Cancelada".equalsIgnoreCase(estado)) {
                        comp.setBackground(new Color(255, 220, 220)); // Rojo claro
                    } else if ("Pendiente".equalsIgnoreCase(estado)) {
                        comp.setBackground(new Color(220, 230, 255)); // Azul claro
                    } else {
                        comp.setBackground(Color.WHITE);
                    }
                }
                
                return comp;
            }
        });
    }

    private void filtrarCitas() {
        String filtroCedula = txtFiltroCedula.getText().trim().toLowerCase();
        String filtroEstado = (String) cmbFiltroEstado.getSelectedItem();
        
        modeloTabla.setRowCount(0);
        ArrayList<Cita> citas = Hospital.getInstancia().getMisCitas();

        for (Cita cita : citas) {
            boolean coincideCedula = filtroCedula.isEmpty() || 
                                   cita.getPaciente().getCedula().toLowerCase().contains(filtroCedula);
            
            boolean coincideEstado = "Todos".equals(filtroEstado) || 
                                   cita.getEstado().equalsIgnoreCase(filtroEstado);
            
            if (coincideCedula && coincideEstado) {
                modeloTabla.addRow(new Object[]{
                    false,
                    cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellido(),
                    cita.getPaciente().getCedula(),
                    "Dr. " + cita.getMedico().getApellido() + " (" + cita.getMedico().getEspecialidad() + ")",
                    cita.getDia(),
                    cita.getEstado()
                });
            }
        }
        
        aplicarColoresPorEstado();
    }

    private void seleccionarTodasCitas() {
        int rowCount = modeloTabla.getRowCount();
        boolean allSelected = true;
        
        // Verificar si todas están seleccionadas
        for (int i = 0; i < rowCount; i++) {
            if (!(Boolean) modeloTabla.getValueAt(i, 0)) {
                allSelected = false;
                break;
            }
        }
        
        // Invertir selección
        boolean newValue = !allSelected;
        for (int i = 0; i < rowCount; i++) {
            modeloTabla.setValueAt(newValue, i, 0);
        }
    }

    private void eliminarCitasSeleccionadas() {
        ArrayList<Cita> citasAEliminar = new ArrayList<>();
        ArrayList<Integer> indicesSeleccionados = new ArrayList<>();
        
        // Obtener citas seleccionadas
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            if ((Boolean) modeloTabla.getValueAt(i, 0)) { // Si el checkbox está marcado
                String cedula = (String) modeloTabla.getValueAt(i, 2);
                String dia = (String) modeloTabla.getValueAt(i, 4);
                String estado = (String) modeloTabla.getValueAt(i, 5);
                
                // Buscar la cita correspondiente
                for (Cita cita : Hospital.getInstancia().getMisCitas()) {
                    if (cita.getPaciente().getCedula().equals(cedula) &&
                        cita.getDia().equals(dia) &&
                        cita.getEstado().equalsIgnoreCase(estado)) {
                        
                        // Verificar permisos para eliminar citas atendidas
                        if ("Atendida".equalsIgnoreCase(estado)) {
                            if (!(usuarioLogueado instanceof Administrador)) {
                                JOptionPane.showMessageDialog(this,
                                    "No tiene permisos para eliminar citas ya atendidas.\n" +
                                    "Solo el administrador puede eliminar citas atendidas.",
                                    "Permiso denegado",
                                    JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                        }
                        
                        citasAEliminar.add(cita);
                        indicesSeleccionados.add(i);
                        break;
                    }
                }
            }
        }
        
        if (citasAEliminar.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No ha seleccionado ninguna cita para eliminar.",
                "Sin selección",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Mostrar confirmación
        StringBuilder mensajeConfirmacion = new StringBuilder();
        mensajeConfirmacion.append("¿Está seguro de que desea eliminar las siguientes citas?\n\n");
        
        for (Cita cita : citasAEliminar) {
            mensajeConfirmacion.append("• ").append(cita.getPaciente().getNombre())
                .append(" ").append(cita.getPaciente().getApellido())
                .append(" - Dr. ").append(cita.getMedico().getApellido())
                .append(" (").append(cita.getDia()).append(") - ")
                .append(cita.getEstado()).append("\n");
        }
        
        mensajeConfirmacion.append("\nTotal: ").append(citasAEliminar.size()).append(" cita(s)");
        
        int confirmacion = JOptionPane.showConfirmDialog(this,
            mensajeConfirmacion.toString(),
            "Confirmar eliminación múltiple",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int eliminadasConExito = 0;
                
                for (Cita cita : citasAEliminar) {
                    // Eliminar de la lista del médico
                    cita.getMedico().getMisCitas().remove(cita);
                    
                    // Eliminar de la lista general
                    Hospital.getInstancia().getMisCitas().remove(cita);
                    
                    eliminadasConExito++;
                }
                
                // Guardar cambios
                Hospital.getInstancia().guardarDatos();
                
                // Actualizar tabla (eliminar filas)
                for (int i = indicesSeleccionados.size() - 1; i >= 0; i--) {
                    modeloTabla.removeRow(indicesSeleccionados.get(i));
                }
                
                // Actualizar cards en Principal si está disponible
                if (principal != null) {
                    principal.actualizarCards();
                }
                
                JOptionPane.showMessageDialog(this,
                    eliminadasConExito + " cita(s) eliminada(s) exitosamente.",
                    "Eliminación completada",
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error al eliminar citas: " + e.getMessage(),
                    "Error del sistema",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}