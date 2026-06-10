package view;

import dao.VehiculoDAO;
import dto.Vehiculo;

import java.util.List;
import java.util.Scanner;

public class MenuVehiculos {

    private final Scanner sc;
    private final VehiculoDAO vehiculoDAO;

    public MenuVehiculos(Scanner sc, VehiculoDAO vehiculoDAO) {
        this.sc = sc;
        this.vehiculoDAO = vehiculoDAO;
    }

    public void mostrarMenu() {
        int op;
        do {
            System.out.println("\n── GESTIÓN DE VEHÍCULOS ──────────────");
            System.out.println("  1. Registrar vehículo");
            System.out.println("  2. Buscar por marca/modelo");
            System.out.println("  3. Listar por tipo");
            System.out.println("  4. Ver vehículos con stock disponible");
            System.out.println("  5. Volver");
            System.out.print("Opción: ");
            op = Utilidades.leerInt(sc);
            switch (op) {
                case 1 -> registrarVehiculo();
                case 2 -> buscarVehiculo();
                case 3 -> listarPorTipo();
                case 4 -> listarConStock();
                case 5 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (op != 5);
    }

    private void registrarVehiculo() {
        System.out.println("\n── REGISTRAR VEHÍCULO ────────────────");
        System.out.println("Tipos: 1=Sedán  2=SUV  3=Camioneta  4=Deportivo  5=Van");
        System.out.print("ID tipo    : "); int idTipo  = Utilidades.leerInt(sc);
        String marca = Utilidades.leerCampo(sc, "Marca      : ");
        String modelo = Utilidades.leerCampo(sc, "Modelo     : ");
        System.out.print("Año        : "); int anio    = Utilidades.leerInt(sc);
        String placa = Utilidades.leerCampo(sc, "Placa      : ", true);
        System.out.print("Color      : "); String color   = sc.nextLine().trim(); //unico posible null
        System.out.print("Precio/día : "); double precio  = Utilidades.leerDouble(sc);
        System.out.print("Stock      : "); int stock   = Utilidades.leerInt(sc);

        Vehiculo v = new Vehiculo(idTipo, marca, modelo, anio, placa, color, precio, stock);
        vehiculoDAO.registrar(v);
    }

    private void buscarVehiculo() {
        System.out.print("\nBuscar marca/modelo: ");
        String texto = sc.nextLine().trim();
        @SuppressWarnings("unchecked")
        List<Vehiculo> lista = (List<Vehiculo>) vehiculoDAO.buscarPorNombre(texto);
        if (lista.isEmpty()) {
            System.out.println("No se encontraron resultados.");
        } else {
            lista.forEach(Vehiculo::mostrarDetalle);
        }
    }

    private void listarPorTipo() {
        System.out.print("\nIngresa el tipo (ej: SUV, Sedán, Van): ");
        String tipo = sc.nextLine().trim();
        vehiculoDAO.listarPorTipo(tipo);
    }

    private void listarConStock() {
        List<Vehiculo> lista = vehiculoDAO.listarConStock();
        if (lista.isEmpty()) {
            System.out.println("No hay vehículos con stock disponible.");
        } else {
            lista.forEach(Vehiculo::mostrarDetalle);
        }
    }
}