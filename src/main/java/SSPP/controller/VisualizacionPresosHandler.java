package SSPP.controller;

import SSPP.dao.ReclusoDAO;
import SSPP.dao.PrisionDAO;
import SSPP.dao.TallerDAO;
import SSPP.model.Prision;
import SSPP.model.Recluso;
import SSPP.model.Taller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisualizacionPresosHandler {

    @FXML
    private ComboBox<String> filtroTallerComboBox;
    @FXML
    private TableView<Recluso> tablaPresosTableView;
    @FXML
    private TableColumn<Recluso, Integer> columnaId;
    @FXML
    private TableColumn<Recluso, String> columnaNombre;
    @FXML
    private TableColumn<Recluso, String> columnaApellido;
    @FXML
    private TableColumn<Recluso, String> columnaFechaNacimiento;
    @FXML
    private TableColumn<Recluso, Integer> columnaPrision;
    @FXML
    private TableColumn<Recluso, Integer> columnaConducta;
    @FXML
    private TableColumn<Recluso, String> columnaTipoDocumento;
    @FXML
    private TableColumn<Recluso, String> columnaNumeroDocumento;
    @FXML
    private TableColumn<Recluso, String> columnaNacionalidad;

    private ReclusoDAO reclusoDAO = new ReclusoDAO();
    private TallerDAO tallerDAO = new TallerDAO();
    private PrisionDAO prisionDAO = new PrisionDAO();
    private Map<Integer, String> mapaPrisiones;  // Mapa para almacenar los nombres de las prisiones

    @FXML
    public void initialize() {
        configurarTablaReclusos();
        cargarTodosLosTalleres();
        cargarMapaPrisiones();  // Cargar el mapa de prisiones
        cargarTodosLosReclusos();
    }

    // Configurar las columnas de la tabla
    private void configurarTablaReclusos() {
        columnaId.setCellValueFactory(new PropertyValueFactory<>("codRecluso"));

        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaNombre.setSortable(true); // Permitir ordenar
        columnaApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        columnaApellido.setSortable(true); // Permitir ordenar

        columnaFechaNacimiento.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        columnaFechaNacimiento.setSortable(true); // Permitir ordenar

        columnaTipoDocumento.setCellValueFactory(new PropertyValueFactory<>("tipoDocumento"));
        columnaTipoDocumento.setSortable(true); // Permitir ordenar

        columnaNumeroDocumento.setCellValueFactory(new PropertyValueFactory<>("numeroDocumento"));
        columnaNumeroDocumento.setSortable(false); // No es necesario ordenar por número de documento

        columnaNacionalidad.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));
        columnaNacionalidad.setSortable(true); // Permitir ordenar

        // Configurar la columna "Prisión" para mostrar el nombre de la prisión en lugar del código
        columnaPrision.setCellValueFactory(new PropertyValueFactory<>("codPrision"));
        columnaPrision.setSortable(true); // Permitir ordenar
        columnaPrision.setCellFactory(new Callback<TableColumn<Recluso, Integer>, TableCell<Recluso, Integer>>() {
            @Override
            public TableCell<Recluso, Integer> call(TableColumn<Recluso, Integer> param) {
                return new TableCell<Recluso, Integer>() {
                    @Override
                    protected void updateItem(Integer codPrision, boolean empty) {
                        super.updateItem(codPrision, empty);
                        if (empty || codPrision == null) {
                            setText(null);
                        } else {
                            setText(mapaPrisiones.getOrDefault(codPrision, "Sin asignar"));
                        }
                    }
                };
            }
        });

        // Configurar la columna "Conducta" para mostrar el texto adecuado según el rango y permitir ordenación
        columnaConducta.setCellValueFactory(new PropertyValueFactory<>("conducta"));
        columnaConducta.setSortable(true); // Permitir ordenar
        columnaConducta.setComparator((o1, o2) -> o2 - o1); // Ordenar en orden descendente
        columnaConducta.setCellFactory(new Callback<TableColumn<Recluso, Integer>, TableCell<Recluso, Integer>>() {
            @Override
            public TableCell<Recluso, Integer> call(TableColumn<Recluso, Integer> param) {
                return new TableCell<Recluso, Integer>() {
                    @Override
                    protected void updateItem(Integer conducta, boolean empty) {
                        super.updateItem(conducta, empty);
                        if (empty || conducta == null) {
                            setText(null);
                        } else {
                            setText(obtenerDescripcionConducta(conducta));
                        }
                    }
                };
            }
        });
    }

    // Método para obtener la descripción de la conducta según el valor
    private String obtenerDescripcionConducta(int conducta) {
        if (conducta >= 0 && conducta < 20) {
            return "Muy mala";
        } else if (conducta >= 20 && conducta < 40) {
            return "Mala";
        } else if (conducta >= 40 && conducta < 60) {
            return "Regular";
        } else if (conducta >= 60 && conducta < 80) {
            return "Buena";
        } else if (conducta >= 80 && conducta <= 100) {
            return "Muy buena";
        } else {
            return "Desconocido"; // Por si el valor está fuera del rango esperado
        }
    }

    // Cargar todas las prisiones y almacenar en un mapa
    private void cargarMapaPrisiones() {
        mapaPrisiones = new HashMap<>();
        try {
            List<Prision> prisiones = prisionDAO.obtenerTodasLasPrisiones();
            for (Prision prision : prisiones) {
                mapaPrisiones.put(prision.getCodPrision(), prision.getNombre());
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al cargar prisiones", "No se pudieron cargar las prisiones.");
        }
    }

    // Cargar todos los talleres en el ComboBox
    private void cargarTodosLosTalleres() {
        try {
            List<Taller> talleres = tallerDAO.obtenerTodosLosTalleres();
            ObservableList<String> tallerNombres = FXCollections.observableArrayList();
            tallerNombres.add("Todos los talleres");  // Añadir opción para mostrar todos los reclusos sin filtro
            for (Taller taller : talleres) {
                tallerNombres.add(taller.getNombre() + " (ID: " + taller.getCodTaller() + ")");
            }
            filtroTallerComboBox.setItems(tallerNombres);
            filtroTallerComboBox.getSelectionModel().selectFirst();  // Seleccionar la opción "Todos los talleres" por defecto
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al cargar talleres", "No se pudieron cargar los talleres.");
        }
    }

    // Cargar todos los reclusos en la tabla
    private void cargarTodosLosReclusos() {
        try {
            List<Recluso> reclusos = reclusoDAO.obtenerTodosLosReclusos();
            mostrarReclusosEnTabla(reclusos);
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al cargar reclusos", "No se pudieron cargar los reclusos.");
        }
    }

    // Mostrar reclusos en la tabla
    private void mostrarReclusosEnTabla(List<Recluso> reclusos) {
        ObservableList<Recluso> reclusoList = FXCollections.observableArrayList(reclusos);
        tablaPresosTableView.setItems(reclusoList);
    }

    // Método para aplicar el filtro por taller
    @FXML
    public void aplicarFiltroTaller(ActionEvent event) {
        String tallerSeleccionado = filtroTallerComboBox.getValue();
        if (tallerSeleccionado.equals("Todos los talleres")) {
            cargarTodosLosReclusos();
        } else {
            int codTaller = obtenerCodTallerDeComboBox(tallerSeleccionado);
            try {
                List<Recluso> reclusosFiltrados = reclusoDAO.obtenerReclusosPorTaller(codTaller);
                mostrarReclusosEnTabla(reclusosFiltrados);
            } catch (SQLException e) {
                e.printStackTrace();
                mostrarAlerta("Error", "Error al aplicar filtro", "No se pudieron aplicar los filtros a los reclusos.");
            }
        }
    }

    // Obtener el ID del taller del ComboBox
    private int obtenerCodTallerDeComboBox(String tallerSeleccionado) {
        String[] partes = tallerSeleccionado.split("ID: ");
        return Integer.parseInt(partes[1].replace(")", "").trim());
    }

    // Regresar al menú principal
    @FXML
    public void volverAlMenuPrincipal(ActionEvent event) {
        try {
            SSPP.SSPP.changeScene("/SSPP/view/Inicio.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al regresar al menú principal", "No se pudo regresar al menú principal.");
        }
    }

    // Mostrar alerta
    private void mostrarAlerta(String titulo, String encabezado, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
