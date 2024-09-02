
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

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatClientProperties;

import mode.Rutinas2;
import raven.toast.Notifications;

public class menuClemente extends JPanel implements ComponentListener, ActionListener, ItemListener, FocusListener {
    final static String TICKETSD = "TICKETSD";
    final static String TICKETSH = "TICKETSH";

    private JPanel panel;
    private JPanel panelContent;
    private JPanel pnlTabla;

    private JComboBox<String> cmbTablas;

    private JLabel lblAtributoFiltro;
    private JComboBox<String> cmbFK;

    private JLabel lblPrecio;
    private JTextField txtPrecio;

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

    public menuClemente(Connection conexion) {
        init();
        HazEscuchas();
    }

    private void HazEscuchas() {
        addComponentListener(this);
        cmbTablas.addItemListener(this);
        cmbTablas.addActionListener(this);
        btnPrecioMasUno.addActionListener(this);
        btnLimpiar.addActionListener(this);
        rdEstado.addActionListener(this);
        rdCiudad.addActionListener(this);
        rdTienda.addActionListener(this);
    }

    public void init() {
        setLayout(null);

        componenteHeader = new ComponenteHeader(this);
        add(componenteHeader, BorderLayout.NORTH);

        // PANEL--------------------------------------------------------
        panel = new JPanel();
        panel.setVisible(true);
        panel.setLayout(null);
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.background;"
                + "border:10,20,30,10");

        cmbTablas = new JComboBox<String>(new String[] { "Seleccione" });
        llenarCombo();
        panel.add(cmbTablas);

