package co.edu.uniquindio.structures;

import java.io.Serializable;
import java.util.*;

public class GrafoDirigido<T> implements Serializable {

    private final Map<T, List<Arista<T>>> adyacencias = new HashMap<>();

    public static class Arista<T> implements Serializable {
        public T destino;
        public String peso;

        public Arista(T destino, String peso) {
            this.destino = destino;
            this.peso = peso;
        }

        @Override
        public String toString() {
            return destino + " (" + peso + ")";
        }
    }

    public void agregarVertice(T vertice) {
        adyacencias.putIfAbsent(vertice, new ArrayList<>());
    }

    public void agregarArista(T origen, T destino, String peso) {
        agregarVertice(origen);
        agregarVertice(destino);

        // Verificar si ya existe una arista entre estos vértices
        for (Arista<T> arista : adyacencias.get(origen)) {
            if (arista.destino.equals(destino)) {
                // Actualizar el peso de la arista existente
                double pesoAnterior = Double.parseDouble(arista.peso.replace(",", "."));
                double nuevoPeso = Double.parseDouble(peso.replace(",", "."));
                arista.peso = String.format("%.2f", pesoAnterior + nuevoPeso);
                return;
            }
        }

        // Si no existe, agregar una nueva arista
        adyacencias.get(origen).add(new Arista<>(destino, peso.replace(",", ".")));
    }

    public List<T> obtenerAdyacentes(T vertice) {
        if (!adyacencias.containsKey(vertice)) return new ArrayList<>();
        List<T> destinos = new ArrayList<>();
        for (Arista<T> a : adyacencias.get(vertice)) {
            destinos.add(a.destino);
        }
        return destinos;
    }

    public List<Arista<T>> obtenerAristasAdyacentes(T vertice) {
        if (!adyacencias.containsKey(vertice)) return new ArrayList<>();
        return new ArrayList<>(adyacencias.get(vertice));
    }

    public Set<T> obtenerVertices() {
        return adyacencias.keySet();
    }

    public boolean existeVertice(T vertice) {
        return adyacencias.containsKey(vertice);
    }

    public boolean existeArista(T origen, T destino) {
        if (!adyacencias.containsKey(origen)) return false;
        return adyacencias.get(origen).stream().anyMatch(a -> a.destino.equals(destino));
    }

    public String obtenerPesoArista(T origen, T destino) {
        if (!adyacencias.containsKey(origen)) return null;
        for (Arista<T> arista : adyacencias.get(origen)) {
            if (arista.destino.equals(destino)) {
                return arista.peso;
            }
        }
        return null;
    }

    public void eliminarArista(T origen, T destino) {
        if (adyacencias.containsKey(origen)) {
            adyacencias.get(origen).removeIf(a -> a.destino.equals(destino));
        }
    }

    public void eliminarVertice(T vertice) {
        adyacencias.remove(vertice);
        for (List<Arista<T>> lista : adyacencias.values()) {
            lista.removeIf(a -> a.destino.equals(vertice));
        }
    }

    public void imprimirGrafo() {
        for (T vertice : adyacencias.keySet()) {
            System.out.print(vertice + " -> ");
            System.out.println(adyacencias.get(vertice));
        }
    }
}