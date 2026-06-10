package view;

import java.util.Scanner;

public class Utilidades {

    public static int leerInt(Scanner sc) {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un número válido: ");
            }
        }
    }

    public static double leerDouble(Scanner sc) {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un valor decimal válido: ");
            }
        }
    }

    public static String leerCampo(Scanner sc, String etiqueta) {
        String valor;
        do {
            System.out.print(etiqueta);
            valor = sc.nextLine().trim();
            if (valor.isEmpty()) System.out.println("Este campo es obligatorio.");
        } while (valor.isEmpty());
        return valor;
    }
}