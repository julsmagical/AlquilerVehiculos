package view;

import dao.AlquilerDAO;
import dao.ClienteDAO;
import dao.VehiculoDAO;
import dto.Alquiler;
import dto.Cliente;
import dto.Vehiculo;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class MenuAlquiler {

    private final Scanner sc;
    private final ClienteDAO clienteDAO;
    private final VehiculoDAO vehiculoDAO;
    private final AlquilerDAO alquilerDAO;

    public MenuAlquiler(Scanner sc, ClienteDAO clienteDAO, VehiculoDAO vehiculoDAO, AlquilerDAO alquilerDAO) {
        this.sc = sc;
        this.clienteDAO = clienteDAO;
        this.vehiculoDAO = vehiculoDAO;
        this.alquilerDAO = alquilerDAO;
    }

    public void mostrarMenu() {
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

    private void nuevoAlquiler() {
        System.out.println("\n── NUEVO ALQUILER ────────────────────");

        String cedula = Utilidades.leerCampo(sc, "Cédula del cliente: ");
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

        while (true) {
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

        alquiler.mostrarResumen();
        System.out.print("¿Confirmar alquiler? (s/n): ");
        String conf = sc.nextLine().trim().toLowerCase();
        if (conf.equals("s")) {
            alquilerDAO.registrar(alquiler);
        } else {
            System.out.println("Alquiler cancelado.");
        }
    }

    private void consultarAlquilerPorCliente() {
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