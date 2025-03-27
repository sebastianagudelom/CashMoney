package co.edu.uniquindio.controllers;

import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.models.Transaccion;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import co.edu.uniquindio.services.ExportadorPDF;

import java.util.ArrayList;

public class HistorialController {
    @FXML private ListView<String> listViewTransacciones;
    @FXML private Label lblMensaje;

    private Cliente clienteActual;

    public void setCliente(Cliente cliente) {
        this.clienteActual = cliente;
        cargarHistorial();
    }

    @FXML
    private void exportarPDF() {
        if (clienteActual != null) {
            ExportadorPDF.exportarHistorial(clienteActual.getHistorialTransacciones(), "Historial_" + clienteActual.getUsuario() + ".pdf");
        }
    }

    private void cargarHistorial() {
        if (clienteActual == null) {
            lblMensaje.setText("Error: Cliente no encontrado.");
            return;
        }

        // Asegurar que historial nunca sea null
        if (clienteActual.getHistorialTransacciones() == null) {
            clienteActual.setHistorialTransacciones(new ArrayList<>());
        }

        if (clienteActual.getHistorialTransacciones().isEmpty()) {
            lblMensaje.setText("No hay transacciones registradas.");
            return;
        }

        listViewTransacciones.getItems().clear();
        for (Transaccion t : clienteActual.getHistorialTransacciones()) {
            listViewTransacciones.getItems().add(t.toString());
        }
    }



    @FXML
    private void volverMenu() {
        ((Stage) lblMensaje.getScene().getWindow()).close();
    }


}
