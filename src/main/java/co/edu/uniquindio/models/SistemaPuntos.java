package co.edu.uniquindio.models;

import java.io.Serializable;
import java.util.HashMap;

public class SistemaPuntos implements Serializable {

    private NodoPuntos raiz;
    private final HashMap<String, Integer> tablaPuntos;

    public SistemaPuntos() {
        this.raiz = null;
        this.tablaPuntos = new HashMap<>();
    }

    public void reemplazarPor(SistemaPuntos otro) {
        this.raiz = otro.raiz;
        this.tablaPuntos.clear();
        this.tablaPuntos.putAll(otro.tablaPuntos);
    }

    public void agregarPuntos(String cedula, int puntosGanados) {
        int total = tablaPuntos.getOrDefault(cedula, 0) + puntosGanados;
        tablaPuntos.put(cedula, total);
        raiz = insertarNodo(raiz, cedula, total);
    }

    private NodoPuntos insertarNodo(NodoPuntos actual, String cedula, int puntos) {
        if (actual == null) return new NodoPuntos(cedula, puntos);
        if (puntos < actual.puntos) actual.izquierdo = insertarNodo(actual.izquierdo, cedula, puntos);
        else if (puntos > actual.puntos) actual.derecho = insertarNodo(actual.derecho, cedula, puntos);
        else actual.cedula = cedula;
        return actual;
    }

    public int consultarPuntos(String cedula) {
        return tablaPuntos.getOrDefault(cedula, 0);
    }

    public RangoCliente consultarRango(String cedula) {
        int puntos = consultarPuntos(cedula);
        return RangoCliente.calcularRango(puntos);
    }
}