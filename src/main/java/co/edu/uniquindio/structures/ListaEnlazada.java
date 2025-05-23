package co.edu.uniquindio.structures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class ListaEnlazada<T> implements Iterable<T>, Serializable {

    private Nodo<T> cabeza;
    private int tamanio;

    public ListaEnlazada() {
        cabeza = null;
        tamanio = 0;
    }

    public void agregar(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        if (cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            Nodo<T> actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevoNodo);
        }
        tamanio++;
    }

    public void agregarInicio(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        nuevoNodo.setSiguiente(cabeza);
        cabeza = nuevoNodo;
        tamanio++;
    }

    public boolean eliminar(T dato) {
        if (cabeza == null) return false;

        if (cabeza.getDato().equals(dato)) {
            cabeza = cabeza.getSiguiente();
            tamanio--;
            return true;
        }

        Nodo<T> actual = cabeza;
        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getDato().equals(dato)) {
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                tamanio--;
                return true;
            }
            actual = actual.getSiguiente();
        }

        return false;
    }

    public T obtener(int indice) {
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException("Índice fuera de rango");
        }
        Nodo<T> actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    public void limpiar() {
        cabeza = null;
        tamanio = 0;
    }

    public int getTamanio() {
        return tamanio;
    }

    public boolean estaVacia() {
        return tamanio == 0;
    }

    public void imprimir() {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            System.out.println(actual.getDato());
            actual = actual.getSiguiente();
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Nodo<T> actual = cabeza;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T dato = actual.getDato();
                actual = actual.getSiguiente();
                return dato;
            }
        };
    }

    public List<T> aListaJava() {
        List<T> lista = new ArrayList<>();
        for (T dato : this) {
            lista.add(dato);
        }
        return lista;
    }
}