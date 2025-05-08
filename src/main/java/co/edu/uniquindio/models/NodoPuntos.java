package co.edu.uniquindio.models;

import java.io.Serial;
import java.io.Serializable;

public class NodoPuntos implements Serializable, Comparable<NodoPuntos> {
    @Serial
    private static final long serialVersionUID = 1L;

    private String cedula;
    private NodoPuntos izquierdo;
    private NodoPuntos derecho;
    private int puntos;

    public NodoPuntos(String cedula, int puntos) {
        this.cedula = cedula;
        this.puntos = puntos;
        this.izquierdo = null;
        this.derecho = null;
    }

    public String getCedula() {
        return cedula;
    }

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

    @Override
    public int compareTo(NodoPuntos otro) {
        int cmp = Integer.compare(this.puntos, otro.puntos);
        if(cmp == 0) {
            cmp = this.cedula.compareTo(otro.cedula);
        }
        return cmp;
    }
}
