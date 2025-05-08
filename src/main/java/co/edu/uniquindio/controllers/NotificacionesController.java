package co.edu.uniquindio.controllers;

import co.edu.uniquindio.models.Notificacion;
import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.structures.ListaCircular;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class NotificacionesController {

    @FXML private ListView<String> listaNotificaciones;
    @FXML private Label lblMensaje;

    private Cliente clienteActual;

    public void setCliente(Cliente cliente) {
        this.clienteActual = cliente;
        cargarNotificaciones();
    }

    private void cargarNotificaciones() {
        if (clienteActual == null || clienteActual.getNotificaciones() == null ||
                clienteActual.getNotificaciones().estaVacia()) {
            lblMensaje.setText("No hay notificaciones.");
            listaNotificaciones.setItems(FXCollections.observableArrayList());
            return;
        }

        ListaCircular<Notificacion> lista = clienteActual.getNotificaciones();
        ObservableList<String> items = FXCollections.observableArrayList();

        for (Notificacion n : lista) {
            items.add(n.getMensaje());
        }

        listaNotificaciones.setItems(items);
        lblMensaje.setText("");
    }

    @FXML
    private void volver() {
        ((Stage) lblMensaje.getScene().getWindow()).close();
    }
}