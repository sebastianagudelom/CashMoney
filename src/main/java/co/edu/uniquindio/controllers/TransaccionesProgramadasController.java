package co.edu.uniquindio.controllers;

import co.edu.uniquindio.models.GestorClientes;
import co.edu.uniquindio.models.GestorTransaccionesProgramadas;
import co.edu.uniquindio.models.TransaccionProgramada;
import co.edu.uniquindio.models.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class TransaccionesProgramadasController {
    @FXML
    private ComboBox<String> cmbDestinatarios;
    @FXML
    private TextField txtMonto;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button btnProgramar, btnVolver;
    @FXML
    private Label lblMensaje;
    @FXML
    private TableView<TransaccionProgramada> tablaTransacciones;
    @FXML
    private TableColumn<TransaccionProgramada, String> colOrigen;
    @FXML
    private TableColumn<TransaccionProgramada, String> colDestino;
    @FXML
    private TableColumn<TransaccionProgramada, Double> colMonto;
    @FXML
    private TableColumn<TransaccionProgramada, LocalDate> colFecha;
    private GestorTransaccionesProgramadas gestorTransacciones;
    private Cliente usuarioActual;

    public void setCliente(Cliente cliente) {
        this.usuarioActual = cliente;
        cargarDestinatarios(); // este método lo explico abajo
    }

    @FXML
    public void initialize() {
        gestorTransacciones = new GestorTransaccionesProgramadas();

        // Configurar columnas de la tabla
        colOrigen.setCellValueFactory(new PropertyValueFactory<>("usuarioOrigen"));
        colDestino.setCellValueFactory(new PropertyValueFactory<>("usuarioDestino"));
        colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaEjecucion"));

        tablaTransacciones.getItems().setAll(gestorTransacciones.getTransacciones());
    }


    private void cargarDestinatarios() {
        if (usuarioActual == null) return;

        for (Cliente c : GestorClientes.getListaClientes()) {
            if (!c.getUsuario().equals(usuarioActual.getUsuario())) {
                cmbDestinatarios.getItems().add(c.getUsuario());
            }
        }
    }

    @FXML
    private void programarTransaccion() {
        String destinatario = cmbDestinatarios.getValue();
        String montoTexto = txtMonto.getText();
        LocalDate fecha = datePicker.getValue();

        if (destinatario == null || montoTexto.isBlank() || fecha == null) {
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

            TransaccionProgramada nueva = new TransaccionProgramada(
                    usuarioActual.getUsuario(),
                    destinatario,
                    monto,
                    fecha
            );

            gestorTransacciones.agregarTransaccion(nueva);
            lblMensaje.setText("Transacción programada exitosamente.");
            tablaTransacciones.getItems().setAll(gestorTransacciones.getTransacciones());

            txtMonto.clear();
            cmbDestinatarios.setValue(null);
            datePicker.setValue(null);

        } catch (NumberFormatException e) {
            lblMensaje.setText("Monto inválido.");
        }
    }

    @FXML
    private void volverMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Menu.fxml"));
            Parent root = loader.load();

            MenuController menuController = loader.getController();
            menuController.setCliente(usuarioActual);

            Stage stage = new Stage();
            stage.setTitle("Menú Principal");
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana actual
            Stage ventanaActual = (Stage) btnVolver.getScene().getWindow();
            ventanaActual.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al volver al menú.");
        }
    }

}
