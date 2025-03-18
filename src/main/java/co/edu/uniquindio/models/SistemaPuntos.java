package co.edu.uniquindio.models;

public class SistemaPuntos {
    private int puntos;

    public SistemaPuntos() { this.puntos = 0; }

    public int getPuntos() { return puntos; }

    public void agregarPuntos(int cantidad) { puntos += cantidad; }

    public boolean canjearPuntos(int cantidad) {
        if (cantidad <= puntos) {
            puntos -= cantidad;
            return true;
        }
        return false;
    }
}
