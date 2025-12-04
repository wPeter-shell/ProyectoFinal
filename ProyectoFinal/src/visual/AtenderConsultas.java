package visual;

import javax.swing.*;

import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import logic.*;


import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AtenderConsultas extends JFrame {

    private JPanel contentPane;
    private JTable tablaCitas;
    private DefaultTableModel modeloTabla;
    private Medico medicoLogueado;

    public AtenderConsultas(Medico medico) {
        this.medicoLogueado = medico;

        setTitle("Atender Consultas");
        setSize(900, 550);
        setResizable(false);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        JLabel lblTitulo = new JLabel("Citas Pendientes", SwingConstants.LEFT);
        lblTitulo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 22));
        lblTitulo.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.add(lblTitulo, BorderLayout.NORTH);

        String[] columnas = {"Paciente", "Cédula", "Fecha", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaCitas = new JTable(modeloTabla);
        tablaCitas.setRowHeight(28);
        tablaCitas.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        cargarCitasPendientes();

        JScrollPane scroll = new JScrollPane(tablaCitas);
        contentPane.add(scroll, BorderLayout.CENTER);

        JButton btnAtender = new JButton("Atender Consulta");
        btnAtender.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnAtender.setBackground(new Color(0, 128, 128));
        btnAtender.setForeground(Color.WHITE);

        btnAtender.addActionListener(e -> abrirFormularioConsulta());
        contentPane.add(btnAtender, BorderLayout.SOUTH);
    }

    private void cargarCitasPendientes() {
        modeloTabla.setRowCount(0);

        ArrayList<Cita> citas = Hospital.getInstancia().getMisCitas();
        for (Cita c : citas) {
            if (c.getMedico().equals(medicoLogueado) && c.getEstado().equals("Pendiente")) {
                modeloTabla.addRow(new Object[]{
                        c.getPaciente().getNombre() + " " + c.getPaciente().getApellido(),
                        c.getPaciente().getCedula(),
                        c.getDia(),
                        c.getEstado()
                });
            }
        }
    }

    private Cita getCitaSeleccionada() {
        int fila = tablaCitas.getSelectedRow();
        if (fila == -1) {
            return null;
        }

        String cedula = (String) modeloTabla.getValueAt(fila, 1);

        for (Cita c : Hospital.getInstancia().getMisCitas()) {
            if (c.getPaciente().getCedula().equals(cedula) &&
                c.getMedico().equals(medicoLogueado) &&
                c.getEstado().equals("Pendiente")) {
                return c;
            }
        }

        return null;
    }

    private void abrirFormularioConsulta() {
        Cita cita = getCitaSeleccionada();
        if (cita == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una cita.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        new FormularioAtencion(cita, this).setVisible(true);
    }

    public void refrescar() {
        cargarCitasPendientes();
    }
}
