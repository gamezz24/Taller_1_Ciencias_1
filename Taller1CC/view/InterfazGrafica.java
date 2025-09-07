package view;

import model.*;
import model.Modelos.Burbuja;
import model.Modelos.Insercion;
import model.Modelos.Merge;
import model.Modelos.Quick;
import model.Modelos.Seleccion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.*;

public class InterfazGrafica extends JFrame {
    private Candidato[] candidatos;
    private final DefaultTableModel candidatosModel = new DefaultTableModel();
    private final DefaultTableModel resultadosModel = new DefaultTableModel();

    private JComboBox<String> cbDistribucion;
    private JComboBox<String> cbAlgoritmo;
    private JComboBox<String> cbAtributo;
    private JComboBox<String> cbOrden;
    private JTextField tfN;
    private JTextField tfM;

    private JTextArea taMetricas;

    // Guarda resultados detallados
    private final java.util.List<ResultadoOrdenamiento> resultadosDetalle = new ArrayList<>();

    public InterfazGrafica() {
        super("Taller - Algoritmos de Ordenamiento");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("N:"));
        tfN = new JTextField("10", 4);
        topPanel.add(tfN);
        topPanel.add(new JLabel("m:"));
        tfM = new JTextField("100", 5);
        topPanel.add(tfM);

        cbDistribucion = new JComboBox<>(new String[]{"Aleatoria", "Casi ordenada", "Inverso"});
        topPanel.add(new JLabel("Distribución:"));
        topPanel.add(cbDistribucion);

        cbAlgoritmo = new JComboBox<>(new String[]{"Burbuja", "Inserción", "Selección", "Merge", "Quick"});
        topPanel.add(new JLabel("Algoritmo:"));
        topPanel.add(cbAlgoritmo);

        cbAtributo = new JComboBox<>(new String[]{"Distancia", "Horas Perdidas", "Prebendas", "Sobornos", "Corrupción"});
        topPanel.add(new JLabel("Atributo:"));
        topPanel.add(cbAtributo);

        cbOrden = new JComboBox<>(new String[]{"Ascendente", "Descendente"});
        topPanel.add(new JLabel("Orden:"));
        topPanel.add(cbOrden);

        JButton btnGenerar = new JButton("Generar Datos");
        btnGenerar.addActionListener(e -> generarDatos());
        topPanel.add(btnGenerar);

        JButton btnEjecutar = new JButton("Ejecutar Ordenamiento");
        btnEjecutar.addActionListener(e -> ejecutarOrdenamiento());
        topPanel.add(btnEjecutar);

        JButton btnExportCSV = new JButton("Exportar CSV");
        btnExportCSV.addActionListener(e -> exportarCSV());
        topPanel.add(btnExportCSV);

        JButton btnExportJSON = new JButton("Exportar JSON");
        btnExportJSON.addActionListener(e -> exportarJSON());
        topPanel.add(btnExportJSON);

        JButton btnExportPDF = new JButton("Exportar PDF/HTML");
        btnExportPDF.addActionListener(e -> exportarHTML());
        topPanel.add(btnExportPDF);

        getContentPane().add(topPanel, BorderLayout.NORTH);

        // Tabla de candidatos
        candidatosModel.setColumnIdentifiers(new Object[]{"Nombre", "Distancia", "Horas Perdidas", "Prebendas", "Sobornos", "Corrupción", "Marchas"});
        JTable tablaCandidatos = new JTable(candidatosModel);
        JScrollPane spCandidatos = new JScrollPane(tablaCandidatos);
        spCandidatos.setBorder(BorderFactory.createTitledBorder("Candidatos"));

        // Tabla de resultados detallados
        resultadosModel.setColumnIdentifiers(new Object[]{"Algoritmo", "Distribución", "Atributo", "N", "Repetición", "Comparaciones", "Intercambios", "Tiempo (ns)"});
        JTable tablaResultados = new JTable(resultadosModel);
        JScrollPane spResultados = new JScrollPane(tablaResultados);
        spResultados.setBorder(BorderFactory.createTitledBorder("Resultados"));

