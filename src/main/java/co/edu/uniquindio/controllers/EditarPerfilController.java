package co.edu.uniquindio.controllers;

import co.edu.uniquindio.exceptions.VistaCargaException;
import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.managers.GestorClientes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.io.IOException;

public class EditarPerfilController {

    private Cliente clienteActual;
    @FXML
    private TextField txtNombre, txtIdentificacion, txtCorreo, txtUsuario, txtClave, txtCiudad;
    @FXML
    private Label lblMensaje;

    public void setCliente(Cliente cliente) {
        this.clienteActual = cliente;
        cargarDatosCliente();
    }

    private void cargarDatosCliente() {
        if (clienteActual != null) {
            txtNombre.setText(clienteActual.getNombre());
            txtIdentificacion.setText(clienteActual.getIdentificacion());
            txtCorreo.setText(clienteActual.getCorreo());
            txtUsuario.setText(clienteActual.getUsuario());
            txtCiudad.setText(clienteActual.getCiudad());
        }
    }

    @FXML
    private void eliminarCuenta() {
        if (clienteActual == null) {
            lblMensaje.setText("Error: No hay usuario activo.");
            lblMensaje.setStyle("-fx-text-fill: red;");
            return;
        }

        String nombreUsuario = clienteActual.getUsuario();
        String numeroCuenta = clienteActual.getNumeroCuenta();

        boolean eliminado = GestorClientes.eliminarCliente(nombreUsuario);

        if (eliminado) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Cuenta Eliminada");
            alerta.setHeaderText("Cuenta eliminada con éxito");
            alerta.setContentText("El usuario '" + nombreUsuario + "' con la cuenta '" + numeroCuenta + "' ha sido eliminado.");

            alerta.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    Stage stage = (Stage) lblMensaje.getScene().getWindow();
                    stage.close();

                    try {
                        abrirLogin();
                    } catch (VistaCargaException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        } else {
            lblMensaje.setText("Error al eliminar la cuenta.");
            lblMensaje.setStyle("-fx-text-fill: red;");
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

    private void abrirLogin() throws VistaCargaException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
            Parent root = loader.load();

            Stage loginStage = new Stage();
            loginStage.setTitle("Iniciar Sesión");
            loginStage.setScene(new Scene(root));
            loginStage.show();
        } catch (IOException e) {
            throw new VistaCargaException("Error al abrir la vista de Login");        }
    }

    @FXML
    private void volverMenu(ActionEvent event)  throws VistaCargaException {
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
}