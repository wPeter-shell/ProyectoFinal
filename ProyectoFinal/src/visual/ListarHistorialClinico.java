package visual;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import logic.Consulta;
import logic.Enfermedad;
import logic.Hospital;
import logic.Medico;
import logic.Paciente;

public class ListarHistorialClinico extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtPaciente;
    private JComboBox<String> cbEnfermedad;
    private JComboBox<String> cbVigilancia;
    private JComboBox<String> cbImportante;

    private Medico medicoSesion;
    private Hospital hospital;

    public ListarHistorialClinico(Medico medicoSesion, Hospital hospital) {
        this.medicoSesion = medicoSesion;
        this.hospital = hospital;

        setTitle("Historial Clínico - Dr. " + medicoSesion.getNombre());
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        JPanel panelTop = new JPanel();
        panelTop.setLayout(null);
        panelTop.setPreferredSize(new java.awt.Dimension(1200, 140));

        JLabel lblPac = new JLabel("Paciente:");
        lblPac.setBounds(20, 10, 150, 30);
        panelTop.add(lblPac);

        txtPaciente = new JTextField();
        txtPaciente.setBounds(20, 45, 180, 25);
        panelTop.add(txtPaciente);

        JLabel lblEnf = new JLabel("Enfermedad:");
        lblEnf.setBounds(220, 10, 150, 30);
        panelTop.add(lblEnf);

        cbEnfermedad = new JComboBox<>();
        cbEnfermedad.setBounds(220, 45, 180, 25);
        cbEnfermedad.addItem("Todas");
        cargarEnfermedades();
        panelTop.add(cbEnfermedad);

        JLabel lblVig = new JLabel("Bajo vigilancia:");
        lblVig.setBounds(420, 10, 120, 30);
        panelTop.add(lblVig);

        cbVigilancia = new JComboBox<>();
        cbVigilancia.setBounds(420, 45, 150, 25);
        cbVigilancia.addItem("Todas");
        cbVigilancia.addItem("Sí");
        cbVigilancia.addItem("No");
        panelTop.add(cbVigilancia);

        JLabel lblImp = new JLabel("Importante:");
        lblImp.setBounds(600, 10, 150, 30);
        panelTop.add(lblImp);

        cbImportante = new JComboBox<>();
        cbImportante.setBounds(600, 45, 150, 25);
        cbImportante.addItem("Todas");
        cbImportante.addItem("Sí");
        cbImportante.addItem("No");
        panelTop.add(cbImportante);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(790, 40, 100, 30);
        panelTop.add(btnBuscar);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(900, 40, 100, 30);
        panelTop.add(btnLimpiar);

        add(panelTop, BorderLayout.NORTH);


        modelo = new DefaultTableModel(
                new Object[]{
                        "Paciente", "Cédula", "Enfermedad",
                        "Bajo Vigilancia", "Importante",
                        "Tratamiento", "Síntomas", "Análisis Historial"
                },
                0
        );

        tabla = new JTable(modelo);
        tabla.setFont(new Font("Arial", Font.PLAIN, 14));
        tabla.setRowHeight(25);
        add(new JScrollPane(tabla), BorderLayout.CENTER);


        btnBuscar.addActionListener(e -> cargarHistorialFiltrado());

        btnLimpiar.addActionListener(e -> {
            txtPaciente.setText("");
            cbEnfermedad.setSelectedIndex(0);
            cbVigilancia.setSelectedIndex(0);
            cbImportante.setSelectedIndex(0);
            cargarHistorial();
        });

        cargarHistorial();
    }

    private void cargarEnfermedades() {
        for (Enfermedad e : hospital.getMisEnfermedades()) {
            cbEnfermedad.addItem(e.getNombre());
        }
    }

    private void cargarHistorial() {

        modelo.setRowCount(0);

        for (Paciente p : hospital.getMisPacientes()) {

            if (p.getHistorial() == null) continue;

            for (Consulta c : p.getHistorial().getMisConsultas()) {

                if (!c.getCita().getMedico().equals(medicoSesion)) continue;

                String enfermedad = c.getDatos().getDiagnostico().getNombre();
                boolean vigilancia = c.esVigilada();
                boolean importante = c.esImportante();

                modelo.addRow(new Object[]{
                        p.getNombre() + " " + p.getApellido(),
                        p.getCedula(),
                        enfermedad,
                        vigilancia ? "Sí" : "No",
                        importante ? "Sí" : "No",
                        c.getDatos().getTratamiento(),
                        c.getDatos().getSintomas(),
                        c.getDatos().getAnalisisHistorial()
                });
            }
        }
    }

    private void cargarHistorialFiltrado() {

        modelo.setRowCount(0);

        String pacFiltro = txtPaciente.getText().toLowerCase().trim();
        String enfFiltro = cbEnfermedad.getSelectedItem().toString();
        String vigFiltro = cbVigilancia.getSelectedItem().toString();
        String impFiltro = cbImportante.getSelectedItem().toString();

        for (Paciente p : hospital.getMisPacientes()) {

            if (p.getHistorial() == null) continue;

            for (Consulta c : p.getHistorial().getMisConsultas()) {

                if (!c.getCita().getMedico().equals(medicoSesion)) continue;

                String nombrePac = p.getNombre() + " " + p.getApellido();
                String enfermedad = c.getDatos().getDiagnostico().getNombre();
                boolean vigilancia = c.esVigilada();
                boolean importante = c.esImportante();

                boolean coincide = true;

                if (!pacFiltro.isEmpty() && !nombrePac.toLowerCase().contains(pacFiltro)) {
                    coincide = false;
                }

                if (!enfFiltro.equals("Todas") && !enfermedad.equalsIgnoreCase(enfFiltro)) {
                    coincide = false;
                }

                if (vigFiltro.equals("Sí") && !vigilancia) coincide = false;
                if (vigFiltro.equals("No") && vigilancia) coincide = false;

                
                if (impFiltro.equals("Sí") && !importante) coincide = false;
                if (impFiltro.equals("No") && importante) coincide = false;

                if (coincide) {
                    modelo.addRow(new Object[]{
                            nombrePac,
                            p.getCedula(),
                            enfermedad,
                            vigilancia ? "Sí" : "No",
                            importante ? "Sí" : "No",
                            c.getDatos().getTratamiento(),
                            c.getDatos().getSintomas(),
                            c.getDatos().getAnalisisHistorial()
                    });
                }
            }
        }
    }
}
