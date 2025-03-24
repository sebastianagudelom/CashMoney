package co.edu.uniquindio.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaccion implements Serializable {
    private static final long serialVersionUID = 1L;
    private double monto;
    private String fecha, referencia, tipo, cuentaDestino;

    public Transaccion(String tipo, double monto, String cuentaDestino) {
        this.tipo = tipo;
        this.monto = monto;
        this.cuentaDestino = cuentaDestino;
        this.fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    }

    // Getters
    public String getTipo() { return tipo; }
    public double getMonto() { return monto; }
    public String getCuentaDestino() { return cuentaDestino; }
    public String getFecha() { return fecha; }
    public String getReferencia() { return referencia; }


    // Setters
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setMonto(double monto) { this.monto = monto; }
    public void setCuentaDestino(String cuentaDestino) { this.cuentaDestino = cuentaDestino; }
    public void setFecha(String fecha) { this.fecha = fecha; }



    @Override
    public String toString() {
        if (cuentaDestino != null) {
            return fecha + " - " + tipo + " de $" + monto + " a cuenta " + cuentaDestino;
        } else {
            return fecha + " - " + tipo + " de $" + monto;
        }
    }
}
