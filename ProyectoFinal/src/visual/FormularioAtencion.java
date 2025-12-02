package visual;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import logic.*;


import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class FormularioAtencion extends JFrame {

    private JPanel contentPane;
    private JTextArea txtSintomas, txtHistorial, txtTratamiento;
    private JCheckBox chkVigilancia, chkImportante;
    private JComboBox<Enfermedad> cbDiagnostico;
    private JList<Vacuna> listaVacunas;

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

        txtSintomas = new JTextArea(3, 20);
        contentPane.add(crearPanelCampo("Síntomas:", txtSintomas));

        txtHistorial = new JTextArea(3, 20);
        contentPane.add(crearPanelCampo("Análisis del historial:", txtHistorial));

        cbDiagnostico = new JComboBox<>();
        for (Enfermedad e : Hospital.getInstancia().getMisEnfermedades()) {
            cbDiagnostico.addItem(e);
        }
        contentPane.add(crearPanelCampo("Diagnóstico:", cbDiagnostico));

        chkVigilancia = new JCheckBox("Bajo vigilancia");
        chkImportante = new JCheckBox("Importante");

        JPanel panelChecks = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelChecks.add(chkVigilancia);
        panelChecks.add(chkImportante);
        contentPane.add(panelChecks);

        txtTratamiento = new JTextArea(3, 20);
        contentPane.add(crearPanelCampo("Tratamiento:", txtTratamiento));

        listaVacunas = new JList<>(Hospital.getInstancia().getControlVacunas().toArray(new Vacuna[0]));
        listaVacunas.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        contentPane.add(crearPanelCampo("Vacunas aplicadas:", new JScrollPane(listaVacunas)));

        JButton btnGuardar = new JButton("Guardar Consulta");
        btnGuardar.setBackground(new Color(0, 128, 128));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        btnGuardar.addActionListener(e -> guardarConsulta());
        contentPane.add(btnGuardar);
    }

    private JPanel crearPanelCampo(String titulo, Component campo) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(campo, BorderLayout.CENTER);
        return panel;
    }

    private void guardarConsulta() {

        String sintomas = txtSintomas.getText().trim();
        String historial = txtHistorial.getText().trim();
        String tratamiento = txtTratamiento.getText().trim();

        if (sintomas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe escribir los síntomas.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Enfermedad diagnostico = (Enfermedad) cbDiagnostico.getSelectedItem();
        boolean vigilancia = chkVigilancia.isSelected();
        boolean importante = chkImportante.isSelected();

        ArrayList<Vacuna> aplicadas = new ArrayList<>(listaVacunas.getSelectedValuesList());

        DatosConsulta datos = new DatosConsulta(
                sintomas,
                historial,
                diagnostico,
                vigilancia,
                importante,
                tratamiento,
                aplicadas
        );

        // Crear consulta real
        new Consulta(cita, datos);

        // Marcar cita como atendida
        cita.setEstado("Atendida");

        JOptionPane.showMessageDialog(this, "Consulta registrada correctamente.", "Éxito",
                JOptionPane.INFORMATION_MESSAGE);

        ventanaPadre.refrescar();
        dispose();
    }
}
