package SSPP.dao;

import SSPP.model.Taller;
import SSPP.utils.DatabaseConnection;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TallerDAOTest {

    private TallerDAO tallerDAO;
    private int idTallerInsertado;

    @BeforeEach
    void setUp() {
        // Inicializa la clase DAO antes de cada prueba
        tallerDAO = new TallerDAO();

        // Inserta un taller de prueba
        Taller taller = new Taller();
        taller.setNombre("Taller de Prueba");
        taller.setDescripcion("Descripción de Prueba");
        taller.setFechaInicio(new Date());
        taller.setFechaFin(new Date(System.currentTimeMillis() + 86400000L)); // Fecha fin un día después
        taller.setCapacidad(20);

        try {
            tallerDAO.insertarTaller(taller);

            // Obtener el ID del taller insertado para usar en las pruebas
            List<Taller> talleres = tallerDAO.obtenerTodosLosTalleres();
            for (Taller t : talleres) {
                if (t.getNombre().equals("Taller de Prueba")) {
                    idTallerInsertado = t.getCodTaller();
                    break;
                }
            }

            assertTrue(idTallerInsertado > 0, "El ID del taller insertado debe ser mayor que cero.");
        } catch (SQLException e) {
            fail("Error al insertar taller de prueba en setUp: " + e.getMessage());
        }
    }

    @Test
    void testInsertarTaller() {
        assertTrue(idTallerInsertado > 0, "El ID del taller insertado debe ser mayor que cero.");
    }

    @Test
    void testObtenerTodosLosTalleres() {
        List<Taller> talleres = tallerDAO.obtenerTodosLosTalleres();
        assertNotNull(talleres, "La lista de talleres no debe ser nula.");
        assertFalse(talleres.isEmpty(), "La lista de talleres debe contener al menos un taller.");
        boolean contieneTaller = talleres.stream().anyMatch(t -> t.getCodTaller() == idTallerInsertado);
        assertTrue(contieneTaller, "La lista de talleres debe contener el taller insertado.");
    }

    @Test
    void testObtenerTallerPorId() {
        Taller taller = tallerDAO.obtenerTallerPorId(idTallerInsertado);
        assertNotNull(taller, "El taller obtenido no debe ser nulo.");
        assertEquals("Taller de Prueba", taller.getNombre(), "El nombre del taller debe coincidir.");
        assertEquals("Descripción de Prueba", taller.getDescripcion(), "La descripción del taller debe coincidir.");
    }

    @Test
    void testActualizarTaller() {
        try {
            Taller taller = tallerDAO.obtenerTallerPorId(idTallerInsertado);
            assertNotNull(taller, "El taller obtenido no debe ser nulo para actualizar.");

            // Actualiza algunos datos del taller
            taller.setNombre("Taller Actualizado");
            taller.setCapacidad(30);
            tallerDAO.actualizarTaller(taller);

            // Verifica que los cambios se hayan aplicado
            Taller tallerActualizado = tallerDAO.obtenerTallerPorId(idTallerInsertado);
            assertNotNull(tallerActualizado, "El taller actualizado no debe ser nulo.");
            assertEquals("Taller Actualizado", tallerActualizado.getNombre(), "El nombre del taller debe haberse actualizado.");
            assertEquals(30, tallerActualizado.getCapacidad(), "La capacidad del taller debe haberse actualizado.");
        } catch (SQLException e) {
            fail("Error al actualizar el taller: " + e.getMessage());
        }
    }

    @Test
    void testEliminarTaller() {
        try {
            tallerDAO.eliminarTaller(idTallerInsertado);

            // Verifica que el taller ya no exista
            Taller tallerEliminado = tallerDAO.obtenerTallerPorId(idTallerInsertado);
            assertNull(tallerEliminado, "El taller debe haber sido eliminado.");
        } catch (SQLException e) {
            fail("Error al eliminar el taller: " + e.getMessage());
        }
    }

    @Test
    void testExisteTallerActivoConNombre() {
        boolean existe = tallerDAO.existeTallerActivoConNombre("Taller de Prueba");
        assertTrue(existe, "El taller activo debería existir con el nombre proporcionado.");

        try {
            // Actualiza la fecha de fin para que el taller no esté activo
            Taller taller = tallerDAO.obtenerTallerPorId(idTallerInsertado);
            taller.setFechaFin(new Date(System.currentTimeMillis() - 86400000L)); // Fecha de fin un día antes
            tallerDAO.actualizarTaller(taller);

            // Verifica nuevamente
            existe = tallerDAO.existeTallerActivoConNombre("Taller de Prueba");
            assertFalse(existe, "El taller no debería estar activo con el nombre proporcionado.");
        } catch (SQLException e) {
            fail("Error al comprobar la existencia del taller activo: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        // Limpia los datos insertados después de cada prueba
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM Taller WHERE Cod_taller = ?")) {
            stmt.setInt(1, idTallerInsertado);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al limpiar los datos de prueba: " + e.getMessage());
        }
    }
}
