package model.Modelos;

import model.AlgoritmoOrdenamiento;

public class Quick implements AlgoritmoOrdenamiento {
    private long comparaciones;
    private long intercambios;
    private long tiempo;

    @Override
    public void ordenar(int[] datos) {
        comparaciones = intercambios = 0;
        long inicio = System.nanoTime();
        quickSort(datos, 0, datos.length - 1);
        tiempo = System.nanoTime() - inicio;
    }

    private void quickSort(int[] arr, int bajo, int alto) {
        if (bajo < alto) {
            int pi = particion(arr, bajo, alto);
            quickSort(arr, bajo, pi - 1);
            quickSort(arr, pi + 1, alto);
        }
    }

    private int particion(int[] arr, int bajo, int alto) {
        int pivote = arr[alto];
        int i = (bajo - 1);

        for (int j = bajo; j < alto; j++) {
            comparaciones++;
            if (arr[j] <= pivote) {
                i++;
                intercambios++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        intercambios++;
        int temp = arr[i + 1];
        arr[i + 1] = arr[alto];
        arr[alto] = temp;

        return i + 1;
    }

    @Override public long getComparaciones() { return comparaciones; }
    @Override public long getIntercambios() { return intercambios; }
    @Override public long getTiempoEjecucion() { return tiempo; }
}
