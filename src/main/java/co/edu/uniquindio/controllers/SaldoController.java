package co.edu.uniquindio.controllers;

import co.edu.uniquindio.models.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SaldoController {

    @FXML private Label lblSaldo;
    private Cliente clienteActual;

    // Método para recibir el cliente que ha iniciado sesión
    public void setCliente(Cliente cliente) {
        this.clienteActual = cliente;
        actualizarSaldo(); // Mostrar el saldo actual al abrir la ventana
    }

    // Método para actualizar el saldo en la vista
    @FXML
    private void actualizarSaldo() {
        if (clienteActual != null) {
            lblSaldo.setText("$" + String.format("%.2f", clienteActual.getCuenta().getSaldo()));
        } else {
            lblSaldo.setText("No disponible");
        }
    }

    // Método para volver al menú
    @FXML
    private void volverMenu(ActionEvent event) {
        ((Stage) lblSaldo.getScene().getWindow()).close(); // Cierra la ventana actual
    }
}
