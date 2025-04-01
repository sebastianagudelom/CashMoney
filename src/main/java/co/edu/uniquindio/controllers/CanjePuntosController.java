package co.edu.uniquindio.controllers;

import co.edu.uniquindio.managers.GestorCanjes;
import co.edu.uniquindio.managers.GestorClientes;
import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.models.Monedero;
import co.edu.uniquindio.structures.ListaEnlazada;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class CanjePuntosController {

    @FXML private Label labelPuntos;
    @FXML private ComboBox<String> comboCanje;
    @FXML private ComboBox<Monedero> comboMonederos;
    @FXML private Button btnCanjear, btnVolver;

    private Cliente cliente;

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        inicializarDatos();
    }

    private void inicializarDatos() {
        labelPuntos.setText("Puntos actuales: " + GestorClientes.getSistemaPuntos()
                .consultarPuntos(cliente.getIdentificacion()));

        comboCanje.getItems().addAll(
                "500 puntos → +10",
                "1000 puntos → +50",
                "2000 puntos → +150"
        );

        ListaEnlazada<Monedero> listaMonederos = cliente.getMonederos();
        ObservableList<Monedero> observableMonederos =
                FXCollections.observableArrayList(listaMonederos.aListaJava());

        comboMonederos.setItems(observableMonederos);

        btnCanjear.setOnAction(e -> realizarCanje());
    }

    private void realizarCanje() {
        String opcionSeleccionada = comboCanje.getValue();
        Monedero monederoSeleccionado = comboMonederos.getValue();

        if (opcionSeleccionada == null || monederoSeleccionado == null) {
            mostrarAlerta("Debes seleccionar una opción de canje y un monedero.");
            return;
        }

        int puntosRequeridos = 0;
        int bono = 0;

        switch (opcionSeleccionada) {
            case "500 puntos → +10" -> { puntosRequeridos = 500; bono = 10; }
            case "1000 puntos → +50" -> { puntosRequeridos = 1000; bono = 50; }
            case "2000 puntos → +150" -> { puntosRequeridos = 2000; bono = 150; }
            default -> {
                mostrarAlerta("Opción de canje no válida.");
                return;
            }
        }

        if (GestorClientes.getSistemaPuntos().consultarPuntos(cliente.getIdentificacion()) < puntosRequeridos)
        {
            mostrarAlerta("No tienes suficientes puntos para este canje.");
            return;
        }

        boolean exitoso = GestorCanjes.canjearBonoMonedero(cliente, puntosRequeridos, monederoSeleccionado);

        if (exitoso) {
            mostrarInfo("¡Canje exitoso! Se añadieron " + bono + " unidades al monedero.");
            labelPuntos.setText("Puntos actuales: " + GestorClientes.getSistemaPuntos()
                    .consultarPuntos(cliente.getIdentificacion()));
        } else {
            mostrarAlerta("No se pudo completar el canje.");
        }
    }


    @FXML
    private void volver() {
        ((Stage) btnVolver.getScene().getWindow()).close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Alerta");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}