package menu_suma_pd;

import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.util.List;
import java.awt.event.ActionEvent;

import conexion.ErrorHandler;
import crud.ModeloTabla;

public class ControladorSuma implements ActionListener {

    private MenuSuma vista;
    private ModeloSuma modelo;

    public ControladorSuma(MenuSuma vista, ModeloSuma modelo) {
        this.vista = vista;
        this.modelo = modelo;
        llenarComboTablas();
        listeners();
        vista.setVisible(true);
    }

    private void llenarComboTablas() {
        try {
            List<String> tablas = modelo.llenarComboTablas();
            vista.llenarCombo(tablas, vista.getTableDropdown());
        } catch (SQLException e) {
            ErrorHandler.handleSqlException(e);
        }
    }

    private void listeners() {
        vista.getBtnPrecioMasUno().addActionListener(this);
        vista.getBtnLimpiar().addActionListener(this);
        vista.getRdEstado().addActionListener(this);
        vista.getRdCiudad().addActionListener(this);
        vista.getRdTienda().addActionListener(this);
        vista.getTableDropdown().addActionListener(this);
        vista.getPriceDropdown().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == vista.getTableDropdown()) {
            vista.getRdEstado().setEnabled(true);
            vista.getRdCiudad().setEnabled(true);
            vista.getRdTienda().setEnabled(true);

            String table = vista.getTableDropdown().getSelectedItem().toString();
            try {
                List<String> columnNames = ModeloTabla.getColumnNames(table);

                vista.setColumnNames(columnNames);
                ModeloTabla.streamTableData(table, (Object[] row) -> vista.addRowToTable(row));
            } catch (SQLException e) {
                ErrorHandler.showNotification("Error: Carga los datos de la tabla" + e);
            }

            return;
        }

        if (evt.getSource() == vista.getBtnPrecioMasUno()) {
            String atributo = "";
            if (vista.getRdEstado().isSelected()) {
                atributo = vista.getRdEstado().getText();
            }
            if (vista.getRdCiudad().isSelected()) {
                atributo = vista.getRdCiudad().getText();
            }
            if (vista.getRdTienda().isSelected()) {
                atributo = vista.getRdTienda().getText();
            }

            String table = vista.getTableDropdown().getSelectedItem().toString();
            String valor = vista.getPriceDropdown().getSelectedItem().toString();
            int dialogResult = JOptionPane.showConfirmDialog(vista,
                    "¿Está seguro de sumar uno al precio de la tabla: " + table
                            + ",  Atributo: " + atributo
                            + ", Valor: " + valor + "?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (dialogResult != JOptionPane.YES_OPTION) {
                return;
            }

            sumarUno(vista.getLblAtributoFiltro().getText(), valor);

        }
        if (evt.getSource() == vista.getRdEstado()) {
            JLabel lbl = vista.getLblAtributoFiltro();
            vista.getBtnPrecioMasUno().setEnabled(true);
            lbl.setText("IDESTADO");
            lbl.setVisible(true);

            String tabla = vista.getTableDropdown().getSelectedItem().toString();
            try {
                List<String> fk = modelo.llenarComboFK(tabla, "IDESTADO");
                vista.llenarCombo(fk, vista.getPriceDropdown());
            } catch (SQLException e) {
                ErrorHandler.handleSqlException(e);
            }
            vista.getPriceDropdown().setVisible(true);
        }
        if (evt.getSource() == vista.getRdCiudad()) {
            vista.getBtnPrecioMasUno().setEnabled(true);
            vista.getLblAtributoFiltro().setText("IDCIUDAD");

            String tabla = vista.getTableDropdown().getSelectedItem().toString();
            try {
                List<String> fk = modelo.llenarComboFK(tabla, "IDCIUDAD");
                vista.llenarCombo(fk, vista.getPriceDropdown());
            } catch (SQLException e) {
                ErrorHandler.handleSqlException(e);
            }

            vista.getPriceDropdown().setVisible(true);
        }
        if (evt.getSource() == vista.getRdTienda()) {
            vista.getBtnPrecioMasUno().setEnabled(true);
            vista.getLblAtributoFiltro().setText("IDTIENDA");

            String tabla = vista.getTableDropdown().getSelectedItem().toString();
            try {
                List<String> fk = modelo.llenarComboFK(tabla, "IDTIENDA");
                vista.llenarCombo(fk, vista.getPriceDropdown());
            } catch (SQLException e) {
                ErrorHandler.handleSqlException(e);
            }

            vista.getPriceDropdown().setVisible(true);
        }
    }

    private void sumarUno(String atributo, String valor) {
        try {
            // if (sumarUnoPrecio() == 0) {
            // ErrorHandler.showNotification("No se pudo sumar uno al precio");
            // break;
            // } else {
            // ErrorHandler.showNotification("Se sumó uno al precio");
            // }
            for (int i = 0; i < 200; i++) {
                System.out.println("SUMANDO UNO");
                int rowsAffected = modelo.actualizarPrecioEnUno(atributo, valor);
                if (rowsAffected == 0) {
                    ErrorHandler.showNotification("No se pudo sumar uno al precio");
                    break;
                } else {
                    ErrorHandler.showNotification("Se sumó uno al precio");
                }
            }

        } catch (SQLException e) {
            ErrorHandler.showNotification("Error: " + e.getLocalizedMessage());
        }
    }
}
