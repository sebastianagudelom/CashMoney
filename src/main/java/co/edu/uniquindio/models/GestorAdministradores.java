package co.edu.uniquindio.models;

public class GestorAdministradores {

    private static final Administrador ADMIN = new Administrador("admin", "admin");

    public static boolean verificarCredenciales(String usuario, String clave) {
        return ADMIN.getUsuario().equals(usuario) && ADMIN.getClave().equals(clave);
    }
}
