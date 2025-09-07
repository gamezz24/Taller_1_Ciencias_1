package model;

import java.util.List;

public class Candidato {
    private String nombre;
    private List<Marcha> marchas;
    private int distancia;
    private int horasPerdidas;
    private int prebendas;
    private int sobornos;
    private int casosCorrupcion;

    public Candidato(String nombre,List<Marcha> marchas, int distancia, int horasPerdidas, int prebendas, int sobornos, int casosCorrupcion){
        this.nombre = nombre;
        this.distancia = distancia;
        this.horasPerdidas = horasPerdidas;
        this.prebendas = prebendas;
        this.sobornos = sobornos;
        this.casosCorrupcion = casosCorrupcion;
        this.marchas = marchas;
    }

    public String getNombre() { return nombre; }
    public List<Marcha> getMarchas() { return marchas; }
    public int getDistancia() { return distancia; }
    public int getHorasPerdidas() { return horasPerdidas; }
    public int getPrebendas() { return prebendas; }
    public int getSobornos() { return sobornos; }
    public int getcasosCorrupciones() { return casosCorrupcion; }

    @Override
    public String toString() {
        return "Candidato{" +
                "nombre='" + nombre + '\'' +
                ", marchas=" + marchas +
                ", distancia=" + distancia +
                ", horasPerdidas=" + horasPerdidas +
                ", prebendas=" + prebendas +
                ", sobornos=" + sobornos +
                ", casosCorrupcion=" + casosCorrupcion +
                '}';
    }
}