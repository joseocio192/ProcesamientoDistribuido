package login;

import conexion.ConexionDB;
import raven.toast.Notifications;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.logging.Logger;

public class ControladorLogin implements ActionListener {

    private Vista vista;

    private Logger logger = Logger.getLogger(ControladorLogin.class.getName());

    public ControladorLogin(Vista vista) {
        this.vista = vista;
        listeners();
        vista.setVisible(true);
    }

    public void listeners() {
        vista.getBtnConectar().addActionListener(this);
        vista.getBtnConectarSuma().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        String servidor = vista.getTxtServidor().getText();
        String basededatos = vista.getTxtBasedeDatos().getText();
        String usuario = vista.getTxtUsuario().getText();
        char[] passwordChars = vista.getTxtPassword().getPassword();
        String password = new String(passwordChars);

        if (servidor.equals("") || basededatos.equals("") || usuario.equals("") ||
                password.equals("")) {
            logger.info("Faltan datos de conexión");
            Notifications.getInstance().show(Notifications.Type.INFO,
                    Notifications.Location.TOP_CENTER,
                    "Escribir los datos de conexión");
            return;
        }
        ConexionDB conexion = new ConexionDB(servidor, basededatos, usuario, password);
        if (evt.getSource() == vista.getBtnConectar()) {
            logger.info("Conectando...");
            conexion.getConexion(1);
        } else if (evt.getSource() == vista.getBtnConectarSuma()) {
            logger.info("Conectando Vista Suma...");
            conexion.getConexion(2);
        }
    }
}
