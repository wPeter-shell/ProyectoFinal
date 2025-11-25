package visual;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logic.Administrador;
import logic.Hospital;
import logic.Medico;
import logic.Secretaria;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login_Inicial extends JFrame {

	private JPanel contentPane;
	private JTextField txtUser;
	private JTextField txtPassword;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login_Inicial frame = new Login_Inicial();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login_Inicial() {
		setResizable(false);
		setTitle("Log-In");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setSize(500, 400);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JLabel lblUser = new JLabel("User");
		lblUser.setFont(new Font("Consolas", Font.BOLD, 18));
		lblUser.setBounds(203, 45, 56, 16);
		getContentPane().add(lblUser);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Consolas", Font.BOLD, 18));
		lblPassword.setBounds(187, 186, 88, 16);
		getContentPane().add(lblPassword);
		
		txtUser = new JTextField();
		txtUser.setBounds(173, 74, 116, 22);
		getContentPane().add(txtUser);
		txtUser.setColumns(10);
		
		txtPassword = new JTextField();
		txtPassword.setColumns(10);
		txtPassword.setBounds(135, 215, 193, 22);
		getContentPane().add(txtPassword);
		
		JButton btnEnter = new JButton("Enter");
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String userVisual = txtUser.getText();
				String passwordVisual = txtPassword.getText();
				
				Object objeto = Hospital.getInstancia().LogIn(userVisual, passwordVisual);
				if(objeto instanceof Administrador) {
					dispose();
					
				}else if(objeto instanceof Secretaria) {
					
					
				}else if(objeto instanceof Medico) {
					
					
				}
				
			}
		});
		btnEnter.setBounds(275, 327, 97, 25);
		getContentPane().add(btnEnter);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnClose.setBounds(385, 327, 97, 25);
		getContentPane().add(btnClose);
	}
	
	
}
