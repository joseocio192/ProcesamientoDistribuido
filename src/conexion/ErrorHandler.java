package conexion;

import java.sql.SQLException;
import java.util.logging.Logger;

import raven.toast.Notifications;

public class ErrorHandler {

    static Logger logger = Logger.getLogger(ErrorHandler.class.getName());;

    private ErrorHandler() {
    }

    public static void handleSqlException(SQLException e) {
        // Examinar el código de error SQL
        int errorCode = e.getErrorCode();

        logger.severe(errorCode + " - " + e.getMessage());
        switch (errorCode) {
            case 201:
                showNotification("Error: 201. Violación de restricción de clave foránea. TipID");
                break;
            case 547:
                // Check constraint violation
                showNotification("Error: 547. Violacion de restriccion(PK/FK) SQL.");
                break;
            case 8115:
                // El campo exede el limite de caracteres numerico
                showNotification("Error: 8115. El campo exede el maximo de caracteres numerico.");
                break;
            case 2628:
                // Excedió el límite de caracteres permitido
                showNotification("Error: 2628. El campo excede el límite de caracteres permitido.");
                break;
            case 230:
                // Error específico cuando está en modo modificar y la PK no es reconocida
                showNotification("Error: 230. No tienes permisos para seleccionar toda la tabla clientes");
                break;
            case 229:
                showNotification("Error: 229. No tiene permisos de usar procedimientos, seleccionar o eliminar");
                break;
            case 5000:
                showNotification("Error: 5000. " + e.getMessage());
                break;
            case 50000:
                // Insersion deshabilitada
                showNotification("Error: 50000. " + e.getMessage());
                break;
            case 168:
                showNotification("Error: 168. El campo exede el limite de almacenamiento caracteres numerico");
                break;
            case 515:
                showNotification("Error: 515. " + e.getLocalizedMessage());
                break;
            default:
                // Otro error SQL no manejado específicamente
                handleGenericSqlError(e);
                break;
        }
    }

    private static void handleGenericSqlError(SQLException e) {
        String errorMessage = "Error SQL no manejado específicamente: " + e.getLocalizedMessage();
        logger.severe(e.getErrorCode() + " - " + e.getLocalizedMessage());
        showNotification(errorMessage);
    }

    public static void showNotification(String message) {
        Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER, message);
    }
}
