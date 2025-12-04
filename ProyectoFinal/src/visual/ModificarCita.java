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
    private Principal principal;
    private Object usuarioLogueado;

    public ModificarCita(Principal principal, Object usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
        this.principal = principal;
        
        // Verificar que el usuario sea secretaria
        if (!(usuarioLogueado instanceof Secretaria)) {
            JOptionPane.showMessageDialog(null,
                "Solo las secretarias pueden modificar citas.",
                "Acceso denegado",
                JOptionPane.WARNING_MESSAGE);
            dispose();
            return;
        }
        
        setTitle("Modificar Cita");
        setModal(true);
        setSize(900, 550);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

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
            cargarCitasModificables();
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
                    } else if ("Confirmada".equalsIgnoreCase(estado)) {
                        comp.setBackground(new Color(255, 255, 200)); // Amarillo claro para confirmadas
                    } else {
                        comp.setBackground(Color.WHITE);
                    }
                }
                
                return comp;
            }
        });

        // Doble clic para modificar
        tablaCitas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    modificarCitaSeleccionada();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaCitas);
        scrollPane.setBorder(new EmptyBorder(10, 20, 10, 20));
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // ====== PANEL DE INFORMACIÓN ======
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInfo.setBackground(new Color(220, 230, 255));
        panelInfo.setBorder(new EmptyBorder(5, 20, 5, 20));
        
        JLabel lblInfo = new JLabel("<html>Se muestran citas <b>Pendientes</b> y <b>Confirmadas</b>. " +
                                  "Haga doble clic sobre una cita o use el botón para modificarla.</html>");
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
        cargarCitasModificables();
    }

    private void cargarCitasModificables() {
        modeloTabla.setRowCount(0);
        
        // Obtener todas las citas del sistema
        ArrayList<Cita> todasLasCitas = obtenerTodasLasCitas();

        for (Cita cita : todasLasCitas) {
            // Mostrar citas que se pueden modificar (Pendientes o Confirmadas)
            String estado = cita.getEstado();
            if ("Pendiente".equalsIgnoreCase(estado) || 
                "Confirmada".equalsIgnoreCase(estado)) {
                modeloTabla.addRow(new Object[]{
                    cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellido(),
                    cita.getPaciente().getCedula(),
                    "Dr. " + cita.getMedico().getNombre() + " " + cita.getMedico().getApellido() + 
                    " (" + cita.getMedico().getEspecialidad() + ")",
                    cita.getDia(),
                    cita.getEstado()
                });
            }
        }
        
        if (modeloTabla.getRowCount() == 0) {
            modeloTabla.addRow(new Object[]{
                "No hay citas modificables", "", "", "", ""
            });
        }
    }

    private ArrayList<Cita> obtenerTodasLasCitas() {
        ArrayList<Cita> todasLasCitas = new ArrayList<>();
        Hospital hospital = Hospital.getInstancia();
        
        // Obtener citas de todos los médicos
        for (Medico medico : hospital.getMisMedicos()) {
            todasLasCitas.addAll(medico.getMisCitas());
        }
        
        return todasLasCitas;
    }

    private void filtrarCitas() {
        String filtro = txtFiltroCedula.getText().trim();
        if (filtro.isEmpty()) {
            cargarCitasModificables();
            return;
        }

        modeloTabla.setRowCount(0);
        ArrayList<Cita> todasLasCitas = obtenerTodasLasCitas();

        for (Cita cita : todasLasCitas) {
            String estado = cita.getEstado();
            String cedula = cita.getPaciente().getCedula();
            
            if (("Pendiente".equalsIgnoreCase(estado) || 
                 "Confirmada".equalsIgnoreCase(estado)) && 
                cedula.contains(filtro)) {
                modeloTabla.addRow(new Object[]{
                    cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellido(),
                    cedula,
                    "Dr. " + cita.getMedico().getNombre() + " " + cita.getMedico().getApellido() + 
                    " (" + cita.getMedico().getEspecialidad() + ")",
                    cita.getDia(),
                    cita.getEstado()
                });
            }
        }
        
        if (modeloTabla.getRowCount() == 0) {
            modeloTabla.addRow(new Object[]{
                "No se encontraron citas", "", "", "", ""
            });
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

        // Verificar si la fila es el mensaje "No hay citas"
        Object valor = tablaCitas.getValueAt(filaSeleccionada, 0);
        if (valor.toString().contains("No hay citas") || 
            valor.toString().contains("No se encontraron")) {
            return null;
        }

        String cedulaPaciente = (String) tablaCitas.getValueAt(filaSeleccionada, 1);
        String diaCita = (String) tablaCitas.getValueAt(filaSeleccionada, 3);
        String estadoCita = (String) tablaCitas.getValueAt(filaSeleccionada, 4);

        // Buscar la cita específica
        return buscarCitaEspecifica(cedulaPaciente, diaCita, estadoCita);
    }

    private Cita buscarCitaEspecifica(String cedulaPaciente, String diaCita, String estadoCita) {
        ArrayList<Cita> todasLasCitas = obtenerTodasLasCitas();
        
        for (Cita cita : todasLasCitas) {
            if (cita.getPaciente().getCedula().equals(cedulaPaciente) &&
                cita.getDia().equals(diaCita) &&
                cita.getEstado().equalsIgnoreCase(estadoCita)) {
                return cita;
            }
        }
        return null;
    }

    private void modificarCitaSeleccionada() {
        Cita citaSeleccionada = obtenerCitaSeleccionada();
        if (citaSeleccionada == null) return;

        JDialog dialogModificar = new JDialog(this, "Modificar Cita - " + 
            citaSeleccionada.getPaciente().getNombre() + " " + 
            citaSeleccionada.getPaciente().getApellido(), true);
        dialogModificar.setSize(500, 450);
        dialogModificar.setLocationRelativeTo(this);
        dialogModificar.setLayout(new BorderLayout());

        JPanel panelInfoCita = new JPanel(new GridLayout(5, 1, 5, 5));
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
            citaSeleccionada.getMedico().getNombre() + " " + 
            citaSeleccionada.getMedico().getApellido() + " (" + 
            citaSeleccionada.getMedico().getEspecialidad() + ")");
        lblMedicoActual.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelInfoCita.add(lblMedicoActual);

        JLabel lblDiaActual = new JLabel("Día actual: " + citaSeleccionada.getDia());
        lblDiaActual.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelInfoCita.add(lblDiaActual);

        JLabel lblEstadoActual = new JLabel("Estado actual: " + citaSeleccionada.getEstado());
        lblEstadoActual.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblEstadoActual.setForeground(
            "Pendiente".equals(citaSeleccionada.getEstado()) ? Color.BLUE : 
            "Confirmada".equals(citaSeleccionada.getEstado()) ? new Color(255, 140, 0) : Color.BLACK);
        panelInfoCita.add(lblEstadoActual);

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
        // Configurar renderer personalizado para mostrar nombre del médico
        cmbMedicos.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                if (value instanceof Medico) {
                    Medico medico = (Medico) value;
                    setText(medico.getNombre() + " " + medico.getApellido() + 
                           " - " + medico.getEspecialidad());
                } else if (value == null) {
                    setText("Seleccione un médico");
                }
                return this;
            }
        });

        cmbMedicos.addItem(citaSeleccionada.getMedico()); // Agregar médico actual primero
        for (Medico medico : Hospital.getInstancia().getMisMedicos()) {
            if (medico.getDisponibilidad() && !medico.equals(citaSeleccionada.getMedico())) {
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
            "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"
        });
        cmbDias.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbDias.setSelectedItem(citaSeleccionada.getDia());
        panelModificacion.add(cmbDias);

        // Estado
        JLabel lblNuevoEstado = new JLabel("Nuevo estado:");
        lblNuevoEstado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelModificacion.add(lblNuevoEstado);

        JComboBox<String> cmbEstado = new JComboBox<>(new String[]{
            "Pendiente", "Confirmada", "Cancelada"
        });
        cmbEstado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbEstado.setSelectedItem(citaSeleccionada.getEstado());
        panelModificacion.add(cmbEstado);

        dialogModificar.add(panelModificacion, BorderLayout.CENTER);

        // ====== PANEL DE DISPONIBILIDAD ======
        JPanel panelDisponibilidad = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelDisponibilidad.setBorder(new EmptyBorder(5, 30, 5, 30));
        panelDisponibilidad.setBackground(new Color(240, 248, 255));
        
        JLabel lblDisponibilidad = new JLabel();
        lblDisponibilidad.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        actualizarEtiquetaDisponibilidad(lblDisponibilidad, 
            (Medico) cmbMedicos.getSelectedItem(), 
            (String) cmbDias.getSelectedItem());
        
        // Listeners para actualizar disponibilidad cuando cambien los combos
        cmbMedicos.addActionListener(e -> actualizarEtiquetaDisponibilidad(
            lblDisponibilidad, 
            (Medico) cmbMedicos.getSelectedItem(), 
            (String) cmbDias.getSelectedItem()));
        
        cmbDias.addActionListener(e -> actualizarEtiquetaDisponibilidad(
            lblDisponibilidad, 
            (Medico) cmbMedicos.getSelectedItem(), 
            (String) cmbDias.getSelectedItem()));
        
        panelDisponibilidad.add(lblDisponibilidad);
        dialogModificar.add(panelDisponibilidad, BorderLayout.SOUTH);

        // ====== PANEL DE BOTONES ======
        JPanel panelBotonesDialog = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panelBotonesDialog.setBackground(Color.WHITE);
        panelBotonesDialog.setBorder(new EmptyBorder(10, 10, 20, 10));

        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnGuardar.setBackground(new Color(0, 128, 128));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(e -> guardarCambiosCita(
            dialogModificar, citaSeleccionada, cmbMedicos, cmbDias, cmbEstado));

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnCancelar.addActionListener(ev -> dialogModificar.dispose());

        panelBotonesDialog.add(btnGuardar);
        panelBotonesDialog.add(btnCancelar);
        dialogModificar.add(panelBotonesDialog, BorderLayout.SOUTH);

        dialogModificar.setVisible(true);
    }

    private void actualizarEtiquetaDisponibilidad(JLabel label, Medico medico, String dia) {
        if (medico == null || dia == null) {
            label.setText("");
            return;
        }
        
        int disponibles = medico.cantCitasDisp(dia);
        if (disponibles > 0) {
            label.setText("Disponibilidad: " + disponibles + " citas disponibles para " + dia);
            label.setForeground(new Color(0, 100, 0));
        } else {
            label.setText("Sin disponibilidad para " + dia);
            label.setForeground(Color.RED);
        }
    }

    private void guardarCambiosCita(JDialog dialog, Cita cita, JComboBox<Medico> cmbMedicos, 
                                   JComboBox<String> cmbDias, JComboBox<String> cmbEstado) {
        Medico nuevoMedico = (Medico) cmbMedicos.getSelectedItem();
        String nuevoDia = (String) cmbDias.getSelectedItem();
        String nuevoEstado = (String) cmbEstado.getSelectedItem();

        // Validar cambios
        if (nuevoMedico == null || nuevoDia == null || nuevoEstado == null) {
            JOptionPane.showMessageDialog(dialog,
                "Todos los campos son obligatorios.",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar si el médico tiene disponibilidad (excepto si es el mismo médico y mismo día)
        boolean mismoMedicoYMismoDia = nuevoMedico.equals(cita.getMedico()) && 
                                      nuevoDia.equals(cita.getDia());
        
        if (!mismoMedicoYMismoDia) {
            int disponibles = nuevoMedico.cantCitasDisp(nuevoDia);
            if (disponibles <= 0) {
                JOptionPane.showMessageDialog(dialog,
                    "El médico no tiene disponibilidad para el día seleccionado.\n" +
                    "Citas disponibles: " + disponibles,
                    "Sin disponibilidad",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        try {
            // Si el médico cambió, eliminar de la lista del médico anterior
            if (!nuevoMedico.equals(cita.getMedico())) {
                cita.getMedico().getMisCitas().remove(cita);
            }

            // Actualizar la cita
            cita.setMedico(nuevoMedico);
            cita.setDia(nuevoDia);
            cita.setEstado(nuevoEstado);

            // Añadir a la lista del nuevo médico si es diferente
            if (!nuevoMedico.getMisCitas().contains(cita)) {
                nuevoMedico.getMisCitas().add(cita);
            }

            // Guardar cambios
            Hospital.getInstancia().guardarDatos();

            JOptionPane.showMessageDialog(dialog,
                "Cita modificada exitosamente.\n\n" +
                " Resumen de cambios:\n" +
                "• Paciente: " + cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellido() + "\n" +
                "• Médico: Dr. " + nuevoMedico.getNombre() + " " + nuevoMedico.getApellido() + "\n" +
                "• Día: " + nuevoDia + "\n" +
                "• Estado: " + nuevoEstado,
                "Modificación exitosa",
                JOptionPane.INFORMATION_MESSAGE);

            dialog.dispose();
            cargarCitasModificables(); // Actualizar tabla
            
            // Actualizar Principal si está disponible
            if (principal != null) {
                principal.actualizarCards();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog,
                "Error al modificar cita: " + ex.getMessage(),
                "Error del sistema",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}