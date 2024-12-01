package SSPP.controller;

import SSPP.SSPP;
import SSPP.dao.ReclusoDAO;
import SSPP.dao.SentenciaDAO;
import SSPP.dao.CrimenSentenciaDAO;
import SSPP.model.Recluso;
import SSPP.model.Sentencia;
import SSPP.model.CrimenSentencia;
import java.io.IOException;
import java.sql.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegistroSentenciaHandler {

    @FXML
    private TextField buscarReclusoField;
    @FXML
    private ListView<String> reclusoListView;
    @FXML
    private DatePicker fechaSentenciaPicker;
    @FXML
    private TextField condenaTotalField;
    @FXML
    private TextArea detallesArea;
    @FXML
    private TextArea comentariosArea;
    @FXML
    private ListView<String> crimenesListView;
    @FXML
    private RadioButton sentenciaActualRadioButton;
    @FXML
    private RadioButton sentenciaPasadaRadioButton;
    @FXML
    private Button agregarCrimenButton;
    @FXML
    private Button finalizarButton;
    @FXML
    private Button volverButton;

    private ReclusoDAO reclusoDAO = new ReclusoDAO();
    private SentenciaDAO sentenciaDAO = new SentenciaDAO();
    private CrimenSentenciaDAO crimenSentenciaDAO = new CrimenSentenciaDAO();
    private List<CrimenSentencia> crimenes = new ArrayList<>();
    private ToggleGroup sentenciaToggleGroup;
    private List<Recluso> reclusos = new ArrayList<>();

    @FXML
    public void initialize() {
        try {
            // Obtener todos los reclusos de la base de datos
            reclusos = reclusoDAO.obtenerTodosLosReclusos();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error SQL al obtener los reclusos: " + e.getMessage());
        }

        // Inicializar lista de búsqueda vacía
        reclusoListView.setItems(FXCollections.observableArrayList());

        // Inicializar ToggleGroup para radio buttons
        sentenciaToggleGroup = new ToggleGroup();
        sentenciaActualRadioButton.setToggleGroup(sentenciaToggleGroup);
        sentenciaPasadaRadioButton.setToggleGroup(sentenciaToggleGroup);
        sentenciaActualRadioButton.setSelected(true);

        // Inicializar la lista de crímenes vacía
        ObservableList<String> crimenesObservableList = FXCollections.observableArrayList();
        crimenesListView.setItems(crimenesObservableList);

        // Deshabilitar los campos hasta que se seleccione un recluso
        setFieldsDisabled(true);

        // Deshabilitar el botón finalizar hasta que se agregue al menos un crimen
        finalizarButton.setDisable(true);

        // Agregar listener para el campo de búsqueda
        buscarReclusoField.textProperty().addListener((observable, oldValue, newValue) -> filtrarReclusos(newValue));
        
        // Agregar listener para la selección de recluso
        reclusoListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setFieldsDisabled(false);
            }
        });
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


    private void setFieldsDisabled(boolean disabled) {
        fechaSentenciaPicker.setDisable(disabled);
        condenaTotalField.setDisable(disabled);
        detallesArea.setDisable(disabled);
        comentariosArea.setDisable(disabled);
        sentenciaActualRadioButton.setDisable(disabled);
        sentenciaPasadaRadioButton.setDisable(disabled);
        agregarCrimenButton.setDisable(disabled);
    }

