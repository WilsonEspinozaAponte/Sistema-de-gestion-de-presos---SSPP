package SSPP.dao;

import SSPP.model.Sentencia;
import SSPP.utils.DatabaseConnection;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SentenciaDAOTest {

    private SentenciaDAO sentenciaDAO;
    private int idSentenciaInsertada;
    private int codRecluso = 1;  // Asegúrate de que el recluso con Cod_recluso = 1 exista para las pruebas.

    @BeforeEach
    void setUp() {
        // Inicializa la clase DAO antes de cada prueba
        sentenciaDAO = new SentenciaDAO();

        // Inserta una sentencia de prueba
        Sentencia sentencia = new Sentencia();
        sentencia.setCodRecluso(codRecluso);
        sentencia.setFechaSentencia(new Date());
        sentencia.setCondenaTotal(5.0);
        sentencia.setEstado("Actual");
        sentencia.setDetalles("Detalles de prueba");
        sentencia.setComentarios("Comentarios de prueba");

        try {
            idSentenciaInsertada = sentenciaDAO.insertarSentenciaYRetornarId(sentencia);
        } catch (SQLException e) {
            fail("Error al insertar sentencia de prueba en setUp: " + e.getMessage());
        }
    }

    @Test
    void testInsertarSentencia() {
        assertTrue(idSentenciaInsertada > 0, "El ID de la sentencia insertada debe ser mayor que cero.");
    }

    @Test
    void testObtenerSentenciasPorRecluso() {
        try {
            List<Sentencia> sentencias = sentenciaDAO.obtenerSentenciasPorRecluso(codRecluso);
            assertNotNull(sentencias, "La lista de sentencias no debe ser nula.");
            assertFalse(sentencias.isEmpty(), "La lista de sentencias debe contener al menos una sentencia.");
            // Asegura que la lista contenga la sentencia insertada en el setUp
            boolean contieneSentencia = sentencias.stream().anyMatch(s -> s.getCodSentencia() == idSentenciaInsertada);
            assertTrue(contieneSentencia, "La lista de sentencias debe contener la sentencia insertada.");
        } catch (SQLException e) {
            fail("Error al obtener sentencias por recluso: " + e.getMessage());
        }
    }

    @Test
    void testActualizarSentencia() {
        try {
            Sentencia sentencia = sentenciaDAO.obtenerSentenciasPorRecluso(codRecluso).stream()
                    .filter(s -> s.getCodSentencia() == idSentenciaInsertada)
                    .findFirst()
                    .orElse(null);
            assertNotNull(sentencia, "La sentencia obtenida no debe ser nula para actualizar.");

            // Actualiza algunos datos de la sentencia
            sentencia.setEstado("Cumplida");
            sentencia.setComentarios("Comentarios actualizados");
            sentenciaDAO.actualizarSentencia(sentencia);

            // Espera un breve momento para asegurarse de que los cambios tengan efecto
            Thread.sleep(500);

            // Verifica que los cambios se hayan aplicado
            Sentencia sentenciaActualizada = sentenciaDAO.obtenerSentenciasPorRecluso(codRecluso).stream()
                    .filter(s -> s.getCodSentencia() == idSentenciaInsertada)
                    .findFirst()
                    .orElse(null);
            assertNotNull(sentenciaActualizada, "La sentencia actualizada no debe ser nula.");
            assertEquals("Cumplida", sentenciaActualizada.getEstado(), "El estado de la sentencia debe haberse actualizado.");
            assertEquals("Comentarios actualizados", sentenciaActualizada.getComentarios(), "Los comentarios de la sentencia deben haberse actualizado.");
        } catch (SQLException | InterruptedException e) {
            fail("Error al actualizar la sentencia: " + e.getMessage());
        }
    }

    @Test
    void testEliminarSentencia() {
        try {
            sentenciaDAO.eliminarSentencia(idSentenciaInsertada);

            // Espera un breve momento para asegurarse de que los cambios tengan efecto
            Thread.sleep(500);

            // Verifica que la sentencia ya no exista
            List<Sentencia> sentencias = sentenciaDAO.obtenerSentenciasPorRecluso(codRecluso);
            boolean contieneSentencia = sentencias.stream().anyMatch(s -> s.getCodSentencia() == idSentenciaInsertada);
            assertFalse(contieneSentencia, "La sentencia debe haber sido eliminada.");
        } catch (SQLException | InterruptedException e) {
            fail("Error al eliminar la sentencia: " + e.getMessage());
        }
    }

    @Test
    void testTieneSentenciaActual() {
        try {
            boolean tieneSentenciaActual = sentenciaDAO.tieneSentenciaActual(codRecluso);
            assertTrue(tieneSentenciaActual, "El recluso debe tener una sentencia actual.");

            // Actualiza el estado de la sentencia para que no sea "Actual"
            Sentencia sentencia = sentenciaDAO.obtenerSentenciasPorRecluso(codRecluso).stream()
                    .filter(s -> s.getCodSentencia() == idSentenciaInsertada)
                    .findFirst()
                    .orElse(null);
            assertNotNull(sentencia, "La sentencia obtenida no debe ser nula para actualizar.");

            sentencia.setEstado("Cumplida");
            sentenciaDAO.actualizarSentencia(sentencia);

            // Espera un breve momento para asegurarse de que los cambios tengan efecto
            Thread.sleep(500);

            tieneSentenciaActual = sentenciaDAO.tieneSentenciaActual(codRecluso);
            assertFalse(tieneSentenciaActual, "El recluso no debe tener una sentencia actual.");
        } catch (SQLException | InterruptedException e) {
            fail("Error al comprobar si el recluso tiene sentencia actual: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        // Limpia los datos insertados después de cada prueba
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM Sentencia WHERE Cod_sentencia = ?")) {
            stmt.setInt(1, idSentenciaInsertada);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al limpiar los datos de prueba: " + e.getMessage());
        }
    }
}
