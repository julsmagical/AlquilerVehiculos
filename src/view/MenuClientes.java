package view;

import java.util.Scanner;

public class MenuClientes {
    private Scanner sc;

    public MenuClientes(Scanner sc) {
        this.sc = sc;
    }

    public void mostrarMenu() {

        int op;

        do {
            System.out.println("\n── GESTIÓN DE CLIENTES ───────────────");
            System.out.println("1. Registrar cliente");
            System.out.println("2. Buscar cliente por cédula");
            System.out.println("3. Listar clientes");
            System.out.println("4. Volver");

            System.out.print("Opción: ");
            op = Integer.parseInt(sc.nextLine());

            switch (op) {
                case 1:
                    System.out.println("Registrar cliente");
                    break;
                case 2:
                    System.out.println("Buscar cliente");
                    break;
                case 3:
                    System.out.println("Listar clientes");
                    break;
            }
        } while (op != 4);
    }
}
