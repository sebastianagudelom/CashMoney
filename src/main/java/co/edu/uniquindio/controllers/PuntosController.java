package co.edu.uniquindio.controllers;

import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.models.RangoCliente;
import co.edu.uniquindio.managers.SistemaPuntos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PuntosController {

    @FXML
    private Label lblNombre, lblPuntos, lblRango;
    private Cliente clienteActual;

    public void inicializar(Cliente cliente, SistemaPuntos sistema) {
        this.clienteActual = cliente;

        lblNombre.setText("Hola, " + cliente.getNombre());

        int puntos = sistema.consultarPuntos(cliente.getIdentificacion());
        RangoCliente rango = sistema.consultarRango(cliente.getIdentificacion());

        lblPuntos.setText("Puntos acumulados: " + puntos);
        lblRango.setText("Rango actual: " + rango.name());
    }

    @FXML
    private void volverAlMenu(ActionEvent event) {
        Stage stage = (Stage) lblNombre.getScene().getWindow();
        stage.close();
    }

    public Cliente getClienteActual() {
        return clienteActual;
    }

    public void setClienteActual(Cliente clienteActual) {
        this.clienteActual = clienteActual;
    }
}