package crud;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatClientProperties;

import conexion.ConexionDB;
import menu_suma_pd.MenuSuma;
import mode.LightDarkMode;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.*;

import main.App;

import mode.Rutinas2;

public class ComponenteHeader extends JPanel implements ComponentListener, ActionListener {
    public static final int MODO_CAPTURA = 0;
    public static final int MODO_CONSULTA = 1;

    public static MenuCrudVista menu;

    private JPanel pnlBtn;
    private JLabel lblModo;
    private JButton btnModoCaptura;
    private JButton btnModoConsulta;
    private JButton logout;

    String modo;

    private LightDarkMode lightDarkMode;

    public ComponenteHeader(MenuCrudVista menu) {
        ComponenteHeader.menu = menu;
        modo = "Elegir modo";
        init();
        addComponentListener(this);
        btnModoCaptura.addActionListener(this);
        btnModoConsulta.addActionListener(this);
        logout.addActionListener(this);
    }

    public ComponenteHeader(String modo) {
        this.modo = modo;
        initSumaHeader();
        addComponentListener(this);
        logout.addActionListener(this);
    }

    public void init() {
        setBorder(new EmptyBorder(2, 2, 2, 2));
        setLayout(null);

        lightDarkMode = new LightDarkMode(60);
        add(lightDarkMode);

        logout = new JButton("Logout");
        logout.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:9;"
                + "focusWidth:0;"
                + "borderWidth:0;"
                + "innerFocusWidth:0");
        add(logout);

        lblModo = new JLabel(modo);
        add(lblModo);

        // -----------------------------------------------------------
        pnlBtn = new JPanel();
        pnlBtn.setLayout(new Header());
        btnModoCaptura = new JButton("Captura");
        btnModoCaptura.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:999;"
                + "foreground:$Menu.foreground;"
                + "focusWidth:0;"
                + "borderWidth:0;"
                + "innerFocusWidth:0");
        pnlBtn.add(btnModoCaptura);

        btnModoConsulta = new JButton("Consulta");
        btnModoConsulta.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:999;"
                + "foreground:$Menu.foreground;"
                + "focusWidth:0;"
                + "borderWidth:0;"
                + "innerFocusWidth:0");
        pnlBtn.add(btnModoConsulta);

        add(pnlBtn);
        // -----------------------------------------------------------
    }

    public void initSumaHeader() {
        setBorder(new EmptyBorder(2, 2, 2, 2));
        setLayout(null);

        lightDarkMode = new LightDarkMode(60);
        add(lightDarkMode);

        logout = new JButton("Logout");
        logout.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:9;"
                + "focusWidth:0;"
                + "borderWidth:0;"
                + "innerFocusWidth:0");
        add(logout);

        lblModo = new JLabel(modo);
        add(lblModo);
        // -----------------------------------------------------------
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (logout == evt.getSource()) {
            try {
                ConexionDB.conexion.close();
                System.out.println("Desconectado");
            } catch (Exception e) {
                System.out.println(e);
            }
            App.logout();
            return;
        }
        if (btnModoCaptura == evt.getSource()) {
            menu.setModo(MODO_CAPTURA);
            return;
        }
        if (btnModoConsulta == evt.getSource()) {
            menu.setModo(MODO_CONSULTA);
            return;
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        int w = this.getWidth();
        int h = (int) (this.getHeight() * .9);

        lblModo.setBounds((int) (w * .05), (int) (h * .05), (int) (w * .40), (int) (h * .35));
        Font fuente = Rutinas2.getFont("Roboto", true, (int) (h), w, h, 350);
        // lblModo.setFont(new Font("Roboto", Font.BOLD, (int) (h * .3)));
        lblModo.setFont(fuente);

        lightDarkMode.setBounds((int) (w * .5), (int) (h * .05), (int) (w * .5), (int) (h * .35));

        logout.setBounds((int) (w * .7), (int) (h * .5), (int) (w * .25), (int) (h * .3));
        logout.setFont(Rutinas2.getFont("Roboto", true, (int) (h), w, h, 670));
        // logout.setFont(new Font("Roboto", Font.BOLD, (int) (h * .3)));
        if (pnlBtn != null)
            pnlBtn.setBounds((int) (w * .05), (int) (h * .5), (int) (w * .55), (int) (h * .30));
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    private class Header implements LayoutManager {

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(5, btnModoConsulta.getPreferredSize().height + 0);
            }
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(0, 0);
            }
        }

        @Override
        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                Insets insets = parent.getInsets();
                int x = insets.left;
                int y = insets.top;
                int gap = 5;
                int width = parent.getWidth() - (insets.left + insets.right);
                int height = parent.getHeight() - (insets.top + insets.bottom);
                int buttonWidth = (width - gap) / 2;

                btnModoCaptura.setBounds(x, y, buttonWidth, height);
                btnModoConsulta.setBounds(x + buttonWidth + gap, y, buttonWidth, height);

                int h = parent.getHeight();

                btnModoCaptura.setFont(getFont(h, buttonWidth, height));
                btnModoConsulta.setFont(getFont(h, buttonWidth, height));

            }
        }

        private Font getFont(int baseSize, int width, int height) {
            double scaleFactor = 55.0;
            int scaledSize = (int) (baseSize * (Math.min(width, height) / scaleFactor));

            return new Font("SegoeUI", Font.PLAIN, scaledSize);
        }

    }
}
