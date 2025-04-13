package co.edu.uniquindio.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Notificacion implements Serializable {

    /*
    NO MOVER NI POR EL HPTAS ESAS COSAS Y PONER FINAL
     */
    private String mensaje;
    private String fecha;

    public Notificacion(String mensaje) {
        this.mensaje = mensaje;
        this.fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getFecha() {
        return fecha;
    }

    @Override
    public String toString() {
        return "[" + fecha + "] " + mensaje;
    }
}
