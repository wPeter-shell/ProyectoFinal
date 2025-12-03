package visual;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import logic.*;

public class ModificarCita extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTable tablaCitas;
    private DefaultTableModel modeloTabla;
    private JTextField txtFiltroCedula;
    private Secretaria secretaria;
    private Object usuarioLogueado;

    public static void main(String[] args) {
        try {
            ModificarCita dialog = new ModificarCita(null, null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ModificarCita(Principal principal, Object usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
        
        // Verificar que el usuario sea secretaria
        if (!(usuarioLogueado instanceof Secretaria)) {
            JOptionPane.showMessageDialog(null,
                "Solo las secretarias pueden modificar citas.",
                "Acceso denegado",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        setTitle("Modificar Cita");
        setModal(true);
        setSize(900, 550);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        secretaria = Hospital.getInstancia().getSecretaria();

        // ====== HEADER ======
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 128, 128));
        headerPanel.setBorder(new EmptyBorder(12, 18, 12, 18));
        headerPanel.setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Modificar Citas");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
        lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
        headerPanel.add(lblTitulo, BorderLayout.WEST);

        JLabel lblSubtitulo = new JLabel("Seleccione una cita para modificar sus datos");
        lblSubtitulo.setForeground(new Color(225, 240, 250));
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setHorizontalAlignment(SwingConstants.LEFT);
        headerPanel.add(lblSubtitulo, BorderLayout.SOUTH);

        getContentPane().add(headerPanel, BorderLayout.NORTH);

        // ====== PANEL DE FILTRO ======
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFiltro.setBackground(new Color(245, 247, 250));
        panelFiltro.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel lblFiltro = new JLabel("Filtrar por cédula paciente:");
        lblFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelFiltro.add(lblFiltro);

        txtFiltroCedula = new JTextField(15);
        txtFiltroCedula.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelFiltro.add(txtFiltroCedula);

        JButton btnFiltrar = new JButton("Buscar");
        btnFiltrar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnFiltrar.addActionListener(e -> filtrarCitas());
        panelFiltro.add(btnFiltrar);

        JButton btnLimpiar = new JButton("Mostrar todas");
        btnLimpiar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnLimpiar.addActionListener(e -> {
            txtFiltroCedula.setText("");
            cargarCitasPendientes();
        });
        panelFiltro.add(btnLimpiar);

        getContentPane().add(panelFiltro, BorderLayout.NORTH);

        // ====== TABLA DE CITAS ======
        String[] columnas = {"Paciente", "Cédula", "Médico", "Día", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCitas = new JTable(modeloTabla);
        tablaCitas.setRowHeight(30);
        tablaCitas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaCitas.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
        
        // Configurar ancho de columnas
        tablaCitas.getColumnModel().getColumn(0).setPreferredWidth(150); // Paciente
        tablaCitas.getColumnModel().getColumn(1).setPreferredWidth(100); // Cédula
        tablaCitas.getColumnModel().getColumn(2).setPreferredWidth(180); // Médico
        tablaCitas.getColumnModel().getColumn(3).setPreferredWidth(100); // Día
        tablaCitas.getColumnModel().getColumn(4).setPreferredWidth(100); // Estado

        // Aplicar colores según estado
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
                
                String estado = (String) table.getValueAt(row, 4); // Columna de estado
                
                if (!isSelected) {
                    if ("Pendiente".equalsIgnoreCase(estado)) {
                        comp.setBackground(new Color(220, 230, 255)); // Azul claro para pendientes
                    } else {
                        comp.setBackground(Color.WHITE);
                    }
                }
                
                return comp;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaCitas);
        scrollPane.setBorder(new EmptyBorder(10, 20, 10, 20));
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // ====== PANEL DE INFORMACIÓN ======
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInfo.setBackground(new Color(220, 230, 255));
        panelInfo.setBorder(new EmptyBorder(5, 20, 5, 20));
        
        JLabel lblInfo = new JLabel("Solo se muestran citas con estado 'Pendiente'. Seleccione una cita para modificarla.");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setForeground(new Color(0, 0, 139));
        panelInfo.add(lblInfo);
        
        getContentPane().add(panelInfo, BorderLayout.SOUTH);

        // ====== PANEL DE BOTONES ======
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBotones.setBorder(new EmptyBorder(10, 18, 10, 18));
        panelBotones.setBackground(Color.WHITE);

        JButton btnModificar = new JButton("Modificar Cita Seleccionada");
        btnModificar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnModificar.setBackground(new Color(0, 128, 128));
        btnModificar.setForeground(Color.WHITE);
        btnModificar.addActionListener(e -> modificarCitaSeleccionada());
        panelBotones.add(btnModificar);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnCerrar.addActionListener(e -> dispose());
        panelBotones.add(btnCerrar);

        getContentPane().add(panelBotones, BorderLayout.SOUTH);

        // Cargar citas iniciales
        cargarCitasPendientes();
    }

    private void cargarCitasPendientes() {
        modeloTabla.setRowCount(0);
        ArrayList<Cita> citas = Hospital.getInstancia().getMisCitas();

        for (Cita cita : citas) {
            // Solo mostrar citas pendientes
            if ("Pendiente".equalsIgnoreCase(cita.getEstado())) {
                modeloTabla.addRow(new Object[]{
                    cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellido(),
                    cita.getPaciente().getCedula(),
                    "Dr. " + cita.getMedico().getApellido() + " (" + cita.getMedico().getEspecialidad() + ")",
                    cita.getDia(),
                    cita.getEstado()
                });
            }
        }
    }

    private void filtrarCitas() {
        String filtro = txtFiltroCedula.getText().trim().toLowerCase();
        if (filtro.isEmpty()) {
            cargarCitasPendientes();
            return;
        }

        modeloTabla.setRowCount(0);
        ArrayList<Cita> citas = Hospital.getInstancia().getMisCitas();

        for (Cita cita : citas) {
            if ("Pendiente".equalsIgnoreCase(cita.getEstado()) && 
                cita.getPaciente().getCedula().toLowerCase().contains(filtro)) {
                modeloTabla.addRow(new Object[]{
                    cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellido(),
                    cita.getPaciente().getCedula(),
                    "Dr. " + cita.getMedico().getApellido() + " (" + cita.getMedico().getEspecialidad() + ")",
                    cita.getDia(),
                    cita.getEstado()
                });
            }
        }
    }

    private Cita obtenerCitaSeleccionada() {
        int filaSeleccionada = tablaCitas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                "Por favor, seleccione una cita de la tabla.",
                "Selección requerida",
                JOptionPane.WARNING_MESSAGE);
            return null;
        }

        String cedulaPaciente = (String) tablaCitas.getValueAt(filaSeleccionada, 1);
        String diaCita = (String) tablaCitas.getValueAt(filaSeleccionada, 3);

        ArrayList<Cita> citas = Hospital.getInstancia().getMisCitas();
        for (Cita cita : citas) {
            if (cita.getPaciente().getCedula().equals(cedulaPaciente) &&
                cita.getDia().equals(diaCita) &&
                "Pendiente".equalsIgnoreCase(cita.getEstado())) {
                return cita;
            }
        }

        return null;
    }

    private void modificarCitaSeleccionada() {
        Cita citaSeleccionada = obtenerCitaSeleccionada();
        if (citaSeleccionada == null) return;

        // Crear un diálogo para modificar la cita
        JDialog dialogModificar = new JDialog(this, "Modificar Cita", true);
        dialogModificar.setSize(500, 350);
        dialogModificar.setLocationRelativeTo(this);
        dialogModificar.setLayout(new BorderLayout());

        // ====== PANEL INFORMACIÓN ======
        JPanel panelInfoCita = new JPanel(new GridLayout(4, 1, 5, 5));
        panelInfoCita.setBorder(new EmptyBorder(15, 20, 15, 20));
        panelInfoCita.setBackground(Color.WHITE);

        JLabel lblPaciente = new JLabel("Paciente: " + 
            citaSeleccionada.getPaciente().getNombre() + " " + 
            citaSeleccionada.getPaciente().getApellido());
        lblPaciente.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelInfoCita.add(lblPaciente);

        JLabel lblCedula = new JLabel("Cédula: " + citaSeleccionada.getPaciente().getCedula());
        lblCedula.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelInfoCita.add(lblCedula);

        JLabel lblMedicoActual = new JLabel("Médico actual: Dr. " + 
            citaSeleccionada.getMedico().getApellido() + " (" + 
            citaSeleccionada.getMedico().getEspecialidad() + ")");
        lblMedicoActual.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelInfoCita.add(lblMedicoActual);

        JLabel lblDiaActual = new JLabel("Día actual: " + citaSeleccionada.getDia());
        lblDiaActual.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelInfoCita.add(lblDiaActual);

        dialogModificar.add(panelInfoCita, BorderLayout.NORTH);

        // ====== PANEL DE MODIFICACIÓN ======
        JPanel panelModificacion = new JPanel(new GridLayout(3, 2, 15, 15));
        panelModificacion.setBorder(new EmptyBorder(20, 30, 20, 30));
        panelModificacion.setBackground(Color.WHITE);

        // Médico
        JLabel lblNuevoMedico = new JLabel("Nuevo médico:");
        lblNuevoMedico.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelModificacion.add(lblNuevoMedico);

        JComboBox<Medico> cmbMedicos = new JComboBox<>();
        for (Medico medico : Hospital.getInstancia().getMisMedicos()) {
            if (!medico.getDisponibilidad()) {
                cmbMedicos.addItem(medico);
            }
        }
        cmbMedicos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbMedicos.setSelectedItem(citaSeleccionada.getMedico());
        panelModificacion.add(cmbMedicos);

        // Día
        JLabel lblNuevoDia = new JLabel("Nuevo día:");
        lblNuevoDia.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelModificacion.add(lblNuevoDia);

        JComboBox<String> cmbDias = new JComboBox<>(new String[]{
            "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"
        });
        cmbDias.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbDias.setSelectedItem(citaSeleccionada.getDia());
        panelModificacion.add(cmbDias);

        // Estado (solo si es necesario)
        JLabel lblNuevoEstado = new JLabel("Estado:");
        lblNuevoEstado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelModificacion.add(lblNuevoEstado);

        JComboBox<String> cmbEstado = new JComboBox<>(new String[]{
            "Pendiente", "Confirmada", "Cancelada"
        });
        cmbEstado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbEstado.setSelectedItem(citaSeleccionada.getEstado());
        panelModificacion.add(cmbEstado);

        dialogModificar.add(panelModificacion, BorderLayout.CENTER);

        // ====== PANEL DE BOTONES ======
        JPanel panelBotonesDialog = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotonesDialog.setBackground(Color.WHITE);
        panelBotonesDialog.setBorder(new EmptyBorder(10, 10, 20, 10));

        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnGuardar.setBackground(new Color(0, 128, 128));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(e -> {
            Medico nuevoMedico = (Medico) cmbMedicos.getSelectedItem();
            String nuevoDia = (String) cmbDias.getSelectedItem();
            String nuevoEstado = (String) cmbEstado.getSelectedItem();

            if (nuevoMedico == null) {
                JOptionPane.showMessageDialog(dialogModificar,
                    "Debe seleccionar un médico.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Si el médico o el día cambian, verificar disponibilidad
            if (!nuevoMedico.equals(citaSeleccionada.getMedico()) || 
                !nuevoDia.equals(citaSeleccionada.getDia())) {
                
                if (!secretaria.verificarDisponibilidad(nuevoMedico, nuevoDia)) {
                    JOptionPane.showMessageDialog(dialogModificar,
                        "El médico no tiene disponibilidad para el día seleccionado.",
                        "Sin disponibilidad",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            try {
                // Si el médico cambió, eliminar de la lista del médico anterior
                if (!nuevoMedico.equals(citaSeleccionada.getMedico())) {
                    citaSeleccionada.getMedico().getMisCitas().remove(citaSeleccionada);
                }

                // Actualizar la cita
                citaSeleccionada.setMedico(nuevoMedico);
                citaSeleccionada.setDia(nuevoDia);
                citaSeleccionada.setEstado(nuevoEstado);

                // Añadir a la lista del nuevo médico
                nuevoMedico.getMisCitas().add(citaSeleccionada);

                // Guardar cambios
                Hospital.getInstancia().guardarDatos();

                JOptionPane.showMessageDialog(dialogModificar,
                    "Cita modificada exitosamente.\n\n" +
                    "Nuevos datos:\n" +
                    "• Paciente: " + citaSeleccionada.getPaciente().getNombre() + " " + 
                    citaSeleccionada.getPaciente().getApellido() + "\n" +
                    "• Médico: Dr. " + nuevoMedico.getApellido() + "\n" +
                    "• Día: " + nuevoDia + "\n" +
                    "• Estado: " + nuevoEstado,
                    "Modificación exitosa",
                    JOptionPane.INFORMATION_MESSAGE);

                dialogModificar.dispose();
                cargarCitasPendientes(); // Actualizar tabla

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialogModificar,
                    "Error al modificar cita: " + ex.getMessage(),
                    "Error del sistema",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnCancelar.addActionListener(e -> dialogModificar.dispose());

        panelBotonesDialog.add(btnGuardar);
        panelBotonesDialog.add(btnCancelar);
        dialogModificar.add(panelBotonesDialog, BorderLayout.SOUTH);

        dialogModificar.setVisible(true);
    }
}