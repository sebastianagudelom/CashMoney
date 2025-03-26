package co.edu.uniquindio.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nombre, identificacion, correo, usuario, clave, ciudad;
    private Cuenta cuenta;
    private Set<String> cuentasInscritas;
    private List<Transaccion> historialTransacciones;


    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public void setClave(String clave) { this.clave = clave; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public void setHistorialTransacciones(List<Transaccion> historial) { this.historialTransacciones = historial; }
    public void setCuenta(Cuenta cuenta) { this.cuenta = cuenta; }
    public void setCuentasInscritas(Set<String> cuentas) { this.cuentasInscritas = cuentas; }

    public Cliente(String nombre, String identificacion, String correo, String usuario, String clave, String ciudad)
    {
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.correo = correo;
        this.usuario = usuario;
        this.clave = clave;
        this.ciudad = ciudad;
        this.cuentasInscritas = new HashSet<>();
        this.historialTransacciones = new ArrayList<>();
        this.cuenta = new Cuenta();
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
    public List<Transaccion> getHistorialTransacciones() { return historialTransacciones; }


    //CAMBIADO
    public void agregarCuentaInscrita(String numeroCuenta) {
        this.cuentasInscritas.add(numeroCuenta);
    }
    //CAMBIADO
    public void agregarTransaccion(Transaccion transaccion) {
        historialTransacciones.add(transaccion);
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