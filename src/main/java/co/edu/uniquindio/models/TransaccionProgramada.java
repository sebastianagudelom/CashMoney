package co.edu.uniquindio.models;

import java.io.Serializable;
import java.time.LocalDate;

public class TransaccionProgramada implements Serializable {

    private String usuarioOrigen;
    private String usuarioDestino;
    private String categoria;
    private double monto;
    private LocalDate fechaEjecucion;

    public TransaccionProgramada(String usuarioOrigen, String usuarioDestino, double monto, LocalDate fechaEjecucion) {
        this.usuarioOrigen = usuarioOrigen;
        this.usuarioDestino = usuarioDestino;
        this.monto = monto;
        this.fechaEjecucion = fechaEjecucion;
    }

    // Setters
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setUsuarioOrigen(String usuarioOrigen) { this.usuarioOrigen = usuarioOrigen; }
    public void setUsuarioDestino(String usuarioDestino) { this.usuarioDestino = usuarioDestino; }
    public void setMonto(double monto) { this.monto = monto; }
    public void setFechaEjecucion(LocalDate fechaEjecucion) { this.fechaEjecucion = fechaEjecucion; }

    // Getters
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
    public String getCategoria() { return categoria; }

    @Override
    public String toString() {
        return "Transferencia de " + usuarioOrigen + " a " + usuarioDestino + " por $" + monto +
                " programada para " + fechaEjecucion;
    }
}
