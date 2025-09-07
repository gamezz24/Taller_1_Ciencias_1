package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneradorDatos {
    private static final String[] NOMBRES = {
        "Giovanny", "Luis", "Maria", "Carlos", "Sofia", "Pedro", "Giovany", "Andres", "Laura", "Felipe"};
    private static final String[] APELLIDOS = {
        "Tarazona", "Rodriguez", "Martinez", "Tarazzona", "Lopez", "Sanchez"};
    private static final String[] MARCHAS = {
        "Pensional", "Anticorrupcion", "Presupuestal",};

    private static Random random = new Random();

    public static String generarNombreAleatorio() {
        String nombre = NOMBRES[random.nextInt(NOMBRES.length)];
        String apellido = APELLIDOS[random.nextInt(APELLIDOS.length)];
        return nombre + " " + apellido;
    }

    public static List<Marcha> generarMarchas() {
        int cantidad = random.nextInt(3) + 1;
        List<Marcha> marchas = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            String nombreMarcha = MARCHAS[random.nextInt(MARCHAS.length)];
            marchas.add(new Marcha(nombreMarcha));
        }
        return marchas;
    }

    public static Candidato generarCandidato(int m) {
        String nombre = generarNombreAleatorio();
        int distancia = random.nextInt(m) + 1;
        int horas = random.nextInt(m) + 1;
        int prebendas = random.nextInt(m) + 1;
        int sobornos = random.nextInt(m) + 1;
        int casosCorrupcion = random.nextInt(m) + 1;
        List<Marcha> marchas = generarMarchas();

        return new Candidato(nombre, marchas, distancia, horas, prebendas, sobornos, casosCorrupcion);
    }

    public static Candidato[] generarPoblacion(int n, int m) {
        Candidato[] candidatos = new Candidato[n];
        for (int i = 0; i < n; i++) {
            candidatos[i] = generarCandidato(m);
        }
        return candidatos;
    }
}