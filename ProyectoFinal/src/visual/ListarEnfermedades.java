package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import logic.Enfermedad;
import logic.Hospital;

public class ListarEnfermedades extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtNombre;
    private JComboBox<String> cbVigilancia;
    private Hospital hospital;

    public ListarEnfermedades(Hospital hospital) {
        this.hospital = hospital;

        setTitle("Listado de Enfermedades");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelTop = new JPanel();
        panelTop.setLayout(null);
        panelTop.setPreferredSize(new java.awt.Dimension(800, 120));

        JLabel lbl1 = new JLabel("Buscar por nombre:");
        lbl1.setBounds(20, 10, 200, 25);
        panelTop.add(lbl1);

        txtNombre = new JTextField();
        txtNombre.setBounds(20, 35, 180, 25);
        panelTop.add(txtNombre);

        JLabel lbl2 = new JLabel("Bajo vigilancia:");
        lbl2.setBounds(220, 10, 200, 25);
        panelTop.add(lbl2);

        cbVigilancia = new JComboBox<>();
        cbVigilancia.addItem("Todas");
        cbVigilancia.addItem("Sí");
        cbVigilancia.addItem("No");
        cbVigilancia.setBounds(220, 35, 120, 25);
        panelTop.add(cbVigilancia);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(380, 30, 100, 30);
        panelTop.add(btnBuscar);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(490, 30, 100, 30);
        panelTop.add(btnLimpiar);

        add(panelTop, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new Object[]{"Nombre", "Bajo Vigilancia"}, 0);
        tabla = new JTable(modelo);
        tabla.setFont(new Font("Arial", Font.PLAIN, 14));
        tabla.setRowHeight(25);

        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int col) {

                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

                String estado = table.getValueAt(row, 1).toString();

                if (estado.equals("Sí")) {
                    c.setBackground(new Color(255, 230, 230));  // light red
                } else {
                    c.setBackground(Color.WHITE);
                }

                return c;
            }
        });

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarEnfermedadesFiltradas();
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtNombre.setText("");
                cbVigilancia.setSelectedIndex(0);
                cargarEnfermedades();
            }
        });

        cargarEnfermedades();
    }

    private void cargarEnfermedades() {
        modelo.setRowCount(0);

        for (Enfermedad e : hospital.getMisEnfermedades()) {
            modelo.addRow(new Object[]{
                    e.getNombre(),
                    e.isBajoVigilancia() ? "Sí" : "No"
            });
        }
    }

    private void cargarEnfermedadesFiltradas() {

        modelo.setRowCount(0);

        String texto = txtNombre.getText().toLowerCase().trim();
        String filtroV = cbVigilancia.getSelectedItem().toString();

        for (Enfermedad e : hospital.getMisEnfermedades()) {

            boolean coincide = true;

            if (!texto.isEmpty()) {
                if (!e.getNombre().toLowerCase().contains(texto)) {
                    coincide = false;
                }
            }

            if (filtroV.equals("Sí") && !e.isBajoVigilancia()) coincide = false;
            if (filtroV.equals("No") && e.isBajoVigilancia()) coincide = false;

            if (coincide) {
                modelo.addRow(new Object[]{
                        e.getNombre(),
                        e.isBajoVigilancia() ? "Sí" : "No"
                });
            }
        }
    }
}
