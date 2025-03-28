package co.edu.uniquindio.controllers;

import co.edu.uniquindio.exceptions.CuentaNoEncontradaException;
import co.edu.uniquindio.managers.GestorClientes;
import co.edu.uniquindio.managers.GestorReversiones;
import co.edu.uniquindio.managers.GestorTransacciones;
import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.models.Transaccion;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ReversionesAdminController {

    @FXML private Label lblDetalle;
    @FXML private Button btnAceptar, btnVolver, btnRechazar;

    @FXML private TableView<Transaccion> tablaReversiones;
    @FXML private TableColumn<Transaccion, String> colNombreOrigen;
    @FXML private TableColumn<Transaccion, String> colCuentaOrigen;
    @FXML private TableColumn<Transaccion, String> colNombreDestino;
    @FXML private TableColumn<Transaccion, String> colCuentaDestino;
    @FXML private TableColumn<Transaccion, Double> colMonto;
    @FXML private TableColumn<Transaccion, String> colFecha;

    private Transaccion transaccionActual;

    @FXML
    public void initialize() {
        colNombreOrigen.setCellValueFactory(data -> {
            try {
                Cliente c = GestorClientes.buscarClientePorCuenta(data.getValue().getCuentaOrigen());
                return new SimpleStringProperty(c.getNombre());
            } catch (CuentaNoEncontradaException e) {
                return new SimpleStringProperty("¿?");
            }
        });

        colCuentaOrigen.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCuentaOrigen())
        );

        colNombreDestino.setCellValueFactory(data -> {
            try {
                Cliente c = GestorClientes.buscarClientePorCuenta(data.getValue().getCuentaDestino());
                return new SimpleStringProperty(c.getNombre());
            } catch (CuentaNoEncontradaException e) {
                return new SimpleStringProperty("¿?");
            }
        });

        colCuentaDestino.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCuentaDestino())
        );

        colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));

        colFecha.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getFecha().toString())
        );

        cargarSiguienteSolicitud();
    }

    private void cargarSiguienteSolicitud() {
        if (!GestorReversiones.getInstance().haySolicitudes()) {
            lblDetalle.setText("No hay solicitudes pendientes.");
            tablaReversiones.setItems(FXCollections.observableArrayList());
            btnAceptar.setDisable(true);
            btnRechazar.setDisable(true);
            return;
        }

        transaccionActual = GestorReversiones.getInstance().verUltimaSolicitud();
        lblDetalle.setText(transaccionActual.toString());

        ObservableList<Transaccion> lista = FXCollections.observableArrayList();
        lista.add(transaccionActual);
        tablaReversiones.setItems(lista);
    }

    @FXML
    private void aceptarSolicitud() {
        try {
            GestorReversiones.getInstance().procesarSolicitud();

            Cliente origen = GestorClientes.buscarClientePorCuenta(transaccionActual.getCuentaOrigen());
            Cliente destino = GestorClientes.buscarClientePorCuenta(transaccionActual.getCuentaDestino());

            if (origen == null || destino == null) {
                mostrarAlerta("Error", "No se pudo encontrar alguno de los clientes involucrados.",
                        Alert.AlertType.ERROR);
                return;
            }

            // Realizar la reversión
            GestorTransacciones.retirarSaldo(destino, transaccionActual.getMonto());
            GestorTransacciones.depositarSaldo(origen, transaccionActual.getMonto());

            // Marcar transacción enviada como revertida
            transaccionActual.setRevertida(true);

            // Buscar y marcar la transacción recibida en el historial del destino
            for (Transaccion t : destino.getHistorialTransacciones()) {
                if (t.getTipo().equals("Transferencia Recibida")
                        && t.getMonto() == transaccionActual.getMonto()
                        && t.getCuentaOrigen().equals(transaccionActual.getCuentaOrigen())
                        && t.getCuentaDestino().equals(transaccionActual.getCuentaDestino())
                        && t.getFecha().equals(transaccionActual.getFecha())) {
                    t.setRevertida(true);
                    break;
                }
            }

            GestorClientes.guardarClientes();

            mostrarAlerta("Reversión realizada",
                    "La transacción fue revertida correctamente.",
                    Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo realizar la reversión: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }

        cargarSiguienteSolicitud();
    }

    @FXML
    private void rechazarSolicitud() {
        GestorReversiones.getInstance().procesarSolicitud();
        mostrarAlerta("Rechazada", "La solicitud fue rechazada.", Alert.AlertType.INFORMATION);
        cargarSiguienteSolicitud();
    }

    @FXML
    private void volverMenu() {
        ((Stage) btnVolver.getScene().getWindow()).close();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}