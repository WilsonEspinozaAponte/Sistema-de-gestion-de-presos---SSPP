package SSPP.dao;

import SSPP.model.Recluso;
import SSPP.model.Sentencia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import SSPP.utils.DatabaseConnection;

public class ReclusoDAO {

    public int insertarRecluso(Recluso recluso) throws SQLException {
        String sql = "INSERT INTO Recluso (Nombre, Apellido, Fecha_nacimiento, Cod_prision, Conducta, Tipo_documento, Numero_documento, Nacionalidad) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, recluso.getNombre());
            stmt.setString(2, recluso.getApellido());
            stmt.setDate(3, new java.sql.Date(recluso.getFechaNacimiento().getTime()));
            stmt.setInt(4, recluso.getCodPrision());
            stmt.setInt(5, recluso.getConducta());
            stmt.setString(6, recluso.getTipoDocumento());
            stmt.setString(7, recluso.getNumeroDocumento());
            stmt.setString(8, recluso.getNacionalidad());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Error al insertar recluso, no se obtuvo el ID.");
                }
            }
        }
    }

    public List<Recluso> obtenerTodosLosReclusos() throws SQLException {
        String sql = "SELECT * FROM Recluso";
        List<Recluso> reclusos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Recluso recluso = new Recluso(
                        rs.getInt("Cod_recluso"),
                        rs.getString("Nombre"),
                        rs.getString("Apellido"),
                        rs.getDate("Fecha_nacimiento"),
                        rs.getInt("Cod_prision"),
                        rs.getInt("Conducta"),
                        rs.getString("Tipo_documento"),
                        rs.getString("Numero_documento"), 
                        rs.getString("Nacionalidad")
                );
                reclusos.add(recluso);
            }
        }
        return reclusos;
    }

    
    // Obtener un recluso por su ID
    public Recluso obtenerReclusoPorId(int codRecluso) throws SQLException {
        String sql = "SELECT * FROM Recluso WHERE Cod_recluso = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codRecluso);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Recluso(
                            rs.getInt("Cod_recluso"),
                            rs.getString("Nombre"),
                            rs.getString("Apellido"),
                            rs.getDate("Fecha_nacimiento"),
                            rs.getInt("Cod_prision"),
                            rs.getInt("Conducta"),
                            rs.getString("Tipo_documento"),
                            rs.getString("Numero_documento"),
                            rs.getString("Nacionalidad")
                    );
                }
            }
        }
        return null;
    }

    // Actualizar datos de un recluso
    public void actualizarRecluso(Recluso recluso) throws SQLException {
        String sql = "UPDATE Recluso SET Nombre = ?, Apellido = ?, Fecha_nacimiento = ?, Cod_prision = ?, Conducta = ?, Tipo_documento = ?, Numero_documento = ?, Nacionalidad = ? WHERE Cod_recluso = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, recluso.getNombre());
            stmt.setString(2, recluso.getApellido());
            stmt.setDate(3, new java.sql.Date(recluso.getFechaNacimiento().getTime()));
            stmt.setInt(4, recluso.getCodPrision());
            stmt.setInt(5, recluso.getConducta());
            stmt.setString(6, recluso.getTipoDocumento());
            stmt.setString(7, recluso.getNumeroDocumento());
            stmt.setString(8, recluso.getNacionalidad());
            stmt.setInt(9, recluso.getCodRecluso());
            stmt.executeUpdate();
        }
    }

    // Eliminar un recluso por su ID
    public void eliminarRecluso(int codRecluso) throws SQLException {
        String sql = "DELETE FROM Recluso WHERE Cod_recluso = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codRecluso);
            stmt.executeUpdate();
        }
    }
    
    // Obtener reclusos por taller específico
    public List<Recluso> obtenerReclusosPorTaller(int codTaller) throws SQLException {
        String sql = "SELECT r.* FROM Recluso r " +
                     "JOIN Matricula m ON r.Cod_recluso = m.Cod_recluso " +
                     "WHERE m.Cod_taller = ?";
        List<Recluso> reclusos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codTaller);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Recluso recluso = new Recluso(
                            rs.getInt("Cod_recluso"),
                            rs.getString("Nombre"),
                            rs.getString("Apellido"),
                            rs.getDate("Fecha_nacimiento"),
                            rs.getInt("Cod_prision"),
                            rs.getInt("Conducta"),
                            rs.getString("Tipo_documento"),
                            rs.getString("Numero_documento"),
                            rs.getString("Nacionalidad")
                    );
                    reclusos.add(recluso);
                }
            }
        }
        return reclusos;
    }

}
