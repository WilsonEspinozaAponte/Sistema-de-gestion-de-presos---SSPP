package SSPP.utils;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest {

    @Test
    void testGetConnection() {
        try {
            // Obtener la conexión
            Connection connection = DatabaseConnection.getConnection();

            // Verificar que la conexión no sea nula y esté abierta
            assertNotNull(connection, "La conexión no debe ser nula.");
            assertFalse(connection.isClosed(), "La conexión debe estar abierta.");

        } catch (SQLException e) {
            fail("Error al obtener la conexión a la base de datos: " + e.getMessage());
        }
    }

    @Test
    void testCloseConnection() {
        try {
            // Cerrar la conexión
            DatabaseConnection.closeConnection();

            // Verificar que la conexión esté cerrada
            Connection connection = DatabaseConnection.getConnection();
            DatabaseConnection.closeConnection();
            assertTrue(connection.isClosed(), "La conexión debe estar cerrada después de llamar a closeConnection().");

        } catch (SQLException e) {
            fail("Error al cerrar la conexión a la base de datos: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        // Asegurarse de cerrar la conexión después de cada prueba
        DatabaseConnection.closeConnection();
    }
}
