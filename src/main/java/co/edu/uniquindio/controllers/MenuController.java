package co.edu.uniquindio.controllers;

import co.edu.uniquindio.models.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MenuController {
    private Cliente clienteActual;

    public void setCliente(Cliente clienteActual) {
        this.clienteActual = clienteActual;
    }

    @FXML
    private void irADepositos(ActionEvent event) {
        if (clienteActual == null) {
            System.out.println("Error: No hay usuario activo.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Depositos.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la nueva ventana
            DepositosController depositosController = loader.getController();
            depositosController.setCliente(clienteActual); // Pasar cliente actual
            System.out.println("Ingreso a Depositos." + " Usuario: " + clienteActual.getUsuario());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void irARetiros(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Retiros.fxml"));
            Parent root = loader.load();

            RetirosController retirosController = loader.getController();
            retirosController.setCliente(clienteActual); // Pasar el cliente actual

            Stage stage = new Stage();
            stage.setTitle("Realizar Retiro");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al abrir la ventana de retiro.");
        }
    }
    @FXML
    private void irATransferencias(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Transferencias.fxml"));
            Parent root = loader.load();

            // Obtener el controlador y pasar el cliente actual
            TransferenciasController transferenciasController = loader.getController();
            transferenciasController.setCliente(clienteActual); // Asegurar que el cliente se pasa antes de inicializar

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Transferencias");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al abrir la ventana de transferencias.");
        }
    }

    @FXML
    private void irAConsultaSaldos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Saldo.fxml"));
            Parent root = loader.load();

            SaldoController saldoController = loader.getController();
            saldoController.setCliente(clienteActual); // Pasar el cliente actual
            System.out.println("Ingreso a Consulta de saldos." + " Usuario: " + clienteActual.getUsuario());

            Stage stage = new Stage();
            stage.setTitle("Consulta de Saldo");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al abrir la ventana de consulta de saldo.");
        }
    }

    @FXML
    private void irAConsultaHistorial(ActionEvent event) {
        cambiarVentana(event, "/views/Historial.fxml");
    }
    @FXML
    private void irAPuntos(ActionEvent event) {
        cambiarVentana(event, "/views/Puntos.fxml");
    }
    @FXML
    private void irALogin(ActionEvent event) {
        cambiarVentana(event, "/views/Login.fxml");
    }

    /**
     * Método para cambiar de escena.
     */
    private void cambiarVentana(ActionEvent event, String rutaFXML) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la ventana: " + rutaFXML);
        }
    }
}
