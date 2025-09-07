package model;

import java.util.Arrays;
import java.util.List;

public class AggregatedResult {
    public String algoritmo, distribucion, atributo;
    public int n;
    public double meanComparaciones;
    public double meanIntercambios;
    public double meanTiempoNs;
    public long iqrComparaciones;
    public long iqrIntercambios;
    public long iqrTiempoNs;

    public AggregatedResult(String algoritmo, String distribucion, String atributo, int n) {
        this.algoritmo = algoritmo;
        this.distribucion = distribucion;
        this.atributo = atributo;
        this.n = n;
    }

    public static AggregatedResult aggregate(List<ResultadoOrdenamiento> runs) {
        if (runs == null || runs.isEmpty()) return null;
        AggregatedResult agg = new AggregatedResult(
                runs.get(0).algoritmo, runs.get(0).distribucion,
                runs.get(0).atributo, runs.get(0).n);

        int k = runs.size();
        double sumC = 0, sumI = 0, sumT = 0;
        long[] arrC = new long[k], arrI = new long[k], arrT = new long[k];
        for (int i = 0; i < k; i++) {
            ResultadoOrdenamiento r = runs.get(i);
            sumC += r.comparaciones;
            sumI += r.intercambios;
            sumT += r.tiempoNs;
            arrC[i] = r.comparaciones;
            arrI[i] = r.intercambios;
            arrT[i] = r.tiempoNs;
        }
        agg.meanComparaciones = sumC / k;
        agg.meanIntercambios = sumI / k;
        agg.meanTiempoNs = sumT / k;

        Arrays.sort(arrC); Arrays.sort(arrI); Arrays.sort(arrT);
        agg.iqrComparaciones = computeIQR(arrC);
        agg.iqrIntercambios = computeIQR(arrI);
        agg.iqrTiempoNs = computeIQR(arrT);
        return agg;
    }

    private static long computeIQR(long[] sorted) {
        int n = sorted.length;
        if (n <= 1) return 0;
        double q1 = percentile(sorted, 25);
        double q3 = percentile(sorted, 75);
        return Math.round(q3 - q1);
    }

    private static double percentile(long[] sorted, double p) {
        double pos = (p / 100.0) * (sorted.length + 1);
        if (pos <= 1) return sorted[0];
        if (pos >= sorted.length) return sorted[sorted.length - 1];
        int idx = (int) pos;
        double frac = pos - idx;
        return sorted[idx - 1] + frac * (sorted[idx] - sorted[idx - 1]);
    }
}
