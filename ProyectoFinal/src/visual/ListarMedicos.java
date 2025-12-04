package visual;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import logic.Hospital;
import logic.Medico;

public class ListarMedicos extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtNombre;
    private JTextField txtCedula;
    private JComboBox<String> cbEspecialidad;

    private Hospital hospital;

    public ListarMedicos(Hospital hospital) {
        this.hospital = hospital;

        setTitle("Listado de Médicos");
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

        JLabel lbl3 = new JLabel("Filtrar por Especialidad:");
        lbl3.setBounds(400, 10, 200, 25);
        panelTop.add(lbl3);

        cbEspecialidad = new JComboBox<>();
        cbEspecialidad.setBounds(400, 35, 200, 25);
        cbEspecialidad.addItem("Todas");
        cargarEspecialidadesDesdeMedicos();
        panelTop.add(cbEspecialidad);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(650, 25, 100, 30);
        panelTop.add(btnBuscar);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(760, 25, 100, 30);
        panelTop.add(btnLimpiar);

        add(panelTop, BorderLayout.NORTH);

        modelo = new DefaultTableModel(
                new Object[]{"Nombre", "Apellido", "Cédula", "Especialidad", "Teléfono", "Disponibilidad"}, 0);
        tabla = new JTable(modelo);
        tabla.setFont(new Font("Arial", Font.PLAIN, 14));
        tabla.setRowHeight(25);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarMedicosFiltrados();
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtNombre.setText("");
                txtCedula.setText("");
                cbEspecialidad.setSelectedIndex(0);
                cargarMedicos();
            }
        });

        cargarMedicos();
    }

    private void cargarEspecialidadesDesdeMedicos() {
        Set<String> nombres = new LinkedHashSet<>();

        for (Medico m : hospital.getMisMedicos()) {
            if (m.getEspecialidad() != null) {
                String nombreEsp = m.getEspecialidad().toString(); // toString() de Especialidad
                if (nombreEsp != null && !nombreEsp.trim().isEmpty()) {
                    nombres.add(nombreEsp.trim());
                }
            }
        }

        for (String n : nombres) {
            cbEspecialidad.addItem(n);
        }
    }

    private String obtenerNombreEspecialidad(Medico m) {
        if (m.getEspecialidad() == null) {
            return "N/A";
        }
       
        return m.getEspecialidad().toString();
    }

    private void cargarMedicos() {
        modelo.setRowCount(0);

        for (Medico m : hospital.getMisMedicos()) {
            modelo.addRow(new Object[]{
                    m.getNombre(),
                    m.getApellido(),
                    m.getCedula(),
                    obtenerNombreEspecialidad(m),
                    m.getTelefono(),
                    m.getDisponibilidad() ? "Disponible" : "No disponible"
            });
        }
    }

    private void cargarMedicosFiltrados() {
        modelo.setRowCount(0);

        String texto = txtNombre.getText().toLowerCase().trim();
        String cedula = txtCedula.getText().trim();
        String espFiltro = cbEspecialidad.getSelectedItem().toString();

        for (Medico m : hospital.getMisMedicos()) {

            boolean coincide = true;

            if (!texto.isEmpty()) {
                if (!(m.getNombre().toLowerCase().contains(texto)
                        || m.getApellido().toLowerCase().contains(texto))) {
                    coincide = false;
                }
            }

            if (!cedula.isEmpty() && !m.getCedula().equalsIgnoreCase(cedula)) {
                coincide = false;
            }

            String nombreEsp = obtenerNombreEspecialidad(m);

            if (!espFiltro.equals("Todas")
                    && !nombreEsp.equalsIgnoreCase(espFiltro)) {
                coincide = false;
            }

            if (coincide) {
                modelo.addRow(new Object[]{
                        m.getNombre(),
                        m.getApellido(),
                        m.getCedula(),
                        nombreEsp,
                        m.getTelefono(),
                        m.getDisponibilidad() ? "Disponible" : "No disponible"
                });
            }
        }
    }
}
