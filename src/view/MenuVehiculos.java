package view;

import java.util.Scanner;

public class MenuVehiculos {
    private Scanner sc;

    public MenuVehiculos(Scanner sc) {
        this.sc = sc;
    }

    public void mostrarMenu() {
        int op;

        do {
            System.out.println("\n── GESTIÓN DE VEHÍCULOS ──────────────");
            System.out.println("1. Registrar vehículo");
            System.out.println("2. Buscar por marca/modelo");
            System.out.println("3. Listar por tipo");
            System.out.println("4. Ver vehículos con stock disponible");
            System.out.println("5. Volver");

            System.out.print("Opción: ");
            op = Integer.parseInt(sc.nextLine());

            switch (op) {
                case 1:
                    System.out.println("Registrar vehículo");
                    break;
                case 2:
                    System.out.println("Buscar vehículo");
                    break;
                case 3:
                    System.out.println("Listar por tipo");
                    break;
                case 4:
                    System.out.println("Listar con stock");
                    break;
            }
        } while (op != 5);
    }
}
