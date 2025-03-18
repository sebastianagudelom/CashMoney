package co.edu.uniquindio.controllers;

import co.edu.uniquindio.models.Cliente;
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
import java.util.ArrayList;
import java.util.List;

public class RegistroController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtUsuario;
    @FXML private TextField txtClave;
    @FXML private TextField txtIdentificacion;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtCiudad;
    @FXML private Button btnRegistrarse;
    @FXML private Button btnVolver;
    @FXML private Label lblMensaje;

    // Lista para almacenar clientes registrados

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
                ||ciudad.isEmpty()) {
            lblMensaje.setText("Por favor, completa todos los campos.");
            return;
        }

        // Creación del cliente
        Cliente nuevoCliente = new Cliente(nombre, identificacion, correo, usuario, clave, ciudad);
        Cliente.agregarCliente(nuevoCliente);


        // Mensaje de confirmación
        lblMensaje.setText("Registro exitoso para: " + nombre);
        System.out.println("Cliente registrado\n" + "Usuaio: " + nuevoCliente.getUsuario() +
                " Clave: " + nuevoCliente.getClave());

        // Redirigir al Login automáticamente después de registrar
        cambiarEscena("/views/Login.fxml", "Iniciar Sesión");
    }

    @FXML
    private void onVolverAction(ActionEvent event) {
        // Cambiar a la pantalla de Login al presionar el botón "Volver"
        cambiarEscena("/views/Login.fxml", "Iniciar Sesión");
    }

    private void cambiarEscena(String rutaFXML, String tituloVentana) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent root = loader.load();

            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setTitle(tituloVentana);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cambiar de escena a: " + rutaFXML);
        }
    }
}
