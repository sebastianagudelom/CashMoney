package co.edu.uniquindio.controllers;

import co.edu.uniquindio.exceptions.CuentaNoEncontradaException;
import co.edu.uniquindio.managers.GestorClientes;
import co.edu.uniquindio.models.Cliente;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InscripcionCuentasController {

    @FXML private TextField txtNumeroCuenta;
    @FXML private Label lblInscripcionMensaje;
    @FXML private Button btnInscribir, btnCerrar;

    private Cliente clienteActual;

    public void setCliente(Cliente cliente) {
        this.clienteActual = cliente;
    }

    @FXML
    private void inscribirCuenta() {
        String numeroCuenta = txtNumeroCuenta.getText().trim();

        if (numeroCuenta.isEmpty()) {
            lblInscripcionMensaje.setText("Ingrese un número de cuenta válido.");
            lblInscripcionMensaje.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            GestorClientes.buscarClientePorCuenta(numeroCuenta);

            if (GestorClientes.inscribirCuentaParaCliente(clienteActual, numeroCuenta)) {
                lblInscripcionMensaje.setText("Cuenta inscrita con éxito.");
                lblInscripcionMensaje.setStyle("-fx-text-fill: green;");
            } else {
                lblInscripcionMensaje.setText("La cuenta ya está inscrita.");
                lblInscripcionMensaje.setStyle("-fx-text-fill: orange;");
            }

        } catch (CuentaNoEncontradaException e) {
            lblInscripcionMensaje.setText(e.getMessage());
            lblInscripcionMensaje.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) btnCerrar.getScene().getWindow();
        stage.close();
    }
}