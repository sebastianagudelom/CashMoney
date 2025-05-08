package co.edu.uniquindio.controllers;

import co.edu.uniquindio.exceptions.VistaCargaException;
import co.edu.uniquindio.models.AnalizadorGastos;
import co.edu.uniquindio.models.Cliente;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalisisController {

    @FXML private BarChart<String, Number> graficoBarras;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private Pane graficoNodos;
    @FXML private Button btnVolver;

    private Cliente clienteActual;

    public void setCliente(Cliente cliente) {
        this.clienteActual = cliente;
        mostrarGraficoBarras();
        mostrarGraficoNodos();
    }

    private void mostrarGraficoBarras() {
        Map<String, Double> gastosPorCategoria = AnalizadorGastos.obtenerGastosPorCategoria(clienteActual);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Gastos por categoría");

        for (Map.Entry<String, Double> entrada : gastosPorCategoria.entrySet()) {
            String categoria = entrada.getKey();
            Double valor = entrada.getValue();
            if (categoria != null && valor != null) {
                series.getData().add(new XYChart.Data<>(categoria, valor));
            }
        }

        graficoBarras.getData().clear();
        graficoBarras.getData().add(series);
    }

    private void mostrarGraficoNodos() {
        graficoNodos.getChildren().clear();

        Map<String, List<String>> grafo = AnalizadorGastos.generarGrafoRelaciones(clienteActual);
        double anchoPane = graficoNodos.getPrefWidth();
        double altoPane = graficoNodos.getPrefHeight();

        int totalNodos = grafo.size();
        double radio = 14;
        double espacioHorizontal = anchoPane / (totalNodos + 1);
        double yCentro = altoPane / 2 - 10;

        Map<String, double[]> posiciones = new HashMap<>();
        int index = 0;

        for (String nodo : grafo.keySet()) {
            double x = espacioHorizontal * (index + 1);
            double y = yCentro;

            Circle circle = new Circle(x, y, radio);
            circle.setFill(Color.web("#27ae60"));
            circle.setStroke(Color.DARKGREEN);
            circle.setStrokeWidth(1.2);

            Text label = new Text(nodo);
            label.setStyle("-fx-font-size: 10px; -fx-fill: #2c3e50; -fx-font-weight: bold;");
            label.setX(x - nodo.length() * 3.5);
            label.setY(y + radio + 10);

            graficoNodos.getChildren().addAll(circle, label);
            posiciones.put(nodo, new double[]{x, y});
            index++;
        }

        for (Map.Entry<String, List<String>> entry : grafo.entrySet()) {
            String origen = entry.getKey();
            List<String> destinos = entry.getValue();

            double[] origenPos = posiciones.get(origen);
            if (origenPos == null) continue;

            for (String destino : destinos) {
                double[] destinoPos = posiciones.get(destino);
                if (destinoPos != null) {
                    Line linea = new Line(origenPos[0], origenPos[1], destinoPos[0], destinoPos[1]);
                    linea.setStroke(Color.web("#7f8c8d"));
                    linea.setStrokeWidth(1);
                    graficoNodos.getChildren().add(linea);
                }
            }
        }
    }

    @FXML
    private void volverMenu() throws VistaCargaException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Menu.fxml"));
            Parent root = loader.load();

            MenuController menuController = loader.getController();
            menuController.setCliente(clienteActual);

            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            throw new VistaCargaException("Error al abrir la vista de Menu");
        }
    }
}