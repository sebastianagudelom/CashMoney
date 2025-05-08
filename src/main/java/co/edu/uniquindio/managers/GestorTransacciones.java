package co.edu.uniquindio.managers;

import co.edu.uniquindio.exceptions.*;
import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.models.Transaccion;
import co.edu.uniquindio.structures.ListaEnlazada;

public class GestorTransacciones {

    public static void agregarTransaccion(Cliente cliente, Transaccion transaccion) {
        if (cliente.getHistorialTransacciones() == null) {
            cliente.setHistorialTransacciones(new ListaEnlazada<>());
        }

        cliente.agregarTransaccion(transaccion);
        GestorClientes.guardarClientes();
    }

    public static void registrarTransferencia(Cliente origen, Cliente destino,
                                              Transaccion enviada, Transaccion recibida) {
        agregarTransaccion(origen, enviada);
        agregarTransaccion(destino, recibida);
    }

    public static boolean retirarSaldo(Cliente cliente, double monto) throws TransaccionInvalidaException {
        if (cliente == null || cliente.getCuenta() == null) {
            throw new TransaccionInvalidaException("El cliente o su cuenta no es válida.");
        }

        if (monto <= 0) {
            throw new TransaccionInvalidaException("El monto a retirar debe ser mayor a cero.");
        }

        if (cliente.getCuenta().getSaldo() < monto) {
            throw new TransaccionInvalidaException("Saldo insuficiente.");
        }

        cliente.getCuenta().retirar(monto);

        Transaccion retiro = new Transaccion(
                "Retiro",
                monto,
                cliente.getCuenta().getNumeroCuenta(),
                cliente.getCuenta().getNumeroCuenta(),
                "Otros"
        );

        agregarTransaccion(cliente, retiro);

        if (cliente.getCuenta().getSaldo() < 10000) {
            cliente.agregarNotificacion("⚠️ Tu saldo es inferior a $10.000.");
        }

        GestorClientes.guardarClientes();
        return true;
    }

    public static boolean depositarSaldo(Cliente cliente, double monto) throws TransaccionInvalidaException {
        if (cliente == null || cliente.getCuenta() == null) {
            throw new TransaccionInvalidaException("Cliente o cuenta no válida para depósito.");
        }

        if (monto <= 0) {
            throw new TransaccionInvalidaException("El monto a depositar debe ser mayor que cero.");
        }

        boolean exito = cliente.getCuenta().depositar(monto);
        if (!exito) {
            throw new TransaccionInvalidaException("No se pudo realizar el depósito.");
        }

        Transaccion deposito = new Transaccion(
                "Depósito",
                monto,
                cliente.getCuenta().getNumeroCuenta(),  // cuentaOrigen
                cliente.getCuenta().getNumeroCuenta(),  // cuentaDestino
                "Otros" // categoría por defecto
        );

        agregarTransaccion(cliente, deposito);

        cliente.agregarNotificacion("💰 Has recibido un depósito exitoso de $" + monto);

        GestorClientes.guardarClientes();
        return true;
    }
}