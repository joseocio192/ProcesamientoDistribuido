package crud;

import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import conexion.ErrorHandler;

import java.awt.event.ActionEvent;

public class ControladorCrud implements ActionListener {

    private MenuCrudVista vista;
    private ModeloTabla modelo;
    private ModeloBD modeloBD;

    public ControladorCrud(MenuCrudVista vista, ModeloTabla modelo, ModeloBD modeloBD) {
        this.vista = vista;
        this.modelo = modelo;
        this.modeloBD = modeloBD;
        listeners();
        llenarComboTablas();
        vista.setVisible(true);
    }

    private void listeners() {
        vista.getBtnGuardar().addActionListener(this);
        vista.getBtnLimpiar().addActionListener(this);
        vista.getBtnBuscar().addActionListener(this);
        vista.getBtnEliminar().addActionListener(this);
        vista.getRdModificar().addActionListener(this);
        vista.getRdNuevo().addActionListener(this);
        vista.getCmbTablas().addActionListener(this);
    }

    private void llenarComboTablas() {
        try {
            List<String> tables = modelo.llenarComboTablas();
            vista.llenarCombo(tables);
        } catch (SQLException e) {
            ErrorHandler.showNotification("Error: No se pudo llenar el combo de tablas" + e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == vista.getCmbTablas()) {
            vista.getRdModificar().setEnabled(true);
            vista.getRdNuevo().setEnabled(true);
            vista.getBtnBuscar().setEnabled(true);
            vista.getBtnEliminar().setEnabled(true);
            vista.clearTable();

            try {
                String tableName = vista.getCmbTablas().getSelectedItem().toString();
                List<String> columnNames = ModeloTabla.getColumnNames(tableName);

                vista.setColumnNames(columnNames);
                ModeloTabla.streamTableData(tableName, (Object[] row) -> vista.addRowToTable(row));
            } catch (SQLException e) {
                ErrorHandler.showNotification("Error: Carga los datos de la tabla" + e);
            }
            return;
        }
        if (evt.getSource() == vista.getBtnLimpiar()) {
            vista.limpiar();
            return;
        }
        if (evt.getSource() == vista.getBtnGuardar()) {
            modificar();
            return;
        }
        if (evt.getSource() == vista.getRdModificar()) {
            vista.getBtnGuardar().setEnabled(true);
            return;
        }
        if (evt.getSource() == vista.getRdNuevo()) {
            vista.getBtnGuardar().setEnabled(true);
            return;
        }
        if (evt.getSource() == vista.getBtnBuscar()) {
            buscar();
            return;
        }
        if (evt.getSource() == vista.getBtnEliminar()) {
            eliminar();
        }
    }

    private void eliminar() {
        int dialogResult = JOptionPane.showConfirmDialog(vista, "¿Está seguro de eliminar el registro?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (dialogResult != JOptionPane.YES_OPTION)
            return;

        try {
            String tableName = vista.getCmbTablas().getSelectedItem().toString();
            String[] attributes = vista.getAttributes();
            Object[] data = vista.getInputData();
            boolean deletedRecord = modeloBD.deleteRecord(tableName, attributes, data);
            if (deletedRecord) {
                ErrorHandler.showNotification("Registro eliminado con éxito");
                vista.clearTable();
                modelo.streamTableData(tableName, (Object[] row) -> vista.addRowToTable(row));
            } else {
                ErrorHandler.showNotification("No se encontró eliminar el registro");
            }
        } catch (SQLException e) {
            ErrorHandler.handleSqlException(e);
        }
    }

    private void buscar() {
        vista.clearTable();
        try {
            String tableName = vista.getCmbTablas().getSelectedItem().toString();
            String[] attributes = vista.getAttributes();
            Object[] data = vista.getInputData();
            modeloBD.searchRecord(tableName, attributes, data, (Object[] row) -> vista.addRowToTable(row));
        } catch (SQLException e) {
            ErrorHandler.handleSqlException(e);
        }
    }

    private void modificar() {
        String tableName = vista.getCmbTablas().getSelectedItem().toString();
        if (vista.getRdNuevo().isSelected()) {
            try {
                Object[] values = vista.getInputData();
                modeloBD.insertRecord(tableName, values);
                ErrorHandler.showNotification("Registro guardado con éxito");
            } catch (SQLException e) {
                ErrorHandler.handleSqlException(e);
            }
        } else if (vista.getRdModificar().isSelected()) {
            // pendiente
        }
    }

}
