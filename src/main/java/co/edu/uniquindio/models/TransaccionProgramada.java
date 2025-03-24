package co.edu.uniquindio.models;

import java.io.Serializable;
import java.time.LocalDate;

public class TransaccionProgramada implements Serializable {

    private String usuarioOrigen;
    private String usuarioDestino;
    private double monto;
    private LocalDate fechaEjecucion;

    public TransaccionProgramada(String usuarioOrigen, String usuarioDestino, double monto, LocalDate fechaEjecucion) {
        this.usuarioOrigen = usuarioOrigen;
        this.usuarioDestino = usuarioDestino;
        this.monto = monto;
        this.fechaEjecucion = fechaEjecucion;
    }

    public String getUsuarioOrigen() {
        return usuarioOrigen;
    }

    public String getUsuarioDestino() {
        return usuarioDestino;
    }

    public double getMonto() {
        return monto;
    }

    public LocalDate getFechaEjecucion() {
        return fechaEjecucion;
    }

    @Override
    public String toString() {
        return "Transferencia de " + usuarioOrigen + " a " + usuarioDestino + " por $" + monto +
                " programada para " + fechaEjecucion;
    }
}
