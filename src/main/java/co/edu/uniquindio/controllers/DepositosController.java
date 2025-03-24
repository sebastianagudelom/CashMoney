package co.edu.uniquindio.controllers;

import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.models.GestorClientes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import java.io.IOException;

public class DepositosController {

    private Cliente clienteActual;
    @FXML
    private TextField txtMonto;
    @FXML
    private Label lblMensaje;

    public void setCliente(Cliente cliente) {
        this.clienteActual = cliente;
    }

    // Métodos para realizar un depósito
    @FXML
    private void realizarDeposito(ActionEvent event) {
        if (clienteActual == null) {
            lblMensaje.setText("Error: No hay usuario activo.");
            return;
        }
        String montoTexto = txtMonto.getText();
        try {
            double monto = Double.parseDouble(montoTexto);
            if (monto <= 0) {
                lblMensaje.setText("Ingrese un monto válido.");
                return;
            }
            clienteActual.getCuenta().depositar(monto);
            int puntos = (int) (monto / 50);  // 1 punto por cada $50
            String rangoAnterior = GestorClientes.getSistemaPuntos().consultarRango(clienteActual.
                    getIdentificacion()).name();
            GestorClientes.getSistemaPuntos().agregarPuntos(clienteActual.getIdentificacion(), puntos);
            GestorClientes.guardarClientes();
            String nuevoRango = GestorClientes.getSistemaPuntos()
                    .consultarRango(clienteActual.getIdentificacion()).name();
            if (!rangoAnterior.equals(nuevoRango)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("🎉 ¡Nuevo Rango!");
                alert.setHeaderText("¡Felicidades, " + clienteActual.getNombre() + "!");
                alert.setContentText("Has alcanzado el rango " + nuevoRango + " 🏅");
                alert.showAndWait();
            }
            lblMensaje.setText("Depósito exitoso. Puntos ganados: " + puntos + ". Nuevo saldo: " +
                    clienteActual.getCuenta().getSaldo());
        } catch (NumberFormatException e) {
            lblMensaje.setText("Ingrese un número válido.");
        }
    }

    // Método para volver al menú
    @FXML
    private void volverMenu(ActionEvent event) {
        cambiarVentana(event, "/views/Menu.fxml");
    }

    // Método para cambiar la pestaña
    private void cambiarVentana(ActionEvent event, String rutaFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent root = loader.load();

            // Pasar el cliente actual al menú para que persista
            MenuController menuController = loader.getController();
            menuController.setCliente(clienteActual);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la ventana: " + rutaFXML);
        }
    }
}