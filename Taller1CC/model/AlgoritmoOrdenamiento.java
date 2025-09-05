package model;

public interface AlgoritmoOrdenamiento {
    void ordenar(int[] datos);
    long getComparaciones();
    long getIntercambios();
    long getTiempoEjecucion();
}
