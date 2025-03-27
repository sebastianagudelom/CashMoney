package co.edu.uniquindio.models;

import java.io.Serial;
import java.io.Serializable;

public class NodoPuntos implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // En NodoPuntos.java
    private String cedula;
    private NodoPuntos izquierdo;
    private NodoPuntos derecho;
    private int puntos;

    // Getters y Setters
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public NodoPuntos getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(NodoPuntos izquierdo) {
        this.izquierdo = izquierdo;
    }

    public NodoPuntos getDerecho() {
        return derecho;
    }

    public void setDerecho(NodoPuntos derecho) {
        this.derecho = derecho;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }


    public NodoPuntos(String cedula, int puntos) {
        this.cedula = cedula;
        this.puntos = puntos;
        this.izquierdo = null;
        this.derecho = null;
    }
}
