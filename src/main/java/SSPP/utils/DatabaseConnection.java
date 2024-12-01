package SSPP.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Parámetros de conexión
    private static final String URL = "jdbc:mysql://baf8gl8eejgvb8fnxaks-mysql.services.clever-cloud.com:3306/baf8gl8eejgvb8fnxaks";
    private static final String USER = "uot41phminqtao4r";
    private static final String PASSWORD = "RGI6n019SKroezluAd3Z";

    // Instancia única de conexión
    private static Connection connection = null;

    // Constructor privado para evitar instanciación
    private DatabaseConnection() {
    }

    // Método para obtener la conexión
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            synchronized (DatabaseConnection.class) {
                if (connection == null || connection.isClosed()) {
                    try {
                        connection = DriverManager.getConnection(URL, USER, PASSWORD);
                    } catch (SQLException e) {
                        throw new SQLException("Error al conectar con la base de datos: " + e.getMessage());
                    }
                }
            }
        }
        return connection;
    }

    // Método para cerrar la conexión (opcional)
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}