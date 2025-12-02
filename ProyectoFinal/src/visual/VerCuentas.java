package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import logic.Hospital;
import logic.Medico;
import logic.Secretaria;

public class VerCuentas extends JDialog {

    private final JPanel contentPanel = new JPanel();

    public static void main(String[] args) {
        try {
            VerCuentas dialog = new VerCuentas();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public VerCuentas() {
        setTitle("Ver Cuentas Registradas");
        setBounds(100, 100, 600, 400);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 128, 128));
        headerPanel.setBorder(new EmptyBorder(12, 18, 12, 18));
        headerPanel.setLayout(new BorderLayout());
        
        JLabel lblTitulo = new JLabel("Cuentas Registradas en el Sistema");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
        headerPanel.add(lblTitulo, BorderLayout.WEST);
        
        JLabel lblSubtitulo = new JLabel("Listado de médicos y secretarias ordenados por registro");
        lblSubtitulo.setForeground(new Color(225, 240, 250));
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        headerPanel.add(lblSubtitulo, BorderLayout.SOUTH);
        
        getContentPane().add(headerPanel, BorderLayout.NORTH);
        
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setLayout(new BorderLayout(0, 0));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        
        String[] columnNames = {"Nombre", "Apellido", "Usuario", "Contraseña"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        cargarDatosEnTabla(model);
        
        JScrollPane scrollPane = new JScrollPane(table);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
    }
    
    private void cargarDatosEnTabla(DefaultTableModel model) {
        Hospital hospital = Hospital.getInstancia();
        
        List<Object[]> todasLasCuentas = new ArrayList<>();
        
        for (Medico medico : hospital.getMisMedicos()) {
            todasLasCuentas.add(new Object[]{
                medico.getNombre(),
                medico.getApellido(),
                medico.getUsuario(),
                medico.getPassword()
            });
        }
        
        Secretaria secretaria = hospital.getSecretaria();
        if (secretaria != null) {
            todasLasCuentas.add(new Object[]{
                secretaria.getNombre(),
                secretaria.getApellido(),
                secretaria.getUsuario(),
                secretaria.getPassword()
            });
        }
        
        todasLasCuentas.sort((a, b) -> {
            String usuarioA = (String) a[2];
            String usuarioB = (String) b[2];
            return usuarioA.compareTo(usuarioB);
        });
        
        for (Object[] cuenta : todasLasCuentas) {
            model.addRow(cuenta);
        }
    }
}