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

    public static String leerCampo(Scanner sc, String etiqueta, boolean mayusculas) {
        String valor;
        do {
            System.out.print(etiqueta);
            valor = sc.nextLine().trim();
            if (mayusculas) {
                valor = valor.toUpperCase();
            }

            if (valor.isEmpty()) {
                System.out.println("Este campo es obligatorio.");
            }
        } while (valor.isEmpty());
        return valor;
    }

    //solo mando true cuando necesite hacer touppercase
    public static String leerCampo(Scanner sc, String etiqueta) {
        return leerCampo(sc, etiqueta, false);
    }
}