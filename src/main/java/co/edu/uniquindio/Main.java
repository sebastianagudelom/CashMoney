package co.edu.uniquindio;

import co.edu.uniquindio.models.GestorClientes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Cargar el archivo FXML
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Login.fxml")));

        // Configurar la escena
        primaryStage.setTitle("CashMoney");
        primaryStage.setScene(new Scene(root, 600, 400)); // O el tamaño que prefieras
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(300);


        // Mostrar la ventana
        primaryStage.show();
    }

    public static void main(String[] args) {
        GestorClientes.cargarClientes(); // Cargar clientes antes de iniciar
        GestorClientes.imprimirClientes(); // Para ver los clientes cargados
        launch(args);
    }

}