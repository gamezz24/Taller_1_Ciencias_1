package controller;

import model.*;
import model.Modelos.Burbuja;
import model.Modelos.Insercion;
import model.Modelos.Merge;
import model.Modelos.Quick;
import model.Modelos.Seleccion;

import java.io.*;
import java.util.*;

public class ExperimentoRigoroso {
    private static final long SEMILLA_MAESTRA = 42L;
    private static final int REPETICIONES_POR_CONFIGURACION = 30;
    private static final int[] TAMAÑOS = {50, 100, 200, 500, 1000, 2000, 5000};
    private static final String[] ALGORITMOS = {"Burbuja", "Inserción", "Selección", "Merge", "Quick"};
    private static final String[] DISTRIBUCIONES = {"Aleatoria", "Casi ordenada", "Inverso"};
    private static final String[] ATRIBUTOS = {"Distancia", "Horas Perdidas", "Prebendas", "Sobornos", "Corrupción"};
    
    private final List<ResultadoOrdenamiento> resultadosDetallados = new ArrayList<>();
    private final Map<String, List<ResultadoOrdenamiento>> resultadosAgrupados = new HashMap<>();
    private final ParametrosExperimento parametros;
    
    public static class ParametrosExperimento {
        public final long semillaMaestra;
        public final int repeticionesPorConfig;
        public final int[] tamaños;
        public final String[] algoritmos;
        public final String[] distribuciones;
        public final String[] atributos;
        public final long timestampInicio;
        public final String versionJVM;
        public final String sistemaOperativo;
        
        public ParametrosExperimento() {
            this.semillaMaestra = SEMILLA_MAESTRA;
            this.repeticionesPorConfig = REPETICIONES_POR_CONFIGURACION;
            this.tamaños = TAMAÑOS.clone();
            this.algoritmos = ALGORITMOS.clone();
            this.distribuciones = DISTRIBUCIONES.clone();
            this.atributos = ATRIBUTOS.clone();
            this.timestampInicio = System.currentTimeMillis();
            this.versionJVM = System.getProperty("java.version");
            this.sistemaOperativo = System.getProperty("os.name") + " " + System.getProperty("os.version");
        }
    }
    
    public ExperimentoRigoroso() {
        this.parametros = new ParametrosExperimento();
        System.out.println("=== EXPERIMENTO RIGUROSO INICIALIZADO ===");
        System.out.println("Semilla maestra: " + parametros.semillaMaestra);
        System.out.println("Repeticiones por configuración: " + parametros.repeticionesPorConfig);
        System.out.println("JVM: " + parametros.versionJVM);
        System.out.println("SO: " + parametros.sistemaOperativo);}

    public void ejecutarExperimentoCompleto() {
        System.out.println("Iniciando experimento completo...");
        int totalConfiguraciones = ALGORITMOS.length * DISTRIBUCIONES.length * ATRIBUTOS.length * TAMAÑOS.length;
        int configActual = 0;
 
        Random generadorSemillas = new Random(parametros.semillaMaestra);
        
        for (String algoritmo : ALGORITMOS) {
            for (String distribucion : DISTRIBUCIONES) {
                for (String atributo : ATRIBUTOS) {
                    for (int n : TAMAÑOS) {
                        configActual++;
                        System.out.printf("Configuración %d/%d: %s - %s - %s - N=%d\n", 
                            configActual, totalConfiguraciones, algoritmo, distribucion, atributo, n);
                        
                        ejecutarConfiguracion(algoritmo, distribucion, atributo, n, generadorSemillas);
                    }
                }
            }
        }
        
        agruparResultados();
        System.out.println("Experimento completado. Total de ejecuciones: " + resultadosDetallados.size());
    }
    
    private void ejecutarConfiguracion(String algoritmo, String distribucion, String atributo, int n, Random generadorSemillas) {
        List<ResultadoOrdenamiento> resultadosConfig = new ArrayList<>();
        
        for (int rep = 1; rep <= parametros.repeticionesPorConfig; rep++) {
            long semillaEjecucion = generadorSemillas.nextLong();
            ResultadoOrdenamiento resultado = ejecutarEjecucionUnica(
                algoritmo, distribucion, atributo, n, rep, semillaEjecucion);
            
            resultadosConfig.add(resultado);
            resultadosDetallados.add(resultado);
        }

        String clave = String.format("%s|%s|%s|%d", algoritmo, distribucion, atributo, n);
        resultadosAgrupados.put(clave, resultadosConfig);
    }

