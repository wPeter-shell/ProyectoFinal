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

import logic.Enfermedad;
import logic.Hospital;
import logic.Vacuna;

public class ListarVacunas extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtNombre;
    private JComboBox<String> cbEnfermedad;

    private Hospital hospital;

    public ListarVacunas(Hospital hospital) {
        this.hospital = hospital;

        setTitle("Listado de Vacunas");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelTop = new JPanel();
        panelTop.setLayout(null);
        panelTop.setPreferredSize(new java.awt.Dimension(900, 120));

        JLabel lblNombre = new JLabel("Buscar por Nombre:");
        lblNombre.setBounds(20, 10, 180, 25);
        panelTop.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(20, 35, 180, 25);
        panelTop.add(txtNombre);

        JLabel lblEnf = new JLabel("Filtrar por Enfermedad:");
        lblEnf.setBounds(220, 10, 180, 25);
        panelTop.add(lblEnf);

        cbEnfermedad = new JComboBox<>();
        cbEnfermedad.setBounds(220, 35, 200, 25);
        cbEnfermedad.addItem("Todas");
        cargarListaEnfermedades();
        panelTop.add(cbEnfermedad);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(450, 30, 100, 30);
        panelTop.add(btnBuscar);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(560, 30, 100, 30);
        panelTop.add(btnLimpiar);

        add(panelTop, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new Object[]{"Nombre", "Enfermedad que Previene"}, 0);
        tabla = new JTable(modelo);
        tabla.setFont(new Font("Arial", Font.PLAIN, 14));
        tabla.setRowHeight(25);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> cargarVacunasFiltradas());
        btnLimpiar.addActionListener(e -> {
            txtNombre.setText("");
            cbEnfermedad.setSelectedIndex(0);
            cargarVacunas();
        });

        cargarVacunas();
    }

    private void cargarListaEnfermedades() {

        Set<String> lista = new LinkedHashSet<>();

        for (Vacuna v : hospital.getControlVacunas()) {
            Enfermedad e = v.getEnfermedadPrevenida();
            if (e != null && e.getNombre() != null) {
                lista.add(e.getNombre());
            }
        }

        for (String nombre : lista) {
            cbEnfermedad.addItem(nombre);
        }
    }

    private void cargarVacunas() {
        modelo.setRowCount(0);

        for (Vacuna v : hospital.getControlVacunas()) {
            String nombreEnf = (v.getEnfermedadPrevenida() != null) ? v.getEnfermedadPrevenida().getNombre() : "N/A";

            modelo.addRow(new Object[]{
                    v.getNombre(),
                    nombreEnf
            });
        }
    }

    private void cargarVacunasFiltradas() {

        modelo.setRowCount(0);

        String texto = txtNombre.getText().toLowerCase().trim();
        String filtroEnf = cbEnfermedad.getSelectedItem().toString();

        for (Vacuna v : hospital.getControlVacunas()) {

            boolean coincide = true;

            if (!texto.isEmpty() && !v.getNombre().toLowerCase().contains(texto)) {
                coincide = false;
            }

            String nombreEnf = (v.getEnfermedadPrevenida() != null) ? v.getEnfermedadPrevenida().getNombre() : "";

            if (!filtroEnf.equals("Todas") && !nombreEnf.equalsIgnoreCase(filtroEnf)) {
                coincide = false;
            }

            if (coincide) {
                modelo.addRow(new Object[]{
                        v.getNombre(),
                        nombreEnf.isEmpty() ? "N/A" : nombreEnf
                });
            }
        }
    }
}
