package co.edu.uniquindio.controllers;

import co.edu.uniquindio.exceptions.TransaccionInvalidaException;
import co.edu.uniquindio.exceptions.VistaCargaException;
import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.managers.GestorClientes;
import co.edu.uniquindio.managers.GestorTransacciones;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import java.io.IOException;

public class DepositosController {

    private Cliente clienteActual;
    @FXML
    private TextField txtMonto;
    @FXML
    private Label lblMensaje, lblCliente, lblSaldo;

    public void setCliente(Cliente cliente) {
        this.clienteActual = cliente;
        lblCliente.setText("Cliente actual: " + cliente.getNombre());
        lblSaldo.setText("Saldo: $" + String.format("%.2f", cliente.getCuenta().getSaldo()));
    }

    @FXML
    private void realizarDeposito() {
        if (clienteActual == null) {
            lblMensaje.setText("Error: No hay usuario activo.");
            return;
        }

        String montoTexto = txtMonto.getText();
        try {
            double monto = Double.parseDouble(montoTexto);

            GestorTransacciones.depositarSaldo(clienteActual, monto);

            int puntos = (int) (monto / 50);
            String rangoAnterior = GestorClientes.getSistemaPuntos()
                    .consultarRango(clienteActual.getIdentificacion()).name();

            GestorClientes.getSistemaPuntos()
                    .agregarPuntos(clienteActual.getIdentificacion(), puntos);

            GestorClientes.guardarClientes();

            String nuevoRango = GestorClientes.getSistemaPuntos()
                    .consultarRango(clienteActual.getIdentificacion()).name();

            if (!rangoAnterior.equals(nuevoRango)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("🎉 ¡Nuevo Rango!");
                alert.setHeaderText("¡Felicidades, " + clienteActual.getNombre() + "!");
                alert.setContentText("Has alcanzado el rango " + nuevoRango + " 🏅");
                alert.showAndWait();
            }

            if (clienteActual.getCuenta().getSaldo() < 10000) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Saldo bajo");
                alert.setHeaderText(clienteActual.getNombre() + " su saldo es bajo");
                alert.setContentText("En este momento su saldo es menor a 10.000");
                alert.showAndWait();
            }

            lblSaldo.setText("Saldo: $" + String.format("%.2f", clienteActual.getCuenta().getSaldo()));

            lblMensaje.setText("Depósito exitoso. Puntos ganados: " + puntos);
            lblMensaje.setStyle("-fx-text-fill: green;");
            txtMonto.clear();

        } catch (NumberFormatException e) {
            lblMensaje.setText("Ingrese un número válido.");
            lblMensaje.setStyle("-fx-text-fill: red;");
        } catch (TransaccionInvalidaException e) {
            lblMensaje.setText("Error en el depósito: " + e.getMessage());
            lblMensaje.setStyle("-fx-text-fill: red;");
        }
    }

    // Metodo para regresar al menu
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
            throw new VistaCargaException("Error al volver al menú de transacciones.");
        }
    }
}