package model;

public class ResultadoOrdenamiento {
    public String algoritmo;
    public String distribucion;
    public String atributo;
    public int n;
    public long comparaciones;
    public long intercambios;
    public long tiempoNs;
    public int repeticion;

    public ResultadoOrdenamiento(String algoritmo, String distribucion, String atributo, int n,
    long comparaciones, long intercambios, long tiempoNs, int repeticion) {
        this.algoritmo = algoritmo;
        this.distribucion = distribucion;
        this.atributo = atributo;
        this.n = n;
        this.comparaciones = comparaciones;
        this.intercambios = intercambios;
        this.tiempoNs = tiempoNs;
        this.repeticion = repeticion;
    }
}
