package login;

import javax.swing.*;

import com.formdev.flatlaf.FlatClientProperties;

import layouts.Contenedorlbl_txtLayout;
import mode.LightDarkMode;
import raven.toast.Notifications;
import mode.Rutinas2;
import main.App;

import java.awt.event.*;
import java.awt.*;

import java.util.logging.Logger;

public class Vista extends JPanel implements ComponentListener {

    private static App app;
    transient Logger logger = Logger.getLogger(Vista.class.getName());

    private JButton btnConectar;
    private JButton btnConectarSuma;
    private JButton btnConectarBulkCleaning;

    private JLabel lblLogin;
    private JLabel lblServidor;
    private JLabel lblBasedeDatos;
    private JLabel lblUsuario;
    private JLabel lblPassword;

    private JTextField txtServidor;
    private JTextField txtBasedeDatos;
    private JTextField txtUsuario;

    private JPasswordField txtPassword;

    private JPanel panel;

    private LightDarkMode lightDarkMode;

    public Vista(App app) {
        Vista.app = app;
        createInterface();
        setMinimumSize(new Dimension(350, 350));

        addComponentListener(this);
        Notifications.getInstance().setJFrame(Vista.app);
    }

    private void createInterface() {
        setLayout(null);

        // Boton de modo claro/oscuro
        lightDarkMode = new LightDarkMode(70);
        add(lightDarkMode);

        panel = new JPanel();

        panel.setLayout(new Contenedorlbl_txtLayout());
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

        lblPassword = new JLabel("Contraseña");
        panel.add(lblPassword);

        txtPassword = new JPasswordField("123456789");
        panel.add(txtPassword);

        btnConectar = new JButton("Conectar");
        panel.add(btnConectar);

        btnConectarSuma = new JButton("Suma");
        panel.add(btnConectarSuma);

        btnConectarBulkCleaning = new JButton("Bulk and Cleaning");
        add(btnConectarBulkCleaning);

        setVisible(true);
    }

    public JButton getBtnConectar() {
        return btnConectar;
    }

    public JButton getBtnConectarSuma() {
        return btnConectarSuma;
    }

    public JButton getBtnConectarBulkCleaning() {
        return btnConectarBulkCleaning;
    }

    public JTextField getTxtServidor() {
        return txtServidor;
    }

    public JTextField getTxtBasedeDatos() {
        return txtBasedeDatos;
    }

    public JTextField getTxtUsuario() {
        return txtUsuario;
    }

    public JPasswordField getTxtPassword() {
        return txtPassword;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        short w = (short) getWidth();
        short h = (short) getHeight();
        String font = "Roboto";
        float reduccion = 450;
        System.out.println("w: " + w + " h: " + h);

        panel.setBounds((short) (w * .1), (short) (h * .1), (short) (w * .8), (short) (h * .6));

        btnConectarBulkCleaning.setBounds(panel.getX(), panel.getHeight() + (short) (panel.getY() * 1.1),
                panel.getWidth(), (short) (panel.getHeight() * .1));

        Font robotoFont0 = Rutinas2.getFont(font, true, 30, w, h, reduccion);
        lblLogin.setFont(robotoFont0);

        Font robotoFont = Rutinas2.getFont(font, true, 14, w, h, reduccion);
        lblServidor.setFont(robotoFont);
        lblBasedeDatos.setFont(robotoFont);
        lblUsuario.setFont(robotoFont);
        lblPassword.setFont(robotoFont);

        Font robotoFont2 = Rutinas2.getFont(font, true, 14, w, h, reduccion);
        txtServidor.setFont(robotoFont2);
        txtBasedeDatos.setFont(robotoFont2);
        txtUsuario.setFont(robotoFont2);
        txtPassword.setFont(robotoFont2);

        Font robotoFont3 = Rutinas2.getFont(font, true, 16, w, h, reduccion);
        btnConectar.setFont(robotoFont3);
        btnConectarSuma.setFont(robotoFont3);
        btnConectarBulkCleaning.setFont(robotoFont3);

        lightDarkMode.setBounds((short) (w * .5), btnConectarBulkCleaning.getY() + btnConectarBulkCleaning.getHeight(),
                (short) (w * .37), (short) (h * .10));
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        // This method is intentionally left empty because we do not need to handle
        // component move events.
    }

    @Override
    public void componentShown(ComponentEvent e) {
        // This method is intentionally left empty because we do not need to handle
        // component shown events.
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        // This method is intentionally left empty because we do not need to handle
        // component hidden events.
    }
}