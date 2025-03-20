package co.edu.uniquindio.controllers;

import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.models.GestorClientes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditarPerfilController {

    private Cliente clienteActual;

    @FXML
    private TextField txtNombre, txtIdentificacion, txtCorreo, txtUsuario, txtClave, txtCiudad;

    @FXML
    private Label lblMensaje;

    public void setCliente(Cliente cliente) {
        this.clienteActual = cliente;
        cargarDatosCliente(); // Cargar la información del cliente actual
    }

    private void cargarDatosCliente() {
        if (clienteActual != null) {
            txtNombre.setText(clienteActual.getNombre());
            txtIdentificacion.setText(clienteActual.getIdentificacion());
            txtCorreo.setText(clienteActual.getCorreo());
            txtUsuario.setText(clienteActual.getUsuario());
            txtClave.setText(clienteActual.getClave());
            txtCiudad.setText(clienteActual.getCiudad());
        }
    }

    @FXML
    private void guardarCambios(ActionEvent event) {
        if (clienteActual == null) {
            lblMensaje.setText("Error: No hay usuario activo.");
            return;
        }

        String nuevoNombre = txtNombre.getText().trim();
        String nuevaIdentificacion = txtIdentificacion.getText().trim();
        String nuevoCorreo = txtCorreo.getText().trim();
        String nuevoUsuario = txtUsuario.getText().trim();
        String nuevaClave = txtClave.getText().trim();
        String nuevaCiudad = txtCiudad.getText().trim();

        // Llamar a GestorClientes para actualizar solo los valores ingresados
        boolean actualizado = GestorClientes.actualizarCliente(
                clienteActual.getUsuario(),
                nuevoNombre.isEmpty() ? null : nuevoNombre,
                nuevaIdentificacion.isEmpty() ? null : nuevaIdentificacion,
                nuevoCorreo.isEmpty() ? null : nuevoCorreo,
                nuevoUsuario.isEmpty() ? null : nuevoUsuario,
                nuevaClave.isEmpty() ? null : nuevaClave,
                nuevaCiudad.isEmpty() ? null : nuevaCiudad
        );

        if (actualizado) {
            lblMensaje.setText("Perfil actualizado correctamente.");
            lblMensaje.setStyle("-fx-text-fill: green;");
        } else {
            lblMensaje.setText("Error al actualizar el perfil.");
            lblMensaje.setStyle("-fx-text-fill: red;");
        }
    }


    @FXML
    private void volverMenu(ActionEvent event) {
        ((Stage) txtNombre.getScene().getWindow()).close(); // Cierra la ventana actual
    }
}
