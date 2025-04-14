package co.edu.uniquindio.structures;

public class ArbolBinarioBusqueda<T extends Comparable<T>> {

    private class Nodo {
        T dato;
        Nodo izquierdo;
        Nodo derecho;

        public Nodo(T dato) {
            this.dato = dato;
            this.izquierdo = null;
            this.derecho = null;
        }
    }

    private Nodo raiz;

    public ArbolBinarioBusqueda() {
        raiz = null;
    }

    public void insertar(T dato) {
        raiz = insertarRec(raiz, dato);
    }

    private Nodo insertarRec(Nodo nodo, T dato) {
        if (nodo == null) {
            return new Nodo(dato);
        }
        int cmp = dato.compareTo(nodo.dato);
        if (cmp < 0) {
            nodo.izquierdo = insertarRec(nodo.izquierdo, dato);
        } else if (cmp > 0) {
            nodo.derecho = insertarRec(nodo.derecho, dato);
        }
        return nodo;
    }

    public boolean buscar(T dato) {
        return buscarRec(raiz, dato);
    }

    private boolean buscarRec(Nodo nodo, T dato) {
        if (nodo == null) {
            return false;
        }
        int cmp = dato.compareTo(nodo.dato);
        if (cmp == 0) {
            return true;
        } else if (cmp < 0) {
            return buscarRec(nodo.izquierdo, dato);
        } else {
            return buscarRec(nodo.derecho, dato);
        }
    }

    public void eliminar(T dato) {
        raiz = eliminarRec(raiz, dato);
    }

    private Nodo eliminarRec(Nodo nodo, T dato) {
        if (nodo == null) {
            return null;
        }
        int cmp = dato.compareTo(nodo.dato);
        if (cmp < 0) {
            nodo.izquierdo = eliminarRec(nodo.izquierdo, dato);
        } else if (cmp > 0) {
            nodo.derecho = eliminarRec(nodo.derecho, dato);
        } else {
            if (nodo.izquierdo == null) {
                return nodo.derecho;
            } else if (nodo.derecho == null) {
                return nodo.izquierdo;
            }
            nodo.dato = minimo(nodo.derecho);
            nodo.derecho = eliminarRec(nodo.derecho, nodo.dato);
        }
        return nodo;
    }

    private T minimo(Nodo nodo) {
        while (nodo.izquierdo != null) {
            nodo = nodo.izquierdo;
        }
        return nodo.dato;
    }

    public void recorrerInOrder() {
        recorrerInOrderRec(raiz);
        System.out.println();
    }

    private void recorrerInOrderRec(Nodo nodo) {
        if (nodo != null) {
            recorrerInOrderRec(nodo.izquierdo);
            System.out.print(nodo.dato + " ");
            recorrerInOrderRec(nodo.derecho);
        }
    }

    public T getMin() {
        if (raiz == null) {
            return null;
        }
        return minimo(raiz);
    }

    public T getMax() {
        if (raiz == null) {
            return null;
        }
        Nodo nodo = raiz;
        while (nodo.derecho != null) {
            nodo = nodo.derecho;
        }
        return nodo.dato;
    }
}
