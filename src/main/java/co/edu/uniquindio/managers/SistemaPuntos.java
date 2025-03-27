package co.edu.uniquindio.managers;

import co.edu.uniquindio.models.NodoPuntos;
import co.edu.uniquindio.models.RangoCliente;

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
        if (puntos < actual.getPuntos()) {
            actual.setIzquierdo(insertarNodo(actual.getIzquierdo(), cedula, puntos));
        } else if (puntos > actual.getPuntos()) {
            actual.setDerecho(insertarNodo(actual.getDerecho(), cedula, puntos));
        } else {
            actual.setCedula(cedula);
        }

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