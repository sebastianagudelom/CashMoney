package co.edu.uniquindio.controllers;

import co.edu.uniquindio.managers.GestorRelacionesMonederos;
import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.structures.GrafoDirigido;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.*;

public class GrafoMonederosController {

    @FXML
    private TextArea txtSalidaGrafo;

    @FXML
    private Canvas grafoCanvas;

    private Cliente cliente;
    private final Map<String, Double[]> posicionesNodos = new HashMap<>();
    private final int RADIO_NODO = 30;
    private final Color[] COLORES_NODO = {
            Color.LIGHTBLUE, Color.LIGHTGREEN, Color.LIGHTYELLOW,
            Color.LIGHTPINK, Color.LIGHTCORAL, Color.LIGHTSALMON
    };

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        mostrarRelaciones();
    }

    @FXML
    private void mostrarRelaciones() {
        if (cliente == null) return;

        GrafoDirigido<String> grafo = GestorRelacionesMonederos.getInstance().obtenerGrafo(cliente.getIdentificacion());

        // Mostrar texto en el área de texto
        StringBuilder sb = new StringBuilder();
        sb.append("Lista de transferencias entre monederos:\n\n");

        Set<String> vertices = grafo.obtenerVertices();
        boolean hayTransferencias = false;

        for (String origen : vertices) {
            List<GrafoDirigido.Arista<String>> adyacentes = grafo.obtenerAristasAdyacentes(origen);
            if (!adyacentes.isEmpty()) {
                hayTransferencias = true;
                sb.append("Desde ").append(origen).append(":\n");
                for (GrafoDirigido.Arista<String> arista : adyacentes) {
                    sb.append("  → ").append(arista.destino)
                            .append(" (Transferencia: $").append(arista.peso).append(")\n");
                }
                sb.append("\n");
            }
        }

        if (!hayTransferencias) {
            sb.append("No se han realizado transferencias entre monederos.\n");
            sb.append("Para crear una transferencia, selecciona 'Transferir entre Monederos' en la pantalla principal.");
        }

        txtSalidaGrafo.setText(sb.toString());

        // Dibujar el grafo en el canvas si está disponible
        if (grafoCanvas != null) {
            dibujarGrafo(grafo);
        }
    }

    private void dibujarGrafo(GrafoDirigido<String> grafo) {
        GraphicsContext gc = grafoCanvas.getGraphicsContext2D();
        double width = grafoCanvas.getWidth();
        double height = grafoCanvas.getHeight();

        // Limpiar canvas
        gc.clearRect(0, 0, width, height);

        Set<String> vertices = grafo.obtenerVertices();
        if (vertices.isEmpty()) {
            return;
        }

        // Calcular posiciones de los nodos
        calcularPosicionesNodos(vertices, width, height);

        // Dibujar aristas con flechas
        for (String origen : vertices) {
            Double[] posOrigen = posicionesNodos.get(origen);
            List<GrafoDirigido.Arista<String>> adyacentes = grafo.obtenerAristasAdyacentes(origen);

            for (GrafoDirigido.Arista<String> arista : adyacentes) {
                String destino = arista.destino;
                Double[] posDestino = posicionesNodos.get(destino);

                if (posOrigen != null && posDestino != null) {
                    dibujarFlecha(gc, posOrigen[0], posOrigen[1], posDestino[0], posDestino[1], arista.peso);
                }
            }
        }

        // Dibujar nodos
        int colorIdx = 0;
        for (String nodo : vertices) {
            Double[] pos = posicionesNodos.get(nodo);
            if (pos != null) {
                Color color = COLORES_NODO[colorIdx % COLORES_NODO.length];
                dibujarNodo(gc, pos[0], pos[1], nodo, color);
                colorIdx++;
            }
        }
    }

    private void calcularPosicionesNodos(Set<String> vertices, double width, double height) {
        posicionesNodos.clear();
        int numNodos = vertices.size();
        if (numNodos == 0) return;

        if (numNodos == 1) {
            // Un solo nodo en el centro
            String nodo = vertices.iterator().next();
            posicionesNodos.put(nodo, new Double[]{width/2, height/2});
        } else if (numNodos == 2) {
            // Dos nodos en horizontal
            Iterator<String> it = vertices.iterator();
            posicionesNodos.put(it.next(), new Double[]{width/4, height/2});
            posicionesNodos.put(it.next(), new Double[]{3*width/4, height/2});
        } else {
            // Más de dos nodos en círculo
            double radio = Math.min(width, height) / 2.5;
            double centerX = width / 2;
            double centerY = height / 2;

            int i = 0;
            for (String nodo : vertices) {
                double angulo = 2 * Math.PI * i / numNodos;
                double x = centerX + radio * Math.cos(angulo);
                double y = centerY + radio * Math.sin(angulo);
                posicionesNodos.put(nodo, new Double[]{x, y});
                i++;
            }
        }
    }

    private void dibujarNodo(GraphicsContext gc, double x, double y, String nombre, Color color) {
        // Dibujar círculo
        gc.setFill(color);
        gc.fillOval(x - RADIO_NODO, y - RADIO_NODO, 2 * RADIO_NODO, 2 * RADIO_NODO);

        // Borde del círculo
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeOval(x - RADIO_NODO, y - RADIO_NODO, 2 * RADIO_NODO, 2 * RADIO_NODO);

        // Texto dentro del nodo
        gc.setFill(Color.BLACK);
        gc.setLineWidth(1);
        String nombreCorto = nombre.length() > 10 ? nombre.substring(0, 9) + "..." : nombre;
        gc.fillText(nombreCorto, x - RADIO_NODO/2, y + 5);
    }

    private void dibujarFlecha(GraphicsContext gc, double x1, double y1, double x2, double y2, String peso) {
        // Calcular puntos de inicio y fin ajustados para no tocar los nodos
        double dx = x2 - x1;
        double dy = y2 - y1;
        double length = Math.sqrt(dx*dx + dy*dy);

        // Ajustar para que la flecha no entre en los nodos
        double x1Adj = x1 + dx * RADIO_NODO / length;
        double y1Adj = y1 + dy * RADIO_NODO / length;
        double x2Adj = x2 - dx * RADIO_NODO / length;
        double y2Adj = y2 - dy * RADIO_NODO / length;

        // Dibujar línea
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.5);
        gc.beginPath();
        gc.moveTo(x1Adj, y1Adj);
        gc.lineTo(x2Adj, y2Adj);
        gc.stroke();

        // Dibujar punta de flecha
        double headSize = 10;
        double angle = Math.atan2(y2Adj - y1Adj, x2Adj - x1Adj);
        gc.beginPath();
        gc.moveTo(x2Adj, y2Adj);
        gc.lineTo(x2Adj - headSize * Math.cos(angle - Math.PI/6), y2Adj - headSize * Math.sin(angle - Math.PI/6));
        gc.lineTo(x2Adj - headSize * Math.cos(angle + Math.PI/6), y2Adj - headSize * Math.sin(angle + Math.PI/6));
        gc.lineTo(x2Adj, y2Adj);
        gc.fill();

        // Dibujar peso de la arista
        gc.setFill(Color.DARKBLUE);
        double midX = (x1Adj + x2Adj) / 2;
        double midY = (y1Adj + y2Adj) / 2;
        gc.fillText("$" + peso, midX + 5, midY - 5);
    }

    @FXML
    private void volver() {
        Stage stage = (Stage) txtSalidaGrafo.getScene().getWindow();
        stage.close();
    }
}