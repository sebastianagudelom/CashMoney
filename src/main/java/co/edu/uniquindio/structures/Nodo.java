package co.edu.uniquindio.structures;

public class Nodo<T> {
    private T dato;
    private Nodo<T> siguiente;

    public Nodo(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    // Getters
    public T getDato() { return dato; }
    public Nodo<T> getSiguiente() { return siguiente; }

    // Setters
    public void setDato(T dato) { this.dato = dato; }
    public void setSiguiente(Nodo<T> siguiente) { this.siguiente = siguiente; }
}