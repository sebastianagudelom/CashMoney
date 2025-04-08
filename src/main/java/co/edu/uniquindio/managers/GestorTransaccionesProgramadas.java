package co.edu.uniquindio.managers;

import co.edu.uniquindio.exceptions.ClienteNoEncontradoException;
import co.edu.uniquindio.exceptions.TransaccionInvalidaException;
import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.models.TransaccionProgramada;
import co.edu.uniquindio.structures.ListaEnlazada;
import java.io.*;
import java.time.LocalDate;

public class GestorTransaccionesProgramadas {

    private static final String ARCHIVO = "src/main/resources/data/transaccionesProgramadas.dat";
    private final ListaEnlazada<TransaccionProgramada> transacciones;

    public GestorTransaccionesProgramadas() {
        this.transacciones = cargarTransacciones();
    }

    public void agregarTransaccion(TransaccionProgramada t) {
        transacciones.agregar(t);
        guardarTransacciones();
    }

    public void ejecutarTransacciones() {
        LocalDate hoy = LocalDate.now();
        ListaEnlazada<TransaccionProgramada> transaccionesEjecutadas = new ListaEnlazada<>();

        for (TransaccionProgramada t : transacciones) {
            if (!t.getFechaEjecucion().isAfter(hoy)) {
                realizarTransferencia(t);
                transaccionesEjecutadas.agregar(t);
            }
        }

        for (TransaccionProgramada t : transaccionesEjecutadas) {
            transacciones.eliminar(t);
        }

        guardarTransacciones();
    }

    private void realizarTransferencia(TransaccionProgramada t) {
        try {
            Cliente origen = GestorClientes.buscarClientePorUsuario(t.getUsuarioOrigen());
            Cliente destino = GestorClientes.buscarClientePorUsuario(t.getUsuarioDestino());

            GestorTransacciones.retirarSaldo(origen, t.getMonto());
            GestorTransacciones.depositarSaldo(destino, t.getMonto());

        } catch (ClienteNoEncontradoException | TransaccionInvalidaException e) {
            System.out.println("Error al ejecutar transferencia programada: " + e.getMessage());
        }
    }

    private void guardarTransacciones() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            out.writeObject(transacciones);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ListaEnlazada<TransaccionProgramada> cargarTransacciones() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARCHIVO))) {
            return (ListaEnlazada<TransaccionProgramada>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ListaEnlazada<>();
        }
    }

    public ListaEnlazada<TransaccionProgramada> getTransacciones() {
        return transacciones;
    }
}