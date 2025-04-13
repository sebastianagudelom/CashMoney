package co.edu.uniquindio.models;

import co.edu.uniquindio.utils.SeguridadUtil;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaccion implements Serializable {
    private static final long serialVersionUID = 1L;
    private double monto;
    private String fecha, tipo, cuentaOrigen, cuentaDestino, categoria, hashVerificacion;
    private boolean revertida = false;

    public boolean isRevertida() {
        return revertida;
    }
    public void setRevertida(boolean revertida) { this.revertida = revertida; }

    // Constructor
    public Transaccion(String tipo, double monto, String cuentaOrigen, String cuentaDestino, String categoria) {
        this.tipo = tipo;
        this.monto = monto;
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.categoria = categoria;
        this.fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        generarHashVerificacion();
    }

    public String getCuentaOrigen() { return cuentaOrigen; }

    public void setCuentaOrigen(String cuentaOrigen) { this.cuentaOrigen = cuentaOrigen; }

    // Getters
    public String getTipo() { return tipo; }
    public double getMonto() { return monto; }
    public String getCuentaDestino() { return cuentaDestino; }
    public String getFecha() { return fecha; }
    public String getCategoria() { return categoria; }
    public String getHashVerificacion() { return hashVerificacion; }

    // Setters
    public void setTipo(String tipo) { this.tipo = tipo; generarHashVerificacion(); }
    public void setMonto(double monto) { this.monto = monto; generarHashVerificacion(); }
    public void setCuentaDestino(String cuentaDestino) { this.cuentaDestino = cuentaDestino; generarHashVerificacion(); }
    public void setFecha(String fecha) { this.fecha = fecha; generarHashVerificacion(); }
    public void setCategoria(String categoria) { this.categoria = categoria; generarHashVerificacion(); }

    public void generarHashVerificacion() {
        String datos = tipo + monto + cuentaDestino + categoria + fecha;
        this.hashVerificacion = SeguridadUtil.encriptar(datos);
    }

    public boolean verificarIntegridad() {
        String datos = tipo + monto + cuentaDestino + categoria + fecha;
        String hashCalculado = SeguridadUtil.encriptar(datos);
        return hashVerificacion != null && hashVerificacion.equals(hashCalculado);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(fecha + " - " + tipo + " de $" + monto);

        if (cuentaDestino != null) sb.append(" a cuenta ").append(cuentaDestino);
        if (cuentaOrigen != null) sb.append(" desde cuenta ").append(cuentaOrigen);

        if (categoria != null) sb.append(" (Categoría: ").append(categoria).append(")");

        if (revertida) sb.append(" [REVERTIDA]");

        return sb.toString();
    }
}