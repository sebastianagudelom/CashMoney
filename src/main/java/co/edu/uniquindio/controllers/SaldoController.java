package co.edu.uniquindio.controllers;

import co.edu.uniquindio.models.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SaldoController {

    @FXML private Label lblSaldo;
    @FXML private Label lblNumeroCuenta;
    private Cliente clienteActual;

    public void setCliente(Cliente cliente) {
        this.clienteActual = cliente;
        actualizarSaldo(); // Mostrar datos al abrir
    }

    @FXML
    private void actualizarSaldo() {
        if (clienteActual != null) {
            lblSaldo.setText("$" + String.format("%.2f", clienteActual.getCuenta().getSaldo()));
            lblNumeroCuenta.setText(clienteActual.getNumeroCuenta());
        } else {
            lblSaldo.setText("No disponible");
            lblNumeroCuenta.setText("No disponible");
        }
    }

    @FXML
    private void volverMenu(ActionEvent event) {
        ((Stage) lblSaldo.getScene().getWindow()).close();
    }
}
