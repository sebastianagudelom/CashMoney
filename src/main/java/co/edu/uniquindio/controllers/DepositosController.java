package co.edu.uniquindio.controllers;

import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.models.GestorClientes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class DepositosController {

    private Cliente clienteActual;
    public void setCliente(Cliente cliente) {
        this.clienteActual = cliente;
    }

    @FXML
    private TextField txtMonto;
    @FXML
    private Label lblMensaje;


    @FXML
    private void realizarDeposito(ActionEvent event) {
        if (clienteActual == null) {
            lblMensaje.setText("Error: No hay usuario activo.");
            return;
        }

        String montoTexto = txtMonto.getText();
        try {
            double monto = Double.parseDouble(montoTexto);
            if (monto <= 0) {
                lblMensaje.setText("Ingrese un monto válido.");
                return;
            }

            // Depositar dinero en la cuenta del usuario
            clienteActual.getCuenta().depositar(monto);
            GestorClientes.guardarClientes();
            lblMensaje.setText("Depósito exitoso. Nuevo saldo: " + clienteActual.getCuenta().getSaldo());

        } catch (NumberFormatException e) {
            lblMensaje.setText("Ingrese un número válido.");
        }
    }

    @FXML
    private void volverMenu(ActionEvent event) {
        cambiarVentana(event, "/views/Menu.fxml");
    }

    private void cambiarVentana(ActionEvent event, String rutaFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent root = loader.load();

            // Pasar el cliente actual al menú para que persista
            MenuController menuController = loader.getController();
            menuController.setCliente(clienteActual);

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
