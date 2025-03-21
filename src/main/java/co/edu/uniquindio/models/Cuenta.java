package co.edu.uniquindio.models;

import java.io.Serializable;
import java.util.Random;

public class Cuenta implements Serializable {
    private static final long serialVersionUID = 1L;
    private String numeroCuenta;
    private double saldo;

    public Cuenta() {
        this.saldo = 0.0;
        if (this.numeroCuenta == null || this.numeroCuenta.isEmpty()) {
            this.numeroCuenta = generarNumeroCuenta();
            System.out.println("✔ Número de cuenta generado: " + this.numeroCuenta);
        }
    }

    // Getters
    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public double getSaldo() {
        return saldo;
    }

    // Método para asignar número de cuenta solo si no lo tiene
    public void setNumeroCuenta(String numeroCuenta) {
        if (this.numeroCuenta == null || this.numeroCuenta.isEmpty()) {
            this.numeroCuenta = numeroCuenta;
        }
    }

    private String generarNumeroCuenta() {
        Random random = new Random();
        int numero = 10000000 + random.nextInt(90000000);
        return String.valueOf(numero);
    }

    public boolean retirar(double monto) {
        if (monto > 0 && saldo >= monto) {
            saldo -= monto;
            GestorClientes.guardarClientes();
            return true;
        }
        return false;
    }

    public void depositar(double monto) {
        if (monto > 0) {
            saldo += monto;
            GestorClientes.guardarClientes();
        }
    }

    @Override
    public String toString() {
        return "Cuenta{" +
                "numeroCuenta='" + numeroCuenta + '\'' +
                ", saldo=" + saldo +
                '}';
    }
}