    private ResultadoOrdenamiento ejecutarEjecucionUnica(String algoritmo, String distribucion, 
            String atributo, int n, int repeticion, long semillaEjecucion) {
        
        // Configurar semilla para esta ejecución específica
        Random.class.getDeclaredFields()[0].setAccessible(true);
        try {
            Random randomDistribuciones = new Random(semillaEjecucion);

            java.lang.reflect.Field field = Distribuciones.class.getDeclaredField("random");
            field.setAccessible(true);
            Random originalRandom = (Random) field.get(null);
            field.set(null, randomDistribuciones);
            
            int[] datos = generarDatosSegunDistribucion(distribucion, n, 1000);

            field.set(null, originalRandom);
            
            AlgoritmoOrdenamiento alg = crearAlgoritmo(algoritmo);
            
            if (repeticion == 1) {
                calentarJVM(alg, datos.clone());
            }

            int[] datosCopia = datos.clone();
            alg.ordenar(datosCopia);
            
            return new ResultadoOrdenamiento(algoritmo, distribucion, atributo, n,
                alg.getComparaciones(), alg.getIntercambios(), alg.getTiempoEjecucion(), repeticion);
                
        } catch (Exception e) {
            throw new RuntimeException("Error en ejecución única", e);
        }
    }
    
    private int[] generarDatosSegunDistribucion(String distribucion, int n, int m) {
        return switch (distribucion) {
            case "Aleatoria" -> Distribuciones.generarAleatoria(n, m);
            case "Casi ordenada" -> Distribuciones.generarCasiOrdenada(n, m);
            case "Inverso" -> Distribuciones.generarInverso(n, m);
            default -> throw new IllegalArgumentException("Distribución no reconocida: " + distribucion);
        };
    }
    
    private AlgoritmoOrdenamiento crearAlgoritmo(String algoritmo) {
        return switch (algoritmo) {
            case "Burbuja" -> new Burbuja();
            case "Inserción" -> new Insercion();
            case "Selección" -> new Seleccion();
            case "Merge" -> new Merge();
            case "Quick" -> new Quick();
            default -> throw new IllegalArgumentException("Algoritmo no reconocido: " + algoritmo);
        };
    }
    
    private void calentarJVM(AlgoritmoOrdenamiento algoritmo, int[] datos) {
        for (int i = 0; i < 10; i++) {
            algoritmo.ordenar(datos.clone());
        }
    }

    private void agruparResultados() {
        System.out.println("Agrupando resultados para análisis estadístico...");}

