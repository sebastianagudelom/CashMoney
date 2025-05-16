package co.edu.uniquindio.managers;

import co.edu.uniquindio.structures.GrafoDirigido;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class GestorRelacionesMonederos implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private static GestorRelacionesMonederos instancia;
    private final Map<String, GrafoDirigido<String>> grafoPorCliente;

    private static final String ARCHIVO = "src/main/resources/data/relacionesMonederos.dat";

    private GestorRelacionesMonederos() {
        this.grafoPorCliente = new HashMap<>();
    }

    public static GestorRelacionesMonederos getInstance() {
        if (instancia == null) {
            instancia = cargarDatos();
        }
        return instancia;
    }

    public void registrarTransferencia(String cedulaCliente, String origen, String destino, double monto) {
        GrafoDirigido<String> grafo = grafoPorCliente.computeIfAbsent(cedulaCliente, c -> new GrafoDirigido<>());
        grafo.agregarArista(origen, destino, String.format("%.2f", monto));
        guardarDatos();
    }

    public GrafoDirigido<String> obtenerGrafo(String cedulaCliente) {
        return grafoPorCliente.computeIfAbsent(cedulaCliente, c -> new GrafoDirigido<>());
    }

    public static void guardarDatos() {
        File dir = new File("src/main/resources/data");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            oos.writeObject(instancia);
            System.out.println("Relaciones de monederos guardadas correctamente.");
        } catch (IOException e) {
            System.err.println("Error al guardar relaciones de monederos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static GestorRelacionesMonederos cargarDatos() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            System.out.println("Archivo de relaciones no encontrado. Creando nuevo gestor.");
            return new GestorRelacionesMonederos();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO))) {
            GestorRelacionesMonederos gestor = (GestorRelacionesMonederos) ois.readObject();
            System.out.println("Relaciones de monederos cargadas correctamente.");
            return gestor;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar relaciones de monederos: " + e.getMessage());
            return new GestorRelacionesMonederos();
        }
    }

    // Metodo para depuración
    public void imprimirGrafos() {
        System.out.println("==== GRAFOS DE MONEDEROS ====");
        for (Map.Entry<String, GrafoDirigido<String>> entry : grafoPorCliente.entrySet()) {
            System.out.println("Cliente: " + entry.getKey());
            entry.getValue().imprimirGrafo();
            System.out.println("-------------------------");
        }
    }
    public void eliminarRelacionesCliente(String cedulaCliente) {
        grafoPorCliente.remove(cedulaCliente);
        guardarDatos();
    }

    public void eliminarRelacionesMonedero(String cedulaCliente, String nombreMonedero) {
        GrafoDirigido<String> grafo = grafoPorCliente.get(cedulaCliente);
        if (grafo != null) {
            grafo.eliminarVertice(nombreMonedero);
            guardarDatos();
        }
    }
}