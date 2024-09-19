package crud;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;

import layouts.ContentLayout;
import mode.Rutinas2;

public class MenuCrudVista extends JPanel implements ComponentListener, ItemListener {

    private JPanel panel;
    private JPanel panelContent;
    private JPanel pnlTabla;

    private JComboBox<String> cmbTablas;

    private JTextField[] txtAttribute;
    private JLabel[] lblAttribute;

    private JButton btnLimpiar;
    private JButton btnGuardar;
    private JButton btnBuscar;
    private JButton btnEliminar;

    private JRadioButton rdNuevo;

    private JRadioButton rdModificar;

    private ButtonGroup grupo;

    private JScrollPane scroll;
    private DefaultTableModel dtModel;

    private boolean selected = false;

    private ComponenteHeader componenteHeader;

    public MenuCrudVista() {
        init();
        listeners();
    }

    private void listeners() {
        addComponentListener(this);
        cmbTablas.addItemListener(this);
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

        cmbTablas = new JComboBox<>(new String[] { "Seleccione" });
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

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setEnabled(false);
        panel.add(btnLimpiar);
        btnGuardar = new JButton("Guardar");
        btnGuardar.setEnabled(false);
        panel.add(btnGuardar);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setEnabled(false);
        panel.add(btnBuscar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setEnabled(false);
        panel.add(btnEliminar);

        add(panel);
        // FIN.PANEL-------------------------------------------------------

        panelContent = new JPanel();
        panelContent.setLayout(new ContentLayout());
        panelContent.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.background;"
                + "border:10,20,30,10");
        add(panelContent);

        // PNLTABLA-------------------------------------------------------
        pnlTabla = new JPanel();
        pnlTabla.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.background;"
                + "border:10,20,30,10");

        dtModel = new DefaultTableModel();
        JTable table = new JTable(dtModel);
        table.setDefaultEditor(Object.class, null);

        scroll = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVisible(false);
        pnlTabla.add(scroll, BorderLayout.CENTER);

