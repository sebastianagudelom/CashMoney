package co.edu.uniquindio.models;

import java.io.Serializable;

public record Administrador(String usuario, String clave) implements Serializable {
}