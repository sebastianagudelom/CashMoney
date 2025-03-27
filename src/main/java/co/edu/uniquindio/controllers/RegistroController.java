package co.edu.uniquindio.controllers;

import co.edu.uniquindio.managers.GestorClientes;
import co.edu.uniquindio.services.CorreoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;

public class RegistroController {

    @FXML private TextField txtNombre, txtUsuario, txtClave, txtIdentificacion, txtCorreo, txtCiudad;
    @FXML private Button btnRegistrarse;
    @FXML private Label lblMensaje;

    @FXML
    private void onRegistrarseAction(ActionEvent event) {
        String nombre = txtNombre.getText();
        String usuario = txtUsuario.getText();
        String clave = txtClave.getText();
        String identificacion = txtIdentificacion.getText();
        String correo = txtCorreo.getText();
        String ciudad = txtCiudad.getText();

        if (nombre.isEmpty() || usuario.isEmpty() || clave.isEmpty() || identificacion.isEmpty() || correo.isEmpty()
                || ciudad.isEmpty()) {
            lblMensaje.setText("Por favor, completa todos los campos.");
            return;
        }

        // Generar código aleatorio
        int codigoVerificacion = new Random().nextInt(900000) + 100000;

        // Enviar el código
        CorreoService.enviarCorreo(correo, "Código de verificación",
                "Tu código de verificación para CashMoney es: " + codigoVerificacion);

        // Mostrar input dialog para el código
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Verificación de correo");
        dialog.setHeaderText("Hemos enviado un código a tu correo.");
        dialog.setContentText("Ingresa el código:");

        Optional<String> resultado = dialog.showAndWait();

        if (resultado.isPresent()) {
            String ingreso = resultado.get();
            if (ingreso.equals(String.valueOf(codigoVerificacion))) {
                boolean registroExitoso = GestorClientes.registrarCliente(nombre, identificacion,
                        correo, usuario, clave, ciudad);

                if (registroExitoso) {
                    lblMensaje.setText("Registro exitoso para: " + nombre);
                    cambiarEscena("/views/Login.fxml", "Iniciar Sesión");
                } else {
                    lblMensaje.setText("El usuario, correo o identificacion ya existen. Intente con otro.");
                }
            } else {
                lblMensaje.setText("Código incorrecto. Registro cancelado.");
            }
        } else {
            lblMensaje.setText("Registro cancelado.");
        }
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
