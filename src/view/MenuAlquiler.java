package view;

import java.util.Scanner;

public class MenuAlquiler {

    private Scanner sc;

    public MenuAlquiler(Scanner sc) {
        this.sc = sc;
    }

    public void mostrarMenu() {

        int op;

        do {
            System.out.println("\n── ALQUILER DE VEHÍCULOS ─────────────");
            System.out.println("1. Nuevo alquiler");
            System.out.println("2. Consultar alquileres");
            System.out.println("3. Volver");

            System.out.print("Opción: ");
            op = Integer.parseInt(sc.nextLine());

            switch (op) {
                case 1:
                    System.out.println("Nuevo alquiler");
                    break;
                case 2:
                    System.out.println("Consultar alquiler");
                    break;
            }

        } while (op != 3);
    }
}