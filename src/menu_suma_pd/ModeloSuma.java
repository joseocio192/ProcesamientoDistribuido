package menu_suma_pd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import conexion.ErrorHandler;

import conexion.ConexionDB;

public class ModeloSuma {
    static final String TICKETSD = "TICKETSD";
    static final String TICKETSH = "TICKETSH";

    public List<String> llenarComboTablas() throws SQLException {
        Statement s = ConexionDB.conexion.createStatement();
        ResultSet rs = s
                .executeQuery(
                        "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = N'" + TICKETSD + "'");
        List<String> tables = new ArrayList<>();
        while (rs.next()) {
            tables.add(rs.getString("TABLE_NAME"));
        }
        return tables;
    }

    public List<String> llenarComboFK(String tabla, String filtro) throws SQLException {
        List<String> fk = new ArrayList<>();
        Statement s = ConexionDB.conexion.createStatement();
        ResultSet rs = s
                .executeQuery(
                        "SELECT distinct(" + filtro + ") FROM " + tabla + " D "
                                + " inner join " + TICKETSH + " H on " + "D.TICKET = H.TICKET order by " + filtro);
        while (rs.next()) {
            fk.add(rs.getString(filtro));
        }
        return fk;
    }

    public int actualizarPrecioEnUno(String filterAttribute, String filterValue) throws SQLException {
        boolean success = false;
        int rowsAffected = 0;

        while (!success) {
            try (Statement s = ConexionDB.conexion.createStatement()) {
                s.executeUpdate("BEGIN transaction");

                String query = "UPDATE TICKETSD " +
                        "SET TICKETSD.PRECIO = TICKETSD.PRECIO + 1 " +
                        "FROM TICKETSD " +
                        "INNER JOIN TICKETSH ON TICKETSD.TICKET = TICKETSH.TICKET " +
                        "WHERE TICKETSH." + filterAttribute + " = " + filterValue +
                        " AND (SELECT COUNT(DISTINCT TICKETSD.IDPRODUCTO) " +
                        "     FROM TICKETSD " +
                        "     WHERE TICKETSD.TICKET = TICKETSH.TICKET) >= 3";

                rowsAffected = s.executeUpdate(query);
                s.executeUpdate("commit transaction");
                success = true;
            } catch (SQLException e) {
                if (e.getErrorCode() == 1205) { // Deadlock error
                    ErrorHandler.showNotification("Error: " + e.getMessage());
                } else {
                    ErrorHandler.handleSqlException(e);
                    success = true;
                }
            }
        }
        return rowsAffected;
    }

}
