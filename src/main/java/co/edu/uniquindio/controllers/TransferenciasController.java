package co.edu.uniquindio.controllers;

import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.models.GestorClientes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class TransferenciasController {

    @FXML private Label lblSaldo, lblMensaje;
    @FXML private TextField txtMonto;
    @FXML private ComboBox<String> cmbUsuarios;

    private Cliente clienteActual;

    @FXML
    private void initialize() {
    }

    // ✅ Método para recibir el cliente actual
    public void setCliente(Cliente cliente) {
        this.clienteActual = cliente;
        actualizarSaldo();
        cargarUsuarios(); // Cargar usuarios después de asignar cliente
    }

    // ✅ Método para actualizar saldo en pantalla
    private void actualizarSaldo() {
        if (clienteActual != null) {
            lblSaldo.setText("$" + String.format("%.2f", clienteActual.getCuenta().getSaldo()));
        } else {
            lblSaldo.setText("No disponible");
        }
    }

    // Método para cargar usuarios disponibles
    private void cargarUsuarios() {
        if (cmbUsuarios == null) {
            System.out.println("⚠ cmbUsuarios aún no está inicializado.");
            return;
        }
        List<Cliente> clientes = GestorClientes.getListaClientes();
        cmbUsuarios.getItems().clear();
        for (Cliente c : clientes) {
            if (!c.getUsuario().equals(clienteActual.getUsuario())) {
                cmbUsuarios.getItems().add(c.getUsuario());
            }
        }
    }

    // ✅ Método para realizar la transferencia
    @FXML
    private void realizarTransferencia() {
        try {
            String usuarioDestino = cmbUsuarios.getValue();
            double monto = Double.parseDouble(txtMonto.getText());

            if (usuarioDestino == null) {
                lblMensaje.setText("Seleccione un destinatario.");
                return;
            }

            if (monto <= 0) {
                lblMensaje.setText("Ingrese un monto válido.");
                return;
            }

            boolean exito = GestorClientes.transferirSaldo(clienteActual.getUsuario(), usuarioDestino, monto);

            if (exito) {
                lblMensaje.setText("Transferencia exitosa.");
                lblMensaje.setStyle("-fx-text-fill: green;");
                actualizarSaldo(); // Actualizar saldo en pantalla
            } else {
                lblMensaje.setText("Saldo insuficiente o error.");
                lblMensaje.setStyle("-fx-text-fill: red;");
            }

        } catch (NumberFormatException e) {
            lblMensaje.setText("Ingrese un número válido.");
        }
    }

    // ✅ Método para cerrar la ventana
    @FXML
    private void volverMenu(ActionEvent event) {
        ((Stage) lblSaldo.getScene().getWindow()).close();
    }
}
