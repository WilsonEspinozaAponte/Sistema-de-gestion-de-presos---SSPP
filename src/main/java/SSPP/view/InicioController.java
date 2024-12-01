package SSPP.view;

import SSPP.SSPP;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;

public class InicioController {

    @FXML
    public void cerrarSesion(ActionEvent event) {
        // Lógica para cerrar sesión (si es necesario)
        System.out.println("Cerrando sesión...");
        System.exit(0); // Cierra la aplicación
    }

    @FXML
    public void abrirRegistroPreso(ActionEvent event) {
        try {
            SSPP.changeScene("/SSPP/view/RegistroPreso.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    
    public void abrirRegistroSentencia(ActionEvent event) {
        try {
            SSPP.changeScene("/SSPP/view/RegistroSentencia.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void abrirRegistroTaller(ActionEvent event) {
        try {
            SSPP.changeScene("/SSPP/view/RegistroTaller.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirRegistroPresoEnTaller(ActionEvent event) {
        try {
            SSPP.changeScene("/SSPP/view/RegistroMatricula.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirModificacionDatos(ActionEvent event) {
        try {
            SSPP.changeScene("/SSPP/view/ModificacionDatos.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void abrirVisualizarPresos(ActionEvent event) {
        try {
            SSPP.changeScene("/SSPP/view/VisualizarPresos.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
}
