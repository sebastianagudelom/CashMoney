package co.edu.uniquindio.controllers;

import co.edu.uniquindio.exceptions.VistaCargaException;
import co.edu.uniquindio.managers.GestorClientes;
import co.edu.uniquindio.managers.GestorTransaccionesProgramadas;
import co.edu.uniquindio.models.TransaccionProgramada;
import co.edu.uniquindio.models.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;

public class TransaccionesProgramadasController {

    @FXML private ComboBox<String> cmbDestinatarios, cmbCategoria;
    @FXML private TextField txtMonto;
    @FXML private DatePicker datePicker;
    @FXML private Button btnProgramar, btnVolver;
    @FXML private Label lblMensaje;
    @FXML private TableView<TransaccionProgramada> tablaTransacciones;
    @FXML private TableColumn<TransaccionProgramada, String> colOrigen, colDestino;
    @FXML private TableColumn<TransaccionProgramada, Double> colMonto;
    @FXML private TableColumn<TransaccionProgramada, LocalDate> colFecha;
    @FXML private Label lblCliente, lblSaldo;

    private GestorTransaccionesProgramadas gestorTransacciones;
    private Cliente usuarioActual;
    private String destinatarioActual;

    public void setCliente(Cliente cliente) {
        this.usuarioActual = cliente;

        if (lblCliente != null && lblSaldo != null) {
            lblCliente.setText("Cliente actual: " + cliente.getNombre());
            lblSaldo.setText("Saldo: $" + String.format("%.2f", cliente.getCuenta().getSaldo()));
        }

        cargarDestinatarios();
        cargarTransaccionesDelUsuario();
    }

    @FXML
    public void initialize() {
        gestorTransacciones = new GestorTransaccionesProgramadas();

        colOrigen.setCellValueFactory(new PropertyValueFactory<>("usuarioOrigen"));
        colDestino.setCellValueFactory(new PropertyValueFactory<>("usuarioDestino"));
        colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaEjecucion"));

        cmbCategoria.setItems(FXCollections.observableArrayList(
                "Alimentos", "Transporte", "Servicios", "Entretenimiento", "Otros"
        ));
    }

    private void cargarDestinatarios() {
        if (usuarioActual == null || usuarioActual.getCuentasInscritas() == null) {
            return;
        }
        cmbDestinatarios.getItems().clear();
        for (Cliente c : GestorClientes.getListaClientes()) {
            if (!c.getUsuario().equals(usuarioActual.getUsuario())
                    && usuarioActual.getCuentasInscritas().contains(c.getCuenta().getNumeroCuenta())) {

                String cuentaFormato = c.getUsuario() + " - " +
                        c.getCuenta().getNumeroCuenta().substring(c.getCuenta().getNumeroCuenta().length() - 4);
                cmbDestinatarios.getItems().add(cuentaFormato);
            }
        }
    }

    private void cargarTransaccionesDelUsuario() {
        ObservableList<TransaccionProgramada> transaccionesUsuario = FXCollections.observableArrayList();
        for (TransaccionProgramada t : gestorTransacciones.getTransacciones()) {
            if (t.getUsuarioOrigen().equals(usuarioActual.getUsuario()) ||
                    t.getUsuarioDestino().equals(usuarioActual.getUsuario())) {
                transaccionesUsuario.add(t);
            }
        }
        tablaTransacciones.setItems(transaccionesUsuario);
    }

    @FXML
    private void programarTransaccion() {
        String destinatario = cmbDestinatarios.getValue();
        destinatarioActual = destinatario.substring(0, destinatario.indexOf(" - "));
        String categoria = cmbCategoria.getValue();
        String montoTexto = txtMonto.getText();
        LocalDate fecha = datePicker.getValue();

        if (destinatario == null || montoTexto.isBlank() || fecha == null || categoria == null) {
            lblMensaje.setText("Todos los campos son obligatorios.");
            return;
        }

        try {
            double monto = Double.parseDouble(montoTexto);

            if (monto <= 0) {
                lblMensaje.setText("El monto debe ser mayor que cero.");
                return;
            }

            if (fecha.isBefore(LocalDate.now())) {
                lblMensaje.setText("La fecha debe ser igual o posterior a hoy.");
                return;
            }

            if (categoria.isBlank()) {
                lblMensaje.setText("Seleccione una categoría.");
                return;
            }

            TransaccionProgramada nueva = new TransaccionProgramada(
                    usuarioActual.getUsuario(),
                    destinatarioActual,
                    monto,
                    fecha
            );
            nueva.setCategoria(categoria);

            gestorTransacciones.agregarTransaccion(nueva);
            lblMensaje.setText("Transacción programada exitosamente.");
            lblMensaje.setStyle("-fx-text-fill: green;");

            cargarTransaccionesDelUsuario();

            txtMonto.clear();
            cmbDestinatarios.setValue(null);
            cmbCategoria.setValue(null);
            datePicker.setValue(null);

        } catch (NumberFormatException e) {
            lblMensaje.setText("Monto inválido.");
        }
    }

    @FXML
    private void volverMenu(ActionEvent event) throws VistaCargaException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/TransaccionesMenu.fxml"));
            Parent root = loader.load();

            TransaccionesMenuController controller = loader.getController();
            controller.setCliente(usuarioActual);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new VistaCargaException("Error al volver al Menu de transacciones");
        }
    }
}