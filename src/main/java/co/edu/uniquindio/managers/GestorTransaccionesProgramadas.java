package co.edu.uniquindio.managers;

import co.edu.uniquindio.exceptions.ClienteNoEncontradoException;
import co.edu.uniquindio.exceptions.TransaccionInvalidaException;
import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.models.TransaccionProgramada;
import co.edu.uniquindio.structures.ColaPrioridad;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;

public class GestorTransaccionesProgramadas {

    private static final String ARCHIVO = "src/main/resources/data/transaccionesProgramadas.dat";
    private ColaPrioridad<TransaccionProgramada> colaTransacciones;

    public GestorTransaccionesProgramadas() {
        colaTransacciones = cargarTransacciones();
    }

    public void agregarTransaccion(TransaccionProgramada t) {
        colaTransacciones.insertar(t);
        guardarTransacciones();
    }

    public void ejecutarTransacciones() {
        LocalDate hoy = LocalDate.now();
        while (!colaTransacciones.esVacia() && !colaTransacciones.verMinimo().getFechaEjecucion().isAfter(hoy)) {
            TransaccionProgramada t = colaTransacciones.extraerMinimo();
            realizarTransferencia(t);
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
            out.writeObject(colaTransacciones);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ColaPrioridad<TransaccionProgramada> cargarTransacciones() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARCHIVO))) {
            return (ColaPrioridad<TransaccionProgramada>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ColaPrioridad<>();
        }
    }

    public ColaPrioridad<TransaccionProgramada> getTransacciones() {
        return colaTransacciones;
    }
}
