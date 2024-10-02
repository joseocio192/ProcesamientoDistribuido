package bulk_cleaning;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import conexion.ErrorHandler;

public class ControladorBulkCleaning implements ActionListener {

    private VistaCleaning vista;
    private ModeloBulkClean modelo;

    public ControladorBulkCleaning(VistaCleaning vista, ModeloBulkClean modelo) {
        this.vista = vista;
        this.modelo = modelo;
        listeners();
    }

    private void listeners() {
        vista.getBtnCleanData().addActionListener(this);
        vista.getBtnInsertDataH().addActionListener(this);
        vista.getBtnInsertDataD().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnCleanData()) {
            vista.getBtnCleanData().setEnabled(false);
            String result = modelo.cleanData();
            System.out.println(result);
            return;
        }
        if (e.getSource() == vista.getBtnInsertDataH()) {
            showConfirmation("TicketsH");
            boolean result = modelo.bulkInsert("TicketsH");
            if (result)
                ErrorHandler.showNotification("Datos insertados correctamente en TicketsH");
            return;
        }
        if (e.getSource() == vista.getBtnInsertDataD()) {
            showConfirmation("TicketsD");
            boolean result = modelo.bulkInsert("TicketsD");
            if (result)
                ErrorHandler.showNotification("Datos insertados correctamente en TicketsD");
        }
    }

    private void showConfirmation(String table) {
        JOptionPane.showMessageDialog(vista, "Seleccione el archivo CSV para insertar los datos", "Insert en " + table,
                JOptionPane.INFORMATION_MESSAGE);
        boolean result = modelo.bulkInsert(table);
    }
}
