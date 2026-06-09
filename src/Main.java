import dao.AlquilerDAO;
import dao.ClienteDAO;
import dao.VehiculoDAO;
import dto.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import view.Utilidades;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static ClienteDAO  clienteDAO  = new ClienteDAO();
    static VehiculoDAO vehiculoDAO = new VehiculoDAO();
    static AlquilerDAO alquilerDAO = new AlquilerDAO();

    public static void main(String[] args) {
        int opcion;
        do {
            menuPrincipal();
            opcion = Utilidades.leerInt(sc);
            switch (opcion) {
                case 1 -> menuVehiculos();
                case 2 -> menuClientes();
                case 3 -> menuAlquiler();
                case 4 -> System.out.println("¡Hasta luego!");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 4);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  MENÚ PRINCIPAL
    // ══════════════════════════════════════════════════════════════════════
    static void menuPrincipal() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE ALQUILER DE VEHÍCULOS   ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  1. Gestión de Vehículos             ║");
        System.out.println("║  2. Gestión de Clientes              ║");
        System.out.println("║  3. Alquiler de Vehículos            ║");
        System.out.println("║  4. Salir                            ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("Opción: ");
    }

    //VIEW DE VEHICULOS
    static void menuVehiculos() {
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

    static void registrarVehiculo() {
        System.out.println("\n── REGISTRAR VEHÍCULO ────────────────");
        System.out.println("Tipos: 1=Sedán  2=SUV  3=Camioneta  4=Deportivo  5=Van");
        System.out.print("ID tipo    : "); int idTipo  = Utilidades.leerInt(sc);
        System.out.print("Marca      : "); String marca   = sc.nextLine().trim();
        System.out.print("Modelo     : "); String modelo  = sc.nextLine().trim();
        System.out.print("Año        : "); int anio    = Utilidades.leerInt(sc);
        System.out.print("Placa      : "); String placa   = sc.nextLine().trim().toUpperCase();
        System.out.print("Color      : "); String color   = sc.nextLine().trim();
        System.out.print("Precio/día : "); double precio  = Utilidades.leerDouble(sc);
        System.out.print("Stock      : "); int stock   = Utilidades.leerInt(sc);

        Vehiculo v = new Vehiculo(idTipo, marca, modelo, anio, placa, color, precio, stock);
        vehiculoDAO.registrar(v);
    }

    static void buscarVehiculo() {
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

    static void listarPorTipo() {
        System.out.print("\nIngresa el tipo (ej: SUV, Sedán, Van): ");
        String tipo = sc.nextLine().trim();
        vehiculoDAO.listarPorTipo(tipo);
    }

    static void listarConStock() {
        List<Vehiculo> lista = vehiculoDAO.listarConStock();
        if (lista.isEmpty()) {
            System.out.println("No hay vehículos con stock disponible.");
        } else {
            lista.forEach(Vehiculo::mostrarDetalle);
        }
    }

    //VIEW DE CLIENTES
    static void menuClientes() {
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

    static void registrarCliente() {
        System.out.println("\n── REGISTRAR CLIENTE ─────────────────");
        System.out.print("Cédula     : "); String cedula    = sc.nextLine().trim();
        System.out.print("Nombres    : "); String nombres   = sc.nextLine().trim();
        System.out.print("Apellidos  : "); String apellidos = sc.nextLine().trim();
        System.out.print("Teléfono   : "); String telefono  = sc.nextLine().trim();
        System.out.print("Email      : "); String email     = sc.nextLine().trim();
        System.out.print("Dirección  : "); String direccion = sc.nextLine().trim();
        System.out.print("Licencia   : "); String licencia  = sc.nextLine().trim();

        if (licencia.isEmpty()) {
            System.out.println("La licencia es obligatoria.");
            return;
        }

        Cliente c = new Cliente(cedula, nombres, apellidos, telefono, email, direccion, licencia);
        clienteDAO.registrar(c);
    }

    static void buscarClientePorCedula() {
        System.out.print("\nIngresa la cédula: ");
        String cedula = sc.nextLine().trim();
        Cliente c = clienteDAO.buscarPorCedula(cedula);
        if (c == null) {
            System.out.println("Cliente no encontrado.");
        } else {
            c.mostrarInfo();
        }
    }

    //VIEW DE ALQUILER
    static void menuAlquiler() {
        int op;
        do {
            System.out.println("\n── ALQUILER DE VEHÍCULOS ─────────────");
            System.out.println("  1. Nuevo alquiler");
            System.out.println("  2. Consultar alquileres por cliente");
            System.out.println("  3. Volver");
            System.out.print("Opción: ");
            op = Utilidades.leerInt(sc);
            switch (op) {
                case 1 -> nuevoAlquiler();
                case 2 -> consultarAlquilerPorCliente();
                case 3 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (op != 3);
    }

    static void nuevoAlquiler() {
        System.out.println("\n── NUEVO ALQUILER ────────────────────");

        System.out.print("Cédula del cliente: ");
        String cedula = sc.nextLine().trim();
        Cliente cliente = clienteDAO.buscarPorCedula(cedula);

        if (cliente == null) {
            System.out.println("Cliente no encontrado. Regístralo primero.");
            return;
        }

        if (cliente.getLicencia() == null || cliente.getLicencia().isEmpty()) {
            System.out.println("El cliente no tiene licencia registrada. No puede alquilar.");
            return;
        }

        System.out.println("Cliente: " + cliente);

        LocalDate fechaDev = null;
        while (fechaDev == null) {
            System.out.print("Fecha de devolución (YYYY-MM-DD): ");
            String fecha = sc.nextLine().trim();
            try {
                fechaDev = LocalDate.parse(fecha);
                if (!fechaDev.isAfter(LocalDate.now())) {
                    System.out.println("La fecha debe ser posterior a hoy.");
                    fechaDev = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Formato inválido. Usa YYYY-MM-DD.");
            }
        }

        Alquiler alquiler = new Alquiler(cliente, fechaDev);

        boolean agregando = true;
        while (agregando) {
            // Mostrar disponibles
            System.out.println("\nVehículos con stock disponible:");
            List<Vehiculo> disponibles = vehiculoDAO.listarConStock();
            if (disponibles.isEmpty()) {
                System.out.println("Sin stock disponible.");
                return;
            }
            disponibles.forEach(v -> System.out.println("  " + v));

            System.out.print("\nID del vehículo a agregar (0 para terminar): ");
            int idVeh = Utilidades.leerInt(sc);
            if (idVeh == 0) break;

            Vehiculo veh = (Vehiculo) vehiculoDAO.buscarPorId(idVeh);
            if (veh == null) {
                System.out.println("Vehículo no encontrado.");
                continue;
            }

            if (!veh.validarStock(1)) {
                System.out.println("Sin stock disponible para ese vehículo.");
                continue;
            }

            System.out.print("Cantidad de días: ");
            int dias = Utilidades.leerInt(sc);
            if (dias <= 0) {
                System.out.println("Los días tienen que ser mayores a 0.");
                continue;
            }

            alquiler.agregarDetalle(veh, dias);
            System.out.printf("Agregado: %s %s | %d días | Subtotal: $%.2f%n",
                    veh.getMarca(), veh.getModelo(), dias, veh.calcularCostoAlquiler(dias));
        }

        if (alquiler.getDetalles().isEmpty()) {
            System.out.println("No se agregó ningún vehículo. Alquiler cancelado.");
            return;
        }

        //resumen
        alquiler.mostrarResumen();
        System.out.print("¿Confirmar alquiler? (s/n): ");
        String conf = sc.nextLine().trim().toLowerCase();
        if (conf.equals("s")) {
            alquilerDAO.registrar(alquiler);
        } else {
            System.out.println("Alquiler cancelado.");
        }
    }

    static void consultarAlquilerPorCliente() {
        System.out.print("\nCédula del cliente: ");
        String cedula = sc.nextLine().trim();
        Cliente c = clienteDAO.buscarPorCedula(cedula);
        if (c == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        System.out.println("Alquileres de: " + c.getNombres() + " " + c.getApellidos());
        alquilerDAO.listarPorCliente(c.getIdCliente());
    }
}