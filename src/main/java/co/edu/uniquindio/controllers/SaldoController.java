package co.edu.uniquindio.controllers;

import co.edu.uniquindio.exceptions.VistaCargaException;
import co.edu.uniquindio.models.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class SaldoController {

    @FXML private Label lblSaldo;
    @FXML private Label lblNumeroCuenta;
    private Cliente clienteActual;

    public void setCliente(Cliente cliente) {
        this.clienteActual = cliente;
        actualizarSaldo();
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
    private void volverMenu(ActionEvent event)  throws VistaCargaException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Menu.fxml"));
            Parent root = loader.load();

            MenuController controller = loader.getController();
            controller.setCliente(clienteActual);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            throw new VistaCargaException("Error al abrir la vista de Menu");
        }
    }
}