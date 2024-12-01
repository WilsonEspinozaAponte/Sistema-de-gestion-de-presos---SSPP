package SSPP.dao;

import SSPP.model.CrimenSentencia;
import SSPP.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CrimenSentenciaDAO {

    // Método para insertar un crimen asociado a una sentencia
    public void insertarCrimenSentencia(CrimenSentencia crimenSentencia) throws SQLException {
        String sql = "INSERT INTO Crimen_Sentencia (Cod_sentencia, Nombre_crimen, Descripcion, Condena_crimen) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, crimenSentencia.getCodSentencia());
            stmt.setString(2, crimenSentencia.getNombreCrimen());
            stmt.setString(3, crimenSentencia.getDescripcion());
            stmt.setDouble(4, crimenSentencia.getCondenaCrimen());
            stmt.executeUpdate();
        }
    }

    // Método para obtener todos los crímenes asociados a una sentencia
    public List<CrimenSentencia> obtenerCrimenesPorSentencia(int codSentencia) throws SQLException {
        List<CrimenSentencia> crimenes = new ArrayList<>();
        String sql = "SELECT * FROM Crimen_Sentencia WHERE Cod_sentencia = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codSentencia);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CrimenSentencia crimen = new CrimenSentencia(
                            rs.getInt("Cod_crimen"),
                            rs.getInt("Cod_sentencia"),
                            rs.getString("Nombre_crimen"),
                            rs.getString("Descripcion"),
                            rs.getDouble("Condena_crimen")
                    );
                    crimenes.add(crimen);
                }
            }
        }
        return crimenes;
    }

    // Método para actualizar un crimen
    public void actualizarCrimenSentencia(CrimenSentencia crimenSentencia) throws SQLException {
        String sql = "UPDATE Crimen_Sentencia SET Nombre_crimen = ?, Descripcion = ?, Condena_crimen = ? WHERE Cod_crimen = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, crimenSentencia.getNombreCrimen());
            stmt.setString(2, crimenSentencia.getDescripcion());
            stmt.setDouble(3, crimenSentencia.getCondenaCrimen());
            stmt.setInt(4, crimenSentencia.getCodCrimenSent());
            stmt.executeUpdate();
        }
    }
    
    // Método para eliminar un crimen
    public void eliminarCrimenSentencia(int codCrimen) throws SQLException {
        String sql = "DELETE FROM Crimen_Sentencia WHERE Cod_crimen = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codCrimen);
            stmt.executeUpdate();
        }
    }    
}
