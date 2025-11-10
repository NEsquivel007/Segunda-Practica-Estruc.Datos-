
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Árbol B+ (solo claves) ===");
        int orden = leerEntero(sc, "Ingrese el orden m del árbol (m >= 3): ", 3, Integer.MAX_VALUE);
        ArbolBMas arbol = new ArbolBMas(orden);

        int opcion;
        do {
            System.out.println("\n--- Menú ---");
            System.out.println("1) Insertar clave");
            System.out.println("2) Buscar clave");
            System.out.println("3) Eliminar clave");
            System.out.println("4) Recorrer (imprimir n claves desde una clave inicial)");
            System.out.println("5) Salir");

            opcion = leerEntero(sc, "Seleccione una opción: ", 1, 5);

            switch (opcion) {
                case 1 -> {
                    int clave = leerEntero(sc, "Ingrese la clave a insertar: ");
                    arbol.insertar(clave);
                    System.out.println(" Clave insertada (o ya existía).");
                }
                case 2 -> {
                    int clave = leerEntero(sc, "Ingrese la clave a buscar: ");
                    boolean existe = arbol.buscar(clave);
                    if (existe)
                        System.out.println(" La clave Si existe en el árbol.");
                    else
                        System.out.println(" La clave NO se encuentra en el árbol.");
                }
                case 3 -> {
                    int clave = leerEntero(sc, "Ingrese la clave a eliminar: ");
                    boolean eliminado = arbol.eliminar(clave);
                    if (eliminado)
                        System.out.println(" Clave eliminada correctamente.");
                    else
                        System.out.println(" La clave no fue encontrada para eliminar.");
                }
                case 4 -> {
                    int claveInicial = leerEntero(sc, "Ingrese la clave inicial para recorrer: ");
                    int n = leerEntero(sc, "¿Cuántas claves desea imprimir? ", 1, Integer.MAX_VALUE);
                    System.out.println(" Recorrido desde la clave " + claveInicial + ":");
                    arbol.recorrer(claveInicial, n);
                }
                case 5 -> System.out.println("Saliendo, muchas gracias:)");
            }
        } while (opcion != 5);

        sc.close();
    }

    // ===== Helpers para lectura segura de enteros =====
    private static int leerEntero(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.println("Entrada inválida. Intente de nuevo.");
            }
        }
    }

    private static int leerEntero(Scanner sc, String prompt, int min, int max) {
        while (true) {
            int valor = leerEntero(sc, prompt);
            if (valor < min || valor > max) {
                System.out.printf("El valor debe estar entre %d y %d.\n", min, max);
                continue;
            }
            return valor;
        }
    }
}

