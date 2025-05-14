package co.edu.uniquindio.controllers;

import co.edu.uniquindio.exceptions.VistaCargaException;
import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.managers.GestorClientes;
import co.edu.uniquindio.managers.GestorAdministradores;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    private Cliente clienteLogeado;
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtClave;
    @FXML
    private Label lblMensaje;

    @FXML
    private void iniciarSesion(ActionEvent event) {
        String usuario = txtUsuario.getText();
        String contrasena = txtClave.getText();

        if (GestorAdministradores.verificarCredenciales(usuario, contrasena)) {
            try {
                cambiarVentanaAdmin(event);
            } catch (VistaCargaException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        clienteLogeado = GestorClientes.verificarUsuario(usuario, contrasena);

        if (clienteLogeado != null) {
            System.out.println("Inicio de sesion correcto" + " Usuario: " + clienteLogeado.getUsuario() +
                    " Clave: " + clienteLogeado.getClave() + " Nombre: " + clienteLogeado.getNombre());
            try {
                cambiarVentanaCliente(event);
            } catch (VistaCargaException e) {
                throw new RuntimeException(e);
            }
        } else {
            lblMensaje.setText("Usuario o contraseña incorrectos.");
        }
    }

    private void cambiarVentanaCliente(ActionEvent event) throws VistaCargaException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Menu.fxml"));
            Parent root = loader.load();

            MenuController menuController = loader.getController();
            menuController.setCliente(clienteLogeado);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new VistaCargaException("Error al abrir la vista de Menu");
        }
    }

    private void cambiarVentanaAdmin(ActionEvent event) throws VistaCargaException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AdminMenu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new VistaCargaException("Error al abrir la vista de AdminMenu");
        }
    }

    @FXML
    private void irARegistro(ActionEvent event) throws VistaCargaException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Registro.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new VistaCargaException("Error al abrir la vista de Registro");
        }
    }
}