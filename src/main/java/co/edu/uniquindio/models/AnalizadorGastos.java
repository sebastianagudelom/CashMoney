package co.edu.uniquindio.models;

import co.edu.uniquindio.exceptions.CuentaNoEncontradaException;
import co.edu.uniquindio.managers.GestorClientes;
import co.edu.uniquindio.structures.GrafoGastos;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalizadorGastos {

    public static Map<String, Double> obtenerGastosPorCategoria(Cliente cliente) {
        Map<String, Double> gastosPorCategoria = new HashMap<>();

        for (Transaccion t : cliente.getHistorialTransacciones()) {
            if (t.getTipo().equalsIgnoreCase("Transferencia Enviada")) {
                String categoria = t.getCategoria();
                gastosPorCategoria.put(categoria,
                        gastosPorCategoria.getOrDefault(categoria, 0.0) + t.getMonto());
            }
        }

        return gastosPorCategoria;
    }

    public static Map<String, List<String>> generarGrafoRelaciones(Cliente cliente) {
        Map<String, List<String>> grafo = new HashMap<>();

        String origen = cliente.getUsuario();
        grafo.putIfAbsent(origen, new ArrayList<>());

        for (Transaccion t : cliente.getHistorialTransacciones()) {
            if (t.getTipo().equals("Transferencia Enviada") && t.getCuentaDestino() != null) {
                try {
                    Cliente destino = GestorClientes.buscarClientePorCuenta(t.getCuentaDestino());

                    if (destino != null) {
                        String usuarioDestino = destino.getUsuario();
                        grafo.get(origen).add(usuarioDestino);
                        grafo.putIfAbsent(usuarioDestino, new ArrayList<>());
                    }

                } catch (CuentaNoEncontradaException e) {
                    System.out.println("Cuenta no encontrada en grafo: " + e.getMessage());
                }
            }
        }
        return grafo;
    }

    public static Map<String, Double> resumenPorCategoria(GrafoGastos grafo, String usuario) {
        return grafo.getAdyacencias().getOrDefault(usuario, new HashMap<>());
    }
}
