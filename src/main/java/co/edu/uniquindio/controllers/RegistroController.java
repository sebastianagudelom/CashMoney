package co.edu.uniquindio.controllers;

import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.models.GestorClientes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class RegistroController {

    @FXML private TextField txtNombre, txtUsuario, txtClave, txtIdentificacion, txtCorreo, txtCiudad;
    @FXML private Button btnRegistrarse, btnVolver;
    @FXML private Label lblMensaje;

    @FXML
    private void onRegistrarseAction(ActionEvent event) {
        // Obtener valores de los campos
        String nombre = txtNombre.getText();
        String usuario = txtUsuario.getText();
        String clave = txtClave.getText();
        String identificacion = txtIdentificacion.getText();
        String correo = txtCorreo.getText();
        String ciudad = txtCiudad.getText();

        // Validación básica
        if (nombre.isEmpty() || usuario.isEmpty() || clave.isEmpty() || identificacion.isEmpty() || correo.isEmpty()
                || ciudad.isEmpty()) {
            lblMensaje.setText("Por favor, completa todos los campos.");
            return;
        }

        // Creación del cliente
        Cliente nuevoCliente = new Cliente(nombre, identificacion, correo, usuario, clave, ciudad);
        boolean registroExitoso = GestorClientes.registrarCliente(nombre, identificacion, correo, usuario, clave,
                ciudad);
        if (registroExitoso) {
            lblMensaje.setText("Registro exitoso para: " + nombre);
            System.out.println("Cliente registrado: " + nuevoCliente);
            cambiarEscena("/views/Login.fxml", "Iniciar Sesión");
        } else {
            lblMensaje.setText("El usuario ya existe. Intente con otro.");
        }
        // Redirigir al Login automáticamente después de registrar
        cambiarEscena("/views/Login.fxml", "Iniciar Sesión");
    }

    @FXML
    private void onVolverAction(ActionEvent event) {
        cambiarEscena("/views/Login.fxml",
                "Iniciar Sesión");
    }

    private void cambiarEscena(String rutaFXML, String tituloVentana) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent root = loader.load();

            Stage stage = (Stage) btnRegistrarse.getScene().getWindow();
            if (stage == null) {
                System.out.println("Error: stage es null");
                return;
            }

            stage.setTitle(tituloVentana);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cambiar de escena a: " + rutaFXML);
        }
    }

}
