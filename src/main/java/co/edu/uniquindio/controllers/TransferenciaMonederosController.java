package co.edu.uniquindio.controllers;

import co.edu.uniquindio.exceptions.VistaCargaException;
import co.edu.uniquindio.managers.GestorClientes;
import co.edu.uniquindio.managers.GestorMonederos;
import co.edu.uniquindio.managers.GestorRelacionesMonederos;
import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.models.Monedero;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class TransferenciaMonederosController {

    @FXML private ComboBox<Monedero> cmbMonederoOrigen;
    @FXML private ComboBox<Monedero> cmbMonederoDestino;
    @FXML private TextField txtMonto;
    @FXML private Label lblSaldoOrigen;
    @FXML private Label lblSaldoDestino;
    @FXML private Button btnTransferir;
    @FXML private Button btnVolver;

    private Cliente cliente;

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        cargarMonederos();
    }

    @FXML
    public void initialize() {
        // Configurar listener para el monedero origen
        cmbMonederoOrigen.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                lblSaldoOrigen.setText(String.format("Saldo disponible: $%.2f", newVal.getSaldo()));
            } else {
                lblSaldoOrigen.setText("Saldo disponible: $0.00");
            }
        });

        // Configurar listener para el monedero destino
        cmbMonederoDestino.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                lblSaldoDestino.setText(String.format("Saldo actual: $%.2f", newVal.getSaldo()));
            } else {
                lblSaldoDestino.setText("Saldo actual: $0.00");
            }
        });
    }

    private void cargarMonederos() {
        if (cliente != null) {
            List<Monedero> monederos = GestorMonederos.getInstance().obtenerTodos(cliente);
            cmbMonederoOrigen.setItems(FXCollections.observableArrayList(monederos));
            cmbMonederoDestino.setItems(FXCollections.observableArrayList(monederos));
        }
    }

    @FXML
    private void transferir() {
        Monedero origen = cmbMonederoOrigen.getValue();
        Monedero destino = cmbMonederoDestino.getValue();

        if (origen == null || destino == null) {
            mostrarAlerta("Error", "Selecciona los monederos de origen y destino.", Alert.AlertType.ERROR);
            return;
        }

        if (origen.equals(destino)) {
            mostrarAlerta("Error", "No puedes transferir dinero al mismo monedero.", Alert.AlertType.ERROR);
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(txtMonto.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El monto debe ser un número válido.", Alert.AlertType.ERROR);
            return;
        }

        if (monto <= 0) {
            mostrarAlerta("Error", "El monto debe ser mayor que cero.", Alert.AlertType.ERROR);
            return;
        }

        if (monto > origen.getSaldo()) {
            mostrarAlerta("Error", "No tienes saldo suficiente para realizar esta transferencia.", Alert.AlertType.ERROR);
            return;
        }

        boolean resultado = GestorMonederos.getInstance().transferirEntreMonederos(
                cliente, origen.getNombre(), destino.getNombre(), monto);

        if (resultado) {
            // Registrar la transferencia en el gestor de relaciones
            GestorRelacionesMonederos.getInstance().registrarTransferencia(
                    cliente.getIdentificacion(), origen.getNombre(), destino.getNombre(), monto);

            GestorClientes.guardarClientes();
            mostrarAlerta("Éxito", String.format("Se transfirieron $%.2f desde %s hacia %s",
                    monto, origen.getNombre(), destino.getNombre()), Alert.AlertType.INFORMATION);

            // Actualizar los saldos mostrados
            lblSaldoOrigen.setText(String.format("Saldo disponible: $%.2f", origen.getSaldo()));
            lblSaldoDestino.setText(String.format("Saldo actual: $%.2f", destino.getSaldo()));

            // Limpiar campos
            txtMonto.clear();
        } else {
            mostrarAlerta("Error", "No se pudo realizar la transferencia.", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void volver(ActionEvent event) throws VistaCargaException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Monederos.fxml"));
            Parent root = loader.load();

            MonederosController controller = loader.getController();
            controller.setCliente(cliente);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            throw new VistaCargaException("Error al abrir la vista de Monederos");
        }
    }
}