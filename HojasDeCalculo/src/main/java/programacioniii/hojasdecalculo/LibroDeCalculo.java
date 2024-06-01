
package programacioniii.hojasdecalculo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase representa un libro de cálculo que contiene varias hojas de cálculo.
 * Permite agregar, eliminar, cargar y guardar hojas de cálculo en un archivo.
 */
public class LibroDeCalculo extends JFrame {
    // Lista de hojas de cálculo en el libro
    private List<HojaDeCalculo> hojas;
    // Panel de pestañas para mostrar las hojas de cálculo
    private JTabbedPane tabbedPane;
    // Nombre del archivo donde se guarda el libro de cálculo
    private String nombreArchivo = "libro.json";

    /**
     * Constructor de la clase LibroDeCalculo.
     * Inicializa la interfaz gráfica y los componentes del libro de cálculo.
     */
    public LibroDeCalculo() {
        // Inicializar la lista de hojas de cálculo
        hojas = new ArrayList<>();
        // Configurar la ventana principal
        setTitle("Libro de Cálculo");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Manejar el evento de cierre de la ventana
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                preguntarGuardarAntesDeSalir();
            }
        });

        // Crear el panel de pestañas
        tabbedPane = new JTabbedPane();

        // Crear botones para añadir y eliminar hojas
        JButton btnAgregarHoja = new JButton("Agregar Hoja");
        btnAgregarHoja.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                agregarHojaDeCalculo();
            }
        });

        JButton btnEliminarHoja = new JButton("Eliminar Hoja");
        btnEliminarHoja.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarHojaDeCalculo();
            }
        });

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregarHoja);
        panelBotones.add(btnEliminarHoja);

        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        getContentPane().add(panelBotones, BorderLayout.NORTH);

        // Crear menú superior
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Menú Archivo
        JMenu menuArchivo = new JMenu("Archivo");
        menuBar.add(menuArchivo);

        // Opciones del menú Archivo
        JMenuItem menuCambiarNombre = new JMenuItem("Cambiar Nombre");
        menuCambiarNombre.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cambiarNombreArchivo();
            }
        });
        menuArchivo.add(menuCambiarNombre);

        JMenuItem menuGuardar = new JMenuItem("Guardar");
        menuGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarEnArchivo();
            }
        });
        menuArchivo.add(menuGuardar);

        JMenuItem menuInicio = new JMenuItem("Inicio");
        menuInicio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                preguntarGuardarAntesDeIrAInicio();
            }
        });
        menuArchivo.add(menuInicio);

        JMenuItem menuSalir = new JMenuItem("Salir");
        menuSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                preguntarGuardarAntesDeSalir();
            }
        });
        menuArchivo.add(menuSalir);

        // Menú Ayuda
        JMenu menuAyuda = new JMenu("Ayuda");
        menuBar.add(menuAyuda);

        // Opción del menú Ayuda
        JMenuItem menuManual = new JMenuItem("Manual de Usuario");
        menuManual.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarManualDeUsuario();
            }
        });
        menuAyuda.add(menuManual);
    }

    /**
     * Agrega una nueva hoja de cálculo al libro.
     * Crea una nueva instancia de HojaDeCalculo y la añade al libro y al panel de pestañas.
     */
    private void agregarHojaDeCalculo() {
        HojaDeCalculo hoja = new HojaDeCalculo();
        hojas.add(hoja);
        tabbedPane.addTab("Hoja " + (hojas.size()), hoja);
    }

    /**
     * Elimina la hoja de cálculo actualmente seleccionada del libro.
     * Remueve la hoja de la lista y del panel de pestañas.
     */
    private void eliminarHojaDeCalculo() {
        int indiceSeleccionado = tabbedPane.getSelectedIndex();
        if (indiceSeleccionado != -1) {
            hojas.remove(indiceSeleccionado);
            tabbedPane.remove(indiceSeleccionado);
        }
    }

    /**
     * Carga las hojas de cálculo desde un archivo JSON.
     * Lee el archivo y convierte el JSON en matrices ortogonales para cada hoja de cálculo.
     */
    public void cargarDesdeArchivo() {
        try (Reader reader = new FileReader(nombreArchivo)) {
            Gson gson = new Gson();
            MatrizOrtogonal[] matrices = gson.fromJson(reader, MatrizOrtogonal[].class);
            for (MatrizOrtogonal matriz : matrices) {
                HojaDeCalculo hoja = new HojaDeCalculo();
                hoja.setMatriz(matriz);
                hojas.add(hoja);
                tabbedPane.addTab("Hoja " + hojas.size(), hoja);
            }
        } catch (IOException e) {
            // Si el archivo no existe o no se puede leer, simplemente no cargamos nada
        }
    }

    /**
     * Guarda las hojas de cálculo del libro en un archivo JSON.
     * Convierte las matrices ortogonales de las hojas en JSON y las guarda en el archivo.
     */
    public void guardarEnArchivo() {
        MatrizOrtogonal[] matrices = new MatrizOrtogonal[hojas.size()];
        for (int i = 0; i < hojas.size(); i++) {
            matrices[i] = hojas.get(i).getMatriz();
        }

        File archivo = new File(nombreArchivo);
        if (archivo.exists()) {
            int respuesta = JOptionPane.showConfirmDialog(this, "El archivo ya existe. ¿Desea sobrescribirlo?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (respuesta != JOptionPane.YES_OPTION) {
                return;
            }
        }

        try (Writer writer = new FileWriter(nombreArchivo)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(matrices, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

         /* Muestra un cuadro de diálogo para ingresar el nuevo nombre del archivo y lo actualiza si se proporciona uno válido.
     */
    private void cambiarNombreArchivo() {
        String nuevoNombre = JOptionPane.showInputDialog(this, "Ingrese el nuevo nombre del archivo:", nombreArchivo);
        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
            nombreArchivo = nuevoNombre.trim();
        }
    }

    /**
     * Pregunta al usuario si desea guardar los cambios antes de salir de la aplicación.
     * Si elige "Sí", guarda los cambios y cierra la aplicación.
     */
    private void preguntarGuardarAntesDeSalir() {
        int opcion = JOptionPane.showConfirmDialog(this, "¿Desea salir?", "Salir", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            System.exit(0); // Salir de la aplicación si se elige "Sí"
        }
    }

    /**
     * Pregunta al usuario si desea guardar los cambios antes de volver al inicio.
     * Si elige "Sí", guarda los cambios y muestra la pantalla de inicio.
     * Si elige "No", simplemente muestra la pantalla de inicio.
     * Si elige "Cancelar", no hace nada.
     */
    private void preguntarGuardarAntesDeIrAInicio() {
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Desea guardar los cambios antes de volver al inicio?", "Confirmar", JOptionPane.YES_NO_CANCEL_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            guardarEnArchivo();
            setVisible(false);
            HojasDeCalculo.showMainMenu();
        } else if (respuesta == JOptionPane.NO_OPTION) {
            setVisible(false);
            HojasDeCalculo.showMainMenu();
        }
    }

    /**
     * Muestra un cuadro de diálogo con el manual de usuario.
     * Proporciona información sobre las funciones y operaciones disponibles en la aplicación.
     */
    private void mostrarManualDeUsuario() {
        String manual = "Manual de Usuario:\n\n" +
                "1. Agregar Hoja: Añade una nueva hoja de cálculo al libro.\n" +
                "2. Eliminar Hoja: Elimina la hoja de cálculo actualmente seleccionada.\n" +
                "3. Cambiar Nombre: Permite cambiar el nombre del archivo del libro de cálculo.\n" +
                "4. Guardar: Guarda el libro de cálculo en un archivo.\n" +
                "5. Inicio: Vuelve a la pantalla de inicio. Se le preguntará si desea guardar los cambios antes de regresar.\n" +
                "6. Salir: Cierra la aplicación. Se le preguntará si desea guardar los cambios antes de salir.\n" +
                "7. Manual de Usuario: Muestra este manual de usuario.\n\n" +
                "Operaciones de Celdas:\n" +
                "- Para realizar operaciones matemáticas en las celdas, ingrese una fórmula comenzando con '='.\n" +
                "  Por ejemplo, '=suma(F1C1,F2C2)' sumará los valores de las celdas F1C1 y F2C2.\n" +
                "- Operaciones soportadas: suma, resta, mult (multiplicación), div (división).\n";

        JOptionPane.showMessageDialog(this, manual, "Manual de Usuario", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Retorna la lista de hojas de cálculo en el libro.
     * @return Lista de hojas de cálculo
     */
    public List<HojaDeCalculo> getHojas() {
        return hojas;
    }

    /**
     * Retorna el nombre del archivo donde se guarda el libro de cálculo.
     * @return Nombre del archivo
     */
    public String getNombreArchivo() {
        return nombreArchivo;
    }

    /**
     * Establece el nombre del archivo donde se guarda el libro de cálculo.
     * @param nombreArchivo Nuevo nombre del archivo
     */
    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
}

