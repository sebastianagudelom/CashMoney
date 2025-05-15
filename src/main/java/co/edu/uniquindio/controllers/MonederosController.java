package co.edu.uniquindio.controllers;

import co.edu.uniquindio.exceptions.VistaCargaException;
import co.edu.uniquindio.managers.GestorClientes;
import co.edu.uniquindio.managers.GestorMonederos;
import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.models.Monedero;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
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
            eliminarMonedero();
            GestorClientes.guardarClientes();
            mostrarAlerta("Éxito", "El saldo del monedero fue transferido a tu cuenta principal.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("No se puede retirar", "La meta de ahorro no ha sido alcanzada aún.", Alert.AlertType.ERROR);
        }

        actualizarTabla();
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
    }

    @FXML
    private void volver(ActionEvent event) throws VistaCargaException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Menu.fxml"));
            Parent root = loader.load();

            MenuController controller = loader.getController();
            controller.setCliente(cliente);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            throw new VistaCargaException("Error al abrir la vista de Menu");
        }
    }
    @FXML
    private void irAAgregarDinero() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/DineroMonedero.fxml"));
            Parent root = loader.load();

            DineroMonederoController controller = loader.getController();
            Monedero seleccionado = tablaMonederos.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                controller.setCliente(cliente);
                controller.setMonedero(seleccionado);

                Stage stage = (Stage) tablaMonederos.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                mostrarAlerta("Error", "Selecciona un monedero para continuar.", Alert.AlertType.WARNING);
            }

        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo abrir la vista de agregar dinero.", Alert.AlertType.ERROR);
        }
    }
}