package co.edu.uniquindio.models;

import java.io.Serial;
import java.io.Serializable;

public class NodoPuntos implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    String cedula;
    int puntos;
    NodoPuntos izquierdo, derecho;

    public NodoPuntos(String cedula, int puntos) {
        this.cedula = cedula;
        this.puntos = puntos;
        this.izquierdo = null;
        this.derecho = null;
    }
}
