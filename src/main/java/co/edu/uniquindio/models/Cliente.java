package co.edu.uniquindio.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nombre, identificacion, correo, usuario, clave, ciudad;
    private Cuenta cuenta;
    private Set<String> cuentasInscritas;

    public Cliente(String nombre, String identificacion, String correo, String usuario, String clave, String ciudad) {
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.correo = correo;
        this.usuario = usuario;
        this.clave = clave;
        this.ciudad = ciudad;
        this.cuenta = new Cuenta();
        this.cuentasInscritas = new HashSet<>();
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getIdentificacion() { return identificacion; }
    public String getCorreo() { return correo; }
    public String getUsuario() { return usuario; }
    public String getClave() { return clave; }
    public Cuenta getCuenta() { return cuenta; }
    public String getCiudad() { return ciudad; }
    public Set<String> getCuentasInscritas() { return cuentasInscritas; }
    public String getNumeroCuenta() { return cuenta.getNumeroCuenta(); }

    // Métodos de Inscripción de Cuentas
    public boolean inscribirCuenta(String numeroCuenta) {
        if (cuentasInscritas == null) {
            cuentasInscritas = new HashSet<>();
        }
        if (cuentasInscritas.contains(numeroCuenta)) {
            return false; // Ya está inscrita
        }
        cuentasInscritas.add(numeroCuenta);
        GestorClientes.guardarClientes();
        return true;
    }

    public boolean estaInscrita(String numeroCuenta) {
        return cuentasInscritas != null && cuentasInscritas.contains(numeroCuenta);
    }

    // Métodos de Transacción
    public void depositar(double monto) {
        cuenta.depositar(monto);
    }

    public boolean retirar(double monto) {
        return cuenta.retirar(monto);
    }

    // Setters
    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public void setCuentasInscritas(Set<String> cuentas) {
        this.cuentasInscritas = cuentas;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "nombre='" + nombre + '\'' +
                ", identificacion='" + identificacion + '\'' +
                ", usuario='" + usuario + '\'' +
                ", clave='" + clave + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", correo='" + correo + '\'' +
                ", saldo='" + cuenta.getSaldo() + '\'' +
                ", numero de cuenta='" + cuenta.getNumeroCuenta() + '\'' +
                '}';
    }
}
