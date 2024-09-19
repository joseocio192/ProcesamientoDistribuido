package menu_suma_pd;

import java.sql.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatClientProperties;

import conexion.ConexionDB;
import conexion.ErrorHandler;
import crud.ComponenteHeader;
import layouts.ContentLayout;
import mode.Rutinas2;
import raven.toast.Notifications;

public class MenuSuma extends JPanel implements ComponentListener, ItemListener {
    static final String TICKETSD = "TICKETSD";
    static final String TICKETSH = "TICKETSH";

    private JPanel panel;
    private JPanel panelContent;
    private JPanel pnlTabla;

    private JComboBox<String> tableDropdown;

    private JLabel lblAtributoFiltro;
    private JComboBox<String> priceDropdown;

    private JButton btnLimpiar;
    private JButton btnPrecioMasUno;

    private JRadioButton rdEstado;
    private JRadioButton rdCiudad;
    private JRadioButton rdTienda;
    private ButtonGroup grupo2;

    private JTable table;
    private JScrollPane scroll;
    private DefaultTableModel modelo;

    private boolean selected = false;

    private ComponenteHeader componenteHeader;

    public MenuSuma() {
        init();
        listeners();
    }

    private void listeners() {
        addComponentListener(this);
        tableDropdown.addItemListener(this);
    }

    public void init() {
        setLayout(null);

        componenteHeader = new ComponenteHeader();
        add(componenteHeader, BorderLayout.NORTH);

        // PANEL--------------------------------------------------------
        panel = new JPanel();
        panel.setVisible(true);
        panel.setLayout(null);
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.background;"
                + "border:10,20,30,10");

        tableDropdown = new JComboBox<>();
        panel.add(tableDropdown);

        rdEstado = new JRadioButton("Estado");
        rdCiudad = new JRadioButton("Ciudad");
        rdTienda = new JRadioButton("Tienda");

        rdEstado.setEnabled(false);
        rdCiudad.setEnabled(false);
        rdTienda.setEnabled(false);

        panel.add(rdEstado);
        panel.add(rdCiudad);
        panel.add(rdTienda);

        grupo2 = new ButtonGroup();
        grupo2.add(rdEstado);
        grupo2.add(rdCiudad);
        grupo2.add(rdTienda);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setEnabled(false);
        panel.add(btnLimpiar);

        btnPrecioMasUno = new JButton("Mas +1");
        btnPrecioMasUno.setEnabled(false);
        panel.add(btnPrecioMasUno);

        add(panel);
        // FIN.PANEL-------------------------------------------------------

