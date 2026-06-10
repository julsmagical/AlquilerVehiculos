import dao.AlquilerDAO;
import dao.ClienteDAO;
import dao.VehiculoDAO;
import view.MenuAlquiler;
import view.MenuClientes;
import view.MenuVehiculos;
import view.Utilidades;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ClienteDAO  clienteDAO  = new ClienteDAO();
        VehiculoDAO vehiculoDAO = new VehiculoDAO();
        AlquilerDAO alquilerDAO = new AlquilerDAO();

        MenuVehiculos menuVehiculos = new MenuVehiculos(sc, vehiculoDAO);
        MenuClientes  menuClientes  = new MenuClientes(sc, clienteDAO);
        MenuAlquiler  menuAlquiler  = new MenuAlquiler(sc, clienteDAO, vehiculoDAO, alquilerDAO);

        int opcion;
        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║   SISTEMA DE ALQUILER DE VEHÍCULOS   ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Gestión de Vehículos             ║");
            System.out.println("║  2. Gestión de Clientes              ║");
            System.out.println("║  3. Alquiler de Vehículos            ║");
            System.out.println("║  4. Salir                            ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Opción: ");
            opcion = Utilidades.leerInt(sc);
            switch (opcion) {
                case 1 -> menuVehiculos.mostrarMenu();
                case 2 -> menuClientes.mostrarMenu();
                case 3 -> menuAlquiler.mostrarMenu();
                case 4 -> System.out.println("¡Hasta luego!");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 4);
    }
}