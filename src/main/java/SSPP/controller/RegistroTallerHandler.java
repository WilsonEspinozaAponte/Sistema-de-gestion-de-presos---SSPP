package SSPP.controller;

import SSPP.dao.TallerDAO;
import SSPP.model.Taller;
import SSPP.SSPP;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

public class RegistroTallerHandler {

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
    @FXML
    private Button registrarButton;
    @FXML
    private Button volverButton;

    private TallerDAO tallerDAO = new TallerDAO();

    @FXML
    public void registrarTaller(ActionEvent event) {
        try {
            String nombre = nombreField.getText();
            String descripcion = descripcionArea.getText();
            LocalDate fechaInicio = fechaInicioPicker.getValue();
            LocalDate fechaFin = fechaFinPicker.getValue();
            int capacidad;

            // Comprobar si algún campo está vacío
            if (nombre.isEmpty() || descripcion.isEmpty() || fechaInicio == null || fechaFin == null || capacidadField.getText().isEmpty()) {
                mostrarAlerta("Error de entrada", "Campos incompletos", "Por favor complete todos los campos antes de registrar el taller.");
                return;
            }

            // Comprobar que la capacidad sea un número válido
            try {
                capacidad = Integer.parseInt(capacidadField.getText());
                if (capacidad <= 0) {
                    mostrarAlerta("Error de entrada", "Capacidad inválida", "La capacidad debe ser un número positivo.");
                    return;
                }
            } catch (NumberFormatException e) {
                mostrarAlerta("Error de entrada", "Capacidad inválida", "Por favor ingrese un número válido para la capacidad.");
                return;
            }

            // Comprobar que la fecha de fin sea posterior a la fecha de inicio
            if (fechaFin.isBefore(fechaInicio)) {
                mostrarAlerta("Error de entrada", "Fechas inválidas", "La fecha de fin debe ser posterior a la fecha de inicio.");
                return;
            }

            // Comprobar si ya existe un taller activo con el mismo nombre
            if (tallerDAO.existeTallerActivoConNombre(nombre)) {
                mostrarAlerta("Error de entrada", "Taller ya registrado", "Ya existe un taller con el mismo nombre que aún está en curso.");
                return;
            }

            // Crear y registrar el nuevo taller
            Taller taller = new Taller(0, nombre, descripcion, Date.valueOf(fechaInicio), Date.valueOf(fechaFin), capacidad);
            tallerDAO.insertarTaller(taller);

            // Mostrar mensaje de éxito y volver al menú principal
            mostrarAlerta("Registro Completado", "Taller registrado exitosamente", "Los datos del taller se han registrado correctamente.");
            volverAlMenuPrincipal();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al registrar el taller", "Por favor revise los datos ingresados.");
        }
    }

    @FXML
    public void volverAlMenuPrincipal() {
        try {
            SSPP.changeScene("/SSPP/view/Inicio.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error IO al cargar la vista del menú principal: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String encabezado, String contenido) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
