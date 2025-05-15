package co.edu.uniquindio.controllers;

import co.edu.uniquindio.managers.GestorClientes;
import co.edu.uniquindio.models.Monedero;
import co.edu.uniquindio.models.Cliente;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class DineroMonederoController {

    @FXML
    private Label lblNombreMonedero,  lblSaldoActual;
    @FXML
    private TextField txtMontoAgregar;

    private Monedero monedero;
    private Cliente cliente;

    public void setMonedero(Monedero monedero) {
        this.monedero = monedero;
        if (monedero != null) {
            lblNombreMonedero.setText("Monedero: " + monedero.getNombre());
            lblSaldoActual.setText("Saldo actual: $" + String.format("%.2f", monedero.getSaldo()));
        }
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @FXML
    private void agregarDinero() {
        try {
            double monto = Double.parseDouble(txtMontoAgregar.getText());

            if (monto <= 0) {
                mostrarAlerta("Error", "El monto debe ser mayor que cero.", Alert.AlertType.WARNING);
                return;
            }

            monedero.agregarSaldo(monto);
            lblSaldoActual.setText("Saldo actual: $" + String.format("%.2f", monedero.getSaldo()));

            GestorClientes.guardarClientes();

            mostrarAlerta("Éxito", "Dinero agregado exitosamente.", Alert.AlertType.INFORMATION);
            txtMontoAgregar.clear();
            volver();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Ingrese un valor numérico válido.", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void volver() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Monederos.fxml"));
            Parent root = loader.load();

            MonederosController controller = loader.getController();
            controller.setCliente(cliente);

            Stage stage = (Stage) lblNombreMonedero.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo volver a la vista de monederos.", Alert.AlertType.ERROR);
        }
    }
}