package co.edu.uniquindio.models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorClientes {
    private static List<Cliente> listaClientes = new ArrayList<>();
    private static final String ARCHIVO_CLIENTES = "clientes.dat"; // Archivo para persistencia
    private static final String ARCHIVO_PUNTOS = "puntos.dat"; // Archivo para persistencia
    private static final SistemaPuntos sistemaPuntos = new SistemaPuntos();

    // 🔥 Instancia singleton
    private static GestorClientes instancia;

    // ✅ Constructor privado
    private GestorClientes() {
        cargarClientes();
    }

    // ✅ Método para obtener la instancia
    public static GestorClientes obtenerInstancia() {
        if (instancia == null) {
            instancia = new GestorClientes();
        }
        return instancia;
    }
    
    public static SistemaPuntos getSistemaPuntos() {
        return sistemaPuntos;
    }
    // Método para obtener la lista de clientes
    public static List<Cliente> getListaClientes() {
        return listaClientes;
    }

    // Método para registrar un nuevo cliente
    public static boolean registrarCliente(String nombre, String identificacion, String correo, String usuario,
                                           String clave, String ciudad) {
        if (buscarClientePorUsuario(usuario) == null) {
            Cliente nuevoCliente = new Cliente(nombre, identificacion, correo, usuario, clave, ciudad);
            listaClientes.add(nuevoCliente);
            guardarClientes();
            System.out.println("Cliente registrado: " + nuevoCliente);
            return true;
        }
        return false;
    }

    // Método para transferir saldo por número de cuenta
    public static boolean transferirSaldoPorCuenta(String numeroCuentaOrigen, String numeroCuentaDestino, double monto)
    {
        Cliente origen = buscarClientePorCuenta(numeroCuentaOrigen);
        Cliente destino = buscarClientePorCuenta(numeroCuentaDestino);
        if (origen == null || destino == null) {
            System.out.println("Error: Una de las cuentas no existe.");
            return false;
        }
        if (origen.getCuenta().getSaldo() < monto) {
            System.out.println("Error: Saldo insuficiente.");
            return false;
        }
        origen.getCuenta().retirar(monto);
        destino.getCuenta().depositar(monto);
        origen.agregarTransaccion(new Transaccion("Transferencia Enviada", monto, numeroCuentaDestino));
        destino.agregarTransaccion(new Transaccion("Transferencia Recibida", monto, numeroCuentaOrigen));
        guardarClientes();
        return true;
    }

    // Método para buscar cliente por número de cuenta
    public static Cliente buscarClientePorCuenta(String numeroCuenta) {
        System.out.println("Buscando cliente con cuenta: " + numeroCuenta);
        for (Cliente c : listaClientes) {
            System.out.println("Cliente: " + c.getUsuario() + " - Cuenta: " + c.getCuenta().getNumeroCuenta());
            if (c.getCuenta() != null && c.getCuenta().getNumeroCuenta().equals(numeroCuenta)) {
                System.out.println("Cliente encontrado: " + c.getUsuario());
                return c;
            }
        }
        System.out.println("No se encontró ningún cliente con la cuenta " + numeroCuenta);
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

    // Método que busca los clientes por el usuario
    public static Cliente buscarClientePorUsuario(String usuario) {
        for (Cliente c : listaClientes) {
            if (c.getUsuario().equals(usuario)) {
                return c;
            }
        }
        return null;
    }

    // Imprime los clientes
    public static void imprimirClientes() {
        System.out.println("Lista de Clientes:");
        for (Cliente c : listaClientes) {
            System.out.println(c);
        }
    }

    // Método para eliminar un cliente de la lista y actualizar el archivo
    public static boolean eliminarCliente(String usuario) {
        Cliente cliente = buscarClientePorUsuario(usuario);
        if (cliente != null) {
            listaClientes.remove(cliente);
            guardarClientes();
            return true;
        }
        return false;
    }

    // Método para actualizar los datos del cliente
    public static boolean actualizarCliente(String usuario, String nuevoNombre, String nuevaIdentificacion,
                                            String nuevoCorreo, String nuevoUsuario, String nuevaClave,
                                            String nuevaCiudad) {
        Cliente cliente = buscarClientePorUsuario(usuario);
        if (cliente != null) {
            if (nuevoNombre != null) cliente.setNombre(nuevoNombre);
            if (nuevaIdentificacion != null) cliente.setIdentificacion(nuevaIdentificacion);
            if (nuevoCorreo != null) cliente.setCorreo(nuevoCorreo);
            if (nuevoUsuario != null && buscarClientePorUsuario(nuevoUsuario) == null) {
                cliente.setUsuario(nuevoUsuario);
            }
            if (nuevaClave != null) cliente.setClave(nuevaClave);
            if (nuevaCiudad != null) cliente.setCiudad(nuevaCiudad);
            guardarClientes();
            return true;
        }
        return false;
    }

    // Método para guardar la lista de clientes en el archivo
    private static void guardarSistemaPuntos() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARCHIVO_PUNTOS))) {
            out.writeObject(sistemaPuntos);
            //System.out.println("Sistema de puntos guardado.");
        } catch (IOException e) {
            System.out.println("Error al guardar el sistema de puntos: " + e.getMessage());
        }
    }

    // Método para guardar la lista de clientes en el archivo
    public static void guardarClientes() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARCHIVO_CLIENTES))) {
            out.writeObject(listaClientes);
        } catch (IOException e) {
            System.out.println("Error al guardar los clientes: " + e.getMessage());
        }
        guardarSistemaPuntos();
    }

    // Método para cargar los puntos de un cliente desde el archivo
    private static void cargarSistemaPuntos() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARCHIVO_PUNTOS))) {
            SistemaPuntos cargado = (SistemaPuntos) in.readObject();
            if (cargado != null) {
                sistemaPuntos.reemplazarPor(cargado);
            }
            //System.out.println("Sistema de puntos cargado.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se pudo cargar el sistema de puntos. Se usará uno nuevo.");
        }
    }

    // Método para cargar clientes desde el archivo
    public static void cargarClientes() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARCHIVO_CLIENTES))) {
            listaClientes = (List<Cliente>) in.readObject();
            for (Cliente c : listaClientes) {
                if (c.getCuenta() == null) {
                    c.setCuenta(new Cuenta());
                } else if (c.getCuenta().getNumeroCuenta() == null || c.getCuenta().getNumeroCuenta().isEmpty()) {
                    c.getCuenta().setNumeroCuenta(c.getCuenta().getNumeroCuenta());
                }
            }
            cargarSistemaPuntos();
            guardarClientes();
            //System.out.println("Clientes cargados correctamente.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se encontraron clientes guardados.");
        }
    }


}
