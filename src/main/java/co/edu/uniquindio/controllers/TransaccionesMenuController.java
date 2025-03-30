package co.edu.uniquindio.controllers;

import co.edu.uniquindio.models.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class TransaccionesMenuController {

    private Cliente clienteActual;
    @FXML
    private Label lblClienteInfo;

    public void setCliente(Cliente clienteActual) {
        this.clienteActual = clienteActual;
        if (lblClienteInfo != null && clienteActual != null) {
            lblClienteInfo.setText("Cliente actual: " + clienteActual.getNombre()
                    + "    Saldo: $" + String.format("%.2f", clienteActual.getCuenta().getSaldo()));
        }
    }

    @FXML
    private void irADepositos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Depositos.fxml"));
            Parent root = loader.load();

            DepositosController controller = loader.getController();
            controller.setCliente(clienteActual);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la vista de depósitos.");
            e.printStackTrace();
        }
    }

    @FXML
    private void irARetiros(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Retiros.fxml"));
            Parent root = loader.load();

            RetirosController controller = loader.getController();
            controller.setCliente(clienteActual);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la vista de retiros.");
            e.printStackTrace();
        }
    }

    @FXML
    private void irATransferencias(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Transferencias.fxml"));
            Parent root = loader.load();

            TransferenciasController controller = loader.getController();
            controller.setCliente(clienteActual);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la vista de transferencias.");
            e.printStackTrace();
        }
    }

    @FXML
    private void irATransaccionesProgramadas(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/TransaccionesProgramadas.fxml"));
            Parent root = loader.load();

            TransaccionesProgramadasController controller = loader.getController();
            controller.setCliente(clienteActual);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la vista de transacciones programadas.");
            e.printStackTrace();
        }
    }

    @FXML
    private void irAConsultaHistorial(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Historial.fxml"));
            Parent root = loader.load();

            HistorialController controller = loader.getController();
            controller.setCliente(clienteActual);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la vista de historial.");
            e.printStackTrace();
        }
    }

    @FXML
    private void irAMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Menu.fxml"));
            Parent root = loader.load();

            MenuController controller = loader.getController();
            controller.setCliente(clienteActual);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al volver al menú principal.");
            e.printStackTrace();
        }
    }
}
