package co.edu.uniquindio.managers;

import co.edu.uniquindio.models.Administrador;

public class GestorAdministradores {

    private static final Administrador ADMIN = new Administrador("admin", "admin");

    public static boolean verificarCredenciales(String usuario, String clave) {
        return ADMIN.usuario().equals(usuario) && ADMIN.clave().equals(clave);
    }
}
