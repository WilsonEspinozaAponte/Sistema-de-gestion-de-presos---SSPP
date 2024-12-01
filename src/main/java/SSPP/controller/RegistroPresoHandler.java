package SSPP.controller;

import SSPP.dao.PrisionDAO;
import SSPP.dao.ReclusoDAO;
import SSPP.model.Prision;
import SSPP.model.Recluso;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class RegistroPresoHandler {

    @FXML
    private TextField nombreField;
    @FXML
    private TextField apellidoField;
    @FXML
    private DatePicker fechaNacimientoPicker;
    @FXML
    private ComboBox<String> tipoDocumentoCombo;
    @FXML
    private TextField documentoField;
    @FXML
    private ComboBox<String> prisionCombo;
    @FXML
    private ComboBox<String> nacionalidadCombo;
    @FXML
    private Slider conductaSlider;
    @FXML
    private Label conductaLabel;
    @FXML
    private Button registrarButton;
    @FXML
    private Button volverButton;

    private ReclusoDAO reclusoDAO = new ReclusoDAO();
    private PrisionDAO prisionDAO = new PrisionDAO();

    @FXML
    public void initialize() {
        // Inicializar opciones de tipo de documento
        tipoDocumentoCombo.setItems(FXCollections.observableArrayList("DNI", "Pasaporte", "Carnet de Extranjería"));

        // Inicializar opciones de nacionalidad
        nacionalidadCombo.setItems(FXCollections.observableArrayList(
                "Argentina", "Bolivia", "Chile", "Colombia", "Ecuador", "Paraguay", "Perú", "Uruguay"
        ));

        // Cargar prisiones desde la base de datos
        List<Prision> prisiones = prisionDAO.obtenerTodasLasPrisiones();
        ObservableList<String> prisionNombres = FXCollections.observableArrayList();
        for (Prision prision : prisiones) {
            prisionNombres.add(prision.getNombre());
        }
        prisionCombo.setItems(prisionNombres);

        // Actualizar etiqueta de conducta con el valor del slider
        conductaSlider.valueProperty().addListener((observable, oldValue, newValue) -> 
            conductaLabel.setText(String.valueOf(newValue.intValue())));
    }

    @FXML
    public void registrarPreso(ActionEvent event) {
        String nombre = nombreField.getText();
        String apellido = apellidoField.getText();
        java.sql.Date fechaNacimiento = java.sql.Date.valueOf(fechaNacimientoPicker.getValue());
        String tipoDocumento = tipoDocumentoCombo.getValue();
        String numeroDocumento = documentoField.getText();  // Cambiado de "documento" a "numeroDocumento"
        String prisionNombre = prisionCombo.getValue();
        String nacionalidad = nacionalidadCombo.getValue();
        int conducta = (int) conductaSlider.getValue();

        Task<Void> registroTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    // Obtener la prisión seleccionada por nombre
                    Prision prisionSeleccionada = prisionDAO.obtenerPrisionPorNombre(prisionNombre);
                    if (prisionSeleccionada == null) {
                        System.out.println("Prisión no encontrada: " + prisionNombre);
                        return null;  // Si no se encuentra la prisión, terminar
                    }

                    // Crear nuevo recluso y registrar en la base de datos
                    Recluso recluso = new Recluso(0, nombre, apellido, fechaNacimiento, prisionSeleccionada.getCodPrision(), conducta, tipoDocumento, numeroDocumento, nacionalidad);
                    int reclusoId = reclusoDAO.insertarRecluso(recluso);

                    System.out.println("Recluso registrado exitosamente con ID: " + reclusoId);

                    // Mostrar mensaje de éxito y redirigir al menú principal en el hilo de la UI
                    javafx.application.Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Registro Exitoso");
                        alert.setHeaderText("Recluso registrado exitosamente");
                        alert.setContentText("ID del recluso: " + reclusoId);
                        alert.showAndWait();

                        // Cambiar la escena al menú principal
                        volverAlMenuPrincipal();
                    });
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.err.println("Error SQL al registrar el recluso: " + e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Error inesperado: " + e.getMessage());
                }
                return null;
            }
        };

        // Ejecutar la tarea en un hilo separado
        new Thread(registroTask).start();
    }

    @FXML
    public void volverAlMenuPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SSPP/view/Inicio.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) volverButton.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error IO al cargar la vista del menú principal: " + e.getMessage());
        }
    }
}
