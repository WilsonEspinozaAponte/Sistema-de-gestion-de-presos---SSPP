package SSPP.dao;

import SSPP.model.Sentencia;
import SSPP.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SentenciaDAO {

    public int insertarSentenciaYRetornarId(Sentencia sentencia) throws SQLException {
        String sql = "INSERT INTO Sentencia (Cod_recluso, Fecha_sentencia, Condena_total, Estado, Detalles, Comentarios) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, sentencia.getCodRecluso());
            statement.setDate(2, new java.sql.Date(sentencia.getFechaSentencia().getTime()));
            statement.setDouble(3, sentencia.getCondenaTotal());
            statement.setString(4, sentencia.getEstado());
            statement.setString(5, sentencia.getDetalles());
            statement.setString(6, sentencia.getComentarios());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Error al insertar sentencia, no se obtuvo el ID.");
                }
            }
        }
    }

    public boolean tieneSentenciaActual(int codRecluso) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Sentencia WHERE Cod_recluso = ? AND Estado = 'Actual'";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, codRecluso);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
// Método para obtener todas las sentencias de un recluso
    public List<Sentencia> obtenerSentenciasPorRecluso(int codRecluso) throws SQLException {
        List<Sentencia> sentencias = new ArrayList<>();
        String sql = "SELECT * FROM Sentencia WHERE Cod_recluso = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, codRecluso);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Sentencia sentencia = new Sentencia(
                            resultSet.getInt("Cod_sentencia"),
                            resultSet.getInt("Cod_recluso"),
                            resultSet.getDate("Fecha_sentencia"),
                            resultSet.getDouble("Condena_total"),
                            resultSet.getString("Estado"),
                            resultSet.getString("Detalles"),
                            resultSet.getString("Comentarios")
                    );
                    sentencias.add(sentencia);
                }
            }
        }
        return sentencias;
    }

    // Método para actualizar una sentencia
    public void actualizarSentencia(Sentencia sentencia) throws SQLException {
        String sql = "UPDATE Sentencia SET Fecha_sentencia = ?, Condena_total = ?, Estado = ?, Detalles = ?, Comentarios = ? WHERE Cod_sentencia = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, new java.sql.Date(sentencia.getFechaSentencia().getTime()));
            statement.setDouble(2, sentencia.getCondenaTotal());
            statement.setString(3, sentencia.getEstado());
            statement.setString(4, sentencia.getDetalles());
            statement.setString(5, sentencia.getComentarios());
            statement.setInt(6, sentencia.getCodSentencia());
            statement.executeUpdate();
        }
    }
    
    // Método para eliminar una sentencia
    public void eliminarSentencia(int codSentencia) throws SQLException {
        String sql = "DELETE FROM Sentencia WHERE Cod_sentencia = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, codSentencia);
            statement.executeUpdate();
        }
    }

}
