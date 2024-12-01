package SSPP.dao;

import SSPP.model.CrimenSentencia;
import SSPP.utils.DatabaseConnection;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class CrimenSentenciaDAOTest {

    private CrimenSentenciaDAO crimenSentenciaDAO;

    @BeforeEach
    void setUp() {
        // Inicializa la clase DAO antes de cada prueba
        crimenSentenciaDAO = new CrimenSentenciaDAO();
    }

    @Test
    void testInsertarCrimenSentencia() {
        // Crea un objeto CrimenSentencia para probar la inserción
        CrimenSentencia crimenSentencia = new CrimenSentencia();
        crimenSentencia.setCodSentencia(1); // Suponiendo que existe una sentencia con Cod_sentencia = 1
        crimenSentencia.setNombreCrimen("Robo");
        crimenSentencia.setDescripcion("Robo a mano armada");
        crimenSentencia.setCondenaCrimen(5.0);

        // Intenta insertar el crimen y luego verifica si fue insertado correctamente
        try {
            crimenSentenciaDAO.insertarCrimenSentencia(crimenSentencia);

            // Verifica si el crimen fue insertado en la base de datos
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Crimen_Sentencia WHERE Cod_sentencia = ? AND Nombre_crimen = ?")) {
                stmt.setInt(1, crimenSentencia.getCodSentencia());
                stmt.setString(2, crimenSentencia.getNombreCrimen());
                try (ResultSet rs = stmt.executeQuery()) {
                    assertTrue(rs.next(), "El crimen debería haber sido insertado correctamente en la base de datos");
                    assertEquals(crimenSentencia.getDescripcion(), rs.getString("Descripcion"), "La descripción debería coincidir");
                    assertEquals(crimenSentencia.getCondenaCrimen(), rs.getDouble("Condena_crimen"), "La condena debería coincidir");
                }
            }

        } catch (SQLException e) {
            fail("La inserción del crimen falló debido a un error SQL: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        // Limpia los datos insertados después de cada prueba para mantener la base de datos limpia
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM Crimen_Sentencia WHERE Nombre_crimen = ?")) {
            stmt.setString(1, "Robo");
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al limpiar los datos de prueba: " + e.getMessage());
        }
    }
}
