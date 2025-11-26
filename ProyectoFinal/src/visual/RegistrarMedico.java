package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;

import logic.Administrador;
import logic.Hospital;
import logic.Medico;

public class RegistrarMedico extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtUsuario;
	private JTextField txtPassword;
	private JTextField txtNombre;
	private JTextField txtApellido;
	private JTextField txtCedula;
	private JTextField txtEdad;
	private JTextField txtTelefono;
	private JTextField txtDireccion;
	private JTextField txtEspecialidad;
	private JTextField txtCitasPorDia;
	private JComboBox<Character> cmbGenero;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			RegistrarMedico dialog = new RegistrarMedico();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public RegistrarMedico() {
		setTitle("Registrar Médico");
		setBounds(100, 100, 500, 500);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(12, 2, 10, 10));
		
		// Campos del formulario
		contentPanel.add(new JLabel("Usuario:"));
		txtUsuario = new JTextField();
		contentPanel.add(txtUsuario);
		
		contentPanel.add(new JLabel("Contraseña:"));
		txtPassword = new JTextField();
		contentPanel.add(txtPassword);
		
		contentPanel.add(new JLabel("Nombre:"));
		txtNombre = new JTextField();
		contentPanel.add(txtNombre);
		
		contentPanel.add(new JLabel("Apellido:"));
		txtApellido = new JTextField();
		contentPanel.add(txtApellido);
		
		contentPanel.add(new JLabel("Cédula:"));
		txtCedula = new JTextField();
		contentPanel.add(txtCedula);
		
		contentPanel.add(new JLabel("Género:"));
		cmbGenero = new JComboBox<>(new Character[]{'M', 'F'});
		contentPanel.add(cmbGenero);
		
		contentPanel.add(new JLabel("Edad:"));
		txtEdad = new JTextField();
		contentPanel.add(txtEdad);
		
		contentPanel.add(new JLabel("Teléfono:"));
		txtTelefono = new JTextField();
		contentPanel.add(txtTelefono);
		
		contentPanel.add(new JLabel("Dirección:"));
		txtDireccion = new JTextField();
		contentPanel.add(txtDireccion);
		
		contentPanel.add(new JLabel("Especialidad:"));
		txtEspecialidad = new JTextField();
		contentPanel.add(txtEspecialidad);
		
		contentPanel.add(new JLabel("Citas por día:"));
		txtCitasPorDia = new JTextField();
		contentPanel.add(txtCitasPorDia);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Registrar");
				okButton.setActionCommand("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						registrarMedico();
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}

	/**
	 * Método para registrar un médico en el sistema
	 */
	private void registrarMedico() {
		try {
			// Validar campos obligatorios
			if (txtUsuario.getText().isEmpty() || txtPassword.getText().isEmpty() ||
				txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty() ||
				txtCedula.getText().isEmpty() || txtEspecialidad.getText().isEmpty() ||
				txtCitasPorDia.getText().isEmpty()) {
				
				JOptionPane.showMessageDialog(this, 
					"Todos los campos son obligatorios", 
					"Error de validación", 
					JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Validar que no exista un médico con la misma cédula
			String cedula = txtCedula.getText();
			for (Medico medicoExistente : Hospital.getInstancia().getMisMedicos()) {
				if (medicoExistente.getCedula().equals(cedula)) {
					JOptionPane.showMessageDialog(this, 
						"Ya existe un médico con esta cédula", 
						"Error de registro", 
						JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

			// Validar que no exista un médico con el mismo usuario
			String usuario = txtUsuario.getText();
			for (Medico medicoExistente : Hospital.getInstancia().getMisMedicos()) {
				if (medicoExistente.getUsuario().equals(usuario)) {
					JOptionPane.showMessageDialog(this, 
						"Ya existe un médico con este usuario", 
						"Error de registro", 
						JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

			// Obtener valores de los campos
			String password = txtPassword.getText();
			String nombre = txtNombre.getText();
			String apellido = txtApellido.getText();
			char genero = (Character) cmbGenero.getSelectedItem();
			int edad = Integer.parseInt(txtEdad.getText());
			String telefono = txtTelefono.getText();
			String direccion = txtDireccion.getText();
			String especialidad = txtEspecialidad.getText();
			int citasPorDia = Integer.parseInt(txtCitasPorDia.getText());

			// Validar edad
			if (edad < 18 || edad > 100) {
				JOptionPane.showMessageDialog(this, 
					"La edad debe estar entre 18 y 100 años", 
					"Error de validación", 
					JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Validar citas por día
			if (citasPorDia <= 0 || citasPorDia > 20) {
				JOptionPane.showMessageDialog(this, 
					"Las citas por día deben estar entre 1 y 20", 
					"Error de validación", 
					JOptionPane.ERROR_MESSAGE);
				return;
			}

			Medico nuevoMedico = new Medico(
				usuario, password, nombre, apellido, cedula, 
				genero, edad, telefono, direccion, especialidad, citasPorDia
			);

			Administrador admin = Hospital.getInstancia().getAdministrador();
			admin.registrarMedico(nuevoMedico);

			JOptionPane.showMessageDialog(this, 
				"Médico registrado exitosamente:\n" +
				"Nombre: " + nombre + " " + apellido + "\n" +
				"Especialidad: " + especialidad + "\n" +
				"Cédula: " + cedula + "\n" +
				"Usuario: " + usuario,
				"Registro Exitoso", 
				JOptionPane.INFORMATION_MESSAGE);

			limpiarCampos();
			dispose();

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, 
				"Error en formato numérico:\n" +
				"Edad y Citas por día deben ser números válidos", 
				"Error de formato", 
				JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, 
				"Error al registrar médico: " + e.getMessage(), 
				"Error del sistema", 
				JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * Método para limpiar todos los campos del formulario
	 */
	private void limpiarCampos() {
		txtUsuario.setText("");
		txtPassword.setText("");
		txtNombre.setText("");
		txtApellido.setText("");
		txtCedula.setText("");
		cmbGenero.setSelectedIndex(0);
		txtEdad.setText("");
		txtTelefono.setText("");
		txtDireccion.setText("");
		txtEspecialidad.setText("");
		txtCitasPorDia.setText("");
	}
}