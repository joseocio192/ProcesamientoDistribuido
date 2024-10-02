package bulk_cleaning;

import java.sql.Connection;

import javax.swing.JFileChooser;

public class ModeloBulkClean {

    String inputFileD = "limpiar_insertar_datos\\TicketD.CVS";
    String outputFileD = "limpiar_insertar_datos\\TicketDL.CSV";
    String inputFileH = "limpiar_insertar_datos\\TicketH.CVS";
    String outputFileH = "limpiar_insertar_datos\\TicketHL.CSV";
    Connection conn;

    public ModeloBulkClean(Connection conn) {
        this.conn = conn;
    }

    public String cleanData() {
        DataCleaning dataCleaning = new DataCleaning();
        String result = dataCleaning.cleanDataD(inputFileD, outputFileD);
        result += " - " + dataCleaning.cleanDataH(inputFileH, outputFileH);
        return result;
    }

    public boolean bulkInsert(String table) {
        deleteData(table);
        return BulkInsertUtility.bulkInsert(conn, openFile(), table);
    }

    public void deleteData(String table) {
        try {
            conn.createStatement().execute("DELETE FROM " + table);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String openFile() {
        JFileChooser fileChooser = new JFileChooser();
        byte result = (byte) fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION)
            return fileChooser.getSelectedFile().getAbsolutePath();

        return null;
    }

}
