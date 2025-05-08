package co.edu.uniquindio.managers;

import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.models.Transaccion;
import co.edu.uniquindio.structures.Pila;

/**
 * Clase que gestiona las solicitudes de reversión de transacciones
 * utilizando una pila para mantener el orden LIFO.
 */
public class GestorReversiones {

    private static GestorReversiones instancia;
    private final Pila<Transaccion> pilaReversiones;

    private GestorReversiones() {
        this.pilaReversiones = new Pila<>();
    }

    /**
     * Devuelve la instancia única del gestor (singleton).
     */
    public static GestorReversiones getInstance() {
        if (instancia == null) {
            instancia = new GestorReversiones();
        }
        return instancia;
    }

    /**
     * Agrega una nueva solicitud de reversión a la pila.
     * @param cliente Cliente que solicita la reversión
     * @param t Transacción que se desea revertir
     */
    public void solicitarReversion(Cliente cliente, Transaccion t) {
        if (t.getCuentaOrigen() == null || t.getCuentaOrigen().isBlank()) {
            t.setCuentaOrigen(cliente.getCuenta().getNumeroCuenta());
        }

        t.setCuentaDestino(t.getCuentaDestino());
        t.setCategoria(t.getCategoria());
        t.setTipo(t.getTipo());
        t.setFecha(t.getFecha());

        pilaReversiones.push(t);
    }

    /**
     * Devuelve la última solicitud sin eliminarla (peek).
     */
    public Transaccion verUltimaSolicitud() {
        return pilaReversiones.peek();
    }

    /**
     * Procesa (elimina) la última solicitud de la pila.
     */
    public Transaccion procesarSolicitud() {
        return pilaReversiones.pop();
    }

    /**
     * Verifica si hay solicitudes pendientes en la pila.
     */
    public boolean haySolicitudes() {
        return !pilaReversiones.estaVacia();
    }

    /**
     * Devuelve la pila completa (por referencia).
     * Se debe tener cuidado al modificarla desde fuera.
     */
    public Pila<Transaccion> getPila() {
        return pilaReversiones;
    }
}
