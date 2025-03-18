package co.edu.uniquindio.models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorClientes {
    private static List<Cliente> listaClientes = new ArrayList<>();

    private static final String ARCHIVO_CLIENTES = "clientes.dat"; // Archivo para persistencia

    // Método para registrar un nuevo cliente
    public static boolean registrarCliente(String nombre, String identificacion, String correo, String usuario,
                                           String clave, String ciudad) {
        if (buscarClientePorUsuario(usuario) == null) { // Verifica si ya existe el usuario
            Cliente nuevoCliente = new Cliente(nombre, identificacion, correo, usuario, clave, ciudad);
            listaClientes.add(nuevoCliente);
            guardarClientes(); // Guardar en archivo después de agregar
            System.out.println("Se guardó el cliente en archivo.");

            System.out.println("Lista de clientes actualizada:");
            imprimirClientes();

            return true; // Registro exitoso
        }
        return false; // Usuario ya existe
    }


    // Método para buscar un cliente por usuario y contraseña
    public static Cliente verificarUsuario(String usuario, String clave) {
        for (Cliente c : listaClientes) {
            if (c.getUsuario().equals(usuario) && c.getClave().equals(clave)) {
                return c;
            }
        }
        return null; // No encontrado
    }

    // Método para buscar un cliente por nombre de usuario
    public static Cliente buscarClientePorUsuario(String usuario) {
        for (Cliente c : listaClientes) {
            if (c.getUsuario().equals(usuario)) {
                return c;
            }
        }
        return null;
    }

    // Método para eliminar un cliente
    public static boolean eliminarCliente(String usuario) {
        Cliente cliente = buscarClientePorUsuario(usuario);
        if (cliente != null) {
            listaClientes.remove(cliente);
            guardarClientes(); // Guardar cambios en archivo
            return true; // Eliminado correctamente
        }
        return false; // Cliente no encontrado
    }

    // Método para actualizar los datos de un cliente
    public static boolean actualizarCliente(String usuario, String nuevoNombre, String nuevaIdentificacion,
                                            String nuevoCorreo, String nuevaClave, String nuevaCiudad) {
        Cliente cliente = buscarClientePorUsuario(usuario);
        if (cliente != null) {
            cliente.setNombre(nuevoNombre);
            cliente.setIdentificacion(nuevaIdentificacion);
            cliente.setCorreo(nuevoCorreo);
            cliente.setClave(nuevaClave);
            cliente.setCiudad(nuevaCiudad);
            guardarClientes(); // Guardar cambios en archivo
            return true; // Actualización exitosa
        }
        return false; // Cliente no encontrado
    }

    // Método para obtener la lista de clientes
    public static List<Cliente> getListaClientes() {
        return listaClientes;
    }

    // Método para imprimir clientes (para depuración)
    public static void imprimirClientes() {
        for (Cliente c : listaClientes) {
            System.out.println(c);
        }
    }

    // Método para transferir saldo entre clientes
    public static boolean transferirSaldo(String usuarioOrigen, String usuarioDestino, double monto) {
        Cliente clienteOrigen = buscarClientePorUsuario(usuarioOrigen);
        Cliente clienteDestino = buscarClientePorUsuario(usuarioDestino);

        // Verificar si ambos clientes existen y si el saldo es suficiente
        if (clienteOrigen != null && clienteDestino != null && clienteOrigen.getCuenta().retirar(monto)) {
            clienteDestino.getCuenta().depositar(monto);
            guardarClientes(); // Guardar cambios en archivo
            return true; // Transferencia exitosa
        }
        return false; // Transferencia fallida (saldo insuficiente o usuario inexistente)
    }



    // Método para guardar la lista de clientes en un archivo
    public static void guardarClientes() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARCHIVO_CLIENTES))) {
            out.writeObject(listaClientes);
            System.out.println("Clientes guardados correctamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar los clientes: " + e.getMessage());
        }
    }



    // Método para cargar clientes desde un archivo
    public static void cargarClientes() {
        File archivo = new File(ARCHIVO_CLIENTES);
        if (!archivo.exists()) {
            System.out.println("El archivo de clientes no existe. Creando uno nuevo...");
            return; // No hay clientes que cargar
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARCHIVO_CLIENTES))) {
            listaClientes = (List<Cliente>) in.readObject();
            System.out.println("Clientes cargados: " + listaClientes.size());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se encontraron clientes guardados.");
        }
    }

}
