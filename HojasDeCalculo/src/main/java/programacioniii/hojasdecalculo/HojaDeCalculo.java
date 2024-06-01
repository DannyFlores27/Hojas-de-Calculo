
package programacioniii.hojasdecalculo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

/**
 * Representa una hoja de cálculo que contiene una tabla de celdas.
 * Permite la inserción, actualización y eliminación de celdas, así como el procesamiento de fórmulas matemáticas.
 */
public class HojaDeCalculo extends JPanel implements Serializable {
    private static final int NUM_FILAS = 20;
    private static final int NUM_COLUMNAS = 20;

    private MatrizOrtogonal matriz;
    private JTable tabla;
    private JLabel lblFilaColumna;
    private JTextField txtEntrada;

    /**
     * Constructor de la clase HojaDeCalculo.
     * Inicializa la hoja de cálculo con una matriz ortogonal y una tabla para mostrar las celdas.
     */
    public HojaDeCalculo() {
        matriz = new MatrizOrtogonal();
        setLayout(new BorderLayout());

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
    }

    /**
     * Actualiza el texto que muestra la fila y columna seleccionada en la hoja de cálculo.
     */
    private void actualizarFilaColumna() {
        int fila = tabla.getSelectedRow();
        int columna = tabla.getSelectedColumn();
        if (fila != -1 && columna != -1) {
            lblFilaColumna.setText("Fila: " + fila + " Columna: " + columna);
        }
    }

    /**
     * Procesa la entrada del usuario, insertando el valor o realizando la operación en la celda seleccionada.
     */
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

    /**
     * Obtiene el valor numérico de una celda en la hoja de cálculo.
     * @param fila Fila de la celda
     * @param columna Columna de la celda
     * @return Valor numérico de la celda
     */
    private double obtenerValorCelda(int fila, int columna) {
        Object valor = tabla.getValueAt(fila, columna);
        if (valor instanceof Number) {
            return ((Number) valor).doubleValue();
        } else if (valor == null || valor.toString().isEmpty()) {
            return 0; // Si la celda está vacía, devolvemos 0
        } else {
            try {
                return Double.parseDouble(valor.toString());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("La celda " + fila + "," + columna + " no contiene un valor numérico.");
            }
        }
    }

    /**
     * Convierte la representación de una celda (ej. F1C2) en coordenadas de fila y columna.
     * @param celda Representación de la celda en forma de cadena
     * @return Coordenadas de fila y columna como un arreglo de enteros
     */
    private int[] parseCelda(String celda) {
        int fila = Integer.parseInt(celda.substring(1, celda.indexOf('C')));
        int columna = Integer.parseInt(celda.substring(celda.indexOf('C') + 1));
        return new int[]{fila, columna};
    }

    /**
     * Obtiene la matriz ortogonal asociada a esta hoja de cálculo.
     * @return La
     * matriz ortogonal asociada a la hoja de cálculo
     */
    public MatrizOrtogonal getMatriz() {
        return matriz;
    }

    /**
     * Establece la matriz ortogonal asociada a esta hoja de cálculo y actualiza la tabla con sus valores.
     * @param matriz La nueva matriz ortogonal a establecer
     */
    public void setMatriz(MatrizOrtogonal matriz) {
        this.matriz = matriz;
        actualizarTablaDesdeMatriz();
    }

    /**
     * Actualiza la tabla de la hoja de cálculo con los valores de la matriz ortogonal actual.
     */
    private void actualizarTablaDesdeMatriz() {
        for (int fila = 0; fila < NUM_FILAS; fila++) {
            for (int columna = 0; columna < NUM_COLUMNAS; columna++) {
                Object valor = matriz.obtenerCelda(fila, columna);
                tabla.setValueAt(valor, fila, columna);
            }
        }
    }
}
