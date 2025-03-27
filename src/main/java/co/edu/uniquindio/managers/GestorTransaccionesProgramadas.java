package co.edu.uniquindio.managers;

import co.edu.uniquindio.managers.GestorClientes;
import co.edu.uniquindio.managers.GestorTransacciones;
import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.models.TransaccionProgramada;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GestorTransaccionesProgramadas {

    private static final String ARCHIVO = "transaccionesProgramadas.dat";
    private final List<TransaccionProgramada> transacciones;

    public GestorTransaccionesProgramadas() {
        this.transacciones = cargarTransacciones();
    }

    public void agregarTransaccion(TransaccionProgramada t) {
        transacciones.add(t);
        guardarTransacciones();
    }

    public void ejecutarTransacciones() {
        LocalDate hoy = LocalDate.now();
        Iterator<TransaccionProgramada> iterador = transacciones.iterator();

        while (iterador.hasNext()) {
            TransaccionProgramada t = iterador.next();
            if (!t.getFechaEjecucion().isAfter(hoy)) {
                realizarTransferencia(t);
                iterador.remove();
            }
        }
        guardarTransacciones();
    }

    private void realizarTransferencia(TransaccionProgramada t) {
        Cliente origen = GestorClientes.buscarClientePorUsuario(t.getUsuarioOrigen());
        Cliente destino = GestorClientes.buscarClientePorUsuario(t.getUsuarioDestino());

        if (origen != null && destino != null) {
            boolean exito = GestorTransacciones.retirarSaldo(origen, t.getMonto());
            if (exito) {
                GestorTransacciones.depositarSaldo(destino, t.getMonto());
            }
        }
    }

    private void guardarTransacciones() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            out.writeObject(transacciones);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public List<TransaccionProgramada> cargarTransacciones() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARCHIVO))) {
            return (List<TransaccionProgramada>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public List<TransaccionProgramada> getTransacciones() {
        return transacciones;
    }
}