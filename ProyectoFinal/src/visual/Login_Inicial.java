package visual;

import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane; 

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import logic.Administrador;
import logic.Hospital;
import logic.Medico;
import logic.Secretaria;

public class Login_Inicial extends JFrame {


   private JPanel contentPane;
   private JTextField txtUser;
   private JPasswordField fieldPassword;

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
      setTitle("Log-In - Sistema Hospital");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(600, 400);
      setLocationRelativeTo(null); 

      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
      contentPane.setLayout(new BorderLayout(10, 10));
      setContentPane(contentPane);

      JPanel panelHeader = new JPanel();
      panelHeader.setBackground(new Color(46, 139, 192));
      panelHeader.setLayout(new BorderLayout());

      JLabel lblTitulo = new JLabel("Hospital Plus+");
      lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
      lblTitulo.setForeground(Color.WHITE);
      lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
      panelHeader.add(lblTitulo, BorderLayout.CENTER);

      contentPane.add(panelHeader, BorderLayout.NORTH);

      JPanel panelForm = new JPanel();
      panelForm.setLayout(null);
      panelForm.setBorder(BorderFactory.createTitledBorder("Credenciales de acceso"));
      contentPane.add(panelForm, BorderLayout.CENTER);

      JLabel lblUser = new JLabel("Usuario:");
      lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 16));
      lblUser.setBounds(70, 60, 100, 25);
      panelForm.add(lblUser);

      txtUser = new JTextField();
      txtUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      txtUser.setBounds(170, 60, 220, 25);
      panelForm.add(txtUser);
      txtUser.setColumns(10);

      JLabel lblPassword = new JLabel("Contraseña:");
      lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
      lblPassword.setBounds(70, 120, 100, 25);
      panelForm.add(lblPassword);

      fieldPassword = new JPasswordField();
      fieldPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      fieldPassword.setBounds(170, 120, 220, 25);
      panelForm.add(fieldPassword);

      JToggleButton tglShowPass = new JToggleButton("Mostrar");
      tglShowPass.setBounds(400, 120, 80, 25);
      panelForm.add(tglShowPass);

      tglShowPass.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            if (tglShowPass.isSelected()) {
               fieldPassword.setEchoChar((char) 0); 
               tglShowPass.setText("Ocultar");
            } else {
               fieldPassword.setEchoChar('*'); 
               tglShowPass.setText("Mostrar");
            }
         }
      });

      JPanel panelBotones = new JPanel();
      panelBotones.setBorder(new EmptyBorder(5, 5, 5, 5));
      contentPane.add(panelBotones, BorderLayout.SOUTH);

      JButton btnEnter = new JButton("Entrar");
      btnEnter.setFont(new Font("Segoe UI", Font.BOLD, 14));
      panelBotones.add(btnEnter);

      JButton btnClose = new JButton("Cerrar");
      btnClose.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      panelBotones.add(btnClose);

      btnEnter.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {

            String userVisual = txtUser.getText();
            String passwordVisual = new String(fieldPassword.getPassword());
            
            if (userVisual.isEmpty() || passwordVisual.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(
                      Login_Inicial.this,
                      "Debe ingresar usuario y contraseña.",
                      "Campos requeridos",
                      javax.swing.JOptionPane.WARNING_MESSAGE
                );
                return;
             }

            
            Object objeto = null;
            
            try {
            	   objeto = Hospital.getInstancia().LogIn(userVisual, passwordVisual);
            	} catch (IllegalArgumentException ex) {
            	   JOptionPane.showMessageDialog(
            	      Login_Inicial.this,     
            	      ex.getMessage(),
            	      "Error",
            	      JOptionPane.ERROR_MESSAGE
            	   );
            	   return;
            	}

            if (objeto == null) {
                JOptionPane.showMessageDialog(
                   Login_Inicial.this,
                   "Usuario o contraseña incorrectos.",
                   "Acceso denegado",
                   JOptionPane.ERROR_MESSAGE
                );
                return;
             }


            if (objeto instanceof Administrador || objeto instanceof Secretaria || objeto instanceof Medico) {

	        	Principal ventanaPrincipal = new Principal(objeto);
	        	ventanaPrincipal.setVisible(true);
	        	dispose();

            }
         }
      });

      btnClose.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            dispose();
         }
      });
   }
}
