package model.Modelos;

import model.AlgoritmoOrdenamiento;

/**
 * Implementación optimizada del algoritmo de ordenamiento Burbuja
 * Incluye optimización de parada temprana cuando el arreglo ya está ordenado
 */
public class Burbuja implements AlgoritmoOrdenamiento {
    private long comparaciones;
    private long intercambios;
    private long tiempo;

    @Override
    public void ordenar(int[] datos) {
        // Validación de entrada
        if (datos == null) {
            throw new IllegalArgumentException("El arreglo no puede ser null");
        }
        
        comparaciones = 0;
        intercambios = 0;
        long inicio = System.nanoTime();

        int n = datos.length;
        boolean huboIntercambio; // Optimización: detecta si ya está ordenado
        
        // Algoritmo burbuja optimizado
        for (int i = 0; i < n - 1; i++) {
            huboIntercambio = false;
            
            // En cada pasada, el elemento más grande "burbujea" hacia el final
            for (int j = 0; j < n - i - 1; j++) {
                comparaciones++;
                
                if (datos[j] > datos[j + 1]) {
                    // Intercambio de elementos
                    int temporal = datos[j];
                    datos[j] = datos[j + 1];
                    datos[j + 1] = temporal;
                    
                    intercambios++;
                    huboIntercambio = true;
                }
            }
            
            // Si no hubo intercambios, el arreglo ya está ordenado
            if (!huboIntercambio) {
                break;
            }
        }

        tiempo = System.nanoTime() - inicio;
    }

    @Override 
    public long getComparaciones() { 
        return comparaciones; 
    }
    
    @Override 
    public long getIntercambios() { 
        return intercambios; 
    }
    
    @Override 
    public long getTiempoEjecucion() { 
        return tiempo; 
    }

    /**
     * Información adicional sobre el algoritmo
     * @return Descripción del algoritmo
     */
    public String getDescripcion() {
        return "Algoritmo Burbuja optimizado con parada temprana. " +
        "Complejidad: O(n²) peor caso, O(n) mejor caso";
    }
}