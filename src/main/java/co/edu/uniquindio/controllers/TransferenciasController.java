package co.edu.uniquindio.controllers;

import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.models.GestorClientes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

public class TransferenciasController {

    @FXML
    private Label lblSaldo, lblMensaje;
    @FXML
    private TextField txtMonto;
    @FXML
    private ComboBox<String> cmbUsuarios;
    @FXML
    private TextField txtNumeroCuenta;
    @FXML
    private Label lblInscripcionMensaje;
    private Map<String, String> cuentasMap = new HashMap<>(); // Mapa para relacionar texto mostrado con el número real de cuenta


    private Cliente clienteActual;

    @FXML
    private void initialize() {
    }

    //  Método para recibir el cliente actual
    public void setCliente(Cliente cliente) {
        this.clienteActual = cliente;
        actualizarSaldo();
        cargarUsuarios(); // Cargar usuarios después de asignar cliente
    }

    //  Método para actualizar saldo en pantalla
    private void actualizarSaldo() {
        if (clienteActual != null) {
            lblSaldo.setText("$" + String.format("%.2f", clienteActual.getCuenta().getSaldo()));
        } else {
            lblSaldo.setText("No disponible");
        }
    }

    // Método para cargar usuarios disponibles
    private void cargarUsuarios() {
        if (clienteActual == null || clienteActual.getCuentasInscritas() == null) {
            System.out.println("⚠ Error: clienteActual es null o no tiene cuentas inscritas.");
            return;
        }

        if (cmbUsuarios == null) {
            System.out.println("⚠ Error: cmbUsuarios no está inicializado.");
            return;
        }

        List<Cliente> clientes = GestorClientes.getListaClientes();
        if (clientes == null || clientes.isEmpty()) {
            System.out.println("⚠ No hay clientes disponibles.");
            return;
        }

        cmbUsuarios.getItems().clear();
        cuentasMap.clear();

        for (Cliente c : clientes) {
            if (!c.getUsuario().equals(clienteActual.getUsuario()) &&
                    clienteActual.getCuentasInscritas().contains(c.getCuenta().getNumeroCuenta())) {

                // Formato: Nombre - Últimos 4 dígitos de la cuenta
                String cuentaFormato = c.getNombre() + " - " + c.getCuenta().getNumeroCuenta().substring(c.getCuenta().getNumeroCuenta().length() - 4);

                // Guardar la relación en el mapa
                cuentasMap.put(cuentaFormato, c.getCuenta().getNumeroCuenta());

                // Agregar el formato al ComboBox
                cmbUsuarios.getItems().add(cuentaFormato);
            }
        }
    }





    @FXML
    private void inscribirCuenta() {
        String numeroCuenta = txtNumeroCuenta.getText().trim();

        if (numeroCuenta.isEmpty()) {
            lblInscripcionMensaje.setText("Ingrese un número de cuenta válido.");
            return;
        }

        Cliente destino = GestorClientes.buscarClientePorCuenta(numeroCuenta);
        if (destino == null) {
            lblInscripcionMensaje.setText("Cuenta no encontrada.");
            return;
        }

        if (clienteActual.inscribirCuenta(numeroCuenta)) {
            lblInscripcionMensaje.setText("Cuenta inscrita con éxito.");
            lblInscripcionMensaje.setStyle("-fx-text-fill: green;");
            cargarUsuarios(); // Actualizar la lista de destinatarios inscritos
        } else {
            lblInscripcionMensaje.setText("La cuenta ya está inscrita.");
            lblInscripcionMensaje.setStyle("-fx-text-fill: red;");
        }
    }





    //  Método para realizar la transferencia
    @FXML
    private void realizarTransferencia() {
        try {
            String seleccionado = cmbUsuarios.getValue();
            if (seleccionado == null) {
                lblMensaje.setText("Seleccione un destinatario.");
                return;
            }

            // Obtener el número de cuenta real desde el mapa
            String numeroCuentaDestino = cuentasMap.get(seleccionado);
            if (numeroCuentaDestino == null) {
                lblMensaje.setText("Error: No se encontró la cuenta.");
                return;
            }

            double monto = Double.parseDouble(txtMonto.getText());

            if (monto <= 0) {
                lblMensaje.setText("Ingrese un monto válido.");
                return;
            }

            // Verificar que realmente estamos pasando números de cuenta
            System.out.println("✅ Transferencia desde: " + clienteActual.getNumeroCuenta() + " hacia: " + numeroCuentaDestino);

            boolean exito = GestorClientes.transferirSaldoPorCuenta(clienteActual.getNumeroCuenta(), numeroCuentaDestino, monto);

            if (exito) {
                lblMensaje.setText("Transferencia exitosa.");
                lblMensaje.setStyle("-fx-text-fill: green;");
                actualizarSaldo();
            } else {
                lblMensaje.setText("Saldo insuficiente o error.");
                lblMensaje.setStyle("-fx-text-fill: red;");
            }

        } catch (NumberFormatException e) {
            lblMensaje.setText("Ingrese un número válido.");
        }
    }





    //  Método para cerrar la ventana
    @FXML
    private void volverMenu(ActionEvent event) {
        ((Stage) lblSaldo.getScene().getWindow()).close();
    }
}
