package co.edu.uniquindio.controllers;

import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.models.RangoCliente;
import co.edu.uniquindio.models.SistemaPuntos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PuntosController {

    @FXML
    private Label lblNombre, lblPuntos, lblRango;

    private Cliente clienteActual;
    private SistemaPuntos sistemaPuntos;

    public void inicializar(Cliente cliente, SistemaPuntos sistema) {
        this.clienteActual = cliente;
        this.sistemaPuntos = sistema;

        lblNombre.setText("Hola, " + cliente.getNombre());

        int puntos = sistemaPuntos.consultarPuntos(cliente.getIdentificacion());
        RangoCliente rango = sistemaPuntos.consultarRango(cliente.getIdentificacion());

        lblPuntos.setText("Puntos acumulados: " + puntos);
        lblRango.setText("Rango actual: " + rango.name());
    }

    @FXML
    private void volverAlMenu(ActionEvent event) {
        Stage stage = (Stage) lblNombre.getScene().getWindow();
        stage.close();
    }
}
