package SSPP.dao;

import SSPP.model.Prision;
import SSPP.utils.DatabaseConnection;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PrisionDAOTest {

    private PrisionDAO prisionDAO;

@BeforeEach
void setUp() {
    // Inicializa la clase DAO antes de cada prueba
    prisionDAO = new PrisionDAO();
    // Inserta una prisión de prueba en la base de datos para verificar su existencia
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement("INSERT INTO Prision (Cod_prision, Nombre, Direccion, Capacidad) VALUES (?, ?, ?, ?)")) {
        stmt.setInt(1, 1); // Cod_prision
        stmt.setString(2, "Prision Test"); // Nombre
        stmt.setString(3, "Direccion Test"); // Dirección
        stmt.setInt(4, 100); // Capacidad
        stmt.executeUpdate();

        // Verifica si la prisión fue insertada
        try (PreparedStatement stmtCheck = conn.prepareStatement("SELECT * FROM Prision WHERE Nombre = ?")) {
            stmtCheck.setString(1, "Prision Test");
            try (ResultSet rs = stmtCheck.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Prisión insertada correctamente en el setUp()");
                } else {
                    System.err.println("Error: No se pudo insertar la prisión en el setUp()");
                }
            }
        }

    } catch (SQLException e) {
        System.err.println("Error al insertar datos de prueba: " + e.getMessage());
    }
}


    @Test
    void testObtenerTodasLasPrisiones() {
        // Intenta obtener todas las prisiones
        List<Prision> prisiones = prisionDAO.obtenerTodasLasPrisiones();
        assertNotNull(prisiones, "La lista de prisiones no debe ser nula");
        assertFalse(prisiones.isEmpty(), "La lista de prisiones debe contener al menos una prisión");
    }

    public Prision obtenerPrisionPorNombre(String nombre) {
        String query = "SELECT * FROM Prision WHERE Nombre = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nombre);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Prision(
                            resultSet.getInt("Cod_prision"),
                            resultSet.getString("Nombre"),
                            resultSet.getString("Direccion"),
                            resultSet.getInt("Capacidad")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener prisión por nombre: " + e.getMessage());
        }
        System.err.println("Advertencia: No se encontró una prisión con el nombre proporcionado: " + nombre);
        return null;
    }


    @AfterEach
    void tearDown() {
        // Limpia los datos insertados después de cada prueba para mantener la base de datos limpia
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM Prision WHERE Nombre = ?")) {
            stmt.setString(1, "Prision Test");
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al limpiar los datos de prueba: " + e.getMessage());
        }
    }
}
