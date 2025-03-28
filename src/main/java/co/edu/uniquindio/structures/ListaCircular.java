package co.edu.uniquindio.structures;

import java.io.Serializable;
import java.util.Iterator;

public class ListaCircular<T extends Serializable> implements Iterable<T>, Serializable {

    private Nodo<T> cabeza;

    public void agregar(T elemento) {
        Nodo<T> nuevo = new Nodo<>(elemento);
        if (cabeza == null) {
            cabeza = nuevo;
            cabeza.setSiguiente(cabeza);
        } else {
            Nodo<T> temp = cabeza;
            while (temp.getSiguiente() != cabeza) {
                temp = temp.getSiguiente();
            }
            temp.setSiguiente(nuevo);
            nuevo.setSiguiente(cabeza);
        }
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Nodo<T> actual = cabeza;
            boolean vueltaCompleta = false;

            @Override
            public boolean hasNext() {
                return actual != null && !vueltaCompleta;
            }

            @Override
            public T next() {
                T dato = actual.getDato();
                actual = actual.getSiguiente();
                if (actual == cabeza) {
                    vueltaCompleta = true;
                }
                return dato;
            }
        };
    }
}
