package bulk_cleaning;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import conexion.ErrorHandler;

public class BulkInsertUtility {

    private BulkInsertUtility() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean bulkInsert(Connection conn, String csvFilePath, String tableName) {
        String bulkInsertQuery = "BULK INSERT " + tableName + " " +
                "FROM '" + csvFilePath + "' " +
                "WITH (" +
                "FIELDTERMINATOR = ',', " +
                "ROWTERMINATOR = '\\n', " +
                "FIRSTROW = 2, " + // Skip header row
                "BATCHSIZE = 10000);";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(bulkInsertQuery);
            return true;
        } catch (SQLException e) {
            ErrorHandler.showNotification(e.getLocalizedMessage());
            e.printStackTrace();
            return false;
        }
    }
}
