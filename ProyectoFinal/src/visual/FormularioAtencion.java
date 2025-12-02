package visual;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import logic.*;

import java.awt.*;
import java.util.ArrayList;

public class FormularioAtencion extends JFrame {

    private JPanel contentPane;
    private JTextArea txtSintomas, txtHistorial, txtTratamiento;
    private JCheckBox chkVigilancia, chkImportante;
    private JTextField txtDiagnostico;

    private JComboBox<Object> cbVacunas;
    private ArrayList<Vacuna> vacunasSeleccionadas = new ArrayList<Vacuna>();

    private Cita cita;
    private AtenderConsultas ventanaPadre;

    public FormularioAtencion(Cita cita, AtenderConsultas ventanaPadre) {
        this.cita = cita;
        this.ventanaPadre = ventanaPadre;

        setTitle("Atender Consulta - " + cita.getPaciente().getNombre());
        setSize(650, 650);
        setResizable(false);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPane.setLayout(new GridLayout(9, 1, 10, 10));
        setContentPane(contentPane);

        JLabel lblTitulo = new JLabel("Datos de la Consulta", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
        contentPane.add(lblTitulo);

        txtSintomas = crearTextArea();
        contentPane.add(crearPanelCampo("Síntomas:", txtSintomas));

        txtHistorial = crearTextArea();
        contentPane.add(crearPanelCampo("Análisis del historial:", txtHistorial));

        txtDiagnostico = crearTextField();
        contentPane.add(crearPanelCampo("Diagnóstico:", txtDiagnostico));

        chkVigilancia = new JCheckBox("Bajo vigilancia");
        chkImportante = new JCheckBox("Importante");

        JPanel panelChecks = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelChecks.add(chkVigilancia);
        panelChecks.add(chkImportante);
        contentPane.add(panelChecks);

        txtTratamiento = crearTextArea();
        contentPane.add(crearPanelCampo("Tratamiento:", txtTratamiento));

        // ================== MULTI-SELECCIÓN DE VACUNAS ==================
        cbVacunas = new JComboBox<Object>();
        cbVacunas.setRenderer(new CheckBoxComboRenderer());
        cbVacunas.setEditable(false);
        cbVacunas.setBorder(new LineBorder(Color.GRAY, 1));

        // Placeholder SIN checkbox
        cbVacunas.addItem("Seleccione vacunas...");

        // Items seleccionables (con checkbox)
        for (Vacuna v : Hospital.getInstancia().getControlVacunas()) {
            cbVacunas.addItem(new CheckableItem(v.getNombre()));
        }

        cbVacunas.addActionListener(e -> {
            Object selected = cbVacunas.getSelectedItem();

            // IGNORAR EL PRIMER ELEMENTO (placeholder)
            if (!(selected instanceof CheckableItem)) {
                return;
            }

            CheckableItem item = (CheckableItem) selected;
            item.setSelected(!item.isSelected());

            actualizarVacunasSeleccionadas();
            cbVacunas.repaint();
        });

        contentPane.add(crearPanelCampo("Vacunas aplicadas:", cbVacunas));
        // =================================================================

        JButton btnGuardar = new JButton("Guardar Consulta");
        btnGuardar.setBackground(new Color(0, 128, 128));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        btnGuardar.addActionListener(e -> guardarConsulta());
        contentPane.add(btnGuardar);
    }

    // =================== MÉTODOS DE UI ===================

    private JTextField crearTextField() {
        JTextField t = new JTextField();
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setBorder(new LineBorder(Color.GRAY, 1));
        return t;
    }

    private JTextArea crearTextArea() {
        JTextArea t = new JTextArea(3, 20);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setBorder(new LineBorder(Color.GRAY, 1));
        return t;
    }

    private JPanel crearPanelCampo(String titulo, Component campo) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(campo, BorderLayout.CENTER);
        return panel;
    }

    // =================== MULTI SELECCIÓN ===================

    private void actualizarVacunasSeleccionadas() {
        vacunasSeleccionadas.clear();

        for (int i = 1; i < cbVacunas.getItemCount(); i++) { // empezamos en 1 para saltar placeholder
            Object element = cbVacunas.getItemAt(i);

            if (element instanceof CheckableItem) {
                CheckableItem item = (CheckableItem) element;

                if (item.isSelected()) {
                    // Buscar vacuna real por nombre
                    for (Vacuna v : Hospital.getInstancia().getControlVacunas()) {
                        if (v.getNombre().equals(item.getLabel())) {
                            vacunasSeleccionadas.add(v);
                        }
                    }
                }
            }
        }

        actualizarTextoCombo();
    }

    private void actualizarTextoCombo() {
        if (vacunasSeleccionadas.isEmpty()) {
            cbVacunas.setSelectedItem("Seleccione vacunas...");
            return;
        }

        String texto = "";
        for (Vacuna v : vacunasSeleccionadas) {
            texto += v.getNombre() + ", ";
        }
        texto = texto.substring(0, texto.length() - 2);

        cbVacunas.setSelectedItem(texto);
    }

    // =================== GUARDAR CONSULTA ===================

    private void guardarConsulta() {

        String sintomas = txtSintomas.getText().trim();
        String historial = txtHistorial.getText().trim();
        String diagnostico = txtDiagnostico.getText().trim();
        String tratamiento = txtTratamiento.getText().trim();

        if (sintomas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe escribir los síntomas.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Enfermedad enf = new Enfermedad(diagnostico, chkVigilancia.isSelected());

        DatosConsulta datos = new DatosConsulta(
                sintomas,
                historial,
                enf,
                chkVigilancia.isSelected(),
                chkImportante.isSelected(),
                tratamiento,
                vacunasSeleccionadas
        );

        new Consulta(cita, datos);
        cita.setEstado("Atendida");

        JOptionPane.showMessageDialog(this, "Consulta registrada correctamente.", "Éxito",
                JOptionPane.INFORMATION_MESSAGE);

        ventanaPadre.refrescar();
        dispose();
    }
}

// =================== CLASES PARA MULTI-SELECCIÓN ===================

class CheckableItem {
    private String label;
    private boolean selected;

    public CheckableItem(String label) {
        this.label = label;
        this.selected = false;
    }

    public String getLabel() {
        return label;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean val) {
        selected = val;
    }

    @Override
    public String toString() {
        return label;
    }
}

class CheckBoxComboRenderer implements ListCellRenderer<Object> {

    @Override
    public Component getListCellRendererComponent(
            JList<?> list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        if (value instanceof CheckableItem) {
            CheckableItem item = (CheckableItem) value;
            JCheckBox chk = new JCheckBox(item.getLabel(), item.isSelected());
            chk.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            chk.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            return chk;

        } else { // PLACEHOLDER SIN CHECKBOX
            JLabel lbl = new JLabel(value.toString());
            lbl.setOpaque(true);
            lbl.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            lbl.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            return lbl;
        }
    }
}
