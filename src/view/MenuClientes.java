package view;

import dao.ClienteDAO;
import dto.Cliente;

import java.util.Scanner;

public class MenuClientes {

    private final Scanner sc;
    private final ClienteDAO clienteDAO;

    public MenuClientes(Scanner sc, ClienteDAO clienteDAO) {
        this.sc = sc;
        this.clienteDAO = clienteDAO;
    }

    public void mostrarMenu() {
        int op;
        do {
            System.out.println("\n── GESTIÓN DE CLIENTES ───────────────");
            System.out.println("  1. Registrar cliente");
            System.out.println("  2. Buscar cliente por cédula");
            System.out.println("  3. Listar todos los clientes");
            System.out.println("  4. Volver");
            System.out.print("Opción: ");
            op = Utilidades.leerInt(sc);
            switch (op) {
                case 1 -> registrarCliente();
                case 2 -> buscarClientePorCedula();
                case 3 -> clienteDAO.listarTodos();
                case 4 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (op != 4);
    }

    private void registrarCliente() {
        System.out.println("\n── REGISTRAR CLIENTE ─────────────────");
        String cedula    = Utilidades.leerCampo(sc, "Cédula     : ");
        String nombres   = Utilidades.leerCampo(sc, "Nombres    : ");
        String apellidos = Utilidades.leerCampo(sc, "Apellidos  : ");
        System.out.print("Teléfono   : "); String telefono  = sc.nextLine().trim();
        System.out.print("Email      : "); String email     = sc.nextLine().trim();
        System.out.print("Dirección  : "); String direccion = sc.nextLine().trim();
        String licencia  = Utilidades.leerCampo(sc, "Licencia   : ");

        Cliente c = new Cliente(cedula, nombres, apellidos, telefono, email, direccion, licencia);
        clienteDAO.registrar(c);
    }

    private void buscarClientePorCedula() {
        System.out.print("\nIngresa la cédula: ");
        String cedula = sc.nextLine().trim();
        Cliente c = clienteDAO.buscarPorCedula(cedula);
        if (c == null) {
            System.out.println("Cliente no encontrado.");
        } else {
            c.mostrarInfo();
        }
    }
}