package co.edu.uniquindio.exceptions;

public class UsuarioYaExisteException extends Exception {
    public UsuarioYaExisteException(String mensaje) {
        super(mensaje);
    }
}