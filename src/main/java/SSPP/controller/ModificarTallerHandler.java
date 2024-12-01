package SSPP.controller;

import SSPP.dao.TallerDAO;
import SSPP.model.Taller;
import SSPP.SSPP;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ModificarTallerHandler {

    @FXML
    private TextField buscarTallerField;
    @FXML
    private ListView<String> tallerListView;
    @FXML
    private TextField nombreField;
    @FXML
    private TextArea descripcionArea;
    @FXML
    private DatePicker fechaInicioPicker;
    @FXML
    private DatePicker fechaFinPicker;
    @FXML
    private TextField capacidadField;

    private TallerDAO tallerDAO = new TallerDAO();
    private List<Taller> talleres;

    @FXML
    public void initialize() {
        // Cargar todos los talleres en la lista
        try {
            talleres = tallerDAO.obtenerTodosLosTalleres();
            ObservableList<String> tallerNombres = FXCollections.observableArrayList();
            for (Taller taller : talleres) {
                tallerNombres.add(taller.getNombre() + " (ID: " + taller.getCodTaller() + ")");
            }
            tallerListView.setItems(tallerNombres);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al cargar los talleres", "No se pudieron cargar los talleres.");
        }

        // Listener para filtrar talleres
        buscarTallerField.textProperty().addListener((observable, oldValue, newValue) -> filtrarTalleres(newValue));

        // Listener para la selección del taller en el ListView
        tallerListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                cargarDatosTaller(obtenerCodTallerDeListView(newValue));
            }
        });
    }

    private void filtrarTalleres(String filtro) {
        ObservableList<String> tallerFiltrado = FXCollections.observableArrayList();
        for (Taller taller : talleres) {
            String nombreCompleto = taller.getNombre() + " (ID: " + taller.getCodTaller() + ")";
            if (nombreCompleto.toLowerCase().contains(filtro.toLowerCase())) {
                tallerFiltrado.add(nombreCompleto);
            }
        }
        tallerListView.setItems(tallerFiltrado);
    }

    private void cargarDatosTaller(int codTaller) {
        try {
            Taller taller = tallerDAO.obtenerTallerPorId(codTaller);
            if (taller != null) {
                nombreField.setText(taller.getNombre());
                descripcionArea.setText(taller.getDescripcion());
                java.sql.Date fechaInicio = (java.sql.Date) taller.getFechaInicio();
                if (fechaInicio != null) {
                    fechaInicioPicker.setValue(fechaInicio.toLocalDate());
                } else {
                    fechaInicioPicker.setValue(null);
                }
                java.sql.Date fechaFin = (java.sql.Date) taller.getFechaFin();
                if (fechaFin != null) {
                    fechaFinPicker.setValue(fechaFin.toLocalDate());
                } else {
                    fechaFinPicker.setValue(null);
                }
                capacidadField.setText(String.valueOf(taller.getCapacidad()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al cargar datos del taller", "No se pudieron cargar los datos del taller seleccionado.");
        }
    }

    @FXML
    public void actualizarTaller(ActionEvent event) {
        try {
            String tallerSeleccionado = tallerListView.getSelectionModel().getSelectedItem();
            if (tallerSeleccionado == null) {
                mostrarAlerta("Error", "Taller no seleccionado", "Por favor seleccione un taller.");
                return;
            }

            int codTaller = obtenerCodTallerDeListView(tallerSeleccionado);
            String nombre = nombreField.getText();
            String descripcion = descripcionArea.getText();
            java.sql.Date fechaInicio = java.sql.Date.valueOf(fechaInicioPicker.getValue());
            java.sql.Date fechaFin = java.sql.Date.valueOf(fechaFinPicker.getValue());
            int capacidad = Integer.parseInt(capacidadField.getText());

            Taller taller = new Taller(codTaller, nombre, descripcion, fechaInicio, fechaFin, capacidad);
            tallerDAO.actualizarTaller(taller);

            mostrarAlerta("Éxito", "Taller actualizado", "Los datos del taller han sido actualizados correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al actualizar datos del taller", "No se pudieron actualizar los datos del taller.");
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de entrada", "Capacidad inválida", "Por favor ingrese un número válido para la capacidad.");
        }
    }

    @FXML
    public void eliminarTaller(ActionEvent event) {
        try {
            String tallerSeleccionado = tallerListView.getSelectionModel().getSelectedItem();
            if (tallerSeleccionado == null) {
                mostrarAlerta("Error", "Taller no seleccionado", "Por favor seleccione un taller.");
                return;
            }

            int codTaller = obtenerCodTallerDeListView(tallerSeleccionado);
            tallerDAO.eliminarTaller(codTaller);

            mostrarAlerta("Éxito", "Taller eliminado", "El taller ha sido eliminado correctamente.");
            tallerListView.getItems().remove(tallerSeleccionado);
            limpiarCampos();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al eliminar taller", "No se pudo eliminar el taller.");
        }
    }

    @FXML
    public void volverAModificarDatos() {
        try {
            SSPP.changeScene("/SSPP/view/ModificacionDatos.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error IO al cargar la vista de modificación de datos: " + e.getMessage());
        }
    }

    private int obtenerCodTallerDeListView(String tallerSeleccionado) {
        String[] partes = tallerSeleccionado.split("ID: ");
        return Integer.parseInt(partes[1].replace(")", "").trim());
    }

    private void mostrarAlerta(String titulo, String encabezado, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    private void limpiarCampos() {
        nombreField.clear();
        descripcionArea.clear();
        fechaInicioPicker.setValue(null);
        fechaFinPicker.setValue(null);
        capacidadField.clear();
    }
}