        pnlTabla.add(scroll, BorderLayout.CENTER);
        pnlTabla.setLayout(null);
        add(pnlTabla);
        // PNLTABLA-------------------------------------------------------
    }

    public void setColumnNames(List<String> columnNames) {
        dtModel.setColumnCount(0);
        panelContent.removeAll();
        lblAttribute = new JLabel[columnNames.size()];
        txtAttribute = new JTextField[columnNames.size()];

        FlatAnimatedLafChange.showSnapshot();
        for (int i = 0; i < columnNames.size(); i++) {
            dtModel.addColumn(columnNames.get(i));

            lblAttribute[i] = new JLabel(columnNames.get(i));
            txtAttribute[i] = new JTextField();
            panelContent.add(lblAttribute[i]);
            panelContent.add(txtAttribute[i]);
        }
        FlatLaf.updateUILater();
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    // Stream Data
    public void addRowToTable(Object[] rowData) {
        dtModel.addRow(rowData);
    }

    public void clearTable() {
        dtModel.setRowCount(0);
    }

    public void limpiar() {
        for (int i = 0; i < txtAttribute.length; i++) {
            txtAttribute[i].setText("");
        }
    }

    public Object[] getInputData() {
        Object[] inputData = new Object[txtAttribute.length];
        for (int i = 0; i < txtAttribute.length; i++) {
            String value = txtAttribute[i].getText().trim();
            if (value.isEmpty()) {
                inputData[i] = null;
            } else {
                inputData[i] = value;
            }
        }
        return inputData;
    }

    public String[] getAttributes() {
        String[] keys = new String[lblAttribute.length];
        for (int i = 0; i < lblAttribute.length; i++) {
            keys[i] = lblAttribute[i].getText().trim();
        }
        return keys;
    }

    public void llenarCombo(List<String> tables) {
        for (String table : tables) {
            cmbTablas.addItem(table);
        }
    }

    public void setModo(int modo) {
        // Common actions
        grupo.clearSelection();
        scroll.setVisible(true);
        panel.setVisible(true);

        if (modo == ComponenteHeader.MODO_CAPTURA) {
            rdModificar.setVisible(true);
            rdNuevo.setVisible(true);
            btnLimpiar.setVisible(true);
            btnGuardar.setVisible(true);
            btnBuscar.setVisible(false);
            btnEliminar.setVisible(false);
        } else if (modo == ComponenteHeader.MODO_CONSULTA) {
            rdModificar.setVisible(false);
            rdNuevo.setVisible(false);
            btnLimpiar.setVisible(false);
            btnGuardar.setVisible(false);
            btnBuscar.setVisible(true);
            btnEliminar.setVisible(true);
        }
    }

    public JButton getBtnLimpiar() {
        return btnLimpiar;
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JRadioButton getRdNuevo() {
        return rdNuevo;
    }

    public JRadioButton getRdModificar() {
        return rdModificar;
    }

    public JComboBox<String> getCmbTablas() {
        return cmbTablas;
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
        String font = "SegoeUI";
        // -----------------------------------------------------------
        cmbTablas.setBounds((int) (w * .025), (int) (h * .03),
                anchoComponente,
                altoComponente);
        cmbTablas.setFont(Rutinas2.getFont(font, false, 10, getWidth(), getHeight(), 350));

        rdModificar.setBounds(cmbTablas.getX() + cmbTablas.getWidth(), (int) (h * .05), (int) (w * .20),
                (int) (h * .45));
        rdModificar.setFont(Rutinas2.getFont(font, false, 10, getWidth(), getHeight(), 400));

        rdNuevo.setBounds(cmbTablas.getX() + cmbTablas.getWidth(), rdModificar.getY() + rdModificar.getHeight(),
                (int) (w * .20),
                (int) (h * .45));
        rdNuevo.setFont(Rutinas2.getFont(font, false, 10, getWidth(), getHeight(), 400));

        btnLimpiar.setBounds(rdModificar.getX() + rdModificar.getWidth(), (int) (h * .05), (int) (w * .35),
                (int) (h * .45));
        btnLimpiar.setFont(Rutinas2.getFont(font, false, 10, getWidth(), getHeight(), 400));

        btnGuardar.setBounds(rdModificar.getX() + rdModificar.getWidth(), btnLimpiar.getY() + btnLimpiar.getHeight(),
                (int) (w * .35),
                (int) (h * .45));
        btnGuardar.setFont(Rutinas2.getFont(font, false, 10, getWidth(), getHeight(), 400));

        btnEliminar.setBounds(cmbTablas.getX() + cmbTablas.getWidth() + (int) (w * .05), (int) (h * .05),
                (int) (w * .25),
                (int) (h * .85));
        btnEliminar.setFont(Rutinas2.getFont(font, false, 10, getWidth(), getHeight(), 400));

        btnBuscar.setBounds(btnEliminar.getX() + btnEliminar.getWidth(), (int) (h * .05),
                (int) (w * .25),
                (int) (h * .85));
        btnBuscar.setFont(Rutinas2.getFont(font, false, 10, getWidth(), getHeight(), 400));

        // -----------------------------------------------------------

        scroll.setBounds((int) (pnlTabla.getWidth() * .03), (int) (pnlTabla.getHeight() * .05),
                (int) (pnlTabla.getWidth() * .95), (int) (pnlTabla.getHeight() * .9));

    }

    @Override
    public void componentMoved(ComponentEvent e) {
        // This method is empty because we do not need to handle component moved events.
    }

    @Override
    public void componentShown(ComponentEvent e) {
        // This method is empty because we do not need to handle component shown events.
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        // This method is empty because we do not need to handle component hidden
        // events.
    }

    @Override
    public void itemStateChanged(ItemEvent evt) {
        if (evt.getSource() == cmbTablas && !selected) {
            cmbTablas.removeItem("Seleccione");
            selected = true;
        }
    }

}