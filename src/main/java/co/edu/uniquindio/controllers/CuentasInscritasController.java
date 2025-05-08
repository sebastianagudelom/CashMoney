package co.edu.uniquindio.controllers;

import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.managers.GestorClientes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CuentasInscritasController {

    @FXML private Label lblCliente;
    @FXML private ListView<String> listCuentasInscritas;
    @FXML private Button btnCerrar;

    private Cliente clienteActual;

    public void setCliente(Cliente cliente) {
        this.clienteActual = cliente;
        lblCliente.setText("Cliente: " + cliente.getNombre());
        cargarCuentas();
    }

    private void cargarCuentas() {
        ObservableList<String> cuentas = FXCollections.observableArrayList();
        for (String numeroCuenta : clienteActual.getCuentasInscritas()) {
            try {
                Cliente titular = GestorClientes.buscarClientePorCuenta(numeroCuenta);
                String texto = titular.getNombre() + " - " + titular.getCuenta().getNumeroCuenta();
                cuentas.add(texto);
            } catch (Exception e) {
                cuentas.add(numeroCuenta + " (no disponible)");
            }
        }
        listCuentasInscritas.setItems(cuentas);
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) btnCerrar.getScene().getWindow();
        stage.close();
    }
}