    public void generarAnalisisEstadistico(String archivoSalida) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivoSalida))) {
            writer.println("=== ANÁLISIS ESTADÍSTICO RIGUROSO ===");
            writer.println("Timestamp: " + new Date());
            writer.println("Semilla maestra: " + parametros.semillaMaestra);
            writer.println("Repeticiones por configuración: " + parametros.repeticionesPorConfig);
            writer.println();

            writer.println("=== ANÁLISIS POR ALGORITMO Y TAMAÑO ===");
            for (String algoritmo : ALGORITMOS) {
                writer.println("\nAlgoritmo: " + algoritmo);
                writer.println("Tamaño\tMean_Comp\tStdDev_Comp\tMean_Intercambios\tStdDev_Intercambios\tMean_Tiempo\tStdDev_Tiempo\tIQR_Tiempo");
                
                for (int n : TAMAÑOS) {
                    List<ResultadoOrdenamiento> resultados = obtenerResultadosPorAlgoritmoYTamaño(algoritmo, n);
                    if (!resultados.isEmpty()) {
                        EstadisticasDescriptivas stats = calcularEstadisticas(resultados);
                        writer.printf("%d\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%d\n",
                            n, stats.meanComparaciones, stats.stddevComparaciones,
                            stats.meanIntercambios, stats.stddevIntercambios,
                            stats.meanTiempo, stats.stddevTiempo, stats.iqrTiempo);
                    }
                }
            }
            
            // Análisis de cruces de desempeño
            writer.println("\n=== ANÁLISIS DE CRUCES DE DESEMPEÑO ===");
            analizarCrucesDesempeño(writer);
        }
    }

    private void analizarCrucesDesempeño(PrintWriter writer) {
        writer.println("Identificación de puntos de cruce entre algoritmos:");
        
        for (int i = 0; i < TAMAÑOS.length - 1; i++) {
            int n1 = TAMAÑOS[i];
            int n2 = TAMAÑOS[i + 1];
            
            writer.printf("\nEntre N=%d y N=%d:\n", n1, n2);

            for (int j = 0; j < ALGORITMOS.length; j++) {
                for (int k = j + 1; k < ALGORITMOS.length; k++) {
                    String alg1 = ALGORITMOS[j];
                    String alg2 = ALGORITMOS[k];
                    
                    EstadisticasDescriptivas stats1_n1 = calcularEstadisticas(obtenerResultadosPorAlgoritmoYTamaño(alg1, n1));
                    EstadisticasDescriptivas stats2_n1 = calcularEstadisticas(obtenerResultadosPorAlgoritmoYTamaño(alg2, n1));
                    EstadisticasDescriptivas stats1_n2 = calcularEstadisticas(obtenerResultadosPorAlgoritmoYTamaño(alg1, n2));
                    EstadisticasDescriptivas stats2_n2 = calcularEstadisticas(obtenerResultadosPorAlgoritmoYTamaño(alg2, n2));
                    
                    boolean cruzaTiempo = (stats1_n1.meanTiempo < stats2_n1.meanTiempo) != (stats1_n2.meanTiempo < stats2_n2.meanTiempo);
                    boolean cruzaComparaciones = (stats1_n1.meanComparaciones < stats2_n1.meanComparaciones) != (stats1_n2.meanComparaciones < stats2_n2.meanComparaciones);
                    
                    if (cruzaTiempo || cruzaComparaciones) {
                        writer.printf("  CRUCE DETECTADO: %s vs %s", alg1, alg2);
                        if (cruzaTiempo) writer.print(" [TIEMPO]");
                        if (cruzaComparaciones) writer.print(" [COMPARACIONES]");
                        writer.println();
                    }
                }
            }
        }
    }

    private List<ResultadoOrdenamiento> obtenerResultadosPorAlgoritmoYTamaño(String algoritmo, int n) {
        return resultadosDetallados.stream()
            .filter(r -> r.algoritmo.equals(algoritmo) && r.n == n)
            .toList();
    }

    private EstadisticasDescriptivas calcularEstadisticas(List<ResultadoOrdenamiento> resultados) {
        if (resultados.isEmpty()) return new EstadisticasDescriptivas();
        
        double[] comparaciones = resultados.stream().mapToDouble(r -> r.comparaciones).toArray();
        double[] intercambios = resultados.stream().mapToDouble(r -> r.intercambios).toArray();
        double[] tiempos = resultados.stream().mapToDouble(r -> r.tiempoNs).toArray();
        
        EstadisticasDescriptivas stats = new EstadisticasDescriptivas();
        stats.meanComparaciones = calcularMedia(comparaciones);
        stats.stddevComparaciones = calcularDesviacionEstandar(comparaciones, stats.meanComparaciones);
        stats.meanIntercambios = calcularMedia(intercambios);
        stats.stddevIntercambios = calcularDesviacionEstandar(intercambios, stats.meanIntercambios);
        stats.meanTiempo = calcularMedia(tiempos);
        stats.stddevTiempo = calcularDesviacionEstandar(tiempos, stats.meanTiempo);
        
        Arrays.sort(tiempos);
        stats.iqrTiempo = (long)(calcularPercentil(tiempos, 75) - calcularPercentil(tiempos, 25));
        
        return stats;
    }
    
    private double calcularMedia(double[] datos) {
        return Arrays.stream(datos).average().orElse(0.0);
    }
    
    private double calcularDesviacionEstandar(double[] datos, double media) {
        double varianza = Arrays.stream(datos).map(x -> Math.pow(x - media, 2)).average().orElse(0.0);
        return Math.sqrt(varianza);
    }
    
    private double calcularPercentil(double[] datosSorted, double percentil) {
        double pos = (percentil / 100.0) * (datosSorted.length + 1);
        if (pos <= 1) return datosSorted[0];
        if (pos >= datosSorted.length) return datosSorted[datosSorted.length - 1];
        int idx = (int) pos;
        double frac = pos - idx;
        return datosSorted[idx - 1] + frac * (datosSorted[idx] - datosSorted[idx - 1]);
    }
    
    public void exportarDatosVisualizacion(String archivo) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo))) {
            writer.println("algoritmo,distribucion,tamaño,mean_comparaciones,mean_intercambios,mean_tiempo_ns,stddev_tiempo,iqr_tiempo");
            
            for (String algoritmo : ALGORITMOS) {
                for (String distribucion : DISTRIBUCIONES) {
                    for (int n : TAMAÑOS) {
                        List<ResultadoOrdenamiento> resultados = resultadosDetallados.stream()
                            .filter(r -> r.algoritmo.equals(algoritmo) && r.distribucion.equals(distribucion) && r.n == n)
                            .toList();
                        
                        if (!resultados.isEmpty()) {
                            EstadisticasDescriptivas stats = calcularEstadisticas(resultados);
                            writer.printf("%s,%s,%d,%.2f,%.2f,%.2f,%.2f,%d\n",
                                algoritmo, distribucion, n,
                                stats.meanComparaciones, stats.meanIntercambios, stats.meanTiempo,
                                stats.stddevTiempo, stats.iqrTiempo);
                        }
                    }
                }
            }
        }
    }
    
    public void generarReporteTrazabilidad(String archivo) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo))) {
            writer.println("=== REPORTE DE TRAZABILIDAD EXPERIMENTAL ===");
            writer.println("Timestamp inicio: " + new Date(parametros.timestampInicio));
            writer.println("Timestamp fin: " + new Date());
            writer.println("Duración total: " + (System.currentTimeMillis() - parametros.timestampInicio) + " ms");
            writer.println("JVM: " + parametros.versionJVM);
            writer.println("Sistema Operativo: " + parametros.sistemaOperativo);
            writer.println("Semilla maestra: " + parametros.semillaMaestra);
            writer.println("Total configuraciones: " + (ALGORITMOS.length * DISTRIBUCIONES.length * ATRIBUTOS.length * TAMAÑOS.length));
            writer.println("Repeticiones por configuración: " + parametros.repeticionesPorConfig);
            writer.println("Total ejecuciones: " + resultadosDetallados.size());
            writer.println();
            
            writer.println("PARÁMETROS DEL EXPERIMENTO:");
            writer.println("Algoritmos: " + Arrays.toString(ALGORITMOS));
            writer.println("Distribuciones: " + Arrays.toString(DISTRIBUCIONES));
            writer.println("Atributos: " + Arrays.toString(ATRIBUTOS));
            writer.println("Tamaños: " + Arrays.toString(TAMAÑOS));
            writer.println();
            
            writer.println("VERIFICACIÓN DE REPRODUCIBILIDAD:");
            writer.println("- Semilla fija utilizada para generación de datos");
            writer.println("- Control de variables ambientales documentado");
            writer.println("- Calentamiento de JVM aplicado");
            writer.println("- Múltiples repeticiones para robustez estadística");
        }
    }
    
    private static class EstadisticasDescriptivas {
        double meanComparaciones, stddevComparaciones;
        double meanIntercambios, stddevIntercambios;
        double meanTiempo, stddevTiempo;
        long iqrTiempo;
    }
    
    /**
     * Método principal para ejecutar experimento completo
     */
    public static void main(String[] args) {
        try {
            ExperimentoRigoroso experimento = new ExperimentoRigoroso();
            
            System.out.println("Ejecutando experimento riguroso...");
            experimento.ejecutarExperimentoCompleto();
            
            System.out.println("Generando análisis estadístico...");
            experimento.generarAnalisisEstadistico("analisis_estadistico.txt");
            
            System.out.println("Exportando datos para visualización...");
            experimento.exportarDatosVisualizacion("datos_visualizacion.csv");
            
            System.out.println("Generando reporte de trazabilidad...");
            experimento.generarReporteTrazabilidad("trazabilidad_experimento.txt");
            
            System.out.println("Experimento completado exitosamente!");
            
        } catch (Exception e) {
            System.err.println("Error en experimento: " + e.getMessage());
            e.printStackTrace();
        }
    }
}