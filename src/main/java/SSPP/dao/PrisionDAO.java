package SSPP.dao;

import SSPP.model.Prision;
import SSPP.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrisionDAO {

    public List<Prision> obtenerTodasLasPrisiones() {
        List<Prision> prisiones = new ArrayList<>();
        String query = "SELECT * FROM Prision";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Prision prision = new Prision(
                        resultSet.getInt("Cod_prision"),
                        resultSet.getString("Nombre"),
                        resultSet.getString("Direccion"),
                        resultSet.getInt("Capacidad")
                );
                prisiones.add(prision);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener prisiones: " + e.getMessage());
        }

        return prisiones;
    }

    public Prision obtenerPrisionPorNombre(String nombre) {
        String query = "SELECT * FROM Prision WHERE Nombre = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nombre);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Prision(
                            resultSet.getInt("Cod_prision"),
                            resultSet.getString("Nombre"),
                            resultSet.getString("Direccion"),
                            resultSet.getInt("Capacidad")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener prisión por nombre: " + e.getMessage());
        }
        return null;
    }
}