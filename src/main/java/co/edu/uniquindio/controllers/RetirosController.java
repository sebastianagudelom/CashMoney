package co.edu.uniquindio.controllers;

import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.models.GestorClientes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RetirosController {

    @FXML private Label lblSaldo, lblMensaje;
    @FXML private TextField txtMonto;
    private Cliente clienteActual;

    // Método para recibir el cliente actual desde el menú
    public void setCliente(Cliente cliente) {
        this.clienteActual = cliente;
        actualizarSaldo(); // Mostrar saldo al abrir la ventana
    }

    // Método para actualizar el saldo en pantalla
    private void actualizarSaldo() {
        if (clienteActual != null) {
            lblSaldo.setText("$" + String.format("%.2f", clienteActual.getCuenta().getSaldo()));
        } else {
            lblSaldo.setText("No disponible");
        }
    }

    // Método para realizar el retiro
    @FXML
    private void realizarRetiro() {
        try {
            double monto = Double.parseDouble(txtMonto.getText());
            if (monto <= 0) {
                lblMensaje.setText("Ingrese un monto válido.");
                return;
            }
            if (clienteActual != null && clienteActual.getCuenta().retirar(monto)) {
                int puntos = (int) (monto / 100) * 2;
                GestorClientes.getSistemaPuntos().agregarPuntos(clienteActual.getIdentificacion(), puntos);
                GestorClientes.guardarClientes();

                lblMensaje.setText("Retiro exitoso. Puntos ganados: " + puntos);
                lblMensaje.setStyle("-fx-text-fill: green;");
                actualizarSaldo();
            } else {
                lblMensaje.setText("Fondos insuficientes.");
                lblMensaje.setStyle("-fx-text-fill: red;");
            }
        } catch (NumberFormatException e) {
            lblMensaje.setText("Ingrese un número válido.");
        }
    }

    // Método para volver al menú
    @FXML
    private void volverMenu(ActionEvent event) {
        ((Stage) lblSaldo.getScene().getWindow()).close(); // Cierra la ventana actual
    }
}