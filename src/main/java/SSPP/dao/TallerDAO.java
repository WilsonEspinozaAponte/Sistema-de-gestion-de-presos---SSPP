package SSPP.dao;

import SSPP.model.Taller;
import SSPP.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TallerDAO {

    // Obtener todos los talleres
    public List<Taller> obtenerTodosLosTalleres() {
        List<Taller> lista = new ArrayList<>();
        String query = "SELECT * FROM Taller";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Taller taller = new Taller(
                    resultSet.getInt("Cod_taller"),
                    resultSet.getString("Nombre"),
                    resultSet.getString("Descripcion"),
                    resultSet.getDate("Fecha_inicio"),
                    resultSet.getDate("Fecha_fin"),
                    resultSet.getInt("Capacidad")
                );
                lista.add(taller);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener talleres: " + e.getMessage());
        }

        return lista;
    }

    // Insertar un taller en la base de datos
    public void insertarTaller(Taller taller) throws SQLException {
        String sql = "INSERT INTO Taller (Nombre, Descripcion, Fecha_inicio, Fecha_fin, Capacidad) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, taller.getNombre());
            statement.setString(2, taller.getDescripcion());
            statement.setDate(3, new java.sql.Date(taller.getFechaInicio().getTime()));
            statement.setDate(4, new java.sql.Date(taller.getFechaFin().getTime()));
            statement.setInt(5, taller.getCapacidad());

            statement.executeUpdate();
        }
    }

    // Verificar si existe un taller activo con un nombre específico
    public boolean existeTallerActivoConNombre(String nombre) {
        String query = "SELECT COUNT(*) FROM Taller WHERE Nombre = ? AND Fecha_fin >= CURRENT_DATE()";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, nombre);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar talleres activos: " + e.getMessage());
        }

        return false;
    }

    // Obtener un taller por su ID
    public Taller obtenerTallerPorId(int codTaller) {
        String query = "SELECT * FROM Taller WHERE Cod_taller = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, codTaller);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Taller(
                        resultSet.getInt("Cod_taller"),
                        resultSet.getString("Nombre"),
                        resultSet.getString("Descripcion"),
                        resultSet.getDate("Fecha_inicio"),
                        resultSet.getDate("Fecha_fin"),
                        resultSet.getInt("Capacidad")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener el taller: " + e.getMessage());
        }

        return null;
    }

    // Actualizar un taller
    public void actualizarTaller(Taller taller) throws SQLException {
        String sql = "UPDATE Taller SET Nombre = ?, Descripcion = ?, Fecha_inicio = ?, Fecha_fin = ?, Capacidad = ? WHERE Cod_taller = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, taller.getNombre());
            statement.setString(2, taller.getDescripcion());
            statement.setDate(3, new java.sql.Date(taller.getFechaInicio().getTime()));
            statement.setDate(4, new java.sql.Date(taller.getFechaFin().getTime()));
            statement.setInt(5, taller.getCapacidad());
            statement.setInt(6, taller.getCodTaller());

            statement.executeUpdate();
        }
    }

    // Eliminar un taller por su ID
    public void eliminarTaller(int codTaller) throws SQLException {
        String sql = "DELETE FROM Taller WHERE Cod_taller = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, codTaller);
            statement.executeUpdate();
        }
    }
}
