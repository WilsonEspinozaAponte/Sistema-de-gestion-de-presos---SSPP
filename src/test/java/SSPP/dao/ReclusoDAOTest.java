package SSPP.dao;

import SSPP.model.Recluso;
import SSPP.utils.DatabaseConnection;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReclusoDAOTest {

    private ReclusoDAO reclusoDAO;
    private int idReclusoInsertado;

    @BeforeEach
    void setUp() {
        // Inicializa la clase DAO antes de cada prueba
        reclusoDAO = new ReclusoDAO();
        
        // Inserta un recluso de prueba
        Recluso recluso = new Recluso();
        recluso.setNombre("Juan");
        recluso.setApellido("Perez");
        recluso.setFechaNacimiento(new Date());
        recluso.setCodPrision(1);  // Asegura que el código de prisión exista
        recluso.setConducta(80);
        recluso.setTipoDocumento("DNI");
        recluso.setNumeroDocumento("12345678");
        recluso.setNacionalidad("Peruano");

        try {
            idReclusoInsertado = reclusoDAO.insertarRecluso(recluso);
        } catch (SQLException e) {
            fail("Error al insertar recluso de prueba en setUp: " + e.getMessage());
        }
    }

    @Test
    void testInsertarRecluso() {
        assertTrue(idReclusoInsertado > 0, "El ID del recluso insertado debe ser mayor que cero.");
    }

    @Test
    void testObtenerTodosLosReclusos() {
        try {
            List<Recluso> reclusos = reclusoDAO.obtenerTodosLosReclusos();
            assertNotNull(reclusos, "La lista de reclusos no debe ser nula.");
            assertFalse(reclusos.isEmpty(), "La lista de reclusos debe contener al menos un recluso.");
        } catch (SQLException e) {
            fail("Error al obtener todos los reclusos: " + e.getMessage());
        }
    }

    @Test
    void testObtenerReclusoPorId() {
        try {
            Recluso recluso = reclusoDAO.obtenerReclusoPorId(idReclusoInsertado);
            assertNotNull(recluso, "El recluso obtenido no debe ser nulo.");
            assertEquals("Juan", recluso.getNombre(), "El nombre del recluso debe coincidir.");
            assertEquals("Perez", recluso.getApellido(), "El apellido del recluso debe coincidir.");
        } catch (SQLException e) {
            fail("Error al obtener recluso por ID: " + e.getMessage());
        }
    }

    @Test
    void testActualizarRecluso() {
        try {
            Recluso recluso = reclusoDAO.obtenerReclusoPorId(idReclusoInsertado);
            assertNotNull(recluso, "El recluso obtenido no debe ser nulo para actualizar.");
            
            // Actualiza algunos datos del recluso
            recluso.setNombre("Carlos");
            recluso.setApellido("Gonzalez");
            recluso.setConducta(90);
            reclusoDAO.actualizarRecluso(recluso);

            // Verifica que los cambios se hayan aplicado
            Recluso reclusoActualizado = reclusoDAO.obtenerReclusoPorId(idReclusoInsertado);
            assertEquals("Carlos", reclusoActualizado.getNombre(), "El nombre del recluso debe haberse actualizado.");
            assertEquals("Gonzalez", reclusoActualizado.getApellido(), "El apellido del recluso debe haberse actualizado.");
            assertEquals(90, reclusoActualizado.getConducta(), "La conducta del recluso debe haberse actualizado.");
        } catch (SQLException e) {
            fail("Error al actualizar el recluso: " + e.getMessage());
        }
    }

    @Test
    void testEliminarRecluso() {
        try {
            reclusoDAO.eliminarRecluso(idReclusoInsertado);

            // Verifica que el recluso ya no exista
            Recluso reclusoEliminado = reclusoDAO.obtenerReclusoPorId(idReclusoInsertado);
            assertNull(reclusoEliminado, "El recluso debe haber sido eliminado.");
        } catch (SQLException e) {
            fail("Error al eliminar el recluso: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        // Limpia los datos insertados después de cada prueba
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM Recluso WHERE Cod_recluso = ?")) {
            stmt.setInt(1, idReclusoInsertado);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al limpiar los datos de prueba: " + e.getMessage());
        }
    }
}
