
package programacioniii.hojasdecalculo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Clase principal que contiene el método main y el menú principal de la aplicación.
 */
public class HojasDeCalculo {
    /**
     * Método principal que inicia la aplicación mostrando el menú principal.
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        showMainMenu();
    }

    /**
     * Muestra el menú principal de la aplicación.
     */
    public static void showMainMenu() {
        // Crear y configurar la ventana del menú principal
        JFrame frame = new JFrame("Menú Principal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(null);

        // Botón para crear un nuevo libro de cálculo
        JButton btnNuevoLibro = new JButton("Nuevo Libro");
        btnNuevoLibro.setBounds(100, 50, 200, 40);
        btnNuevoLibro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Ocultar el menú principal y abrir un nuevo libro de cálculo
                frame.setVisible(false);
                abrirLibroDeCalculo(false);
            }
        });
        frame.add(btnNuevoLibro);

        // Botón para cargar un libro de cálculo existente
        JButton btnCargarLibro = new JButton("Cargar Libro");
        btnCargarLibro.setBounds(100, 120, 200, 40);
        btnCargarLibro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Ocultar el menú principal y abrir un libro de cálculo cargado desde un archivo
                frame.setVisible(false);
                abrirLibroDeCalculo(true);
            }
        });
        frame.add(btnCargarLibro);

        // Mostrar la ventana del menú principal
        frame.setVisible(true);
    }

    /**
     * Abre un libro de cálculo, ya sea creando uno nuevo o cargando uno existente desde un archivo.
     * @param cargar Indica si se debe cargar un libro existente desde un archivo
     */
    private static void abrirLibroDeCalculo(boolean cargar) {
        String nombreArchivo = "libro.json";

        // Si se va a cargar un libro existente, mostrar un cuadro de diálogo para seleccionar el archivo
        if (cargar) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                nombreArchivo = fileChooser.getSelectedFile().getAbsolutePath();
            } else {
                // Si el usuario cancela la selección, mostrar nuevamente el menú principal y salir del método
                showMainMenu();
                return;
            }
        }

        // Crear un nuevo libro de cálculo y configurarlo con el nombre del archivo
        LibroDeCalculo libro = new LibroDeCalculo();
        libro.setNombreArchivo(nombreArchivo);

        // Si se va a cargar un libro existente, cargar las hojas de cálculo desde el archivo
        if (cargar) {
            libro.cargarDesdeArchivo();
        }

        // Mostrar el libro de cálculo
        libro.setVisible(true);

        // Agregar un hook de apagado para guardar automáticamente el libro al cerrar la aplicación
        Runtime.getRuntime().addShutdownHook(new Thread(() -> libro.guardarEnArchivo()));

        // Agregar un listener para preguntar al usuario si desea volver al menú principal al cerrar el libro
        libro.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int respuesta = JOptionPane.showConfirmDialog(libro, "¿Desea volver al menú principal?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    // Si elige volver al menú principal, ocultar el libro y mostrar el menú principal
                    libro.setVisible(false);
                    showMainMenu();
                }
            }
        });
    }
}
