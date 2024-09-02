
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
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;

import mode.Rutinas2;
import raven.toast.Notifications;

public class menu extends JPanel implements ComponentListener, ActionListener, ItemListener, FocusListener {

    private JPanel panel;
    private JPanel panelContent;
    private JPanel pnlTabla;

    private JComboBox<String> cmbTablas;

    private JLabel[] lblAttribute;

    private JTextField[] txtAttribute;

    private JButton btnLimpiar;
    private JButton btnGuardar;
    private JButton btnBuscar;
    private JButton btnConsultar;

    private JRadioButton rdNuevo;
    private JRadioButton rdModificar;
    private ButtonGroup grupo;

    private JRadioButton rdEstado;
    private JRadioButton rdCiudad;
    private JRadioButton rdTienda;
    private ButtonGroup grupo2;

    private JTable table;
    private JScrollPane scroll;
    private DefaultTableModel modelo;

    private boolean selected = false;
    private boolean band = false;

    private ComponenteHeader componenteHeader;

    public menu(Connection conexion) {
        init();
        HazEscuchas();
    }

    private void HazEscuchas() {
        addComponentListener(this);
        cmbTablas.addItemListener(this);
        cmbTablas.addActionListener(this);
        btnBuscar.addActionListener(this);
        btnConsultar.addActionListener(this);
        btnLimpiar.addActionListener(this);
        btnGuardar.addActionListener(this);
        rdModificar.addActionListener(this);
        rdNuevo.addActionListener(this);

    }

    public void init() {
        setLayout(null);
        //
        componenteHeader = new ComponenteHeader(this);
        add(componenteHeader, BorderLayout.NORTH);

        // PANEL--------------------------------------------------------
        panel = new JPanel();
        panel.setVisible(false);
        panel.setLayout(null);
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.background;"
                + "border:10,20,30,10");

        cmbTablas = new JComboBox<String>(new String[] { "Seleccione" });
        llenarCombo();
        panel.add(cmbTablas);

        rdModificar = new JRadioButton("Modificar");
        rdModificar.setEnabled(false);
        rdNuevo = new JRadioButton("Nuevo");
        rdNuevo.setEnabled(false);
        panel.add(rdModificar);
        panel.add(rdNuevo);

        grupo = new ButtonGroup();
        grupo.add(rdModificar);
        grupo.add(rdNuevo);

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
        btnGuardar = new JButton("Guardar");
        btnGuardar.setEnabled(false);
        panel.add(btnGuardar);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setEnabled(false);
        panel.add(btnBuscar);

        btnConsultar = new JButton("Mas +1");
        btnConsultar.setEnabled(false);
        panel.add(btnConsultar);

        add(panel);
        // FIN.PANEL-------------------------------------------------------

