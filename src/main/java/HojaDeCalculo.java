import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class HojaDeCalculo extends JFrame {
    private static final int NUM_FILAS = 20;
    private static final int NUM_COLUMNAS = 20;

    private MatrizOrtogonal matriz;
    private JTable tabla;
    private JLabel lblFilaColumna;
    private JTextField txtEntrada;
    private String archivoNombre = "matriz.json";

    public HojaDeCalculo() {
        matriz = new MatrizOrtogonal();
        setTitle("Hoja de Cálculo");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Menú superior
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemGuardar = new JMenuItem("Guardar");
        JMenuItem itemGuardarYSalir = new JMenuItem("Guardar y Salir");
        JMenuItem itemSalir = new JMenuItem("Salir");

        itemGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarArchivo();
            }
        });

        itemGuardarYSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarArchivo();
                System.exit(0);
            }
        });

        itemSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menuArchivo.add(itemGuardar);
        menuArchivo.add(itemGuardarYSalir);
        menuArchivo.add(itemSalir);
        menuBar.add(menuArchivo);

        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem itemManualUsuario = new JMenuItem("Manual de Usuario");
        itemManualUsuario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarManualUsuario();
            }
        });
        menuAyuda.add(itemManualUsuario);
        menuBar.add(menuAyuda);

        setJMenuBar(menuBar);

        // Menú secundario
        JPanel panelSecundario = new JPanel(new BorderLayout());
        lblFilaColumna = new JLabel("Fila: - Columna: -");
        txtEntrada = new JTextField();
        JButton btnProcesar = new JButton("Procesar");

        btnProcesar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                procesarEntrada();
            }
        });

        panelSecundario.add(lblFilaColumna, BorderLayout.WEST);
        panelSecundario.add(txtEntrada, BorderLayout.CENTER);
        panelSecundario.add(btnProcesar, BorderLayout.EAST);

        add(panelSecundario, BorderLayout.NORTH);

        // Tabla de celdas
        DefaultTableModel modeloTabla = new DefaultTableModel(NUM_FILAS, NUM_COLUMNAS);
        tabla = new JTable(modeloTabla);
        tabla.setCellSelectionEnabled(true);
        tabla.getSelectionModel().addListSelectionListener(e -> actualizarFilaColumna());

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarArchivo();
    }

    private void actualizarFilaColumna() {
        int fila = tabla.getSelectedRow();
        int columna = tabla.getSelectedColumn();
        if (fila != -1 && columna != -1) {
            lblFilaColumna.setText("Fila: " + fila + " Columna: " + columna);
        }
    }

    private void procesarEntrada() {
        String entrada = txtEntrada.getText().trim();
        int fila = tabla.getSelectedRow();
        int columna = tabla.getSelectedColumn();

        if (fila == -1 || columna == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una celda primero.");
            return;
        }

        if (entrada.startsWith("=")) {
            try {
                String[] partes = entrada.substring(1).split("\\(");
                String operacion = partes[0].toLowerCase();
                String[] celdas = partes[1].replace(")", "").split(",");

                double resultado = 0;
                switch (operacion) {
                    case "suma":
                        for (String celda : celdas) {
                            int[] pos = parseCelda(celda);
                            resultado += obtenerValorCelda(pos[0], pos[1]);
                        }
                        break;
                    case "resta":
                        resultado = obtenerValorCelda(parseCelda(celdas[0])[0], parseCelda(celdas[0])[1]);
                        for (int i = 1; i < celdas.length; i++) {
                            int[] pos = parseCelda(celdas[i]);
                            resultado -= obtenerValorCelda(pos[0], pos[1]);
                        }
                        break;
                    case "mult":
                        if (celdas.length != 2) {
                            throw new IllegalArgumentException("La operación multiplicación requiere exactamente 2 celdas.");
                        }
                        resultado = obtenerValorCelda(parseCelda(celdas[0])[0], parseCelda(celdas[0])[1])
                                * obtenerValorCelda(parseCelda(celdas[1])[0], parseCelda(celdas[1])[1]);
                        break;
                    case "div":
                        if (celdas.length != 2) {
                            throw new IllegalArgumentException("La operación división requiere exactamente 2 celdas.");
                        }
                        resultado = obtenerValorCelda(parseCelda(celdas[0])[0], parseCelda(celdas[0])[1])
                                / obtenerValorCelda(parseCelda(celdas[1])[0], parseCelda(celdas[1])[1]);
                        break;
                    default:
                        throw new IllegalArgumentException("Operación no soportada.");
                }

                matriz.insertarCelda(fila, columna, resultado);
                tabla.setValueAt(resultado, fila, columna);
                ((DefaultTableModel) tabla.getModel()).fireTableCellUpdated(fila, columna); // Actualizar la tabla
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error en la entrada: " + ex.getMessage());
            }
        } else {
            matriz.insertarCelda(fila, columna, entrada);
            tabla.setValueAt(entrada, fila, columna);
            ((DefaultTableModel) tabla.getModel()).fireTableCellUpdated(fila, columna); // Actualizar la tabla
        }
    }


    private double obtenerValorCelda(int fila, int columna) {
        Object valor = tabla.getValueAt(fila, columna);
        if (valor instanceof String && ((String) valor).startsWith("=")) {
            String formula = ((String) valor).substring(1);
            return evaluarFormula(formula);
        } else if (valor instanceof Number) {
            return ((Number) valor).doubleValue();
        } else if (valor == null || valor.toString().isEmpty()) {
            return 0; // Si la celda está vacía, devolvemos 0
        } else {
            try {
                // Intentamos convertir el valor a un número
                return Double.parseDouble(valor.toString());
            } catch (NumberFormatException e) {
                // Si no se puede convertir a número, lanzamos una excepción
                throw new IllegalArgumentException("La celda " + fila + "," + columna + " no contiene un valor numérico.");
            }
        }
    }


    private double evaluarFormula(String formula) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        try {
            Object result = engine.eval(formula);
            if (result instanceof Number) {
                return ((Number) result).doubleValue();
            } else {
                throw new IllegalArgumentException("La fórmula no produce un valor numérico.");
            }
        } catch (ScriptException e) {
            throw new IllegalArgumentException("Error al evaluar la fórmula: " + e.getMessage());
        }
    }

    private int[] parseCelda(String celda) {
        int fila = Integer.parseInt(celda.substring(1, celda.indexOf('C')));
        int columna = Integer.parseInt(celda.substring(celda.indexOf('C') + 1));
        return new int[]{fila, columna};
    }

    private double getValorCelda(int fila, int columna) {
        Object valor = matriz.obtenerCelda(fila, columna);
        if (valor instanceof Number) {
            return ((Number) valor).doubleValue();
        } else {
            throw new IllegalArgumentException("La celda " + fila + "," + columna + " no contiene un valor numérico.");
        }
    }

    private void mostrarManualUsuario() {
        JOptionPane.showMessageDialog(this, "Manual de Usuario:\n" +
                "1. Seleccione una celda haciendo clic en ella.\n" +
                "2. Ingrese los datos en el cuadro de texto y presione 'Procesar'.\n" +
                "3. Para realizar operaciones, ingrese la fórmula en el formato:\n" +
                "   =suma(F1C1,F2C2,...) para suma\n" +
                "   =resta(F1C1,F2C2,...) para resta\n" +
                "   =mult(F1C1,F2C2) para multiplicación\n" +
                "   =div(F1C1,F2C2) para división");
    }

    private void guardarArchivo() {
        try {
            matriz.guardarEnArchivo(archivoNombre);
            JOptionPane.showMessageDialog(this, "Archivo guardado exitosamente.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo.");
            e.printStackTrace();
        }
    }

    private void cargarArchivo() {
        try {
            matriz = MatrizOrtogonal.cargarDesdeArchivo(archivoNombre);
            actualizarTablaDesdeMatriz();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el archivo.");
            e.printStackTrace();
        }
    }

    private void actualizarTablaDesdeMatriz() {
        for (int fila = 0; fila < NUM_FILAS; fila++) {
            for (int columna = 0; columna < NUM_COLUMNAS; columna++) {
                Object valor = matriz.obtenerCelda(fila, columna);
                tabla.setValueAt(valor, fila, columna);
            }
        }
    }
}
