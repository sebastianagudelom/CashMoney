package co.edu.uniquindio.managers;

import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.models.Monedero;
import co.edu.uniquindio.structures.GrafoDirigido;
import java.util.List;

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

    public boolean eliminarMonedero(Cliente cliente, String nombre) {
        Monedero objetivo = buscarMonederoPorNombre(cliente, nombre);
        if (objetivo != null) {
            cliente.getMonederos().eliminar(objetivo);
            grafoMonederos.eliminarVertice(nombre);
            return true;
        }
        return false;
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
            return true;
        }
        return false;
    }

    public List<Monedero> obtenerTodos(Cliente cliente) {
        return cliente.getMonederos().aListaJava();
    }
}