        panelContent = new JPanel();
        panelContent.setLayout(new ContentLayout());
        panelContent.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.background;"
                + "border:10,20,30,10");
        add(panelContent);

        lblAtributoFiltro = new JLabel();
        panelContent.add(lblAtributoFiltro);
        priceDropdown = new JComboBox<>();
        panelContent.add(priceDropdown);
        priceDropdown.setVisible(false);

        // PNLTABLA-------------------------------------------------------
        pnlTabla = new JPanel();
        pnlTabla.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.background;"
                + "border:10,20,30,10");

        modelo = new DefaultTableModel();
        table = new JTable(modelo);
        table.setDefaultEditor(Object.class, null);

        scroll = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pnlTabla.add(scroll, BorderLayout.CENTER);

        pnlTabla.add(scroll, BorderLayout.CENTER);
        pnlTabla.setLayout(null);
        add(pnlTabla);
        // PNLTABLA-------------------------------------------------------
    }

    private int sumarUnoPrecio() throws SQLException, InterruptedException {
        boolean success = false;
        int rowsAffected = 0;

        while (!success) {
            try (Statement s = ConexionDB.conexion.createStatement()) {
                System.out.println(lblAtributoFiltro.getText() + " " + priceDropdown.getSelectedItem().toString());
                s.executeUpdate("BEGIN transaction");
                String query = "UPDATE TICKETSD " +
                        "SET TICKETSD.PRECIO = TICKETSD.PRECIO + 1 " +
                        "FROM TICKETSD " +
                        "INNER JOIN TICKETSH ON TICKETSD.TICKET = TICKETSH.TICKET " +
                        "WHERE TICKETSH." + lblAtributoFiltro.getText() + " = "
                        + priceDropdown.getSelectedItem().toString() +
                        " AND (SELECT COUNT(DISTINCT TICKETSD.IDPRODUCTO) " +
                        "     FROM TICKETSD " +
                        "     WHERE TICKETSD.TICKET = TICKETSH.TICKET) >= 3";
                rowsAffected = s.executeUpdate(query);
                s.executeUpdate("commit transaction");
                success = true;
            } catch (SQLException e) {
                if (e.getErrorCode() == 1205) {
                    ErrorHandler.showNotification("Error: " + e.getMessage());
                } else {
                    ErrorHandler.handleSqlException(e);
                    success = true;
                }
            }
        }
        return rowsAffected;
    }

    public void setColumnNames(List<String> columnNames) {
        modelo.setColumnCount(0);
        modelo.setRowCount(0);

        for (String columnName : columnNames) {
            modelo.addColumn(columnName);
        }
    }

    public void addRowToTable(Object[] row) {
        modelo.addRow(row);
    }

    public void llenarCombo(List<String> tablas, JComboBox<String> combo) {
        combo.removeAllItems();
        combo.addItem("Seleccione");
        for (String t : tablas) {
            combo.addItem(t);
        }
        combo.setSelectedIndex(0);
    }

    // Getters
    public JComboBox<String> getTableDropdown() {
        return tableDropdown;
    }

    public JComboBox<String> getPriceDropdown() {
        return priceDropdown;
    }

    public JRadioButton getRdEstado() {
        return rdEstado;
    }

    public JRadioButton getRdCiudad() {
        return rdCiudad;
    }

    public JRadioButton getRdTienda() {
        return rdTienda;
    }

    public JButton getBtnLimpiar() {
        return btnLimpiar;
    }

    public JButton getBtnPrecioMasUno() {
        return btnPrecioMasUno;
    }

    public JLabel getLblAtributoFiltro() {
        return lblAtributoFiltro;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        componenteHeader.setBounds(0, 0, getWidth(), (int) (getHeight() * .2));
        panel.setBounds(0, componenteHeader.getHeight(), getWidth(), (int) (getHeight() * .12));

        panelContent.setBounds(0, panel.getY() + panel.getHeight(), getWidth(),
                (int) (getHeight() * .5) - panel.getHeight());

        pnlTabla.setBounds(0, panelContent.getY() + panelContent.getHeight(),
                getWidth(),
                getHeight() - panelContent.getY() - panelContent.getHeight());

        int w = panel.getWidth();
        int h = panel.getHeight();

        int anchoComponente = (int) (w * .4);
        int altoComponente = (int) (h * .8);
        // -----------------------------------------------------------
        tableDropdown.setBounds((int) ((w * .025)), (int) (h * .03),
                anchoComponente,
                altoComponente);
        tableDropdown.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 350));

        rdEstado.setBounds((int) (tableDropdown.getX() * 1.2) + tableDropdown.getWidth(), (int) (h * .05),
                (int) (w * .20),
                (int) (h * .30));
        rdEstado.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 370));

        rdCiudad.setBounds(rdEstado.getX(), rdEstado.getY() + rdEstado.getHeight(),
                rdEstado.getWidth(),
                (int) (h * .30));
        rdCiudad.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 370));

        rdTienda.setBounds(rdEstado.getX(), rdCiudad.getY() + rdCiudad.getHeight(),
                rdEstado.getWidth(),
                (int) (h * .30));
        rdTienda.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 370));

        btnLimpiar.setBounds(rdEstado.getX() + rdEstado.getWidth(), (int) (h * .05),
                (int) (w * .97) - tableDropdown.getWidth() - rdEstado.getWidth() - tableDropdown.getWidth(),
                (int) (h * .45));
        btnLimpiar.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 400));

        btnPrecioMasUno.setBounds(rdEstado.getX() + rdEstado.getWidth(), (int) (h * .05),
                (int) (w * .25),
                (int) (h * .85));
        btnPrecioMasUno.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 400));
        // -----------------------------------------------------------)

        scroll.setBounds((int) (pnlTabla.getWidth() * .03), (int) (pnlTabla.getHeight() * .05),
                (int) (pnlTabla.getWidth() * .95), (int) (pnlTabla.getHeight() * .9));
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        // This method is not used
    }

    @Override
    public void componentShown(ComponentEvent e) {
        // This method is not used
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        // This method is not used
    }

    @Override
    public void itemStateChanged(ItemEvent evt) {
        // if (evt.getSource() == tableDropdown && !selected) {
        // tableDropdown.removeItem("Seleccione");
        // selected = true;
        // }
    }
}