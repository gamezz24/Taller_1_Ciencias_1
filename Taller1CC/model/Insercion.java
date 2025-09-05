package model;

public class Insercion implements AlgoritmoOrdenamiento {
    private long comparaciones;
    private long intercambios;
    private long tiempo;

    @Override
    public void ordenar(int[] datos) {
        comparaciones = intercambios = 0;
        long inicio = System.nanoTime();

        int n = datos.length;
        for (int i = 1; i < n; i++) {
            int clave = datos[i];
            int j = i - 1;

            while (j >= 0) {
                comparaciones++;
                if (datos[j] > clave) {
                    datos[j + 1] = datos[j];
                    j--;
                    intercambios++;
                } else {
                    break;
                }
            }
            datos[j + 1] = clave;
        }

        tiempo = System.nanoTime() - inicio;
    }

    @Override public long getComparaciones() { return comparaciones; }
    @Override public long getIntercambios() { return intercambios; }
    @Override public long getTiempoEjecucion() { return tiempo; }
}
