package co.edu.uniquindio.controllers;

import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.models.GestorClientes;
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

    private Cliente clienteLogeado;  // Variable para almacenar el cliente actual

    @FXML
    private Button btnIniciarSesion;
    @FXML
    private Hyperlink linkRegistro;
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

        clienteLogeado = GestorClientes.verificarUsuario(usuario, contrasena);

        if (clienteLogeado != null) {
            System.out.println("Inicio de sesion correcto" + " Usuario: " + clienteLogeado.getUsuario() +
                    " Clave: " + clienteLogeado.getClave() + " Nombre: " + clienteLogeado.getNombre());
            cambiarVentana(event, "/views/Menu.fxml");
        } else {
            lblMensaje.setText("Usuario o contraseña incorrectos.");
        }
    }


    private void cambiarVentana(ActionEvent event, String rutaFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent root = loader.load();

            // Obtener el controlador de la nueva ventana
            MenuController menuController = loader.getController();
            menuController.setCliente(clienteLogeado); // Pasar el cliente logeado

            // Cambiar la ventana
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la ventana: " + rutaFXML);
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