        rdEstado = new JRadioButton("Estado");
        rdCiudad = new JRadioButton("Ciudad");
        rdTienda = new JRadioButton("Tienda");
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
        panelContent.setLayout(new Content());
        panelContent.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.background;"
                + "border:10,20,30,10");
        add(panelContent);

        lblPrecio = new JLabel("Precio");
        panelContent.add(lblPrecio);
        lblPrecio.setVisible(selected);
        txtPrecio = new JTextField();
        panelContent.add(txtPrecio);
        txtPrecio.setVisible(selected);

        lblAtributoFiltro = new JLabel();
        panelContent.add(lblAtributoFiltro);
        cmbFK = new JComboBox<String>(new String[] { "Seleccione" });
        panelContent.add(cmbFK);
        cmbFK.setVisible(false);

        // PNLTABLA-------------------------------------------------------
        pnlTabla = new JPanel();
        pnlTabla.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.background;"
                + "border:10,20,30,10");

        modelo = new DefaultTableModel();
        table = new JTable(modelo);
        table.setDefaultEditor(Object.class, null);

        scroll = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVisible(false);
        pnlTabla.add(scroll, BorderLayout.CENTER);

        pnlTabla.add(scroll, BorderLayout.CENTER);
        pnlTabla.setLayout(null);
        add(pnlTabla);
        // PNLTABLA-------------------------------------------------------

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == cmbTablas) {
            rdEstado.setEnabled(true);
            rdCiudad.setEnabled(true);
            rdTienda.setEnabled(true);
            lblPrecio.setVisible(selected);
            txtPrecio.setVisible(selected);
            // if (!band) {
            // if (rdNuevo.isSelected()) {
            // txtTipid.setText("*");
            // txtTipid.setEditable(false);
            // txtClienteId.setText("*");
            // txtClienteId.setEditable(false);
            // }
            // if (rdModificar.isSelected()) {
            // txtTipid.setText("");
            // txtTipid.setEditable(true);
            // txtClienteId.setText("");
            // txtClienteId.setEditable(true);
            // }
            // }

        }
        if (evt.getSource() == btnLimpiar) {
            limpiar();
            return;
        }

        if (evt.getSource() == btnPrecioMasUno) {
            String atributo = "";
            if (rdEstado.isSelected()) {
                atributo = rdEstado.getText();
            }
            if (rdCiudad.isSelected()) {
                atributo = rdCiudad.getText();
            }
            if (rdTienda.isSelected()) {
                atributo = rdTienda.getText();
            }

            int dialogResult = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de sumar uno al precio de la tabla: " + cmbTablas.getSelectedItem().toString()
                            + ",  Atributo: " + atributo + ", Valor: " + cmbFK.getSelectedItem().toString() + "?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (dialogResult != JOptionPane.YES_OPTION) {
                return;
            }

            sumarUnoPrecio();
        }
        if (evt.getSource() == rdEstado) {
            btnPrecioMasUno.setEnabled(true);
            lblAtributoFiltro.setText("IDESTADO");
            llenarComboFK("IDESTADO");
            cmbFK.setVisible(true);
        }
        if (evt.getSource() == rdCiudad) {
            btnPrecioMasUno.setEnabled(true);
            lblAtributoFiltro.setText("IDCIUDAD");
            llenarComboFK("IDCIUDAD");
            cmbFK.setVisible(true);
        }
        if (evt.getSource() == rdTienda) {
            btnPrecioMasUno.setEnabled(true);
            lblAtributoFiltro.setText("IDTIENDA");
            llenarComboFK("IDTIENDA");
            cmbFK.setVisible(true);
        }

        try {
            Statement s = ConexionDB.conexion.createStatement();
            ResultSet rs = s
                    .executeQuery(
                            "SELECT top 1 TABLE_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = N'"
                                    + cmbTablas.getSelectedItem().toString()
                                    + "' ");
            rs.next();

            llenarTabla(rs.getString(1));

        } catch (Exception e) {
            ErrorHandler.showNotification("Error: " + e.getMessage());
        }
    }

    // limpieza de los campos
    private void limpiar() {

    }

    private void sumarUnoPrecio() {
        Statement s = null;
        int maxRetries = 5;
        int attempt = 0;
        boolean success = false;

        while (attempt < maxRetries && !success) {
            try {
                System.out.println(lblAtributoFiltro.getText() + " " + cmbFK.getSelectedItem().toString());
                s = ConexionDB.conexion.createStatement();
                s.executeUpdate("SET TRANSACTION ISOLATION LEVEL SERIALIZABLE\r\n"
                        + "BEGIN transaction");
                String query = "UPDATE TICKETSD " +
                        "SET TICKETSD.PRECIO = TICKETSD.PRECIO + 1 " +
                        "FROM TICKETSD " +
                        "INNER JOIN TICKETSH ON TICKETSD.TICKET = TICKETSH.TICKET " +
                        "WHERE TICKETSH." + lblAtributoFiltro.getText() + " = " + cmbFK.getSelectedItem().toString() +
                        " AND (SELECT COUNT(DISTINCT TICKETSD.IDPRODUCTO) " +
                        "     FROM TICKETSD " +
                        "     WHERE TICKETSD.TICKET = TICKETSH.TICKET) >= 3";
                int rowsAffected = s.executeUpdate(query);
                s.executeUpdate("commit transaction");
                if (rowsAffected > 0) {
                    System.out.println("Registro actualizado");
                    Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER,
                            "Registro actualizado");
                } else {
                    Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER,
                            "No se realizaron Cambios");
                }
                success = true;
            } catch (SQLException e) {
                if (e.getMessage().contains("Transaction (Process ID 55) was deadlocked")) {
                    attempt++;
                    if (attempt >= maxRetries) {
                        Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER,
                                "Error: Transaction (Process ID 55) was deadlocked on lock | communication buffer resources with another process and has been chosen as the deadlock victim. Rerun the transaction.");
                    }
                } else {
                    ErrorHandler.showNotification("Error: " + e.getMessage());
                    break;
                }
            } finally {
                if (s != null) {
                    try {
                        s.close();
                    } catch (SQLException e) {
                        ErrorHandler.showNotification("Error closing statement: " + e.getMessage());
                    }
                }
            }
        }
    }

    private void llenarTabla(String Tabla) {
        try {
            modelo.setColumnCount(0);
            modelo.setRowCount(0);

            Statement s = ConexionDB.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs1 = s
                    .executeQuery(
                            "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = N'" + Tabla
                                    + "' ");
            while (rs1.next()) {
                modelo.addColumn(rs1.getString("COLUMN_NAME"));
            }

            ResultSet rs2 = s.executeQuery("SELECT top 500 * FROM " + Tabla);

            while (rs2.next()) {
                Object[] fila = new Object[modelo.getColumnCount()];
                for (int i = 0; i < modelo.getColumnCount(); i++) {
                    fila[i] = rs2.getObject(i + 1);
                }
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void llenarCombo() {
        try {
            Statement s = ConexionDB.conexion.createStatement();
            ResultSet rs = s
                    .executeQuery("select TABLE_NAME from INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = N'"
                            + TICKETSD + "' ");
            while (rs.next()) {
                cmbTablas.addItem(rs.getString("TABLE_NAME"));
            }
            cmbTablas.setSelectedIndex(0);
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER,
                    "Error, " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void llenarComboFK(String filtro) {
        try {
            cmbFK.removeAllItems();
            cmbFK.addItem("Seleccione");
            Statement s = ConexionDB.conexion.createStatement();
            ResultSet rs = s
                    .executeQuery(
                            "SELECT distinct(" + filtro + ") FROM " + cmbTablas.getSelectedItem().toString() + " D "
                                    + " inner join " + TICKETSH + " H on " + "D.TICKET = H.TICKET ");
            while (rs.next()) {
                cmbFK.addItem(rs.getString(filtro));
            }
            cmbFK.setSelectedIndex(0);
        } catch (Exception e) {
            ErrorHandler.showNotification("Error: " + e.getMessage());
        }
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
        cmbTablas.setBounds((int) ((w * .025)), (int) (h * .03),
                anchoComponente,
                altoComponente);
        cmbTablas.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 350));

        rdEstado.setBounds((int) (cmbTablas.getX() * 1.2) + cmbTablas.getWidth(), (int) (h * .05),
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
                (int) (w * .97) - cmbTablas.getWidth() - rdEstado.getWidth() - cmbTablas.getWidth(),
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
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void itemStateChanged(ItemEvent evt) {
        if (evt.getSource() == cmbTablas) {
            if (!selected) {
                cmbTablas.removeItem("Seleccione");
                selected = true;
                return;
            }
        }
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {

    }

}