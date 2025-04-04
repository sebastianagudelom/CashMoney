package co.edu.uniquindio.structures;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Pila<T> implements Iterable<T> {

    private final LinkedList<T> elementos;

    public Pila() {
        elementos = new LinkedList<>();
    }

    public void push(T elemento) {
        elementos.addFirst(elemento);
    }

    public T pop() {
        if (estaVacia()) {
            throw new NoSuchElementException("La pila está vacía");
        }
        return elementos.removeFirst();
    }

    public T peek() {
        if (estaVacia()) {
            throw new NoSuchElementException("La pila está vacía");
        }
        return elementos.getFirst();
    }

    public boolean estaVacia() {
        return elementos.isEmpty();
    }

    public int tamano() {
        return elementos.size();
    }

    @Override
    public Iterator<T> iterator() {
        return elementos.iterator();
    }
}
