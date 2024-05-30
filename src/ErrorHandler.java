import java.sql.SQLException;

import javax.swing.JOptionPane;

public class ErrorHandler {

    public static void handleSqlException(SQLException e) {
        // Examinar el código de error SQL
        int errorCode = e.getErrorCode();

        // Personalizar mensajes de error según el código de error SQL
        switch (errorCode) {
            case 547:
                // Check constraint violation
                showNotification("Violacion de restriccion SQL.", "Error: 547.", JOptionPane.ERROR_MESSAGE);
                break;
            case 8115:
                // El campo exede el limite de caracteres numerico
                showNotification("El campo exede el maximo de caracteres numerico.", "Error: 8115.",
                        JOptionPane.ERROR_MESSAGE);
                break;
            case 2628:
                // Excedió el límite de caracteres permitido
                showNotification("El campo excede el límite de caracteres permitido.", "Error: 2628.",
                        JOptionPane.ERROR_MESSAGE);
                break;
            case 230:
                // Error específico cuando está en modo modificar y la PK no es reconocida
                showNotification("No tienes permisos para seleccionar toda la tabla clientes", "Error: 230.",
                        JOptionPane.ERROR_MESSAGE);
                break;
            case 229:
                showNotification("No tiene permisos de usar procedimientos, seleccionar o eliminar", "Error: 229.",
                        JOptionPane.ERROR_MESSAGE);
                break;
            case 5000:
                showNotification(e.getMessage(), "Error: 5000.", JOptionPane.ERROR_MESSAGE);
                break;
            case 50000:
                // Insersion deshabilitada
                showNotification(e.getMessage(), "Error: 50000.", JOptionPane.ERROR_MESSAGE);
                break;
            case 168:
                showNotification("El campo exede el limite de almacenamiento caracteres numerico", "Error: 168.",
                        JOptionPane.ERROR_MESSAGE);
                break;
            default:
                // Otro error SQL no manejado específicamente
                handleGenericSqlError(e);
                break;
        }
    }

    private static void handleGenericSqlError(SQLException e) {
        String errorMessage = "Error SQL no manejado específicamente: " + e.getMessage();
        showNotification(errorMessage, "Error SQL", JOptionPane.ERROR_MESSAGE);
    }

    public static void showNotification(String message, String title, int messageType) {
        System.out.println("Mostrar notificación de error en el centro: " + message);
        JOptionPane.showMessageDialog(null, message, title, messageType);
        // Notifications.getInstance().show(Notifications.Type.ERROR,
        // Notifications.Location.TOP_CENTER, message);
    }
}
