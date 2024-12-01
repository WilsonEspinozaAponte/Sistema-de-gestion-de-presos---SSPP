package SSPP.controller;

import SSPP.dao.PrisionDAO;
import SSPP.dao.ReclusoDAO;
import SSPP.dao.SentenciaDAO;
import SSPP.model.Prision;
import SSPP.model.Recluso;
import SSPP.SSPP;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ModificarReclusoHandler {

    @FXML
    private TextField buscarReclusoField;
    @FXML
    private ListView<String> reclusoListView;
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

    private ReclusoDAO reclusoDAO = new ReclusoDAO();
    private SentenciaDAO sentenciaDAO = new SentenciaDAO();
    private PrisionDAO prisionDAO = new PrisionDAO();
    private List<Recluso> reclusos;
    private List<Prision> prisiones;

    @FXML
    public void initialize() {
        // Cargar todos los reclusos en la lista
        try {
            reclusos = reclusoDAO.obtenerTodosLosReclusos();
            ObservableList<String> reclusoNombres = FXCollections.observableArrayList();
            for (Recluso recluso : reclusos) {
                reclusoNombres.add(recluso.getNombre() + " " + recluso.getApellido() + " (ID: " + recluso.getCodRecluso() + ")");
            }
            reclusoListView.setItems(reclusoNombres);

            // Cargar las prisiones en el ComboBox
            prisiones = prisionDAO.obtenerTodasLasPrisiones();
            ObservableList<String> prisionNombres = FXCollections.observableArrayList();
            for (Prision prision : prisiones) {
                prisionNombres.add(prision.getNombre() + " (ID: " + prision.getCodPrision() + ")");
            }
            prisionCombo.setItems(prisionNombres);

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al cargar los datos", "No se pudieron cargar los reclusos o prisiones.");
        }

        // Configurar ComboBox de tipo de documento y nacionalidad
        tipoDocumentoCombo.setItems(FXCollections.observableArrayList("DNI", "Pasaporte", "Carnet de Extranjería"));
        nacionalidadCombo.setItems(FXCollections.observableArrayList("Argentina", "Bolivia", "Chile", "Colombia", "Ecuador", "Paraguay", "Perú", "Uruguay"));

        // Listener para filtrar reclusos
        buscarReclusoField.textProperty().addListener((observable, oldValue, newValue) -> filtrarReclusos(newValue));

        // Listener para la selección del recluso en el ListView
        reclusoListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                cargarDatosRecluso(obtenerCodReclusoDeListView(newValue));
            }
        });
    }

    private void filtrarReclusos(String filtro) {
        ObservableList<String> reclusoFiltrado = FXCollections.observableArrayList();
        for (Recluso recluso : reclusos) {
            String nombreCompleto = recluso.getNombre() + " " + recluso.getApellido() + " (ID: " + recluso.getCodRecluso() + ")";
            if (nombreCompleto.toLowerCase().contains(filtro.toLowerCase())) {
                reclusoFiltrado.add(nombreCompleto);
            }
        }
        reclusoListView.setItems(reclusoFiltrado);
    }

    private void cargarDatosRecluso(int codRecluso) {
        try {
            Recluso recluso = reclusoDAO.obtenerReclusoPorId(codRecluso);
            if (recluso != null) {
                nombreField.setText(recluso.getNombre());
                apellidoField.setText(recluso.getApellido());
                java.sql.Date fechaNacimiento = (java.sql.Date) recluso.getFechaNacimiento();
                if (fechaNacimiento != null) {
                    fechaNacimientoPicker.setValue(fechaNacimiento.toLocalDate());
                } else {
                    fechaNacimientoPicker.setValue(null);
                }
                tipoDocumentoCombo.setValue(recluso.getTipoDocumento());
                documentoField.setText(recluso.getNumeroDocumento());
                prisionCombo.setValue(obtenerPrisionNombrePorId(recluso.getCodPrision()));
                nacionalidadCombo.setValue(recluso.getNacionalidad());
                conductaSlider.setValue(recluso.getConducta());
                conductaLabel.setText(String.valueOf(recluso.getConducta()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al cargar datos del recluso", "No se pudieron cargar los datos del recluso seleccionado.");
        }
    }

    private String obtenerPrisionNombrePorId(int codPrision) {
        for (Prision prision : prisiones) {
            if (prision.getCodPrision() == codPrision) {
                return prision.getNombre() + " (ID: " + prision.getCodPrision() + ")";
            }
        }
        return null;
    }

    @FXML
    public void modificarRecluso(ActionEvent event) {
        try {
            String reclusoSeleccionado = reclusoListView.getSelectionModel().getSelectedItem();
            if (reclusoSeleccionado == null) {
                mostrarAlerta("Error", "Recluso no seleccionado", "Por favor seleccione un recluso.");
                return;
            }

            int codRecluso = obtenerCodReclusoDeListView(reclusoSeleccionado);
            String nombre = nombreField.getText();
            String apellido = apellidoField.getText();
            java.sql.Date fechaNacimiento = java.sql.Date.valueOf(fechaNacimientoPicker.getValue());
            String tipoDocumento = tipoDocumentoCombo.getValue();
            String documento = documentoField.getText();
            String nacionalidad = nacionalidadCombo.getValue();
            int conducta = (int) conductaSlider.getValue();
            int codPrision = obtenerCodPrisionDeComboBox(prisionCombo.getValue());

            Recluso recluso = new Recluso(codRecluso, nombre, apellido, fechaNacimiento, codPrision, conducta, tipoDocumento, documento, nacionalidad);
            reclusoDAO.actualizarRecluso(recluso);

            mostrarAlerta("Éxito", "Recluso actualizado", "Los datos del recluso han sido actualizados correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al actualizar datos del recluso", "No se pudieron actualizar los datos del recluso.");
        }
    }

    @FXML
    public void eliminarRecluso(ActionEvent event) {
        try {
            String reclusoSeleccionado = reclusoListView.getSelectionModel().getSelectedItem();
            if (reclusoSeleccionado == null) {
                mostrarAlerta("Error", "Recluso no seleccionado", "Por favor seleccione un recluso.");
                return;
            }

            int codRecluso = obtenerCodReclusoDeListView(reclusoSeleccionado);
            reclusoDAO.eliminarRecluso(codRecluso);

            mostrarAlerta("Éxito", "Recluso eliminado", "El recluso ha sido eliminado correctamente.");
            reclusoListView.getItems().remove(reclusoSeleccionado);
            limpiarCampos();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al eliminar recluso", "No se pudo eliminar el recluso.");
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

    private int obtenerCodReclusoDeListView(String reclusoSeleccionado) {
        String[] partes = reclusoSeleccionado.split("ID: ");
        return Integer.parseInt(partes[1].replace(")", "").trim());
    }

    private int obtenerCodPrisionDeComboBox(String prisionSeleccionada) {
        String[] partes = prisionSeleccionada.split("ID: ");
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
        apellidoField.clear();
        fechaNacimientoPicker.setValue(null);
        tipoDocumentoCombo.setValue(null);
        documentoField.clear();
        nacionalidadCombo.setValue(null);
        conductaSlider.setValue(50);
        conductaLabel.setText("50");
        prisionCombo.setValue(null);
    }
}
