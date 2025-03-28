package co.edu.uniquindio.managers;

import co.edu.uniquindio.exceptions.*;
import co.edu.uniquindio.models.*;
import co.edu.uniquindio.structures.ListaCircular;
import co.edu.uniquindio.structures.ListaEnlazada;
import co.edu.uniquindio.utils.SeguridadUtil;
import java.io.*;

public class GestorClientes {

    private static ListaEnlazada<Cliente> listaClientes = new ListaEnlazada<>();
    private static final String ARCHIVO_CLIENTES = "src/main/resources/data/clientes.dat";
    private static final String ARCHIVO_PUNTOS = "src/main/resources/data/puntos.dat";
    private static final SistemaPuntos sistemaPuntos = new SistemaPuntos();

    private GestorClientes() {
        cargarClientes();
    }

    public static SistemaPuntos getSistemaPuntos() {
        return sistemaPuntos;
    }
    public static ListaEnlazada<Cliente> getListaClientes() {
        return listaClientes;
    }

    // Método para registrar un nuevo cliente
    public static boolean registrarCliente(String nombre, String identificacion, String correo,
                                           String usuario, String clave, String ciudad) {
        try {
            verificarDuplicados(usuario, correo, identificacion);

            String claveEncriptada = SeguridadUtil.encriptar(clave);
            Cliente nuevoCliente = new Cliente(nombre, identificacion, correo, usuario, claveEncriptada,
                    ciudad);
            listaClientes.agregar(nuevoCliente);
            guardarClientes();
            System.out.println("Cliente registrado: " + nuevoCliente);
            return true;

        } catch (UsuarioYaExisteException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    // Transfiere el saldo por la cuenta
    public static boolean transferirSaldoPorCuenta(String cuentaOrigen, String cuentaDestino,
                                                   double monto, String categoria)
            throws TransaccionInvalidaException, CuentaNoEncontradaException {

        Cliente origen = buscarClientePorCuenta(cuentaOrigen);
        Cliente destino = buscarClientePorCuenta(cuentaDestino);

        if (cuentaOrigen.equals(cuentaDestino)) {
            throw new TransaccionInvalidaException("No se puede transferir a la misma cuenta.");
        }

        if (monto <= 0) {
            throw new TransaccionInvalidaException("El monto debe ser mayor que cero.");
        }

        GestorTransacciones.retirarSaldo(origen, monto);
        GestorTransacciones.depositarSaldo(destino, monto);

        Transaccion enviada = new Transaccion("Transferencia Enviada", monto, cuentaOrigen, cuentaDestino, categoria);
        enviada.setCuentaOrigen(cuentaOrigen);
        enviada.setCategoria(categoria);

        Transaccion recibida = new Transaccion("Transferencia Recibida", monto, cuentaOrigen, cuentaDestino, categoria);
        recibida.setCuentaOrigen(cuentaOrigen);
        recibida.setCategoria(categoria);

        GestorTransacciones.registrarTransferencia(origen, destino, enviada, recibida);
        guardarClientes();

        return true;
    }

    // Inscribe cuentas para un cliente
    public static boolean inscribirCuentaParaCliente(Cliente cliente, String numeroCuenta) {
        if (cliente.getCuentasInscritas() == null) {
            cliente.setCuentasInscritas(new java.util.HashSet<>());
        }

        if (cliente.getCuentasInscritas().contains(numeroCuenta)) {
            return false;
        }

        cliente.agregarCuentaInscrita(numeroCuenta);
        guardarClientes();
        return true;
    }

    // Busca el cliente por el número de cuenta
    public static Cliente buscarClientePorCuenta(String numeroCuenta) throws CuentaNoEncontradaException {
        for (Cliente cliente : listaClientes) {
            if (cliente.getCuenta().getNumeroCuenta().equals(numeroCuenta)) {
                return cliente;
            }
        }
        throw new CuentaNoEncontradaException("No se encontró la cuenta con número: " + numeroCuenta);
    }


    // Verifica el usuario y la clave para login
    public static Cliente verificarUsuario(String usuario, String claveIngresada) {
        String claveEncriptada = SeguridadUtil.encriptar(claveIngresada);
        for (Cliente cliente : listaClientes) {
            if (cliente.getUsuario().equals(usuario) && cliente.getClave().equals(claveEncriptada)) {
                return cliente;
            }
        }
        return null;
    }

    // Busca si existe el usuario
    public static Cliente buscarClientePorUsuario(String usuario) throws ClienteNoEncontradoException {
        for (Cliente c : listaClientes) {
            if (c.getUsuario().equals(usuario)) {
                return c;
            }
        }
        throw new ClienteNoEncontradoException("El cliente con usuario '" + usuario + "' no existe.");
    }

    // Verifica si antes de registrar hay clientes que tengan el usuario, correo o identificacion
    public static void verificarDuplicados(String usuario, String correo, String identificacion)
            throws UsuarioYaExisteException {
        for (Cliente c : listaClientes) {
            if (c.getUsuario().equals(usuario) ||
                    c.getCorreo().equals(correo) ||
                    c.getIdentificacion().equals(identificacion)) {
                throw new UsuarioYaExisteException("El usuario, correo o identificación ya están en uso.");
            }
        }
    }

    public static void imprimirClientes() {
        for (Cliente c : listaClientes) {
            System.out.println(c);
        }
    }

    public static boolean eliminarCliente(String usuario) {
        try {
            Cliente cliente = buscarClientePorUsuario(usuario);
            listaClientes.eliminar(cliente);
            guardarClientes();
            return true;
        } catch (ClienteNoEncontradoException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }


    public static boolean actualizarCliente(String usuario, String nuevoNombre, String nuevaIdentificacion,
                                            String nuevoCorreo, String nuevoUsuario, String nuevaClave,
                                            String nuevaCiudad) {
        try {
            Cliente cliente = buscarClientePorUsuario(usuario);

            if (nuevoNombre != null) cliente.setNombre(nuevoNombre);
            if (nuevaIdentificacion != null) cliente.setIdentificacion(nuevaIdentificacion);
            if (nuevoCorreo != null) cliente.setCorreo(nuevoCorreo);
            if (nuevaClave != null) cliente.setClave(nuevaClave);
            if (nuevaCiudad != null) cliente.setCiudad(nuevaCiudad);

            if (nuevoUsuarioValido(nuevoUsuario, usuario)) {
                cliente.setUsuario(nuevoUsuario);
            }

            guardarClientes();
            return true;
        } catch (ClienteNoEncontradoException e) {
            System.out.println("Error al actualizar cliente: " + e.getMessage());
            return false;
        }
    }

    private static boolean nuevoUsuarioValido(String nuevoUsuario, String actualUsuario) {
        if (nuevoUsuario == null || nuevoUsuario.equals(actualUsuario)) {
            return false;
        }

        try {
            // Si lo encuentra, entonces el nuevo ya existe, no es válido
            buscarClientePorUsuario(nuevoUsuario);
            return false;
        } catch (ClienteNoEncontradoException e) {
            // No lo encontró: está disponible
            return true;
        }
    }

    private static void guardarSistemaPuntos() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARCHIVO_PUNTOS))) {
            out.writeObject(sistemaPuntos);
        } catch (IOException e) {
            System.out.println("Error al guardar el sistema de puntos: " + e.getMessage());
        }
    }

    public static void guardarClientes() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARCHIVO_CLIENTES))) {
            out.writeObject(listaClientes);
        } catch (IOException e) {
            System.out.println("Error al guardar los clientes: " + e.getMessage());
        }
        guardarSistemaPuntos();
    }

    private static void cargarSistemaPuntos() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARCHIVO_PUNTOS))) {
            SistemaPuntos cargado = (SistemaPuntos) in.readObject();
            if (cargado != null) {
                sistemaPuntos.reemplazarPor(cargado);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se pudo cargar el sistema de puntos.");
        }
    }

    public static void cargarClientes() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARCHIVO_CLIENTES))) {
            listaClientes = (ListaEnlazada<Cliente>) in.readObject();
            for (Cliente c : listaClientes) {
                if (c.getCuenta() == null) {
                    c.setCuenta(new Cuenta());
                } else if (c.getCuenta().getNumeroCuenta() == null || c.getCuenta().getNumeroCuenta().isEmpty()) {
                    c.getCuenta().setNumeroCuenta(c.getCuenta().getNumeroCuenta());
                }

                // 💥 Asegurar que la lista circular de notificaciones esté inicializada
                if (c.getNotificaciones() == null) {
                    c.setNotificaciones(new ListaCircular<>());
                }
            }

            cargarSistemaPuntos();
            guardarClientes();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se encontraron clientes guardados.");
        }
    }
}