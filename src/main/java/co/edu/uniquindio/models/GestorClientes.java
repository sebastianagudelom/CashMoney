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
            guardarClientes();
            return true;
        }
        return false;
    }

    // Método para transferir saldo por número de cuenta
    public static boolean transferirSaldoPorCuenta(String numeroCuentaOrigen, String numeroCuentaDestino, double monto) {
        Cliente origen = buscarClientePorCuenta(numeroCuentaOrigen);
        Cliente destino = buscarClientePorCuenta(numeroCuentaDestino);

        if (origen == null || destino == null) {
            System.out.println("❌ Error: Una de las cuentas no existe.");
            return false;
        }

        if (origen.getCuenta().retirar(monto)) {
            destino.getCuenta().depositar(monto);
            guardarClientes();
            return true;
        }
        return false;
    }

    // Método para buscar cliente por número de cuenta
    public static Cliente buscarClientePorCuenta(String numeroCuenta) {
        System.out.println("🔍 Buscando cliente con cuenta: " + numeroCuenta);

        for (Cliente c : listaClientes) {
            System.out.println("👤 Cliente: " + c.getUsuario() + " - Cuenta: " + c.getCuenta().getNumeroCuenta());

            if (c.getCuenta() != null && c.getCuenta().getNumeroCuenta().equals(numeroCuenta)) {
                System.out.println("✅ Cliente encontrado: " + c.getUsuario());
                return c;
            }
        }

        System.out.println("❌ No se encontró ningún cliente con la cuenta " + numeroCuenta);
        return null;
    }


    // Método para verificar usuario por usuario y clave
    public static Cliente verificarUsuario(String usuario, String clave) {
        for (Cliente c : listaClientes) {
            if (c.getUsuario().equals(usuario) && c.getClave().equals(clave)) {
                return c;
            }
        }
        return null;
    }

    public static Cliente buscarClientePorUsuario(String usuario) {
        for (Cliente c : listaClientes) {
            if (c.getUsuario().equals(usuario)) {
                return c;
            }
        }
        return null;
    }

    // Método para obtener la lista de clientes
    public static List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public static void imprimirClientes() {
        System.out.println("📋 Lista de Clientes:");
        for (Cliente c : listaClientes) {
            System.out.println(c);
        }
    }


    // Método para guardar la lista de clientes en un archivo
    public static void guardarClientes() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARCHIVO_CLIENTES))) {
            out.writeObject(listaClientes);
        } catch (IOException e) {
            System.out.println("Error al guardar los clientes: " + e.getMessage());
        }
    }

    // Método para cargar clientes desde un archivo
    public static void cargarClientes() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARCHIVO_CLIENTES))) {
            listaClientes = (List<Cliente>) in.readObject();
            for (Cliente c : listaClientes) {
                if (c.getCuenta() == null) {
                    c.setCuenta(new Cuenta());
                }
            }
            guardarClientes();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ No se encontraron clientes guardados.");
        }
    }
}
