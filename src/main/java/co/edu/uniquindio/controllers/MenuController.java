package co.edu.uniquindio.controllers;

import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.managers.GestorClientes;
import co.edu.uniquindio.managers.GestorTransaccionesProgramadas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class MenuController {
    private Cliente clienteActual;

    @FXML
    private Label lblSaludo;

    public void setCliente(Cliente clienteActual) {
        this.clienteActual = clienteActual;
        lblSaludo.setText("Bienvenido " + clienteActual.getNombre() + ", ¿Qué desea hacer hoy?");

        GestorTransaccionesProgramadas gestor = new GestorTransaccionesProgramadas();
        gestor.ejecutarTransacciones();

        if (clienteActual.getCuenta().getSaldo() < 10000) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Saldo bajo");
            alert.setHeaderText(clienteActual.getNombre() + " su saldo es bajo");
            alert.setContentText("En este momento su saldo es menor a 10.000");
            alert.showAndWait();
        }
    }

    @FXML
    private void irAMenuTransacciones(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/TransaccionesMenu.fxml"));
            Parent root = loader.load();

            TransaccionesMenuController controller = loader.getController();
            controller.setCliente(clienteActual);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al abrir el menú de transacciones.");
        }
    }

    @FXML
    private void irAConsultaSaldos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Saldo.fxml"));
            Parent root = loader.load();

            SaldoController saldoController = loader.getController();
            saldoController.setCliente(clienteActual);

            Stage stage = new Stage();
            stage.setTitle("Consulta de Saldo");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al abrir la ventana de consulta de saldo.");
        }
    }

    @FXML
    private void irEditarPerfil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EditarPerfil.fxml"));
            Parent root = loader.load();

            EditarPerfilController editarPerfilController = loader.getController();
            editarPerfilController.setCliente(clienteActual);

            Stage stage = new Stage();
            stage.setTitle("Editar Perfil");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al abrir la ventana de editar perfil.");
        }
    }

    @FXML
    private void irAPuntos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Puntos.fxml"));
            Parent root = loader.load();

            PuntosController controller = loader.getController();
            controller.inicializar(clienteActual, GestorClientes.getSistemaPuntos());

            Stage stage = new Stage();
            stage.setTitle("Mis Puntos");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al abrir la ventana de puntos.");
        }
    }

    @FXML
    private void irAAnalisis(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Analisis.fxml"));
            Parent root = loader.load();

            AnalisisController controller = loader.getController();
            controller.setCliente(clienteActual);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al abrir la ventana de analisis.");
        }
    }

    @FXML
    private void irANotificaciones(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Notificaciones.fxml"));
            Parent root = loader.load();

            NotificacionesController controller = loader.getController();
            controller.setCliente(clienteActual);

            Stage stage = new Stage();
            stage.setTitle("Notificaciones");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al abrir la ventana de notificaciones.");
        }
    }

    @FXML
    private void irAMonederos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Monederos.fxml"));
            Parent root = loader.load();

            MonederosController controller = loader.getController();
            controller.setCliente(clienteActual);

            Stage stage = new Stage();
            stage.setTitle("Monederos");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al abrir la ventana de monederos.");
        }
    }

    @FXML
    private void irACanjePuntos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CanjePuntos.fxml"));
            Parent root = loader.load();

            CanjePuntosController controller = loader.getController();
            controller.setCliente(clienteActual);

            Stage stage = new Stage();
            stage.setTitle("Canje de Puntos");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al abrir la ventana de canje de puntos.");
        }
    }

    @FXML
    private void irALogin(ActionEvent event) {
        cambiarVentana(event, "/views/Login.fxml");
    }

    private void cambiarVentana(ActionEvent event, String rutaFXML) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la ventana: " + rutaFXML);
        }
    }
}
