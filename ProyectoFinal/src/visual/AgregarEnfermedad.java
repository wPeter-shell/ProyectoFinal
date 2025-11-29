package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import logic.Administrador;
import logic.Enfermedad;
import logic.Hospital;

public class AgregarEnfermedad extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtNombre;
	private JCheckBox chkVigilancia;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AgregarEnfermedad dialog = new AgregarEnfermedad();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AgregarEnfermedad() {
		setTitle("Agregar Enfermedad");
		setBounds(100, 100, 400, 200);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(3, 2, 10, 10));
		
		// Campos del formulario
		contentPanel.add(new JLabel("Nombre de la enfermedad:"));
		txtNombre = new JTextField();
		contentPanel.add(txtNombre);
		
		contentPanel.add(new JLabel("Bajo vigilancia:"));
		chkVigilancia = new JCheckBox("Marcar bajo vigilancia");
		contentPanel.add(chkVigilancia);
		
		// Panel de botones
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Agregar");
				okButton.setActionCommand("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						agregarEnfermedad();
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
	 * Metodo para agregar una enfermedad al sistema
	 */
	private void agregarEnfermedad() {
		try {
			// Validar campo obligatorio
			String nombre = txtNombre.getText().trim();
			
			if (nombre.isEmpty()) {
				JOptionPane.showMessageDialog(this, 
					"El nombre de la enfermedad es obligatorio", 
					"Error de validación", 
					JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Validar que no exista una enfermedad con el mismo nombre
			for (Enfermedad enfermedadExistente : Hospital.getInstancia().getMisEnfermedades()) {
				if (enfermedadExistente.getNombre().equalsIgnoreCase(nombre)) {
					JOptionPane.showMessageDialog(this, 
						"Ya existe una enfermedad con este nombre", 
						"Error de registro", 
						JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

			boolean bajoVigilancia = chkVigilancia.isSelected();
			Enfermedad nuevaEnfermedad = new Enfermedad(nombre, bajoVigilancia);

			Administrador admin = Hospital.getInstancia().getAdministrador();
			admin.agregarEnfermedad(nuevaEnfermedad);

			// Si esta marcada como bajo vigilancia, agregarla a enfermedades vigiladas
			if (bajoVigilancia) {
				admin.marcarComoVigilada(nuevaEnfermedad);
			}

			String mensaje = "Enfermedad agregada exitosamente:\n" +
							"Nombre: " + nombre + "\n" +
							"Bajo vigilancia: " + (bajoVigilancia ? "Sí" : "No");
			
			JOptionPane.showMessageDialog(this, 
				mensaje,
				"Registro Éxitoso", 
				JOptionPane.INFORMATION_MESSAGE);

			limpiarCampos();
			dispose();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, 
				"Error al agregar enfermedad: " + e.getMessage(), 
				"Error del sistema", 
				JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * Metodo para limpiar todos los campos del formulario
	 */
	private void limpiarCampos() {
		txtNombre.setText("");
		chkVigilancia.setSelected(false);
	}
}