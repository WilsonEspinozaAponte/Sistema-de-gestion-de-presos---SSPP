package SSPP.view;

import SSPP.dao.PrisionDAO;
import SSPP.dao.ReclusoDAO;
import SSPP.model.Prision;
import SSPP.model.Recluso;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.sql.SQLException;
import java.util.List;

public class RegistroPresoController {

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
        try {
            String nombre = nombreField.getText();
            String apellido = apellidoField.getText();
            java.sql.Date fechaNacimiento = java.sql.Date.valueOf(fechaNacimientoPicker.getValue());
            String tipoDocumento = tipoDocumentoCombo.getValue();
            String numeroDocumento = documentoField.getText();
            String prisionNombre = prisionCombo.getValue();
            String nacionalidad = nacionalidadCombo.getValue();
            int conducta = (int) conductaSlider.getValue();

            // Obtener la prisión seleccionada por nombre
            Prision prisionSeleccionada = prisionDAO.obtenerPrisionPorNombre(prisionNombre);

            // Crear nuevo recluso y registrar en la base de datos
            Recluso recluso = new Recluso(0, nombre, apellido, fechaNacimiento, prisionSeleccionada.getCodPrision(), conducta, tipoDocumento, numeroDocumento, nacionalidad);
            reclusoDAO.insertarRecluso(recluso);

            System.out.println("Recluso registrado exitosamente");
            // Aquí podríamos mostrar un mensaje de éxito o limpiar los campos para una nueva entrada
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
