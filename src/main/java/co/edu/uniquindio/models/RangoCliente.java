package co.edu.uniquindio.models;

public enum RangoCliente {
    BRONCE,
    PLATA,
    ORO,
    PLATINO;

    public static RangoCliente calcularRango(int puntos) {
        if (puntos <= 500) return BRONCE;
        else if (puntos <= 1000) return PLATA;
        else if (puntos <= 5000) return ORO;
        else return PLATINO;
    }
}
