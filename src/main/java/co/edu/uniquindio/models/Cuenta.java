package co.edu.uniquindio.models;

import java.io.Serializable;

public class Cuenta implements Serializable {
    private static final long serialVersionUID = 1L; // Agregar un UID para la compatibilidad de versiones

    private double saldo;

    public Cuenta() {
        this.saldo = 0.0;
    }

    public double getSaldo() {
        return saldo;
    }

    public void depositar(double monto) {
        if (monto > 0) {
            saldo += monto;
        }
    }

    public boolean retirar(double monto) {
        if (monto > 0 && saldo >= monto) {
            saldo -= monto;
            return true;
        }
        return false; // Saldo insuficiente
    }

    @Override
    public String toString() {
        return "Cuenta{saldo=" + saldo + "}";
    }
}
