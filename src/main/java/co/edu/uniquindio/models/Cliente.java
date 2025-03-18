package co.edu.uniquindio.models;

import java.util.*;

public class Cliente {
    private static List<Cliente> listaClientes = new ArrayList<>(); // Lista de clientes
    private String nombre, identificacion, correo, usuario, clave, ciudad;
    private int puntos;
    private double saldo;


    // Constructor completo para el registro
    public Cliente(String nombre, String identificacion, String correo, String usuario, String clave, String ciudad) {
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.correo = correo;
        this.usuario = usuario;
        this.clave = clave;
        this.ciudad = ciudad;

        // Agregar automáticamente a la lista de clientes
        listaClientes.add(this);
    }

    public void depositar(double monto) {
        this.saldo += monto;
    }

    public double getSaldo() {
        return saldo;
    }


    // Método para agregar un cliente manualmente
    public static void agregarCliente(Cliente cliente) {
        listaClientes.add(cliente);
    }

    // Método para obtener la lista de clientes
    public static List<Cliente> getListaClientes() {
        return listaClientes;
    }

    // Método para buscar un cliente por usuario y contraseña
    public static Cliente verificarUsuario(String usuario, String contrasena) {
        for (Cliente c : listaClientes) {
            if (c.usuario.equals(usuario) && c.clave.equals(contrasena)) {
                return c;
            }
        }
        return null; // Retorna null si no encuentra coincidencia
    }

    // GETTERS
    public String getNombre() {
        return nombre;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getCorreo() {
        return correo;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getClave() {
        return clave;
    }

    public String getCiudad() {
        return ciudad;
    }

    // SETTERS (para modificar datos después del registro)
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }


    @Override
    public String toString() {
        return "Cliente{" +
                "nombre='" + nombre + '\'' +
                ", identificacion='" + identificacion + '\'' +
                ", correo='" + correo + '\'' +
                ", usuario='" + usuario + '\'' +
                ", ciudad='" + ciudad + '\'' +
                '}';
    }
}
