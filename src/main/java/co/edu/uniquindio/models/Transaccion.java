package co.edu.uniquindio.models;

import java.time.LocalDateTime;

public class Transaccion {
    private String tipo; // "Depósito" o "Retiro"
    private double monto;
    private LocalDateTime fecha;

    public Transaccion(String tipo, double monto) {
        this.tipo = tipo;
        this.monto = monto;
        this.fecha = LocalDateTime.now();
    }

    public String getTipo() { return tipo; }
    public double getMonto() { return monto; }
    public LocalDateTime getFecha() { return fecha; }
}
