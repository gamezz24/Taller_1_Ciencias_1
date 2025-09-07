package controller;

import model.*;
import model.Modelos.Burbuja;
import model.Modelos.Insercion;
import model.Modelos.Merge;
import model.Modelos.Quick;
import model.Modelos.Seleccion;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = 20;
        int m = 50;

        Candidato[] candidatos = GeneradorDatos.generarPoblacion(n, m);

        mostrarTabla(candidatos, " Candidatos");

        System.out.println("\nseleccione el algoritmo de ordenamiento:");
        System.out.println("1.Burbuja");
        System.out.println("2.Inserción");
        System.out.println("3.Selección");
        System.out.println("4.Merge");
        System.out.println("5.Quick");
        int opcionAlg = sc.nextInt();

        System.out.println("\nseleccione el atributo para ordenar:");
        System.out.println("1.Distancia");
        System.out.println("2.Horas Perdidas");
        System.out.println("3.Prebendas");
        System.out.println("4.Sobornos");
        System.out.println("5.Corrupción");
        int opcionAttr = sc.nextInt();

        System.out.println("\nseleccione el orden:");
        System.out.println("1.Ascendente");
        System.out.println("2.Descendente");
        int opcionOrden = sc.nextInt();

        int[] datos = new int[candidatos.length];
        for (int i = 0; i < candidatos.length; i++) {
            datos[i] = switch (opcionAttr) {
                case 1 -> candidatos[i].getDistancia();
                case 2 -> candidatos[i].getHorasPerdidas();
                case 3 -> candidatos[i].getPrebendas();
                case 4 -> candidatos[i].getSobornos();
                case 5 -> candidatos[i].getcasosCorrupciones();
                default -> 0;
            };
        }

        AlgoritmoOrdenamiento algoritmo = switch (opcionAlg) {
            case 1 -> new Burbuja();
            case 2 -> new Insercion();
            case 3 -> new Seleccion();
            case 4 -> new Merge();
            case 5 -> new Quick();
            default -> new Burbuja();
        };

        algoritmo.ordenar(datos);

        if (opcionOrden == 2) {
            for (int i = 0; i < datos.length / 2; i++) {
                int tmp = datos[i];
                datos[i] = datos[datos.length - 1 - i];
                datos[datos.length - 1 - i] = tmp;
            }
        }

        Candidato[] ordenados = new Candidato[candidatos.length];
        for (int i = 0; i < datos.length; i++) {
            for (int j = 0; j < candidatos.length; j++) {
                int valor = switch (opcionAttr) {
                    case 1 -> candidatos[j].getDistancia();
                    case 2 -> candidatos[j].getHorasPerdidas();
                    case 3 -> candidatos[j].getPrebendas();
                    case 4 -> candidatos[j].getSobornos();
                    case 5 -> candidatos[j].getcasosCorrupciones();
                    default -> 0;
                };
                if (valor == datos[i] && !Arrays.asList(ordenados).contains(candidatos[j])) {
                    ordenados[i] = candidatos[j];
                    break;
                }
            }
        }

        System.out.println("\n RESULTADOS DEL ORDENAMIENTO ");
        System.out.println("Algoritmo: " + algoritmo.getClass().getSimpleName());
        System.out.println("Comparaciones: " + algoritmo.getComparaciones());
        System.out.println("Intercambios: " + algoritmo.getIntercambios());
        System.out.println("Tiempo (ns): " + algoritmo.getTiempoEjecucion());

        mostrarTabla(ordenados, "\n CANDIDATOS ORDENADOS");
        sc.close();
    }

    private static void mostrarTabla(Candidato[] candidatos, String titulo) {
        System.out.println("\n" + titulo);
        System.out.printf("%-20s %-10s %-15s %-12s %-10s %-12s %-40s\n",
                "Nombre", "Distancia", "Horas Perdidas", "Prebendas", "Sobornos", "Corrupción", "Marchas");
        System.out.println("--------------------------------------------------------------------------------------------------------------------");

        for (Candidato c : candidatos) {
            String distanciaTexto = c.getDistancia() + " m";
            StringBuilder marchasTexto = new StringBuilder();
            int totalMarchas = c.getMarchas().size();
            for (int i = 0; i < totalMarchas; i++) {
                marchasTexto.append(c.getMarchas().get(i).getNombre());
                if (i < totalMarchas - 1) marchasTexto.append(", ");
            }
            marchasTexto.append(" (").append(totalMarchas).append(")");

            System.out.printf("%-20s %-10s %-15d %-12d %-10d %-12d %-40s\n",
                    c.getNombre(), distanciaTexto, c.getHorasPerdidas(),
                    c.getPrebendas(), c.getSobornos(), c.getcasosCorrupciones(),
                    marchasTexto.toString());
        }
    }
}
