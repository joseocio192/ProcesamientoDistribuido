package main;

import java.util.logging.Logger;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import login.Vista;
import login.ControladorLogin;
import menu_suma_pd.ControladorSuma;
import menu_suma_pd.MenuSuma;
import menu_suma_pd.ModeloSuma;
import crud.MenuCrudVista;
import crud.ModeloBD;
import crud.ModeloTabla;
import crud.ControladorCrud;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class App extends JFrame {

    private static final Logger logger = Logger.getLogger(App.class.getName());
    private static App app;

    private JPanel panel;

    public App() {
        super("App");
        app = this;
        interfaz();
        setContentPane(panel);
        setVisible(true);

    }

    public void interfaz() {
        setSize(650, 700);
        setMinimumSize(new Dimension(350, 350));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        Vista vista = new Vista(app);
        panel = vista;
        new ControladorLogin(vista);

        add(panel);
    }

    public static void login() {
        MenuCrudVista menu = new MenuCrudVista();
        ModeloTabla modelo = new ModeloTabla();
        ModeloBD modeloBD = new ModeloBD();
        new ControladorCrud(menu, modelo, modeloBD);
        app.setContentPane(menu);
        menu.applyComponentOrientation(app.getComponentOrientation());
        SwingUtilities.updateComponentTreeUI(menu);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    public static void loginSuma() {
        MenuSuma menuSuma = new MenuSuma();
        ModeloSuma modeloSuma = new ModeloSuma();
        new ControladorSuma(menuSuma, modeloSuma);
        app.setContentPane(menuSuma);
        menuSuma.applyComponentOrientation(app.getComponentOrientation());
        SwingUtilities.updateComponentTreeUI(menuSuma);
    }

    public static void logout() {
        Vista vista = new Vista(app);
        new ControladorLogin(vista);

        FlatAnimatedLafChange.showSnapshot();
        app.setContentPane(vista);
        vista.applyComponentOrientation(app.getComponentOrientation());
        SwingUtilities.updateComponentTreeUI(vista);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            logger.severe("Failed to initialize LaF");
        }
        FlatRobotoFont.install();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        FlatMacDarkLaf.setup();
        new App();
    }
}
