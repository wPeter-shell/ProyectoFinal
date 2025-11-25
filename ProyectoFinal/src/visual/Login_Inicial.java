package visual;

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

      // PANEL SUPERIOR (HEADER) 
      JPanel panelHeader = new JPanel();
      panelHeader.setBackground(new Color(46, 139, 192));
      panelHeader.setLayout(new BorderLayout());

      JLabel lblTitulo = new JLabel("Hospital San Pedro");
      lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
      lblTitulo.setForeground(Color.WHITE);
      lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
      panelHeader.add(lblTitulo, BorderLayout.CENTER);

      contentPane.add(panelHeader, BorderLayout.NORTH);

      // PANEL CENTRAL (FORMULARIO) 
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

      JLabel lblPassword = new JLabel("Contrase�a:");
      lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
      lblPassword.setBounds(70, 120, 100, 25);
      panelForm.add(lblPassword);

      fieldPassword = new JPasswordField();
      fieldPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      fieldPassword.setBounds(170, 120, 220, 25);
      panelForm.add(fieldPassword);

      // Toggle opcional para mostrar/ocultar contraseña
      JToggleButton tglShowPass = new JToggleButton("Mostrar");
      tglShowPass.setBounds(400, 120, 80, 25);
      panelForm.add(tglShowPass);

      tglShowPass.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            if (tglShowPass.isSelected()) {
               fieldPassword.setEchoChar((char) 0); // muestra texto
               tglShowPass.setText("Ocultar");
            } else {
               fieldPassword.setEchoChar('*'); // oculta texto
               tglShowPass.setText("Mostrar");
            }
         }
      });

      // ───── PANEL INFERIOR (BOTONES) ─────
      JPanel panelBotones = new JPanel();
      panelBotones.setBorder(new EmptyBorder(5, 5, 5, 5));
      contentPane.add(panelBotones, BorderLayout.SOUTH);

      JButton btnEnter = new JButton("Entrar");
      btnEnter.setFont(new Font("Segoe UI", Font.BOLD, 14));
      panelBotones.add(btnEnter);

      JButton btnClose = new JButton("Cerrar");
      btnClose.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      panelBotones.add(btnClose);

      // ───── LÓGICA DE LOS BOTONES ─────
      btnEnter.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {

            String userVisual = txtUser.getText();
            String passwordVisual = new String(fieldPassword.getPassword());

            Object objeto = Hospital.getInstancia().LogIn(userVisual, passwordVisual);

            if (objeto == null) {
               javax.swing.JOptionPane.showMessageDialog(
                     Login_Inicial.this,
                     "Usuario o contraseña incorrectos",
                     "Error de autenticacion",
                     javax.swing.JOptionPane.ERROR_MESSAGE
               );
               return;
            }

            if (objeto instanceof Administrador) {
               // TODO: abrir ventana de administrador
               // new VentanaAdmin((Administrador) objeto).setVisible(true);
               dispose();

            } else if (objeto instanceof Secretaria) {
               // TODO: abrir ventana de secretaria
               // new VentanaSecretaria((Secretaria) objeto).setVisible(true);
               dispose();

            } else if (objeto instanceof Medico) {
               // TODO: abrir ventana de médico
               // new VentanaMedico((Medico) objeto).setVisible(true);
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
