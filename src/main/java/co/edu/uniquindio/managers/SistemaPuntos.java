package co.edu.uniquindio.managers;

import co.edu.uniquindio.models.NodoPuntos;
import co.edu.uniquindio.models.RangoCliente;
import co.edu.uniquindio.structures.ArbolBinarioBusqueda;
import java.io.Serializable;
import java.util.HashMap;

public class SistemaPuntos implements Serializable {

    private ArbolBinarioBusqueda<NodoPuntos> arbolPuntos;
    private final HashMap<String, Integer> tablaPuntos;

    public SistemaPuntos() {
        tablaPuntos = new HashMap<>();
        arbolPuntos = new ArbolBinarioBusqueda<>();
    }

    public void reemplazarPor(SistemaPuntos otro) {
        this.arbolPuntos = otro.arbolPuntos;
        tablaPuntos.clear();
        tablaPuntos.putAll(otro.tablaPuntos);
    }

    public void agregarPuntos(String cedula, int puntosGanados) {
        int total = tablaPuntos.getOrDefault(cedula, 0) + puntosGanados;
        tablaPuntos.put(cedula, total);
        NodoPuntos nuevoNodo = new NodoPuntos(cedula, total);
        if (arbolPuntos.buscar(nuevoNodo)) {
            arbolPuntos.eliminar(nuevoNodo);
        }
        arbolPuntos.insertar(nuevoNodo);
        GestorClientes.guardarSistemaPuntos();
    }

    public int consultarPuntos(String cedula) {
        return tablaPuntos.getOrDefault(cedula, 0);
    }

    public RangoCliente consultarRango(String cedula) {
        int puntos = consultarPuntos(cedula);
        return RangoCliente.calcularRango(puntos);
    }

    public void restarPuntos(String identificacion, int puntos) {
        Integer puntosActuales = tablaPuntos.get(identificacion);
        if (puntosActuales == null || puntosActuales < puntos) {
            throw new IllegalArgumentException("Puntos insuficientes para realizar el canje.");
        }
        int total = puntosActuales - puntos;
        tablaPuntos.put(identificacion, total);
        NodoPuntos nodoActualizado = new NodoPuntos(identificacion, total);
        if (arbolPuntos.buscar(nodoActualizado)) {
            arbolPuntos.eliminar(nodoActualizado);
        }
        arbolPuntos.insertar(nodoActualizado);
        GestorClientes.guardarSistemaPuntos();
    }
}
