package co.edu.uniquindio.controllers;

import co.edu.uniquindio.exceptions.VistaCargaException;
import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.models.RangoCliente;
import co.edu.uniquindio.managers.SistemaPuntos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

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
    private void volverAlMenu(ActionEvent event) throws VistaCargaException {
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

    public Cliente getClienteActual() {
        return clienteActual;
    }

    public void setClienteActual(Cliente clienteActual) {
        this.clienteActual = clienteActual;
    }
}