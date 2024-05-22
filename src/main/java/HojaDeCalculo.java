import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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
        JButton btnSumar = new JButton("Sumar");
        JButton btnRestar = new JButton("Restar");
        JButton btnMultiplicar = new JButton("Multiplicar");
        JButton btnDividir = new JButton("Dividir");

        btnSumar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                realizarOperacion("sumar");
            }
        });

        btnRestar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                realizarOperacion("restar");
            }
        });

        btnMultiplicar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                realizarOperacion("multiplicar");
            }
        });

        btnDividir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                realizarOperacion("dividir");
            }
        });

        JPanel panelOperaciones = new JPanel();
        panelOperaciones.add(btnSumar);
        panelOperaciones.add(btnRestar);
        panelOperaciones.add(btnMultiplicar);
        panelOperaciones.add(btnDividir);

        panelSecundario.add(lblFilaColumna, BorderLayout.WEST);
        panelSecundario.add(txtEntrada, BorderLayout.CENTER);
        panelSecundario.add(panelOperaciones, BorderLayout.EAST);

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

    private void realizarOperacion(String operacion) {
        int fila1 = tabla.getSelectedRow();
        int columna1 = tabla.getSelectedColumn();
        int fila2 = -1;
        int columna2 = -1;

        try {
            String[] partes = txtEntrada.getText().split(",");
            fila2 = Integer.parseInt(partes[0]);
            columna2 = Integer.parseInt(partes[1]);

            String resultado = matriz.realizarOperacion(fila1, columna1, fila2, columna2, operacion);
            JOptionPane.showMessageDialog(this, "Resultado: " + resultado);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: Entrada no válida. Use el formato 'fila,columna'");
        }
    }

    private void mostrarManualUsuario() {
        JOptionPane.showMessageDialog(this, "Manual de Usuario:\n" +
                "1. Seleccione una celda haciendo clic en ella.\n" +
                "2. Ingrese los datos en el cuadro de texto y presione Enter.\n" +
                "3. Para realizar operaciones, seleccione una celda y luego ingrese 'fila,columna' en el cuadro de texto.");
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
