package SSPP;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SSPP extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        SSPP.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/SSPP/view/Inicio.fxml"));
        primaryStage.setTitle("Sistema de Seguimiento de Presos");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void changeScene(String fxml) throws IOException {
        if (primaryStage == null) {
            throw new IllegalStateException("primaryStage no está inicializado");
        }

        // Imprimir la ruta para depurar si es necesario
        System.out.println("Intentando cargar: " + fxml);

        // Cargar el archivo FXML usando la referencia correcta
        Parent pane = FXMLLoader.load(SSPP.class.getResource(fxml));
        if (pane == null) {
            throw new IOException("No se pudo cargar el archivo FXML: " + fxml);
        }

        primaryStage.getScene().setRoot(pane);
    }


    public static void main(String[] args) {
        launch(args);
    }
}