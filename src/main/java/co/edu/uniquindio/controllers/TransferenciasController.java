package co.edu.uniquindio.controllers;

import co.edu.uniquindio.exceptions.*;
import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.managers.GestorClientes;
import co.edu.uniquindio.structures.ListaEnlazada;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.*;
import java.util.*;

public class TransferenciasController {

    @FXML private Label lblSaldo, lblMensaje, lblInscripcionMensaje;
    @FXML private TextField txtMonto, txtNumeroCuenta;
    @FXML private ComboBox<String> cmbUsuarios;
    @FXML private ComboBox<String> cmbCategoria;
    private final Map<String, String> cuentasMap = new HashMap<>();
    private Cliente clienteActual;

    @FXML
    private void initialize() {
        cmbCategoria.setItems(FXCollections.observableArrayList("Alimentos", "Transporte",
                "Servicios", "Entretenimiento", "Otros"));
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

    // Método para cargar las cuentas inscritas
    private void cargarUsuarios() {
        if (clienteActual == null || clienteActual.getCuentasInscritas() == null) {
            System.out.println("Error: clienteActual es null o no tiene cuentas inscritas.");
            return;
        }
        if (cmbUsuarios == null) {
            System.out.println("Error: cmbUsuarios no está inicializado.");
            return;
        }
        ListaEnlazada<Cliente> clientes = GestorClientes.getListaClientes();
        if (clientes == null || clientes.estaVacia()) {
            System.out.println("No hay clientes disponibles.");
            return;
        }
        cmbUsuarios.getItems().clear();
        cuentasMap.clear();
        for (Cliente c : clientes) {
            if (!c.getUsuario().equals(clienteActual.getUsuario()) &&
                    clienteActual.getCuentasInscritas().contains(c.getCuenta().getNumeroCuenta())) {

                // Formato: Nombre - Últimos 4 dígitos de la cuenta
                String cuentaFormato = c.getNombre() + " - " + c.getCuenta().getNumeroCuenta().
                        substring(c.getCuenta().getNumeroCuenta().length() - 4);

                // Guardar la relación en el mapa
                cuentasMap.put(cuentaFormato, c.getCuenta().getNumeroCuenta());

                // Agregar el formato al ComboBox
                cmbUsuarios.getItems().add(cuentaFormato);
            }
        }
    }

    // Método para inscribir una cuenta
    @FXML
    private void inscribirCuenta() {
        String numeroCuenta = txtNumeroCuenta.getText().trim();

        if (numeroCuenta.isEmpty()) {
            lblInscripcionMensaje.setText("Ingrese un número de cuenta válido.");
            return;
        }

        try {
            Cliente destino = GestorClientes.buscarClientePorCuenta(numeroCuenta);

            if (GestorClientes.inscribirCuentaParaCliente(clienteActual, numeroCuenta)) {
                lblInscripcionMensaje.setText("Cuenta inscrita con éxito.");
                lblInscripcionMensaje.setStyle("-fx-text-fill: green;");
                cargarUsuarios(); // Actualizar la lista de destinatarios inscritos
            } else {
                lblInscripcionMensaje.setText("La cuenta ya está inscrita.");
                lblInscripcionMensaje.setStyle("-fx-text-fill: red;");
            }

        } catch (CuentaNoEncontradaException e) {
            lblInscripcionMensaje.setText(e.getMessage());
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

            String categoria = cmbCategoria.getValue();
            if (categoria == null || categoria.isBlank()) {
                lblMensaje.setText("Seleccione una categoría.");
                return;
            }

            System.out.println("Transferencia desde: " + clienteActual.getNumeroCuenta() + " hacia: " +
                    numeroCuentaDestino);

            try {
                boolean exito = GestorClientes.transferirSaldoPorCuenta(
                        clienteActual.getNumeroCuenta(), numeroCuentaDestino, monto, categoria);

                if (exito) {
                    GestorClientes.guardarClientes();

                    int puntos = (int) ((monto / 100) * 3);
                    String rangoAnterior = GestorClientes.getSistemaPuntos()
                            .consultarRango(clienteActual.getIdentificacion()).name();

                    GestorClientes.getSistemaPuntos().agregarPuntos(clienteActual.getIdentificacion(),
                            puntos);

                    String nuevoRango = GestorClientes.getSistemaPuntos()
                            .consultarRango(clienteActual.getIdentificacion()).name();

                    if (!rangoAnterior.equals(nuevoRango)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("¡Nuevo Rango!");
                        alert.setHeaderText("¡Felicidades, " + clienteActual.getNombre() + "!");
                        alert.setContentText("Has alcanzado el rango " + nuevoRango + " 🏅");
                        alert.showAndWait();
                    }

                    lblMensaje.setText("Transferencia exitosa. Puntos ganados: " + puntos);
                    lblMensaje.setStyle("-fx-text-fill: green;");
                    actualizarSaldo();
                }

            } catch (TransaccionInvalidaException | CuentaNoEncontradaException e) {
                lblMensaje.setText(e.getMessage());
                lblMensaje.setStyle("-fx-text-fill: red;");
            }

        } catch (NumberFormatException e) {
            lblMensaje.setText("Ingrese un número válido.");
            lblMensaje.setStyle("-fx-text-fill: red;");
        }
    }

    //  Método para cerrar la ventana
    @FXML
    private void volverMenu(ActionEvent event) {
        ((Stage) lblSaldo.getScene().getWindow()).close();
    }
}