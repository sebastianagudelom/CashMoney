package co.edu.uniquindio.controllers;

import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.models.Transaccion;
import co.edu.uniquindio.structures.ListaEnlazada;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import co.edu.uniquindio.services.ExportadorPDF;
import co.edu.uniquindio.managers.GestorReversiones;

public class HistorialController {

    @FXML private ListView<String> listViewTransacciones;
    @FXML private Label lblMensaje;
    @FXML private CheckBox chkSoloAlteradas;
    @FXML private Button btnSolicitarReversion;
    private Cliente clienteActual;

    @FXML
    private void initialize() {
        listViewTransacciones.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);

                    if (item.contains("[Revertida]")) {
                        setStyle("-fx-text-fill: red;");
                    } else if (item.contains("Depósito")) {
                        setStyle("-fx-text-fill: green;");
                    } else if (item.contains("Retiro")) {
                        setStyle("-fx-text-fill: red;");
                    } else {
                        setStyle("-fx-text-fill: black;");
                    }
                }
            }
        });
    }


    public void setCliente(Cliente cliente) {
        this.clienteActual = cliente;
        cargarHistorial();
    }

    @FXML
    private void exportarPDF() {
        if (clienteActual != null) {
            ExportadorPDF.exportarHistorial(
                    clienteActual.getHistorialTransacciones().aListaJava(),
                    "Historial_" + clienteActual.getUsuario() + ".pdf"
            );
        }
    }

    @FXML
    private void cargarHistorial() {
        if (clienteActual == null) {
            lblMensaje.setText("Error: Cliente no encontrado.");
            return;
        }

        if (clienteActual.getHistorialTransacciones() == null) {
            clienteActual.setHistorialTransacciones(new ListaEnlazada<>());
        }

        listViewTransacciones.getItems().clear();

        if (clienteActual.getHistorialTransacciones().estaVacia()) {
            lblMensaje.setText("No hay transacciones registradas.");
            return;
        }

        for (Transaccion t : clienteActual.getHistorialTransacciones()) {
            if (chkSoloAlteradas.isSelected()) {
                if (t.isRevertida()) {
                    listViewTransacciones.getItems().add("[REVERTIDA] " + t.toString());
                }
            } else {
                String texto = t.isRevertida() ? "[REVERTIDA] " + t.toString() : t.toString();
                listViewTransacciones.getItems().add(texto);
            }
        }


        lblMensaje.setText(""); // Limpiar mensaje si todo va bien
    }

    @FXML
    private void solicitarReversion() {
        int index = listViewTransacciones.getSelectionModel().getSelectedIndex();

        if (index == -1) {
            lblMensaje.setText("Selecciona una transacción para revertir.");
            return;
        }

        Transaccion transaccionSeleccionada = clienteActual.getHistorialTransacciones().obtener(index);

        if (!"Transferencia Enviada".equals(transaccionSeleccionada.getTipo())) {
            lblMensaje.setText("Solo puedes solicitar reversión de transferencias enviadas.");
            return;
        }

        GestorReversiones.getInstance().solicitarReversion(clienteActual, transaccionSeleccionada);
        lblMensaje.setText("Solicitud de reversión enviada.");
        lblMensaje.setStyle("-fx-text-fill: green;");
    }

    @FXML
    private void volverMenu() {
        ((Stage) lblMensaje.getScene().getWindow()).close();
    }
}