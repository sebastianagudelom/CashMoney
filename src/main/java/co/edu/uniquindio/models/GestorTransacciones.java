package co.edu.uniquindio.models;

import java.util.ArrayList;

public class GestorTransacciones {

    /**
     * Agrega una transacción al historial del cliente
     */
    public static void agregarTransaccion(Cliente cliente, Transaccion transaccion) {
        if (cliente.getHistorialTransacciones() == null) {
            cliente.setHistorialTransacciones(new ArrayList<>());
        }

        cliente.agregarTransaccion(transaccion);
        GestorClientes.guardarClientes(); // mantener persistencia
    }

    /**
     * Registra una transferencia entre dos clientes
     */
    public static void registrarTransferencia(Cliente origen, Cliente destino,
                                              Transaccion enviada, Transaccion recibida) {
        agregarTransaccion(origen, enviada);
        agregarTransaccion(destino, recibida);
    }

    public static boolean retirarSaldo(Cliente cliente, double monto) {
        if (cliente == null || cliente.getCuenta() == null) {
            System.out.println("Error: cliente o cuenta no válida.");
            return false;
        }

        boolean exito = cliente.getCuenta().retirar(monto);
        if (exito) {
            GestorClientes.guardarClientes();
        } else {
            System.out.println("Saldo insuficiente.");
        }

        return exito;
    }

    public static boolean depositarSaldo(Cliente cliente, double monto) {
        if (cliente == null || cliente.getCuenta() == null) {
            System.out.println("Error: cliente o cuenta no válida.");
            return false;
        }

        boolean exito = cliente.getCuenta().depositar(monto);
        if (exito) {
            GestorClientes.guardarClientes();
        }

        return exito;
    }


}
