package co.edu.uniquindio.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/**
 * Utilidad de seguridad para encriptar contraseñas usando SHA-256.
 */
public class SeguridadUtil {

    /**
     * Encripta una cadena usando SHA-256.
     *
     * @param clave La contraseña original en texto plano.
     * @return La contraseña encriptada en formato hexadecimal.
     */
    public static String encriptar(String clave) {
        try {
            // Obtener una instancia de MessageDigest para SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Aplicar el algoritmo a los bytes de la clave
            byte[] hash = digest.digest(clave.getBytes(StandardCharsets.UTF_8));

            // Convertir el hash en una cadena hexadecimal
            return bytesAHex(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al encriptar la clave: algoritmo no soportado", e);
        }
    }

    /**
     * Convierte un arreglo de bytes a una cadena hexadecimal.
     *
     * @param bytes Arreglo de bytes a convertir.
     * @return Cadena hexadecimal representando los bytes.
     */
    private static String bytesAHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02x", b)); // formato hexadecimal con dos dígitos
        }
        return hex.toString();
    }
}