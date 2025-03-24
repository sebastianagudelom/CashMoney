package co.edu.uniquindio.controllers;

import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.models.GestorClientes;
import co.edu.uniquindio.models.GestorTransaccionesProgramadas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    }

    @FXML
    private void irADepositos(ActionEvent event) {
        if (clienteActual == null) {
            System.out.println("Error: No hay usuario activo.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Depositos.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la nueva ventana
            DepositosController depositosController = loader.getController();
            depositosController.setCliente(clienteActual);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void irARetiros(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Retiros.fxml"));
            Parent root = loader.load();

            RetirosController retirosController = loader.getController();
            retirosController.setCliente(clienteActual);

            Stage stage = new Stage();
            stage.setTitle("Realizar Retiro");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al abrir la ventana de retiro.");
        }
    }
    @FXML
    private void irATransferencias(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Transferencias.fxml"));
            Parent root = loader.load();

            // Obtener el controlador y pasar el cliente actual
            TransferenciasController transferenciasController = loader.getController();
            transferenciasController.setCliente(clienteActual);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Transferencias");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al abrir la ventana de transferencias.");
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
        }
    }

    @FXML
    private void irAConsultaHistorial(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Historial.fxml"));
            Parent root = loader.load();

            HistorialController historialController = loader.getController();
            historialController.setCliente(clienteActual);

            Stage stage = new Stage();
            stage.setTitle("Historial de Transacciones");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al abrir el historial.");
        }    }
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
        }
    }

    @FXML
    private void irATransaccionesProgramadas(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/TransaccionesProgramadas.fxml"));
            Parent root = loader.load();

            TransaccionesProgramadasController controller = loader.getController();
            controller.setCliente(clienteActual);

            Stage stage = new Stage();
            stage.setTitle("Transacciones Programadas");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al abrir la ventana de transacciones programadas.");
        }
    }

    @FXML
    private void irALogin(ActionEvent event) {
        cambiarVentana(event, "/views/Login.fxml");
    }

    /**
     * Método para cambiar de escena.
     */
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