        panelContent = new JPanel();
        panelContent.setLayout(new Content());
        panelContent.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.background;"
                + "border:10,20,30,10");
        add(panelContent);

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

    public void createAttributes(String Tabla) {
        try {
            Statement s = ConexionDB.conexion.createStatement();
            ResultSet rs1 = s
                    .executeQuery(
                            "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = N'" + Tabla
                                    + "' ");

            lblAttribute = null;
            txtAttribute = null;

            lblAttribute = new JLabel[rs1.getRow()];
            txtAttribute = new JTextField[rs1.getRow()];
            int j = 0;
            while (rs1.next()) {
                lblAttribute[j] = new JLabel();
                lblAttribute[j].setText(rs1.getString("COLUMN_NAME"));

                txtAttribute[j] = new JTextField();
            }
        } catch (Exception e) {
            ErrorHandler.showNotification(e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == cmbTablas) {
            rdModificar.setEnabled(true);
            rdNuevo.setEnabled(true);
            btnBuscar.setEnabled(true);
            btnConsultar.setEnabled(true);

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

        if (evt.getSource() == btnGuardar) {
            insertarTabla();
            return;
        }

        if (evt.getSource() == rdModificar) {
        }
        if (evt.getSource() == rdNuevo) {

        }
        if (evt.getSource() == btnBuscar) {
        }
        if (evt.getSource() == btnConsultar) {
            // Mensaje de alerta para confirmar la eliminación
            // int dialogResult = JOptionPane.showConfirmDialog(this, "¿Está seguro de
            // eliminar el registro?",
            // "Confirmar", JOptionPane.YES_NO_OPTION);
            // if (dialogResult != JOptionPane.YES_OPTION) {
            // return;
            // }
            if (rdEstado.isSelected()) {
                System.out.println("estado");
                consultaSumarUno("IDESTADO");
                return;
            }
            if (rdCiudad.isSelected()) {
                System.out.println("ciudad");
                consultaSumarUno("IDCIUDAD");
                return;
            }
            if (rdTienda.isSelected()) {
                System.out.println("tienda");
                consultaSumarUno("IDTIENDA");
                return;
            }
        }

        if (evt.getSource() == rdEstado) {

        }
        if (evt.getSource() == rdCiudad) {

        }
        if (evt.getSource() == rdTienda) {

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
            showAttributes();

        } catch (Exception e) {
            ErrorHandler.showNotification("Error: " + e.getMessage());
        }
    }

    // limpieza de los campos
    private void limpiar() {
        for (int i = 0; i < txtAttribute.length; i++) {
            txtAttribute[i].setText("");
        }
    }

    // eliminaciones en la base de datos
    private void eliminar() {

    }

    private int buscarAttributeOrder(String attribute) throws SQLException {
        Statement s = ConexionDB.conexion.createStatement();
        ResultSet rs = s
                .executeQuery(
                        "SELECT ORDINAL_POSITION FROM INFORMATION_SCHEMA.COLUMNS WHERE COLUMN_NAME = N'"
                                + attribute + "' AND TABLE_NAME = N'" + cmbTablas.getSelectedItem().toString()
                                + "' ");
        rs.next();

        return rs.getInt(1);
    }

    // insertarciones en la base de datos
    private void insertarTabla() {

        try {
            Statement s = ConexionDB.conexion.createStatement();
            s.executeUpdate("SET TRANSACTION ISOLATION LEVEL SERIALIZABLE\r\n" + "GO\r\n" + "BEGIN transaction");
            String query = "insert into " + cmbTablas.getSelectedItem().toString() + " values(";
            for (int i = 0; i < txtAttribute.length; i++) {
                if (i == txtAttribute.length - 1) {
                    query += txtAttribute[i].getText() + ")";
                } else {
                    query += txtAttribute[i].getText() + ",";
                }
            }
            System.out.println(query);
            s.executeUpdate(query);
            s.executeUpdate("commit transaction");
            System.out.println("Registro insertado");
        } catch (Exception e) {
            ErrorHandler.showNotification("Error: " + e.getMessage());
            Statement s;
            try {
                s = ConexionDB.conexion.createStatement();
                s.executeUpdate("rollback transaction");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void consultaSumarUno(String filtro) {
        try {

            int posInArray = buscarAttributeOrder(filtro);
            int txtId = Integer.parseInt(txtAttribute[posInArray - 1].getText());

            sumarUnoPrecio(txtId, posInArray - 1);

        } catch (Exception e) {
            ErrorHandler.showNotification("Error: " + e.getMessage());
        }
    }

    private void sumarUnoPrecio(int id, int posInArray) {
        Statement s = null;
        try {
            System.out.println(lblAttribute[posInArray].getText() + " " + id);
            s = ConexionDB.conexion.createStatement();
            s.executeUpdate("SET TRANSACTION ISOLATION LEVEL SERIALIZABLE\r\n"
                    + "BEGIN transaction");
            String query = "UPDATE TICKETSD " +
                    "SET TICKETSD.PRECIO = TICKETSD.PRECIO + 1 " +
                    "FROM TICKETSD " +
                    "INNER JOIN TICKETSH ON TICKETSD.TICKET = TICKETSH.TICKET " +
                    "WHERE TICKETSH." + lblAttribute[posInArray].getText() + " = " + id +
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
                System.out.println("No se realizaron cambios");
                Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER,
                        "No se realizaron cambios");
            }
        } catch (Exception e) {
            ErrorHandler.showNotification("Error: " + e.getMessage());
            if (s != null) {
                try {
                    s.executeUpdate("rollback transaction");
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (SQLException se) {
                    se.printStackTrace();
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

            rs1.last();
            lblAttribute = null;
            txtAttribute = null;

            lblAttribute = new JLabel[rs1.getRow()];
            txtAttribute = new JTextField[rs1.getRow()];
            rs1.beforeFirst();
            int j = 0;
            while (rs1.next()) {
                modelo.addColumn(rs1.getString("COLUMN_NAME"));

                lblAttribute[j] = new JLabel();
                lblAttribute[j].setText(rs1.getString("COLUMN_NAME"));

                txtAttribute[j] = new JTextField();
                j++;
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

    private void showAttributes() {
        panelContent.removeAll();

        FlatAnimatedLafChange.showSnapshot();
        for (int i = 0; i < lblAttribute.length; i++) {
            panelContent.add(lblAttribute[i]);
            panelContent.add(txtAttribute[i]);
        }

        pnlTabla.add(scroll, BorderLayout.CENTER);

        FlatLaf.updateUILater();
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    private void llenarCombo() {
        try {
            Statement s = ConexionDB.conexion.createStatement();
            ResultSet rs = s
                    .executeQuery("select TABLE_NAME from INFORMATION_SCHEMA.TABLES");
            while (rs.next()) {
                cmbTablas.addItem(rs.getString("TABLE_NAME"));
            }
            cmbTablas.setSelectedIndex(0);
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER,
                    "Error No tiene permisos en una tabla");
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void setModo(int modo) {
        // Common actions
        grupo.clearSelection();
        btnGuardar.setVisible(false);
        btnLimpiar.setVisible(false);
        scroll.setVisible(true);
        panel.setVisible(true);

        if (modo == ComponenteHeader.MODO_CAPTURA) {
            // Set properties for capture mode
            band = false;
            rdModificar.setVisible(true);
            rdNuevo.setVisible(true);
            btnLimpiar.setVisible(true);
            btnGuardar.setVisible(true);
            btnBuscar.setVisible(false);
            btnConsultar.setVisible(false);
        } else if (modo == ComponenteHeader.MODO_CONSULTA) {
            band = true;
            // Set properties for consultation mode
            rdModificar.setVisible(false);
            rdNuevo.setVisible(false);
            btnBuscar.setVisible(true);
            btnConsultar.setVisible(true);
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

        rdModificar.setBounds(cmbTablas.getX() + cmbTablas.getWidth(), (int) (h * .05), (int) (w * .14),
                (int) (h * .45));
        rdModificar.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 400));

        rdNuevo.setBounds(cmbTablas.getX() + cmbTablas.getWidth(), rdModificar.getY() + rdModificar.getHeight(),
                (int) (w * .20),
                (int) (h * .45));
        rdNuevo.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 400));

        rdEstado.setBounds(rdNuevo.getX() + rdNuevo.getWidth(), (int) (h * .05),
                rdNuevo.getWidth(),
                (int) (h * .30));
        rdEstado.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 400));

        rdCiudad.setBounds(rdNuevo.getX() + rdNuevo.getWidth(), rdEstado.getY() + rdEstado.getHeight(),
                rdNuevo.getWidth(),
                (int) (h * .30));
        rdCiudad.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 400));

        rdTienda.setBounds(rdNuevo.getX() + rdNuevo.getWidth(), rdCiudad.getY() + rdCiudad.getHeight(),
                rdNuevo.getWidth(),
                (int) (h * .30));
        rdTienda.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 400));

        btnLimpiar.setBounds(rdEstado.getX() + rdEstado.getWidth(), (int) (h * .05),
                (int) (w * .97) - cmbTablas.getWidth() - rdEstado.getWidth() - rdNuevo.getWidth(),
                (int) (h * .45));
        btnLimpiar.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 400));

        btnGuardar.setBounds(rdEstado.getX() + rdEstado.getWidth(), btnLimpiar.getY() + btnLimpiar.getHeight(),
                btnLimpiar.getWidth(),
                btnLimpiar.getHeight());
        btnGuardar.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 400));

        btnConsultar.setBounds(cmbTablas.getX() + cmbTablas.getWidth() + (int) (w * .05), (int) (h * .05),
                (int) (w * .25),
                (int) (h * .85));
        btnConsultar.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 400));

        btnBuscar.setBounds(btnConsultar.getX() + btnConsultar.getWidth(), (int) (h * .05),
                (int) (w * .25),
                (int) (h * .85));
        btnBuscar.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 400));

        // -----------------------------------------------------------

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

    private String procedimientoTipos() {
        return "CREATE PROCEDURE Sp_MttoTipos\n"
                + "@tipId INT OUTPUT, @tipNombre VARCHAR(20)\n"
                + "AS\n"
                + "BEGIN\n"
                + "    IF EXISTS(SELECT * FROM tipos WHERE tipid = @tipId)\n"
                + "    BEGIN\n"
                + "        UPDATE tipos SET tipnombre = @tipNombre WHERE tipid = @tipId;\n"
                + "        IF @@ERROR <> 0\n"
                + "        BEGIN\n"
                + "            RAISERROR('Error al Actualizar en la tabla Tipos', 16, 10);\n"
                + "        END\n"
                + "    END\n"
                + "    ELSE\n"
                + "    BEGIN\n"
                + "        SELECT @tipId = COALESCE(MAX(tipid), 0) + 1 FROM tipos;\n"
                + "        INSERT INTO tipos VALUES(@tipId, @tipNombre);\n"
                + "        IF @@ERROR <> 0\n"
                + "        BEGIN\n"
                + "            RAISERROR('Error al Insertar en la tabla Tipos', 16, 10);\n"
                + "        END\n"
                + "    END\n"
                + "END;";
    }

    private String procedimientoClientes() {
        return "CREATE PROCEDURE Sp_MttoClientes\n"
                + "@cliId INT OUTPUT, @cliNombre VARCHAR(50), @cliApellidos VARCHAR(50),\n"
                + "@cliSexo CHAR(1), @cliLimiteCredito NUMERIC(12, 2), @tipId INT\n"
                + "AS\n"
                + "BEGIN\n"
                + "    IF EXISTS(SELECT * FROM clientes WHERE cliid = @cliId)\n"
                + "    BEGIN\n"
                + "        UPDATE clientes\n"
                + "        SET clinombre = @cliNombre, cliApellidos = @cliApellidos,\n"
                + "            cliSexo = @cliSexo, cliLimiteCredito = @cliLimiteCredito,\n"
                + "            tipid = @tipId\n"
                + "        WHERE cliid = @cliId;\n"
                + "        IF @@ERROR <> 0\n"
                + "        BEGIN\n"
                + "            RAISERROR('Error al Actualizar en la tabla Clientes', 16, 10);\n"
                + "        END\n"
                + "    END\n"
                + "    ELSE\n"
                + "    BEGIN\n"
                + "        SELECT @cliId = COALESCE(MAX(cliid), 0) + 1 FROM clientes;\n"
                + "        INSERT INTO clientes\n"
                + "        VALUES(@cliId, @cliNombre, @cliApellidos, @cliSexo, @cliLimiteCredito, @tipId);\n"
                + "        IF @@ERROR <> 0\n"
                + "        BEGIN\n"
                + "            RAISERROR('Error al Insertar en la tabla Clientes', 16, 10);\n"
                + "        END\n"
                + "    END\n"
                + "END;";
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {

    }

}