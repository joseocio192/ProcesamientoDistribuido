package src;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLDatabaseConnection {
    public static void main(String[] args) {
        String connectionUrl =
                "jdbc:sqlserver://localhost:1433;" // Cambia 'localhost' por el nombre de tu servidor
                        + "database=Empresa;" // Nombre de la base de datos
                        + "integratedSecurity=true;" // Usar autenticación integrada de Windows
                        + "encrypt=true;" // Encriptación de la conexión
                        + "trustServerCertificate=true;" // Confiar en el certificado del servidor
                        + "loginTimeout=30;"; // Tiempo de espera para el inicio de sesión

        try (Connection connection = DriverManager.getConnection(connectionUrl);) {
            System.out.println("Connected to SQL Database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}