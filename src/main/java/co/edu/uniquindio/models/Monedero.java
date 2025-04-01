package co.edu.uniquindio.models;

import java.io.Serializable;

public class Monedero implements Serializable {

    private String nombre;
    private double saldo;
    private double meta;
    private String descripcion;
    private String colorHex;

    public Monedero(String nombre, double meta, String descripcion, String colorHex) {
        this.nombre = nombre;
        this.meta = meta;
        this.descripcion = descripcion;
        this.colorHex = colorHex;
        this.saldo = 0;
    }

    public boolean seCompletoMeta() {
        return saldo >= meta;
    }

    public boolean puedeRetirar() {
        return seCompletoMeta();
    }

    public void agregarSaldo(double monto) {
        if (monto > 0) {
            saldo += monto;
        }
    }

    public boolean retirarSaldo(double monto) {
        if (puedeRetirar() && monto > 0 && monto <= saldo) {
            saldo -= monto;
            return true;
        }
        return false;
    }

    // Getters
    public String getNombre() { return nombre; }
    public double getSaldo() { return saldo; }
    public double getMeta() { return meta; }
    public String getDescripcion() { return descripcion; }
    public String getColorHex() { return colorHex; }

    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setSaldo(double saldo) { this.saldo = saldo; }
    public void setMeta(double meta) { this.meta = meta; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }

    @Override
    public String toString() {
        String nombreSeguro = (nombre != null) ? nombre : "Monedero";
        return String.format("%s (Saldo: %.2f)", nombreSeguro, saldo);
    }

}
