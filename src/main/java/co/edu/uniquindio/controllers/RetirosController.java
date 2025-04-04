package co.edu.uniquindio.controllers;

import co.edu.uniquindio.exceptions.TransaccionInvalidaException;
import co.edu.uniquindio.exceptions.VistaCargaException;
import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.managers.GestorClientes;
import co.edu.uniquindio.managers.GestorTransacciones;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class RetirosController {

    @FXML private Label lblMensaje, lblCliente, lblSaldo;
    @FXML private TextField txtMonto;
    private Cliente clienteActual;

    public void setCliente(Cliente cliente) {
        this.clienteActual = cliente;
        lblCliente.setText("Cliente actual: " + cliente.getNombre());
        lblSaldo.setText("Saldo: $" + String.format("%.2f", cliente.getCuenta().getSaldo()));
    }

    private void actualizarBarraSuperior() {
        if (clienteActual != null) {
            lblCliente.setText("Cliente actual: " + clienteActual.getNombre());
            lblSaldo.setText("Saldo: $" + String.format("%.2f", clienteActual.getCuenta().getSaldo()));
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

            GestorTransacciones.retirarSaldo(clienteActual, monto);

            int puntos = (int) (monto / 100) * 2;
            String rangoAnterior = GestorClientes.getSistemaPuntos()
                    .consultarRango(clienteActual.getIdentificacion()).name();

            GestorClientes.getSistemaPuntos()
                    .agregarPuntos(clienteActual.getIdentificacion(), puntos);

            String nuevoRango = GestorClientes.getSistemaPuntos()
                    .consultarRango(clienteActual.getIdentificacion()).name();

            if (!rangoAnterior.equals(nuevoRango)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("¡Nuevo Rango!");
                alert.setHeaderText("¡Felicidades, " + clienteActual.getNombre() + "!");
                alert.setContentText("Has alcanzado el rango " + nuevoRango + " 🏅");
                alert.showAndWait();
            }

            lblMensaje.setText("Retiro exitoso. Puntos ganados: " + puntos);
            lblMensaje.setStyle("-fx-text-fill: green;");
            actualizarBarraSuperior();

        } catch (NumberFormatException e) {
            lblMensaje.setText("Ingrese un número válido.");
            lblMensaje.setStyle("-fx-text-fill: red;");
        } catch (TransaccionInvalidaException e) {
            lblMensaje.setText(e.getMessage());
            lblMensaje.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void volverMenu(ActionEvent event) throws VistaCargaException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/TransaccionesMenu.fxml"));
            Parent root = loader.load();

            TransaccionesMenuController controller = loader.getController();
            controller.setCliente(clienteActual);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new VistaCargaException("Error al abrir la vista de Menu de transacciones");
        }
    }
}
