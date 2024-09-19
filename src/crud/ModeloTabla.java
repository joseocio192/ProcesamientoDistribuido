package crud;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import conexion.ConexionDB;

public class ModeloTabla {

    public List<String> llenarComboTablas() throws SQLException {
        Statement s = ConexionDB.conexion.createStatement();
        ResultSet rs = s
                .executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE'");
        List<String> tables = new ArrayList<>();
        while (rs.next()) {
            tables.add(rs.getString("TABLE_NAME"));
        }
        return tables;
    }

    public static List<String> getColumnNames(String tableName) throws SQLException {
        List<String> columnNames = new ArrayList<>();
        Statement s = ConexionDB.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = s.executeQuery(
                "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = N'" + tableName + "'");

        while (rs.next()) {
            columnNames.add(rs.getString("COLUMN_NAME"));
        }
        return columnNames;
    }

    public static List<Object[]> getTableData(String tableName) throws SQLException { // unused
        List<Object[]> data = new ArrayList<>();
        Statement s = ConexionDB.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = s.executeQuery("SELECT * FROM " + tableName);

        int columnCount = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                row[i] = rs.getObject(i + 1);
            }
            data.add(row);
        }
        return data;
    }

    public static void streamTableData(String tableName, DataProcessor processor) throws SQLException { // preferred
        Statement s = ConexionDB.conexion.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        s.setFetchSize(1000);

        ResultSet rs = s.executeQuery("SELECT * FROM " + tableName);
        int columnCount = rs.getMetaData().getColumnCount();

        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                row[i] = rs.getObject(i + 1);
            }
            processor.processRow(row);
        }
    }
}
