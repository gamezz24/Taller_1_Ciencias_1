package model;

public class Marcha {
    private String nombre;

    public Marcha(String nombre) {
        this.nombre = nombre;}

    public String getNombre() { return nombre; }

    @Override
    public String toString() {
        return nombre;
    }
}
