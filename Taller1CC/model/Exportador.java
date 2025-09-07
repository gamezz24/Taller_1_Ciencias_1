package model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Exportador {

    public static void exportResultadosDetalladosCSV(List<ResultadoOrdenamiento> rows, String path) throws IOException {
        try (FileWriter fw = new FileWriter(path)) {
            fw.write("algoritmo,distribucion,atributo,n,repeticion,comparaciones,intercambios,tiempo_ns\n");
            for (ResultadoOrdenamiento r : rows) {
                fw.write(String.format("%s,%s,%s,%d,%d,%d,%d,%d\n",
                        r.algoritmo, r.distribucion, r.atributo, r.n, r.repeticion,
                        r.comparaciones, r.intercambios, r.tiempoNs));
            }
        }
    }

    public static void exportResultadosDetalladosJSON(List<ResultadoOrdenamiento> rows, String path) throws IOException {
        try (FileWriter fw = new FileWriter(path)) {
            fw.write("[\n");
            for (int i = 0; i < rows.size(); i++) {
                ResultadoOrdenamiento r = rows.get(i);
                String obj = String.format(
                        "  {\"algoritmo\":\"%s\",\"distribucion\":\"%s\",\"atributo\":\"%s\",\"n\":%d," +
                                "\"repeticion\":%d,\"comparaciones\":%d,\"intercambios\":%d,\"tiempo_ns\":%d}",
                        r.algoritmo, r.distribucion, r.atributo, r.n,
                        r.repeticion, r.comparaciones, r.intercambios, r.tiempoNs
                );
                fw.write(obj + (i < rows.size() - 1 ? ",\n" : "\n"));
            }
            fw.write("]\n");
        }
    }
}
