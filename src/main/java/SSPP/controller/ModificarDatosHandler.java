package SSPP.controller;

import SSPP.SSPP;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;

public class ModificarDatosHandler {

    @FXML
    public void modificarReclusos(ActionEvent event) {
        try {
            // Cambiar a la vista de modificar reclusos
            SSPP.changeScene("/SSPP/view/ModificarRecluso.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error IO al cargar la vista de modificación de reclusos: " + e.getMessage());
        }
    }
    
    @FXML
    public void modificarTalleres(ActionEvent event) {
        try {
            // Cambiar a la vista de modificar talleres
            SSPP.changeScene("/SSPP/view/ModificarTaller.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error IO al cargar la vista de modificación de talleres: " + e.getMessage());
        }
    }

    @FXML
    public void volverAlMenuPrincipal(ActionEvent event) {
        try {
            // Cambiar a la vista del menú principal
            SSPP.changeScene("/SSPP/view/Inicio.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error IO al cargar la vista del menú principal: " + e.getMessage());
        }
    }
}
