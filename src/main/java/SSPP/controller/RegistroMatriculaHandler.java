package SSPP.controller;

import SSPP.dao.MatriculaDAO;
import SSPP.dao.ReclusoDAO;
import SSPP.dao.TallerDAO;
import SSPP.model.Matricula;
import SSPP.model.Recluso;
import SSPP.model.Taller;
import SSPP.SSPP;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RegistroMatriculaHandler {

    @FXML
    private TextField buscarReclusoField;
    @FXML
    private ListView<String> reclusoListView;
    @FXML
    private ComboBox<String> tallerComboBox;
    @FXML
    private Button registrarButton;
    @FXML
    private Button volverButton;

    private ReclusoDAO reclusoDAO = new ReclusoDAO();
    private TallerDAO tallerDAO = new TallerDAO();
    private MatriculaDAO matriculaDAO = new MatriculaDAO();
    private List<Recluso> reclusos;
    private List<Taller> talleres;

    @FXML
    public void initialize() {
        // Cargar reclusos en ListView
        try {
            reclusos = reclusoDAO.obtenerTodosLosReclusos();
            ObservableList<String> reclusoNombres = FXCollections.observableArrayList();
            for (Recluso recluso : reclusos) {
                reclusoNombres.add(recluso.getNombre() + " " + recluso.getApellido() + " (ID: " + recluso.getCodRecluso() + ", Documento: " + recluso.getNumeroDocumento() + ")");
            }
            reclusoListView.setItems(reclusoNombres);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al cargar los reclusos", "No se pudieron cargar los reclusos.");
        }

        // Cargar talleres en ComboBox
        talleres = tallerDAO.obtenerTodosLosTalleres();
        for (Taller taller : talleres) {
            tallerComboBox.getItems().add(taller.getNombre() + " (ID: " + taller.getCodTaller() + ")");
        }

        // Agregar listener para el campo de búsqueda
        buscarReclusoField.textProperty().addListener((observable, oldValue, newValue) -> filtrarReclusos(newValue));
    }


    private void filtrarReclusos(String filtro) {
       ObservableList<String> reclusoFiltrado = FXCollections.observableArrayList();
       for (Recluso recluso : reclusos) {
           String nombreCompleto = recluso.getNombre() + " " + recluso.getApellido() + " (ID: " + recluso.getCodRecluso() + ", Documento: " + recluso.getNumeroDocumento() + ")";
           if (nombreCompleto.toLowerCase().contains(filtro.toLowerCase())) {
               reclusoFiltrado.add(nombreCompleto);
           }
       }
       reclusoListView.setItems(reclusoFiltrado);
   }


    @FXML
    public void registrarMatricula(ActionEvent event) {
        String reclusoSeleccionado = reclusoListView.getSelectionModel().getSelectedItem();
        String tallerSeleccionado = tallerComboBox.getValue();

        if (reclusoSeleccionado == null || tallerSeleccionado == null) {
            mostrarAlerta("Error de entrada", "Campos incompletos", "Por favor seleccione un recluso y un taller.");
            return;
        }

        int codRecluso = obtenerCodReclusoDeListView(reclusoSeleccionado);
        int codTaller = obtenerCodTallerDeComboBox(tallerSeleccionado);

        // Verificar si el taller está lleno
        if (matriculaDAO.tallerEstaLleno(codTaller)) {
            mostrarAlerta("Error de capacidad", "Taller lleno", "El taller seleccionado ya ha alcanzado su capacidad máxima.");
            return;
        }

        // Registrar la matrícula
        try {
            Matricula matricula = new Matricula(0, codRecluso, codTaller, Date.valueOf(LocalDate.now()));
            matriculaDAO.insertarMatricula(matricula);

            mostrarAlerta("Registro Completado", "Matrícula registrada exitosamente", "El recluso ha sido matriculado en el taller correctamente.");

            // Volver al menú principal
            volverAlMenuPrincipal();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al registrar la matrícula", "No se pudo registrar la matrícula, por favor intente nuevamente.");
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

    private int obtenerCodReclusoDeListView(String reclusoSeleccionado) {
        String[] partes = reclusoSeleccionado.split("ID: ");
        return Integer.parseInt(partes[1].split(",")[0].trim());
    }


    private int obtenerCodTallerDeComboBox(String tallerSeleccionado) {
        String[] partes = tallerSeleccionado.split("ID: ");
        return Integer.parseInt(partes[1].replace(")", "").trim());
    }

    private void mostrarAlerta(String titulo, String encabezado, String contenido) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
