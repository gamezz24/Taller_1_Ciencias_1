package model;

public class Burbuja implements AlgoritmoOrdenamiento {
    private long comparaciones;
    private long intercambios;
    private long tiempo;

    @Override
    public void ordenar(int[] datos) {
        comparaciones = intercambios = 0;
        long inicio = System.nanoTime();

        int n = datos.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                comparaciones++;
                if (datos[j] > datos[j + 1]) {
                    int temp = datos[j];
                    datos[j] = datos[j + 1];
                    datos[j + 1] = temp;
                    intercambios++;
                }
            }
        }

        tiempo = System.nanoTime() - inicio;
    }

    @Override public long getComparaciones() { return comparaciones; }
    @Override public long getIntercambios() { return intercambios; }
    @Override public long getTiempoEjecucion() { return tiempo; }
}
