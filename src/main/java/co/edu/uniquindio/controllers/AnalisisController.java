package co.edu.uniquindio.controllers;

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
import javafx.scene.text.Text;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalisisController {

    @FXML
    private BarChart<String, Number> graficoBarras;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private Pane graficoNodos;
    @FXML
    private Button btnVolver;
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
            System.out.println("Categoría: " + categoria + " - Valor: " + valor);

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

        Map<String, double[]> posiciones = new HashMap<>();
        int i = 0;

        for (String nodo : grafo.keySet()) {
            double espacio = anchoPane / (totalNodos + 1);
            double x1 = (i + 1) * espacio;
            double y1 = (i % 2 == 0) ? altoPane / 3 : 2 * altoPane / 3;

            Circle circle = new Circle(x1, y1, 25);
            Text label = new Text(x1 - 20, y1 + 40, nodo);
            graficoNodos.getChildren().addAll(circle, label);

            posiciones.put(nodo, new double[]{x1, y1});
            i++;
        }

        // Dibujar líneas entre nodos
        for (Map.Entry<String, List<String>> entry : grafo.entrySet()) {
            String origen = entry.getKey();
            List<String> destinos = entry.getValue();

            double[] origenPos = posiciones.get(origen);

            for (String destino : destinos) {
                double[] destinoPos = posiciones.get(destino);
                if (destinoPos != null) {
                    Line linea = new Line(origenPos[0], origenPos[1], destinoPos[0], destinoPos[1]);
                    graficoNodos.getChildren().add(linea);
                }
            }
        }
    }



    @FXML
    private void volverMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Menu.fxml"));
            Parent root = loader.load();

            // Pasar el cliente actual al controlador del menú
            MenuController menuController = loader.getController();
            menuController.setCliente(clienteActual);

            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al volver al menú.");
        }
    }

}