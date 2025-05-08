package co.edu.uniquindio.structures;

import java.util.*;

public class GrafoGastos {
    private final Map<String, Map<String, Double>> adyacencias = new HashMap<>();

    public void agregarRelacion(String origen, String destino, double monto) {
        adyacencias.putIfAbsent(origen, new HashMap<>());
        Map<String, Double> conexiones = adyacencias.get(origen);
        conexiones.put(destino, conexiones.getOrDefault(destino, 0.0) + monto);
    }

    public Map<String, Map<String, Double>> getAdyacencias() {
        return adyacencias;
    }

    public Set<String> getNodos() {
        return adyacencias.keySet();
    }
}