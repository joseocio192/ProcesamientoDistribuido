package crud;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import conexion.ConexionDB;

public class ModeloBD {

    public void insertRecord(String tableName, Object[] values) throws SQLException {
        StringBuilder query = new StringBuilder("INSERT INTO " + tableName + " VALUES (");

        for (int i = 0; i < values.length; i++) {
            query.append("?");
            if (i < values.length - 1) {
                query.append(", ");
            }
        }
        query.append(")");

        try (PreparedStatement ps = ConexionDB.conexion.prepareStatement(query.toString())) {
            for (int i = 0; i < values.length; i++) {
                ps.setObject(i + 1, values[i]);
            }
            ps.executeUpdate();
        }
    }

    public void updateRecord(String tableName, Object[] values, Object[] keys) throws SQLException {
        StringBuilder query = new StringBuilder("UPDATE " + tableName + " SET ");

        for (int i = 0; i < values.length; i++) {
            query.append(keys[i] + " = ?");
            if (i < values.length - 1) {
                query.append(", ");
            }
        }
        query.append(" WHERE ");

        for (int i = 0; i < keys.length; i++) {
            query.append(keys[i] + " = ?");
            if (i < keys.length - 1) {
                query.append(" AND ");
            }
        }

        try (PreparedStatement ps = ConexionDB.conexion.prepareStatement(query.toString())) {
            for (int i = 0; i < values.length; i++) {
                ps.setObject(i + 1, values[i]);
            }
            for (int i = 0; i < keys.length; i++) {
                ps.setObject(i + values.length + 1, keys[i]);
            }
            ps.executeUpdate();
        }
    }

    public boolean deleteRecord(String tableName, String[] attribute, Object[] data) throws SQLException {
        StringBuilder query = new StringBuilder("DELETE FROM " + tableName);

        boolean hasWhereClause = false;
        for (int i = 0; i < data.length; i++) {
            if (data[i] != null) {
                if (!hasWhereClause) {
                    query.append(" WHERE ");
                    hasWhereClause = true;
                } else {
                    query.append(" AND ");
                }
                query.append(attribute[i] + " = ?");
            }
        }

        if (!hasWhereClause) {
            throw new SQLException("Especificacion de Atributos no proporcionados, Abortando");
        }

        try (PreparedStatement ps = ConexionDB.conexion.prepareStatement(query.toString())) {
            int paramIndex = 1;
            for (int i = 0; i < data.length; i++) {
                if (data[i] != null) {
                    ps.setObject(paramIndex++, data[i]);
                }
            }
            ps.executeUpdate();

            if (ps.getUpdateCount() > 0)
                return true;
        }
        return false;
    }

    public void searchRecord(String tableName, String[] attribute, Object[] data, DataProcessor processor)
            throws SQLException {
        Statement s = ConexionDB.conexion.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        s.setFetchSize(1000);

        StringBuilder query = new StringBuilder("SELECT * FROM " + tableName);

        boolean hasWhereClause = false;
        for (int i = 0; i < data.length; i++) {
            if (data[i] != null) {
                if (!hasWhereClause) {
                    query.append(" WHERE ");
                    hasWhereClause = true;
                } else {
                    query.append(" AND ");
                }
                query.append(attribute[i] + " LIKE ?");
            }
        }

        try (PreparedStatement ps = ConexionDB.conexion.prepareStatement(query.toString())) {
            int paramIndex = 1;
            for (int i = 0; i < data.length; i++) {
                if (data[i] != null) {
                    ps.setObject(paramIndex++, "%" + data[i] + "%"); // Set only non-null parameters
                }
            }

            ResultSet rs = ps.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    row[i] = rs.getObject(i + 1);
                }
                processor.processRow(row); // Process each row
            }
        }
    }

}
