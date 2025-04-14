package co.edu.uniquindio.structures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ColaPrioridad<T extends Comparable<T>> implements Iterable<T>, Serializable {

    private List<T> heap;

    public ColaPrioridad() {
        heap = new ArrayList<>();
    }

    public void insertar(T elemento) {
        heap.add(elemento);
        int indice = heap.size() - 1;
        while (indice > 0 && heap.get(padre(indice)).compareTo(heap.get(indice)) > 0) {
            intercambiar(indice, padre(indice));
            indice = padre(indice);
        }
    }

    public T extraerMinimo() {
        if (heap.isEmpty()) {
            return null;
        }
        T minimo = heap.get(0);
        T ultimo = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, ultimo);
            bajar(0);
        }
        return minimo;
    }

    private void bajar(int indice) {
        int menor = indice;
        int izq = izquierda(indice);
        int der = derecha(indice);
        if (izq < heap.size() && heap.get(izq).compareTo(heap.get(menor)) < 0) {
            menor = izq;
        }
        if (der < heap.size() && heap.get(der).compareTo(heap.get(menor)) < 0) {
            menor = der;
        }
        if (menor != indice) {
            intercambiar(indice, menor);
            bajar(menor);
        }
    }

    private int padre(int i) {
        return (i - 1) / 2;
    }

    private int izquierda(int i) {
        return 2 * i + 1;
    }

    private int derecha(int i) {
        return 2 * i + 2;
    }

    private void intercambiar(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    public boolean esVacia() {
        return heap.isEmpty();
    }

    public int tamano() {
        return heap.size();
    }

    public T verMinimo() {
        if (heap.isEmpty()) {
            return null;
        }
        return heap.get(0);
    }

    @Override
    public Iterator<T> iterator() {
        return heap.iterator();
    }
}