@FXML
public void agregarCrimen(ActionEvent event) {
    // Crear un cuadro de diálogo para ingresar los detalles del crimen
    Dialog<CrimenSentencia> dialog = new Dialog<>();
    dialog.setTitle("Agregar Crimen");
    dialog.setHeaderText("Ingrese los detalles del crimen");

    // Crear los campos del formulario
    ComboBox<String> nombreCrimenComboBox = new ComboBox<>();
    nombreCrimenComboBox.setItems(FXCollections.observableArrayList(
        "Robo", "Asalto", "Homicidio", "Fraude", "Tráfico de Drogas", "Extorsión", "Secuestro", "Violencia Doméstica"
    ));
    nombreCrimenComboBox.setPromptText("Seleccione el crimen");

    TextArea descripcionArea = new TextArea();
    descripcionArea.setPromptText("Descripción del Crimen");

    TextField condenaCrimenField = new TextField();
    condenaCrimenField.setPromptText("Condena por el Crimen (en años)");

    // Agregar los campos al cuadro de diálogo
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.add(new Label("Nombre del Crimen:"), 0, 0);
    grid.add(nombreCrimenComboBox, 1, 0);
    grid.add(new Label("Descripción del Crimen:"), 0, 1);
    grid.add(descripcionArea, 1, 1);
    grid.add(new Label("Condena (años):"), 0, 2);
    grid.add(condenaCrimenField, 1, 2);

    dialog.getDialogPane().setContent(grid);
    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

    dialog.setResultConverter(dialogButton -> {
        if (dialogButton == ButtonType.OK) {
            // Verificar si el nombre del crimen está seleccionado
            String nombreCrimen = nombreCrimenComboBox.getValue();
            if (nombreCrimen == null) {
                mostrarError("Crimen no seleccionado", "Por favor seleccione un crimen de la lista.");
                return null;
            }

            // Verificar si la descripción está vacía
            String descripcion = descripcionArea.getText();
            if (descripcion.trim().isEmpty()) {
                mostrarError("Descripción vacía", "Por favor ingrese una descripción para el crimen.");
                return null;
            }

            // Verificar si la condena por el crimen es un número válido
            double condenaCrimen;
            try {
                condenaCrimen = Double.parseDouble(condenaCrimenField.getText());
                if (condenaCrimen <= 0) {
                    mostrarError("Condena inválida", "Por favor ingrese un valor positivo para la condena.");
                    return null;
                }
            } catch (NumberFormatException e) {
                mostrarError("Condena inválida", "Por favor ingrese un número válido para la condena.");
                return null;
            }

            // Si todo está bien, devolver el nuevo crimen
            return new CrimenSentencia(0, 0, nombreCrimen, descripcion, condenaCrimen);
        }
        return null;
    });

    dialog.showAndWait().ifPresent(crimen -> {
        crimenes.add(crimen);
        crimenesListView.getItems().add(crimen.getNombreCrimen());
        finalizarButton.setDisable(false);
    });
}


    @FXML
    public void finalizarRegistro(ActionEvent event) {
        try {
            // Verificar si todos los campos de la sentencia están completos
            if (reclusoListView.getSelectionModel().getSelectedItem() == null) {
                mostrarError("Recluso no seleccionado", "Por favor seleccione un recluso.");
                return;
            }

            if (fechaSentenciaPicker.getValue() == null) {
                mostrarError("Fecha de Sentencia Vacía", "Por favor ingrese la fecha de la sentencia.");
                return;
            }

            if (condenaTotalField.getText().isEmpty()) {
                mostrarError("Condena Total Vacía", "Por favor ingrese la condena total.");
                return;
            }

            try {
                Double.parseDouble(condenaTotalField.getText());
            } catch (NumberFormatException e) {
                mostrarError("Condena Total Inválida", "Por favor ingrese un valor numérico válido para la condena total.");
                return;
            }

            if (detallesArea.getText().isEmpty()) {
                mostrarError("Detalles Vacíos", "Por favor ingrese los detalles de la sentencia.");
                return;
            }

            if (comentariosArea.getText().isEmpty()) {
                mostrarError("Comentarios Vacíos", "Por favor ingrese los comentarios de la sentencia.");
                return;
            }

            if (crimenes.isEmpty()) {
                mostrarError("Crímenes No Registrados", "Debe agregar al menos un crimen antes de finalizar el registro de la sentencia.");
                return;
            }

            // Obtener el preso seleccionado
            String reclusoSeleccionado = reclusoListView.getSelectionModel().getSelectedItem();
            int codRecluso = obtenerCodReclusoDeListView(reclusoSeleccionado);

            // Verificar si el recluso ya tiene una sentencia actual
            if (sentenciaActualRadioButton.isSelected() && sentenciaDAO.tieneSentenciaActual(codRecluso)) {
                mostrarError("Sentencia Actual Ya Registrada", "El recluso seleccionado ya tiene una sentencia actual registrada.");
                return;
            }

            // Registrar la sentencia
            Date fechaSentencia = java.sql.Date.valueOf(fechaSentenciaPicker.getValue());
            double condenaTotal = Double.parseDouble(condenaTotalField.getText());
            String detalles = detallesArea.getText();
            String comentarios = comentariosArea.getText();
            String estado = sentenciaActualRadioButton.isSelected() ? "Actual" : "Cumplida";

            Sentencia sentencia = new Sentencia(0, codRecluso, fechaSentencia, condenaTotal, estado, detalles, comentarios);
            int codSentencia = sentenciaDAO.insertarSentenciaYRetornarId(sentencia);

            // Registrar los crímenes asociados a la sentencia
            for (CrimenSentencia crimen : crimenes) {
                crimen.setCodSentencia(codSentencia);
                crimenSentenciaDAO.insertarCrimenSentencia(crimen);
            }

            System.out.println("Sentencia y crímenes registrados exitosamente");

            // Redirigir al menú principal o mostrar mensaje de éxito
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registro Completado");
            alert.setHeaderText("Sentencia registrada exitosamente");
            alert.setContentText("Los datos de la sentencia se han registrado correctamente.");
            alert.showAndWait();

            volverAlMenuPrincipal();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error al Registrar", "Ha ocurrido un error al registrar la sentencia. Por favor revise los datos e intente nuevamente.");
        }
    }

private void mostrarError(String encabezado, String contenido) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(encabezado);
    alert.setContentText(contenido);
    alert.showAndWait();
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

}

