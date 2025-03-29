package co.edu.uniquindio.controllers;

import co.edu.uniquindio.managers.GestorClientes;
import co.edu.uniquindio.managers.GestorMonederos;
import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.models.Monedero;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class MonederosController {

    @FXML private Button btnVolver;
    @FXML private TableView<Monedero> tablaMonederos;
    @FXML private TableColumn<Monedero, String> colNombre;
    @FXML private TableColumn<Monedero, Double> colSaldo;
    @FXML private TableColumn<Monedero, Double> colMeta;
    @FXML private TableColumn<Monedero, String> colDescripcion;

    @FXML private TextField txtNombre;
    @FXML private TextField txtMeta;
    @FXML private TextField txtDescripcion;
    @FXML private ComboBox<String> cmbColor;
    @FXML private TextField txtMonto;

    private Cliente cliente;
    private final Map<String, String> coloresDisponibles = new HashMap<>();

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        actualizarTabla();
    }

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colSaldo.setCellValueFactory(new PropertyValueFactory<>("saldo"));
        colMeta.setCellValueFactory(new PropertyValueFactory<>("meta"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        // Establecer colores disponibles
        coloresDisponibles.put("Rojo", "#FF5733");
        coloresDisponibles.put("Azul", "#33C1FF");
        coloresDisponibles.put("Verde", "#33FF57");
        coloresDisponibles.put("Amarillo", "#FFC133");
        coloresDisponibles.put("Morado", "#B733FF");

        cmbColor.setItems(FXCollections.observableArrayList(coloresDisponibles.keySet()));

        tablaMonederos.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Monedero monedero, boolean empty) {
                super.updateItem(monedero, empty);
                if (monedero == null || empty) {
                    setStyle("");
                } else {
                    setStyle("-fx-background-color: " + monedero.getColorHex() + ";");
                }
            }
        });
    }

    private void actualizarTabla() {
        if (cliente != null) {
            ObservableList<Monedero> lista = FXCollections.observableArrayList(
                    GestorMonederos.getInstance().obtenerTodos(cliente)
            );
            tablaMonederos.setItems(lista);
        }
    }

    @FXML
    private void agregarMonedero() {
        String nombre = txtNombre.getText();
        String descripcion = txtDescripcion.getText();
        String nombreColor = cmbColor.getValue();
        double meta;

        if (nombreColor == null || !coloresDisponibles.containsKey(nombreColor)) {
            mostrarAlerta("Error", "Selecciona un color válido.", Alert.AlertType.ERROR);
            return;
        }

        try {
            meta = Double.parseDouble(txtMeta.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "La meta de ahorro debe ser un número.", Alert.AlertType.ERROR);
            return;
        }

        String colorHex = coloresDisponibles.get(nombreColor);
        Monedero m = new Monedero(nombre, meta, descripcion, colorHex);
        GestorMonederos.getInstance().agregarMonedero(cliente, m);
        GestorClientes.guardarClientes();

        mostrarAlerta("Éxito", "Monedero agregado correctamente.", Alert.AlertType.INFORMATION);
        actualizarTabla();
        limpiarCampos();
    }

    @FXML
    private void eliminarMonedero() {
        Monedero seleccionado = tablaMonederos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            GestorMonederos.getInstance().eliminarMonedero(cliente, seleccionado.getNombre());
            GestorClientes.guardarClientes();

            mostrarAlerta("Éxito", "Monedero eliminado.", Alert.AlertType.INFORMATION);
            actualizarTabla();
        } else {
            mostrarAlerta("Error", "Selecciona un monedero para eliminar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void retirarAMonedaPrincipal() {
        Monedero monederoSeleccionado = tablaMonederos.getSelectionModel().getSelectedItem();

        if (monederoSeleccionado == null) {
            mostrarAlerta("Selecciona un monedero", "Por favor selecciona un monedero de la tabla para retirar.", Alert.AlertType.WARNING);
            return;
        }

        boolean exito = GestorMonederos.getInstance().retirarAMonederoPrincipal(cliente, monederoSeleccionado.getNombre());

        if (exito) {
            GestorClientes.guardarClientes();
            mostrarAlerta("Éxito", "El saldo del monedero fue transferido a tu cuenta principal.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("No se puede retirar", "La meta de ahorro no ha sido alcanzada aún.", Alert.AlertType.ERROR);
        }

        actualizarTabla();
    }

    @FXML
    private void agregarDinero() {
        Monedero seleccionado = tablaMonederos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta("Error", "Selecciona un monedero para agregar dinero.", Alert.AlertType.WARNING);
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

        if (cliente.getCuenta().getSaldo() < monto) {
            mostrarAlerta("Error", "No tienes suficiente saldo en tu cuenta.", Alert.AlertType.ERROR);
            return;
        }

        // Descontar de la cuenta principal
        cliente.getCuenta().retirar(monto);

        // Agregar al monedero
        seleccionado.agregarSaldo(monto);

        GestorClientes.guardarClientes();
        tablaMonederos.refresh();

        mostrarAlerta("Éxito", "Saldo agregado al monedero.", Alert.AlertType.INFORMATION);
        actualizarTabla();
        txtMonto.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtMeta.clear();
        txtDescripcion.clear();
        cmbColor.getSelectionModel().clearSelection();
        txtMonto.clear();
    }

    @FXML
    private void volver() {
        ((Stage) btnVolver.getScene().getWindow()).close();
    }
}