        // Métricas
        taMetricas = new JTextArea(6, 40);
        taMetricas.setEditable(false);
        JScrollPane spMetricas = new JScrollPane(taMetricas);
        spMetricas.setBorder(BorderFactory.createTitledBorder("Métricas"));

        JSplitPane bottomSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, spResultados, spMetricas);
        bottomSplit.setResizeWeight(0.7);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, spCandidatos, bottomSplit);
        mainSplit.setResizeWeight(0.6);

        getContentPane().add(mainSplit, BorderLayout.CENTER);
    }

    private void generarDatos() {
        int n = Integer.parseInt(tfN.getText().trim());
        int m = Integer.parseInt(tfM.getText().trim());
        candidatos = GeneradorDatos.generarPoblacion(n, m);
        refrescarTablaCandidatos();
    }

    private void refrescarTablaCandidatos() {
        candidatosModel.setRowCount(0);
        if (candidatos == null) return;
        for (Candidato c : candidatos) {
            String marchas = c.getMarchas().toString().replace("[", "").replace("]", "");
            candidatosModel.addRow(new Object[]{
                    c.getNombre(),
                    c.getDistancia() + " m",
                    c.getHorasPerdidas(),
                    c.getPrebendas(),
                    c.getSobornos(),
                    c.getcasosCorrupciones(),
                    marchas + " (" + c.getMarchas().size() + ")"
            });
        }
    }

    private void ejecutarOrdenamiento() {
        if (candidatos == null) {
            JOptionPane.showMessageDialog(this, "Genera primero los datos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String distrib = (String) cbDistribucion.getSelectedItem();
        String alg = (String) cbAlgoritmo.getSelectedItem();
        String atributo = (String) cbAtributo.getSelectedItem();
        boolean asc = cbOrden.getSelectedIndex() == 0;

        int n = candidatos.length;
        int[] datos = new int[n];
        for (int i = 0; i < n; i++) {
            datos[i] = switch (atributo) {
                case "Distancia" -> candidatos[i].getDistancia();
                case "Horas Perdidas" -> candidatos[i].getHorasPerdidas();
                case "Prebendas" -> candidatos[i].getPrebendas();
                case "Sobornos" -> candidatos[i].getSobornos();
                case "Corrupción" -> candidatos[i].getcasosCorrupciones();
                default -> candidatos[i].getDistancia();
            };
        }

        AlgoritmoOrdenamiento algoritmo = switch (alg) {
            case "Burbuja" -> new Burbuja();
            case "Inserción" -> new Insercion();
            case "Selección" -> new Seleccion();
            case "Merge" -> new Merge();
            case "Quick" -> new Quick();
            default -> new Burbuja();
        };

        algoritmo.ordenar(datos);

        if (!asc) {
            for (int i = 0; i < datos.length / 2; i++) {
                int tmp = datos[i];
                datos[i] = datos[datos.length - 1 - i];
                datos[datos.length - 1 - i] = tmp;
            }
        }

        Candidato[] ordenados = new Candidato[n];
        boolean[] usado = new boolean[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int val = switch (atributo) {
                    case "Distancia" -> candidatos[j].getDistancia();
                    case "Horas Perdidas" -> candidatos[j].getHorasPerdidas();
                    case "Prebendas" -> candidatos[j].getPrebendas();
                    case "Sobornos" -> candidatos[j].getSobornos();
                    case "Corrupción" -> candidatos[j].getcasosCorrupciones();
                    default -> candidatos[j].getDistancia();
                };
                if (!usado[j] && val == datos[i]) {
                    ordenados[i] = candidatos[j];
                    usado[j] = true;
                    break;
                }
            }
        }

        candidatos = ordenados;
        refrescarTablaCandidatos();

        ResultadoOrdenamiento ro = new ResultadoOrdenamiento(alg, distrib, atributo, n,
                algoritmo.getComparaciones(), algoritmo.getIntercambios(), algoritmo.getTiempoEjecucion(), 1);
        resultadosDetalle.add(ro);
        resultadosModel.addRow(new Object[]{
                ro.algoritmo, ro.distribucion, ro.atributo, ro.n, ro.repeticion, ro.comparaciones, ro.intercambios, ro.tiempoNs
        });

        taMetricas.setText("");
        taMetricas.append("Algoritmo: " + ro.algoritmo + "\n");
        taMetricas.append("Distribución: " + ro.distribucion + "\n");
        taMetricas.append("Atributo: " + ro.atributo + "\n");
        taMetricas.append("N: " + ro.n + "\n");
        taMetricas.append("Comparaciones: " + ro.comparaciones + "\n");
        taMetricas.append("Intercambios: " + ro.intercambios + "\n");
        taMetricas.append("Tiempo (ns): " + ro.tiempoNs + "\n");
    }

    private void exportarCSV() {
        if (resultadosDetalle.isEmpty()) return;
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try {
                Exportador.exportResultadosDetalladosCSV(resultadosDetalle, f.getAbsolutePath());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void exportarJSON() {
        if (resultadosDetalle.isEmpty()) return;
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try {
                Exportador.exportResultadosDetalladosJSON(resultadosDetalle, f.getAbsolutePath());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void exportarHTML() {
        if (resultadosDetalle.isEmpty()) return;

        // Agrupamos para 
        Map<String, java.util.List<ResultadoOrdenamiento>> groups = new HashMap<>();
        for (ResultadoOrdenamiento r : resultadosDetalle) {
            String key = r.algoritmo + "|" + r.distribucion + "|" + r.atributo + "|" + r.n;
            groups.computeIfAbsent(key, k -> new ArrayList<>()).add(r);
        }

        java.util.List<AggregatedResult> aggs = new ArrayList<>();
        for (java.util.List<ResultadoOrdenamiento> runs : groups.values()) {
            aggs.add(AggregatedResult.aggregate(runs));
        }

        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try {
                String htmlPath = f.getAbsolutePath().endsWith(".html") ? f.getAbsolutePath() : f.getAbsolutePath() + ".html";
                StringBuilder sb = new StringBuilder();
                sb.append("<html><head><meta charset='UTF-8'><title>Resultados </title>");
                sb.append("<style>table{border-collapse:collapse;}td,th{border:1px solid #333;padding:6px;}</style>");
                sb.append("</head><body><h2>Resultados </h2>");
                sb.append("<table><tr><th>Algoritmo</th><th>Distribución</th><th>Atributo</th><th>N</th><th>Comparaciones</th><th>Intercambios</th><th>Tiempo(ns)</th><th>IQR Comp</th><th>IQR Int</th><th>IQR Time</th></tr>");
                for (AggregatedResult a : aggs) {
                    sb.append("<tr><td>").append(a.algoritmo).append("</td>")
                    .append("<td>").append(a.distribucion).append("</td>")
                    .append("<td>").append(a.atributo).append("</td>")
                    .append("<td>").append(a.n).append("</td>")
                    .append("<td>").append(String.format("%.2f", a.meanComparaciones)).append("</td>")
                    .append("<td>").append(String.format("%.2f", a.meanIntercambios)).append("</td>")
                    .append("<td>").append(String.format("%.2f", a.meanTiempoNs)).append("</td>")
                    .append("<td>").append(a.iqrComparaciones).append("</td>")
                    .append("<td>").append(a.iqrIntercambios).append("</td>")
                    .append("<td>").append(a.iqrTiempoNs).append("</td></tr>");
                }
                sb.append("</table></body></html>");

                java.nio.file.Files.write(java.nio.file.Paths.get(htmlPath), sb.toString().getBytes());
                JOptionPane.showMessageDialog(this, "HTML creado: " + htmlPath);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InterfazGrafica().setVisible(true);
        });
    }
}
