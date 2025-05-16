package co.edu.uniquindio.managers;

import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.models.Monedero;
import co.edu.uniquindio.structures.GrafoDirigido;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class GestorMonederos {

    private static GestorMonederos instancia;
    private final GrafoDirigido<String> grafoMonederos;

    private GestorMonederos() {
        grafoMonederos = new GrafoDirigido<>();
    }

    public static GestorMonederos getInstance() {
        if (instancia == null) {
            instancia = new GestorMonederos();
        }
        return instancia;
    }

    public void agregarMonedero(Cliente cliente, Monedero monedero) {
        cliente.getMonederos().agregar(monedero);
        grafoMonederos.agregarVertice(monedero.getNombre());
    }

    public void eliminarMonedero(Cliente cliente, String nombre) {
        Monedero objetivo = buscarMonederoPorNombre(cliente, nombre);
        if (objetivo != null) {
            cliente.getMonederos().eliminar(objetivo);
            grafoMonederos.eliminarVertice(nombre);
            GestorRelacionesMonederos.getInstance().eliminarRelacionesMonedero(cliente.getIdentificacion(), nombre);
        }
    }

    public Monedero buscarMonederoPorNombre(Cliente cliente, String nombre) {
        for (Monedero m : cliente.getMonederos()) {
            if (m.getNombre().equalsIgnoreCase(nombre)) {
                return m;
            }
        }
        return null;
    }

    public boolean retirarAMonederoPrincipal(Cliente cliente, String nombre) {
        Monedero m = buscarMonederoPorNombre(cliente, nombre);
        if (m != null && m.getSaldo() >= m.getMeta()) {
            cliente.getCuenta().depositar(m.getSaldo());
            m.setSaldo(0);
            GestorRelacionesMonederos.getInstance().eliminarRelacionesMonedero(cliente.getIdentificacion(), nombre);
            return true;
        }
        return false;
    }

    public List<Monedero> obtenerTodos(Cliente cliente) {
        return cliente.getMonederos().aListaJava();
    }

    /**
     * Realiza una transferencia de dinero entre dos monederos
     * @param cliente El cliente dueño de los monederos
     * @param nombreOrigen Nombre del monedero origen
     * @param nombreDestino Nombre del monedero destino
     * @param monto Cantidad a transferir
     * @return true si la transferencia se realizó correctamente, false en caso contrario
     */
    public boolean transferirEntreMonederos(Cliente cliente, String nombreOrigen, String nombreDestino, double monto) {
        // Validar que los monederos existan
        Monedero origen = buscarMonederoPorNombre(cliente, nombreOrigen);
        Monedero destino = buscarMonederoPorNombre(cliente, nombreDestino);

        if (origen == null || destino == null) {
            return false;
        }

        if (monto <= 0 || origen.getSaldo() < monto) {
            return false;
        }

        origen.setSaldo(origen.getSaldo() - monto);
        destino.agregarSaldo(monto);

        String pesoTransferencia = String.format("%.2f", monto);
        grafoMonederos.agregarArista(nombreOrigen, nombreDestino, pesoTransferencia);

        return true;
    }

    /**
     * Obtiene el historial de transferencias salientes de un monedero
     * @param nombreMonedero Nombre del monedero
     * @return Lista de nombres de monederos destino
     */
    public List<String> obtenerTransferenciasSalientes(String nombreMonedero) {
        if (!grafoMonederos.existeVertice(nombreMonedero)) {
            return new ArrayList<>();
        }
        return grafoMonederos.obtenerAdyacentes(nombreMonedero);
    }

    /**
     * Verifica si existe una transferencia entre dos monederos
     * @param origen Nombre del monedero origen
     * @param destino Nombre del monedero destino
     * @return true si existe una transferencia, false en caso contrario
     */
    public boolean existeTransferencia(String origen, String destino) {
        return grafoMonederos.existeArista(origen, destino);
    }

    /**
     * Obtiene todos los monederos registrados en el grafo
     * @return Conjunto de nombres de monederos
     */
    public Set<String> obtenerTodosLosMonederosEnGrafo() {
        return grafoMonederos.obtenerVertices();
    }

    /**
     * Imprime el grafo de transferencias entre monederos
     */
    public void imprimirGrafoTransferencias() {
        grafoMonederos.imprimirGrafo();
    }
}