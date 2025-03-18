package co.edu.uniquindio.models;

import java.io.Serializable;

public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L; // Versión para evitar errores de serialización
    private String nombre, identificacion, correo, usuario, clave, ciudad;
    private Cuenta cuenta;

    public Cliente(String nombre, String identificacion, String correo, String usuario, String clave, String ciudad) {
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.correo = correo;
        this.usuario = usuario;
        this.clave = clave;
        this.ciudad = ciudad;
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



    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setUsuario(String usuario) { this.usuario = usuario;}
    public void setClave(String clave) { this.clave = clave; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }


    @Override
    public String toString() {
        return "Cliente{" +
                "nombre='" + nombre + '\'' +
                ", usuario='" + usuario + '\'' +
                ", ciudad='" + ciudad + '\'' +
                '}';
    }
}
