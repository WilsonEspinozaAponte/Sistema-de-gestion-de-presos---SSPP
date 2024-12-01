package SSPP.dao;

import SSPP.model.Matricula;
import SSPP.model.Recluso;
import SSPP.model.Taller;
import SSPP.utils.DatabaseConnection;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MatriculaDAOTest {

    private MatriculaDAO matriculaDAO;
    private int idMatriculaInsertada;

    @BeforeEach
    void setUp() {
        // Inicializa la clase DAO antes de cada prueba
        matriculaDAO = new MatriculaDAO();

        // Inserta una matrícula de prueba
        Matricula matricula = new Matricula();
        matricula.setCodRecluso(1); // Suponiendo que el recluso con Cod_recluso = 1 existe
        matricula.setCodTaller(1);  // Suponiendo que el taller con Cod_taller = 1 existe
        matricula.setFechaMatricula(new Date());

        try {
            matriculaDAO.insertarMatricula(matricula);

            // Obtener el ID de la matrícula insertada
            List<Matricula> matriculas = matriculaDAO.obtenerTodasLasMatriculas();
            for (Matricula m : matriculas) {
                if (m.getCodRecluso() == 1 && m.getCodTaller() == 1) {
                    idMatriculaInsertada = m.getCodMatricula();
                    break;
                }
            }

            assertTrue(idMatriculaInsertada > 0, "El ID de la matrícula insertada debe ser mayor que cero.");
        } catch (SQLException e) {
            fail("Error al insertar matrícula de prueba en setUp: " + e.getMessage());
        }
    }

    @Test
    void testObtenerTodasLasMatriculas() {
        List<Matricula> matriculas = matriculaDAO.obtenerTodasLasMatriculas();
        assertNotNull(matriculas, "La lista de matrículas no debe ser nula.");
        assertFalse(matriculas.isEmpty(), "La lista de matrículas debe contener al menos una matrícula.");
        boolean contieneMatricula = matriculas.stream().anyMatch(m -> m.getCodMatricula() == idMatriculaInsertada);
        assertTrue(contieneMatricula, "La lista de matrículas debe contener la matrícula insertada.");
    }

    @Test
    void testTallerEstaLleno() {
        try {
            // Verificar la capacidad del taller
            TallerDAO tallerDAO = new TallerDAO();
            Taller taller = tallerDAO.obtenerTallerPorId(1);
            assertNotNull(taller, "El taller obtenido no debe ser nulo.");
            int capacidadTaller = taller.getCapacidad();
            System.out.println("Capacidad del taller: " + capacidadTaller);

            // Insertar reclusos adicionales para llenar el taller
            ReclusoDAO reclusoDAO = new ReclusoDAO();
            for (int i = 0; i < capacidadTaller; i++) {
                Recluso recluso = new Recluso();
                recluso.setNombre("Recluso " + (i + 2));
                recluso.setApellido("Prueba");
                recluso.setFechaNacimiento(new Date());
                recluso.setCodPrision(1);  // Suponiendo que la prisión con Cod_prision = 1 existe
                recluso.setConducta(80);
                recluso.setTipoDocumento("DNI");
                recluso.setNumeroDocumento("1234567" + i);
                recluso.setNacionalidad("Peruano");

                int idRecluso = reclusoDAO.insertarRecluso(recluso);
                assertTrue(idRecluso > 0, "El ID del recluso insertado debe ser mayor que cero.");

                // Insertar matrícula para cada recluso
                Matricula matricula = new Matricula();
                matricula.setCodRecluso(idRecluso);
                matricula.setCodTaller(1);
                matricula.setFechaMatricula(new Date());

                matriculaDAO.insertarMatricula(matricula);
            }

            // Verificar si el taller está lleno
            boolean tallerLleno = matriculaDAO.tallerEstaLleno(1);
            assertTrue(tallerLleno, "El taller debería estar lleno.");
        } catch (SQLException e) {
            fail("Error al comprobar si el taller está lleno: " + e.getMessage());
        }
    }



    @AfterEach
    void tearDown() {
        // Limpia los datos insertados después de cada prueba
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM Matricula WHERE Cod_matricula = ?")) {
            stmt.setInt(1, idMatriculaInsertada);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al limpiar los datos de prueba: " + e.getMessage());
        }
    }
}
