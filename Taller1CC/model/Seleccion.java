package model;

public class Seleccion implements AlgoritmoOrdenamiento {
    private long comparaciones;
    private long intercambios;
    private long tiempo;

    @Override
    public void ordenar(int[] datos) {
        comparaciones = intercambios = 0;
        long inicio = System.nanoTime();

        int n = datos.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                comparaciones++;
                if (datos[j] < datos[minIdx]) {
                    minIdx = j;
                }
            }
            if (minIdx != i) {
                int temp = datos[i];
                datos[i] = datos[minIdx];
                datos[minIdx] = temp;
                intercambios++;
            }
        }

        tiempo = System.nanoTime() - inicio;
    }

    @Override public long getComparaciones() { return comparaciones; }
    @Override public long getIntercambios() { return intercambios; }
    @Override public long getTiempoEjecucion() { return tiempo; }
}
