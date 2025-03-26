package co.edu.uniquindio.models;

import java.io.Serializable;
import java.util.Random;

public class Cuenta implements Serializable {
    private static final long serialVersionUID = 1L;
    private String numeroCuenta;
    private double saldo;

    public Cuenta() {
        this.saldo = 0.0;
        this.numeroCuenta = generarNumeroCuenta();
        System.out.println("Número de cuenta generado: " + this.numeroCuenta);
    }

    // Getters
    public String getNumeroCuenta() { return numeroCuenta; }
    public double getSaldo() { return saldo; }


    // Setters
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    private String generarNumeroCuenta() {
        Random random = new Random();
        int numero = 10000000 + random.nextInt(90000000);
        return String.valueOf(numero);
    }

    public boolean retirar(double monto) {
        if (monto > 0 && saldo >= monto) {
            saldo -= monto;
            return true;
        }
        return false;
    }

    public boolean depositar(double monto) {
        if (monto > 0) {
            saldo += monto;
            return true;
        }
        return false;
    }


    @Override
    public String toString() {
        return "Cuenta{" +
                "numeroCuenta='" + numeroCuenta + '\'' +
                ", saldo=" + saldo +
                '}';
    }
}