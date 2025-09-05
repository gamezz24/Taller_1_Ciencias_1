package model;

import java.util.Random;

public class Distribuciones {
    private static final Random random = new Random(1234); // semilla fija

    // Aleatoria uniforme
    public static int[] generarAleatoria(int n, int m) {
        int[] datos = new int[n];
        for (int i = 0; i < n; i++) {
            datos[i] = random.nextInt(m) + 1;
        }
        return datos;
    }

    // Casi ordenada 90-10
    public static int[] generarCasiOrdenada(int n, int m) {
        int[] datos = new int[n];
        for (int i = 0; i < n; i++) {
            datos[i] = i + 1;
        }

        for (int i = 0; i < n / 10; i++) {
            int idx1 = random.nextInt(n);
            int idx2 = random.nextInt(n);
            int temp = datos[idx1];
            datos[idx1] = datos[idx2];
            datos[idx2] = temp;
        }
        return datos;
    }

    public static int[] generarInverso(int n, int m) {
        int[] datos = new int[n];
        for (int i = 0; i < n; i++) {
            datos[i] = n - i;
        }
        return datos;
    }
}
