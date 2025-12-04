package visual;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import logic.Cita;
import logic.Hospital;

public class ListarCitas extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtPaciente;
    private JTextField txtMedico;

    private Hospital hospital;

    public ListarCitas(Hospital hospital) {
        this.hospital = hospital;

        setTitle("Listado de Citas");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ----------------------------
        // PANEL SUPERIOR (FILTROS)
        // ----------------------------
        JPanel panelTop = new JPanel();
        panelTop.setLayout(null);
        panelTop.setPreferredSize(new java.awt.Dimension(950, 120));

        JLabel lblPac = new JLabel("Buscar por Paciente:");
        lblPac.setBounds(20, 10, 200, 25);
        panelTop.add(lblPac);

        txtPaciente = new JTextField();
        txtPaciente.setBounds(20, 35, 200, 25);
        panelTop.add(txtPaciente);

        JLabel lblMed = new JLabel("Buscar por Médico:");
        lblMed.setBounds(250, 10, 200, 25);
        panelTop.add(lblMed);

        txtMedico = new JTextField();
        txtMedico.setBounds(250, 35, 200, 25);
        panelTop.add(txtMedico);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(480, 30, 100, 30);
        panelTop.add(btnBuscar);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(590, 30, 100, 30);
        panelTop.add(btnLimpiar);

        add(panelTop, BorderLayout.NORTH);

        // ----------------------------
        // TABLA
        // ----------------------------
        modelo = new DefaultTableModel(
                new Object[]{
                        "Paciente", "Cédula", "Médico", "Especialidad",
                        "Día", "Estado"
                },
                0
        );

        tabla = new JTable(modelo);
        tabla.setFont(new Font("Arial", Font.PLAIN, 14));
        tabla.setRowHeight(25);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // ----------------------------
        // EVENTOS DE BOTONES
        // ----------------------------
        btnBuscar.addActionListener(e -> cargarCitasFiltradas());

        btnLimpiar.addActionListener(e -> {
            txtPaciente.setText("");
            txtMedico.setText("");
            cargarCitas();
        });

        // CARGA INICIAL
        cargarCitas();
    }

    // --------------------------------------------------------------------
    // MÉTODOS PRINCIPALES
    // --------------------------------------------------------------------

    private void cargarCitas() {
        modelo.setRowCount(0);

        for (Cita c : hospital.getMisCitas()) {

            String nombrePaciente = c.getPaciente().getNombre() + " " + c.getPaciente().getApellido();
            String cedula = c.getPaciente().getCedula();
            String nombreMedico = c.getMedico().getNombre() + " " + c.getMedico().getApellido();
            String especialidad = (c.getMedico().getEspecialidad() != null)
                    ? c.getMedico().getEspecialidad().toString()
                    : "N/A";

            modelo.addRow(new Object[]{
                    nombrePaciente,
                    cedula,
                    nombreMedico,
                    especialidad,
                    c.getDia(),
                    c.getEstado()
            });
        }
    }

    private void cargarCitasFiltradas() {

        modelo.setRowCount(0);

        String pacFiltro = txtPaciente.getText().toLowerCase().trim();
        String medFiltro = txtMedico.getText().toLowerCase().trim();

        for (Cita c : hospital.getMisCitas()) {

            boolean coincide = true;

            String nombrePaciente = c.getPaciente().getNombre() + " " + c.getPaciente().getApellido();
            String nombreMedico = c.getMedico().getNombre() + " " + c.getMedico().getApellido();

            // FILTRO PACIENTE
            if (!pacFiltro.isEmpty() && !nombrePaciente.toLowerCase().contains(pacFiltro)) {
                coincide = false;
            }

            // FILTRO MEDICO
            if (!medFiltro.isEmpty() && !nombreMedico.toLowerCase().contains(medFiltro)) {
                coincide = false;
            }

            if (coincide) {
                String especialidad = (c.getMedico().getEspecialidad() != null)
                        ? c.getMedico().getEspecialidad().toString()
                        : "N/A";

                modelo.addRow(new Object[]{
                        nombrePaciente,
                        c.getPaciente().getCedula(),
                        nombreMedico,
                        especialidad,
                        c.getDia(),
                        c.getEstado()
                });
            }
        }
    }
}
