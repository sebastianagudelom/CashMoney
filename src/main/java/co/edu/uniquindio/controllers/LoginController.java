package co.edu.uniquindio.controllers;

import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.managers.GestorClientes;
import co.edu.uniquindio.managers.GestorAdministradores; // <--- Agrega esto

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

        // Validación modo administrador
        if (GestorAdministradores.verificarCredenciales(usuario, contrasena)) {
            cambiarVentanaAdmin(event, "/views/AdminMenu.fxml");
            return;
        }

        // Validación de cliente normal
        clienteLogeado = GestorClientes.verificarUsuario(usuario, contrasena);

        if (clienteLogeado != null) {
            System.out.println("Inicio de sesion correcto" + " Usuario: " + clienteLogeado.getUsuario() +
                    " Clave: " + clienteLogeado.getClave() + " Nombre: " + clienteLogeado.getNombre());
            cambiarVentanaCliente(event, "/views/Menu.fxml");
        } else {
            lblMensaje.setText("Usuario o contraseña incorrectos.");
        }
    }

    private void cambiarVentanaCliente(ActionEvent event, String rutaFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent root = loader.load();

            MenuController menuController = loader.getController();
            menuController.setCliente(clienteLogeado);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la ventana: " + rutaFXML);
        }
    }

    private void cambiarVentanaAdmin(ActionEvent event, String rutaFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la ventana de administrador.");
        }
    }

    @FXML
    private void irARegistro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Registro.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la ventana de registro.");
        }
    }
}