package model;

import java.util.Random;

/**
 * Clase utilitaria para generar diferentes tipos de distribuciones de datos
 * utilizadas en las pruebas de algoritmos de ordenamiento.
 * Permite evaluar el rendimiento de los algoritmos en diferentes escenarios.
 */
public class Distribuciones {
    private static final Random random = new Random(1234); // Semilla fija para reproducibilidad

    /**
     * Establece una nueva semilla para el generador aleatorio
     * @param semilla Nueva semilla a utilizar
     */
    public static void establecerSemilla(long semilla) {
        random.setSeed(semilla);
    }

    /**
     * Genera un arreglo con distribución aleatoria uniforme
     * Cada elemento tiene la misma probabilidad de aparecer en cualquier posición
     * @param n Tamaño del arreglo
     * @param m Valor máximo (los valores estarán entre 1 y m)
     * @return Arreglo con valores aleatorios uniformes
     */
    public static int[] generarAleatoria(int n, int m) {
        validarParametros(n, m);
        
        int[] datos = new int[n];
        for (int i = 0; i < n; i++) {
            datos[i] = random.nextInt(m) + 1;
        }
        return datos;
    }

    /**
     * Genera un arreglo casi ordenado (90% ordenado, 10% aleatorio)
     * Simula datos que están mayormente ordenados con algunas perturbaciones
     * Útil para evaluar algoritmos que se benefician de datos parcialmente ordenados
     * @param n Tamaño del arreglo
     * @param m Valor máximo (no utilizado, mantenido por compatibilidad)
     * @return Arreglo casi ordenado
     */
    public static int[] generarCasiOrdenada(int n, int m) {
        validarParametros(n, m);
        
        int[] datos = new int[n];
        
        // Generar secuencia ordenada inicial
        for (int i = 0; i < n; i++) {
            datos[i] = i + 1;
        }

        // Perturbar aproximadamente el 10% de los elementos
        int perturbaciones = Math.max(1, n / 10);
        for (int i = 0; i < perturbaciones; i++) {
            int indice1 = random.nextInt(n);
            int indice2 = random.nextInt(n);
            
            // Intercambiar elementos
            int temporal = datos[indice1];
            datos[indice1] = datos[indice2];
            datos[indice2] = temporal;
        }
        
        return datos;
    }

    /**
     * Genera un arreglo en orden completamente inverso (descendente)
     * Representa el peor caso para muchos algoritmos de ordenamiento
     * @param n Tamaño del arreglo
     * @param m Valor máximo (no utilizado, mantenido por compatibilidad)
     * @return Arreglo en orden descendente
     */
    public static int[] generarInverso(int n, int m) {
        validarParametros(n, m);
        
        int[] datos = new int[n];
        for (int i = 0; i < n; i++) {
            datos[i] = n - i; // Valores desde n hasta 1
        }
        return datos;
    }

    /**
     * Genera un arreglo con muchos elementos duplicados
     * Útil para probar el comportamiento con valores repetidos
     * @param n Tamaño del arreglo
     * @param m Valor máximo
     * @param porcentajeDuplicados Porcentaje de elementos duplicados (0-100)
     * @return Arreglo con elementos duplicados
     */
    public static int[] generarConDuplicados(int n, int m, int porcentajeDuplicados) {
        validarParametros(n, m);
        if (porcentajeDuplicados < 0 || porcentajeDuplicados > 100) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 100");
        }
        
        int[] datos = new int[n];
        int elementosDuplicados = (n * porcentajeDuplicados) / 100;
        int valorDuplicado = random.nextInt(m) + 1;
        
        // Llenar con el valor duplicado
        for (int i = 0; i < elementosDuplicados; i++) {
            datos[i] = valorDuplicado;
        }
        
        // Llenar el resto con valores aleatorios
        for (int i = elementosDuplicados; i < n; i++) {
            datos[i] = random.nextInt(m) + 1;
        }
        
        // Mezclar el arreglo
        mezclarArreglo(datos);
        
        return datos;
    }

    /**
     * Genera un arreglo con distribución en forma de montaña
     * Los valores crecen hasta la mitad y luego decrecen
     * @param n Tamaño del arreglo
     * @param m Valor máximo (no utilizado, mantenido por compatibilidad)
     * @return Arreglo en forma de montaña
     */
    public static int[] generarMontana(int n, int m) {
        validarParametros(n, m);
        
        int[] datos = new int[n];
        int mitad = n / 2;
        
        // Parte creciente
        for (int i = 0; i < mitad; i++) {
            datos[i] = i + 1;
        }
        
        // Parte decreciente
        for (int i = mitad; i < n; i++) {
            datos[i] = n - i;
        }
        
        return datos;
    }

    /**
     * Mezcla aleatoriamente los elementos de un arreglo (algoritmo Fisher-Yates)
     * @param arreglo Arreglo a mezclar (se modifica en lugar)
     */
    private static void mezclarArreglo(int[] arreglo) {
        for (int i = arreglo.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temporal = arreglo[i];
            arreglo[i] = arreglo[j];
            arreglo[j] = temporal;
        }
    }

    /**
     * Valida que los parámetros sean válidos
     * @param n Tamaño del arreglo
     * @param m Valor máximo
     * @throws IllegalArgumentException si los parámetros son inválidos
     */
    private static void validarParametros(int n, int m) {
        if (n <= 0) {
            throw new IllegalArgumentException("El tamaño n debe ser positivo");
        }
        if (m <= 0) {
            throw new IllegalArgumentException("El valor máximo m debe ser positivo");
        }
    }

    /**
     * Obtiene información sobre el tipo de distribución de un arreglo
     * @param datos Arreglo a analizar
     * @return String describiendo las características del arreglo
     */
    public static String analizarDistribucion(int[] datos) {
        if (datos == null || datos.length == 0) {
            return "Arreglo vacío o nulo";
        }
        
        boolean ordenado = estaOrdenado(datos);
        boolean inverso = estaOrdenadoInverso(datos);
        int duplicados = contarDuplicados(datos);
        
        StringBuilder analisis = new StringBuilder();
        analisis.append(String.format("Arreglo de %d elementos:\n", datos.length));
        
        if (ordenado) {
            analisis.append("- Completamente ordenado (ascendente)\n");
        } else if (inverso) {
            analisis.append("- Completamente ordenado (descendente)\n");
        } else {
            analisis.append("- Distribución mixta\n");
        }
        
        analisis.append(String.format("- Elementos duplicados: %d (%.1f%%)\n", 
                                     duplicados, (duplicados * 100.0) / datos.length));
        
        return analisis.toString();
    }

    /**
     * Verifica si un arreglo está ordenado ascendentemente
     */
    private static boolean estaOrdenado(int[] datos) {
        for (int i = 1; i < datos.length; i++) {
            if (datos[i] < datos[i-1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifica si un arreglo está ordenado descendentemente
     */
    private static boolean estaOrdenadoInverso(int[] datos) {
        for (int i = 1; i < datos.length; i++) {
            if (datos[i] > datos[i-1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Cuenta el número de elementos duplicados en el arreglo
     */
    private static int contarDuplicados(int[] datos) {
        int duplicados = 0;
        for (int i = 0; i < datos.length; i++) {
            for (int j = i + 1; j < datos.length; j++) {
                if (datos[i] == datos[j]) {
                    duplicados++;
                    break; // Solo cuenta una vez por elemento
                }
            }
        }
        return duplicados;
    }
}