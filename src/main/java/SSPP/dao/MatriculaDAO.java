package SSPP.dao;

import SSPP.model.Matricula;
import SSPP.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatriculaDAO {

    public List<Matricula> obtenerTodasLasMatriculas() {
        List<Matricula> lista = new ArrayList<>();
        String query = "SELECT * FROM Matricula";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Matricula matricula = new Matricula(
                    resultSet.getInt("Cod_matricula"),
                    resultSet.getInt("Cod_recluso"),
                    resultSet.getInt("Cod_taller"),
                    resultSet.getDate("Fecha_matricula")
                );
                lista.add(matricula);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener matrículas: " + e.getMessage());
        }

        return lista;
    }

    // Insertar una matrícula
    public void insertarMatricula(Matricula matricula) throws SQLException {
        String sql = "INSERT INTO Matricula (Cod_recluso, Cod_taller, Fecha_matricula) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, matricula.getCodRecluso());
            statement.setInt(2, matricula.getCodTaller());
            statement.setDate(3, new java.sql.Date(matricula.getFechaMatricula().getTime()));
            statement.executeUpdate();
        }
    }

    // Verificar si el taller está lleno
    public boolean tallerEstaLleno(int codTaller) {
        String query = "SELECT COUNT(*) AS inscritos, t.Capacidad " +
                       "FROM Matricula m " +
                       "JOIN Taller t ON m.Cod_taller = t.Cod_taller " +
                       "WHERE m.Cod_taller = ? " +
                       "GROUP BY t.Capacidad";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, codTaller);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int inscritos = resultSet.getInt("inscritos");
                    int capacidad = resultSet.getInt("Capacidad");
                    return inscritos >= capacidad;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar capacidad del taller: " + e.getMessage());
        }

        return false;
    }
}
