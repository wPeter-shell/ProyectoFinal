package visual;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import logic.Cita;
import logic.Enfermedad;
import logic.Hospital;
import logic.Paciente;

public class ListarPaciente extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtNombre;
    private JTextField txtCedula;
    private JComboBox<String> cbEnfermedad;

    private Hospital hospital;

    public ListarPaciente(Hospital hospital) {
        this.hospital = hospital;

        setTitle("Listado de Pacientes con Citas");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelTop = new JPanel();
        panelTop.setLayout(null);
        panelTop.setPreferredSize(new java.awt.Dimension(900, 120));

        JLabel lbl1 = new JLabel("Buscar por Nombre/Apellido:");
        lbl1.setBounds(20, 10, 200, 25);
        panelTop.add(lbl1);

        txtNombre = new JTextField();
        txtNombre.setBounds(20, 35, 180, 25);
        panelTop.add(txtNombre);

        JLabel lbl2 = new JLabel("Buscar por Cédula:");
        lbl2.setBounds(220, 10, 150, 25);
        panelTop.add(lbl2);

        txtCedula = new JTextField();
        txtCedula.setBounds(220, 35, 150, 25);
        panelTop.add(txtCedula);

        JLabel lbl3 = new JLabel("Filtrar por Enfermedad:");
        lbl3.setBounds(400, 10, 200, 25);
        panelTop.add(lbl3);

        cbEnfermedad = new JComboBox<>();
        cbEnfermedad.setBounds(400, 35, 200, 25);
        cbEnfermedad.addItem("Todas");
        cargarEnfermedades();
        panelTop.add(cbEnfermedad);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(650, 25, 100, 30);
        panelTop.add(btnBuscar);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(760, 25, 100, 30);
        panelTop.add(btnLimpiar);

        add(panelTop, BorderLayout.NORTH);

        modelo = new DefaultTableModel(
                new Object[]{"Nombre", "Apellido", "Cédula", "Edad", "Teléfono", "Enfermedad Última Consulta"}, 0);
        tabla = new JTable(modelo);
        tabla.setFont(new Font("Arial", Font.PLAIN, 14));
        tabla.setRowHeight(25);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarPacientesFiltrados();
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtNombre.setText("");
                txtCedula.setText("");
                cbEnfermedad.setSelectedIndex(0);
                cargarPacientes();
            }
        });

        cargarPacientes();
    }


    private void cargarEnfermedades() {
        for (Enfermedad enf : hospital.getMisEnfermedades()) {
            cbEnfermedad.addItem(enf.getNombre());
        }
    }

    /**
     * SOLO pacientes que tengan al menos UNA cita registrada
     */
    private ArrayList<Paciente> obtenerPacientesConCita() {
        ArrayList<Paciente> lista = new ArrayList<>();

        for (Cita c : hospital.getMisCitas()) {
            Paciente p = c.getPaciente();
            if (!lista.contains(p)) {
                lista.add(p);
            }
        }
        return lista;
    }

    /**
     * Carga completa sin filtros
     */
    private void cargarPacientes() {
        modelo.setRowCount(0);
        ArrayList<Paciente> lista = obtenerPacientesConCita();

        for (Paciente p : lista) {
            String ultimaEnf = "N/A";

            if (p.getHistorial() != null && p.getHistorial().getUltimaConsulta() != null) {
            	ultimaEnf = p.getHistorial().getUltimaConsulta().getDatos().getDiagnostico().getNombre();


            }

            modelo.addRow(new Object[]{
                    p.getNombre(),
                    p.getApellido(),
                    p.getCedula(),
                    p.getEdad(),
                    p.getTelefono(),
                    ultimaEnf
            });
        }
    }

    /**
     * Carga con filtros
     */
    private void cargarPacientesFiltrados() {
        modelo.setRowCount(0);

        String texto = txtNombre.getText().toLowerCase().trim();
        String cedula = txtCedula.getText().trim();
        String enfFiltro = cbEnfermedad.getSelectedItem().toString();

        for (Paciente p : obtenerPacientesConCita()) {

            boolean coincide = true;

            if (!texto.isEmpty()) {
                if (!(p.getNombre().toLowerCase().contains(texto)
                        || p.getApellido().toLowerCase().contains(texto))) {
                    coincide = false;
                }
            }

            if (!cedula.isEmpty() && !p.getCedula().equalsIgnoreCase(cedula)) {
                coincide = false;
            }

            String ultimaEnf = "";
            if (p.getHistorial() != null && p.getHistorial().getUltimaConsulta() != null) {
            	ultimaEnf = p.getHistorial().getUltimaConsulta().getDatos().getDiagnostico().getNombre();

            }

            if (!enfFiltro.equals("Todas") && !ultimaEnf.equalsIgnoreCase(enfFiltro)) {
                coincide = false;
            }

            if (coincide) {
                modelo.addRow(new Object[]{
                        p.getNombre(),
                        p.getApellido(),
                        p.getCedula(),
                        p.getEdad(),
                        p.getTelefono(),
                        ultimaEnf.isEmpty() ? "N/A" : ultimaEnf
                });
            }
        }
    }
}
