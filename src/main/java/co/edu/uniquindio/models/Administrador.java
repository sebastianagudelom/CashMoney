package co.edu.uniquindio.models;

import java.io.Serializable;

public class Administrador implements Serializable {
    private final String usuario;
    private final String clave;

    public Administrador(String usuario, String clave) {
        this.usuario = usuario;
        this.clave = clave;
    }

    public String getUsuario() { return usuario; }
    public String getClave() { return clave; }
}