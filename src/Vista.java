import javax.swing.*;

import com.formdev.flatlaf.FlatClientProperties;

import mode.LightDarkMode;
import raven.toast.Notifications;
import mode.Rutinas2;

import java.awt.event.*;
import java.awt.*;

public class Vista extends JPanel implements ActionListener, ComponentListener {

    private static App app;

    private JButton btnConectar;
    private JButton btnConectarClemente;

    private JLabel lblLogin;
    private JLabel lblServidor;
    private JLabel lblBasedeDatos;
    private JLabel lblUsuario;
    private JLabel lblContraseña;

    private JTextField txtServidor;
    private JTextField txtBasedeDatos;
    private JTextField txtUsuario;

    private JPasswordField txtContraseña;

    private JPanel panel;

    private LightDarkMode lightDarkMode;

    public Vista(App app) {
        Vista.app = app;
        HazInterfaz();
        setMinimumSize(new Dimension(350, 350));

        HazEscuchas();
        Notifications.getInstance().setJFrame(Vista.app);
    }

    private void HazEscuchas() {
        addComponentListener(this);
        btnConectar.addActionListener(this);
        btnConectarClemente.addActionListener(this);
    }

    private void HazInterfaz() {
        setLayout(null);

        // Boton de modo claro/oscuro
        lightDarkMode = new LightDarkMode(70);
        add(lightDarkMode);

        panel = new JPanel();

        panel.setLayout(new Contenedorlbl_txt());
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:20,2,2,2;"
                + "background:$Menu.background;"
                + "arc:10");
        add(panel);

        lblLogin = new JLabel("Login");
        panel.add(lblLogin);

        lblServidor = new JLabel("Servidor");
        panel.add(lblServidor);

        txtServidor = new JTextField("Once");
        panel.add(txtServidor);

        lblBasedeDatos = new JLabel("Base de Datos");
        panel.add(lblBasedeDatos);

        txtBasedeDatos = new JTextField("DBTickets");
        panel.add(txtBasedeDatos);

        lblUsuario = new JLabel("Usuario");
        panel.add(lblUsuario);

        txtUsuario = new JTextField("sa");
        panel.add(txtUsuario);

        lblContraseña = new JLabel("Contraseña");
        panel.add(lblContraseña);

        txtContraseña = new JPasswordField("123456789");
        panel.add(txtContraseña);

        btnConectar = new JButton("Conectar");
        panel.add(btnConectar);

        btnConectarClemente = new JButton("Clemente");
        panel.add(btnConectarClemente);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        String servidor = txtServidor.getText();
        String basededatos = txtBasedeDatos.getText();
        String usuario = txtUsuario.getText();
        char[] passwordChars = txtContraseña.getPassword();
        String contraseña = new String(passwordChars);

        if (servidor.equals("") || basededatos.equals("") || usuario.equals("") ||
                contraseña.equals("")) {
            System.out.println("Faltan datos de conexión");
            Notifications.getInstance().show(Notifications.Type.INFO,
                    Notifications.Location.TOP_CENTER,
                    "Escribir los datos de conexión");
            return;
        }
        ConexionDB conexion = new ConexionDB(servidor, basededatos, usuario, contraseña);
        if (evt.getSource() == btnConectar) {
            System.out.println("Conectando...");

            conexion.getConexion(1);
        } else if (evt.getSource() == btnConectarClemente) {
            System.out.println("Conectando Clemente...");
            conexion.getConexion(2);
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        int w = this.getWidth();
        int h = this.getHeight();
        float reduccion = 450;
        System.out.println("w: " + w + " h: " + h);

        panel.setBounds((int) (w * .1), (int) (h * .1), (int) (w * .8), (int) (h * .6));

        Font robotoFont0 = Rutinas2.getFont("Roboto", true, 30, w, h, reduccion);
        lblLogin.setFont(robotoFont0);

        Font robotoFont = Rutinas2.getFont("Roboto", true, 14, w, h, reduccion);
        lblServidor.setFont(robotoFont);
        lblBasedeDatos.setFont(robotoFont);
        lblUsuario.setFont(robotoFont);
        lblContraseña.setFont(robotoFont);

        Font robotoFont2 = Rutinas2.getFont("Roboto", true, 14, w, h, reduccion);
        txtServidor.setFont(robotoFont2);
        txtBasedeDatos.setFont(robotoFont2);
        txtUsuario.setFont(robotoFont2);
        txtContraseña.setFont(robotoFont2);

        Font robotoFont3 = Rutinas2.getFont("Roboto", true, 16, w, h, reduccion);
        btnConectar.setFont(robotoFont3);
        btnConectarClemente.setFont(robotoFont3);

        lightDarkMode.setBounds((int) (w * .4), (int) (h * .80), (int) (w * .55), (int) (h * .10));
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {
        System.out.println("Shown");
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        System.out.println("Hidden");
    }
}