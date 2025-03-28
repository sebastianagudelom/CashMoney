package co.edu.uniquindio.controllers;

import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.managers.GestorClientes;
import co.edu.uniquindio.models.Transaccion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminMenuController {

    @FXML
    private TableView<Cliente> tablaClientes;
    @FXML
    private TableColumn<Cliente, String> colNombre;
    @FXML
    private TableColumn<Cliente, String> colUsuario;
    @FXML
    private TableColumn<Cliente, String> colCorreo;
    @FXML
    private TableColumn<Cliente, String> colCiudad;
    @FXML
    private TableView<Transaccion> tablaTransacciones;
    @FXML
    private TableColumn<Transaccion, String> colTipo;
    @FXML
    private TableColumn<Transaccion, Double> colMonto;
    @FXML
    private TableColumn<Transaccion, String> colReferencia;
    @FXML
    private Label lblMensaje;

    @FXML
    private void initialize() {
        // Configurar columnas de clientes
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colUsuario.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getUsuario()));
        colCorreo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCorreo()));
        colCiudad.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCiudad()));

        // Configurar columnas de transacciones
        colTipo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTipo()));
        colMonto.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getMonto()));
        colReferencia.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCategoria()));

        cargarClientes();
    }

    private void cargarClientes() {
        ObservableList<Cliente> clientes = FXCollections.observableArrayList();

        for (Cliente cliente : GestorClientes.getListaClientes()) {
            clientes.add(cliente);
        }

        tablaClientes.setItems(clientes);
    }


    @FXML
    private void eliminarCliente() {
        Cliente seleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            boolean eliminado = GestorClientes.eliminarCliente(seleccionado.getUsuario());
            if (eliminado) {
                lblMensaje.setText("Cliente eliminado correctamente");
                cargarClientes();
                tablaTransacciones.getItems().clear();
            } else {
                lblMensaje.setText("Error al eliminar el cliente");
            }
        } else {
            lblMensaje.setText("Selecciona un cliente primero");
        }
    }

    @FXML
    private void verTransacciones() {
        Cliente seleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            tablaTransacciones.setItems(
                    FXCollections.observableArrayList(seleccionado.getHistorialTransacciones().aListaJava())
            );
        } else {
            lblMensaje.setText("Selecciona un cliente para ver transacciones");
        }
    }

    @FXML
    private void abrirReversiones() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ReversionesAdmin.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Solicitudes de Reversión");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cerrarSesion() {
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/views/Login.fxml"));
        try {
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) tablaClientes.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}