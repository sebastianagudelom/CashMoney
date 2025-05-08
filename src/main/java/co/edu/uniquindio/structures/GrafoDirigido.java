package co.edu.uniquindio.structures;

import java.io.Serializable;
import java.util.*;

public class GrafoDirigido<T> implements Serializable {

    private final Map<T, List<Arista<T>>> adyacencias = new HashMap<>();

    private static class Arista<T> implements Serializable {
        T destino;
        String peso;

        Arista(T destino, String peso) {
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
        adyacencias.get(origen).add(new Arista<>(destino, peso));
    }

    public List<T> obtenerAdyacentes(T vertice) {
        if (!adyacencias.containsKey(vertice)) return new ArrayList<>();
        List<T> destinos = new ArrayList<>();
        for (Arista<T> a : adyacencias.get(vertice)) {
            destinos.add(a.destino);
        }
        return destinos;
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