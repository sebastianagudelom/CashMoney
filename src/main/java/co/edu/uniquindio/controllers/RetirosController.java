package co.edu.uniquindio.controllers;

import co.edu.uniquindio.exceptions.TransaccionInvalidaException;
import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.managers.GestorClientes;
import co.edu.uniquindio.managers.GestorTransacciones;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RetirosController {

    @FXML private Label lblSaldo, lblMensaje;
    @FXML private TextField txtMonto;
    private Cliente clienteActual;

    public void setCliente(Cliente cliente) {
        this.clienteActual = cliente;
        actualizarSaldo();
    }

    private void actualizarSaldo() {
        if (clienteActual != null) {
            lblSaldo.setText("$" + String.format("%.2f", clienteActual.getCuenta().getSaldo()));
        } else {
            lblSaldo.setText("No disponible");
        }
    }

    @FXML
    private void realizarRetiro() {
        try {
            double monto = Double.parseDouble(txtMonto.getText());

            if (monto <= 0) {
                throw new TransaccionInvalidaException("El monto debe ser mayor que cero.");
            }

            if (clienteActual == null) {
                throw new TransaccionInvalidaException("Cliente no encontrado.");
            }

            // Intenta retirar (esto lanza la excepción si no hay saldo)
            GestorTransacciones.retirarSaldo(clienteActual, monto);

            // Calcular puntos
            int puntos = (int) (monto / 100) * 2;
            String rangoAnterior = GestorClientes.getSistemaPuntos()
                    .consultarRango(clienteActual.getIdentificacion()).name();

            GestorClientes.getSistemaPuntos()
                    .agregarPuntos(clienteActual.getIdentificacion(), puntos);

            String nuevoRango = GestorClientes.getSistemaPuntos()
                    .consultarRango(clienteActual.getIdentificacion()).name();

            // Mostrar alerta si cambia de rango
            if (!rangoAnterior.equals(nuevoRango)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("¡Nuevo Rango!");
                alert.setHeaderText("¡Felicidades, " + clienteActual.getNombre() + "!");
                alert.setContentText("Has alcanzado el rango " + nuevoRango + " 🏅");
                alert.showAndWait();
            }

            lblMensaje.setText("Retiro exitoso. Puntos ganados: " + puntos);
            lblMensaje.setStyle("-fx-text-fill: green;");
            actualizarSaldo();

        } catch (NumberFormatException e) {
            lblMensaje.setText("Ingrese un número válido.");
            lblMensaje.setStyle("-fx-text-fill: red;");
        } catch (TransaccionInvalidaException e) {
            lblMensaje.setText(e.getMessage());
            lblMensaje.setStyle("-fx-text-fill: red;");
        }
    }


    @FXML
    private void volverMenu(ActionEvent event) {
        ((Stage) lblSaldo.getScene().getWindow()).close();
    }
}