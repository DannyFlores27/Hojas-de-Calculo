
package programacioniii.hojasdecalculo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

/**
 * Esta clase representa una matriz ortogonal que puede contener celdas con valores.
 * Proporciona métodos para insertar, obtener y eliminar celdas, así como para realizar operaciones matemáticas.
 * También puede guardar y cargar matrices ortogonales desde archivos JSON.
 */
public class MatrizOrtogonal implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int CAPACIDAD_INICIAL = 200;

    private Nodo[] tablaHash;

    /**
     * Constructor de la clase MatrizOrtogonal.
     * Inicializa la tabla hash con una capacidad inicial predefinida.
     */
    public MatrizOrtogonal() {
        this.tablaHash = new Nodo[CAPACIDAD_INICIAL];
    }

    /**
     * Calcula el hash para una celda en la matriz.
     * @param fila Fila de la celda
     * @param columna Columna de la celda
     * @return Valor hash para la celda
     */
    private int hash(int fila, int columna) {
        int hash = 17;
        hash = 31 * hash + fila;
        hash = 31 * hash + columna;
        return Math.abs(hash) % CAPACIDAD_INICIAL;
    }

    /**
     * Genera un identificador único para una celda en la matriz.
     * @param fila Fila de la celda
     * @param columna Columna de la celda
     * @return Identificador único de la celda
     */
    private String generarId(int fila, int columna) {
        return "F" + fila + "C" + columna;
    }

    /**
     * Inserta una nueva celda con un valor en la matriz.
     * @param fila Fila de la celda
     * @param columna Columna de la celda
     * @param valor Valor a insertar en la celda
     */
    public void insertarCelda(int fila, int columna, Object valor) {
        int indice = hash(fila, columna);
        String id = generarId(fila, columna);
        Celda nuevaCelda = new Celda(id);
        nuevaCelda.setValor(valor);

        Nodo nuevoNodo = new Nodo(nuevaCelda);

        if (tablaHash[indice] == null) {
            tablaHash[indice] = nuevoNodo;
        } else {
            Nodo actual = tablaHash[indice];
            while (actual != null) {
                if (actual.getCelda().getId().equals(id)) {
                    actual.getCelda().setValor(valor); // Actualiza el valor si la celda ya existe
                    return;
                }
                if (actual.getSiguiente() == null) {
                    break;
                }
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevoNodo); // Añade nuevo nodo si no se encontró la celda existente
        }
    }

    /**
     * Obtiene el valor de una celda en la matriz.
     * @param fila Fila de la celda
     * @param columna Columna de la celda
     * @return Valor de la celda, o null si la celda no existe
     */
    public Object obtenerCelda(int fila, int columna) {
        int indice = hash(fila, columna);
        Nodo actual = tablaHash[indice];

        while (actual != null) {
            if (actual.getCelda().getId().equals(generarId(fila, columna))) {
                return actual.getCelda().getValor();
            }
            actual = actual.getSiguiente();
        }

        return null;
    }

    /**
     * Elimina una celda de la matriz.
     * @param fila Fila de la celda a eliminar
     * @param columna Columna de la celda a eliminar
     */
    public void eliminarCelda(int fila, int columna) {
        int indice = hash(fila, columna);
        Nodo actual = tablaHash[indice];
        Nodo anterior = null;

        while (actual != null && !actual.getCelda().getId().equals(generarId(fila, columna))) {
            anterior = actual;
            actual = actual.getSiguiente();
        }

        if (actual != null) {
            if (anterior == null) {
                tablaHash[indice] = actual.getSiguiente();
            } else {
                anterior.setSiguiente(actual.getSiguiente());
            }
        }
    }

    /**
     * Realiza una operación matemática entre dos celdas de la matriz.
     * @param fila1 Fila de la primera celda
     * @param columna1 Columna de la primera celda
     * @param fila2 Fila de la segunda celda
     * @param columna2 Columna de la segunda celda
     * @param operacion Operación a realizar ("sumar", "restar", "multiplicar", "dividir")
     * @return Resultado de la operación, o un mensaje de error si la operación no se puede realizar
     */
    public String realizarOperacion(int fila1, int columna1, int fila2, int columna2, String operacion) {
        Object valor1 = obtenerCelda(fila1, columna1);
        Object valor2 = obtenerCelda(fila2, columna2);

        if (valor1 instanceof Number && valor2 instanceof Number) {
            double num1 = ((Number) valor1).doubleValue();
            double num2 = ((Number) valor2).doubleValue();

            switch (operacion) {
                case "sumar":
                    return String.valueOf(num1 + num2);
                case "restar":
                    return String.valueOf(num1 - num2);
                case "multiplicar":
                    return String.valueOf(num1 * num2);
                case "dividir":
                    if (num2 == 0) {
                        return "Error: División por cero";
                    }
                    return String.valueOf(num1 / num2);
                default:
                    return "Operación no soportada";
            }
        } else {
            return "Error: Ambos valores deben ser numéricos";
        }
    }

    /**
     * Guarda la matriz ortogonal en un archivo JSON.
     * @param nombreArchivo Nombre del archivo donde se guardará la matriz
     * @throws IOException Si ocurre un error al escribir en el archivo
     */
    public void guardarEnArchivo(String nombreArchivo) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this);

        FileWriter writer = null;
        try {
            writer = new FileWriter(nombreArchivo);
            writer.write(json);
                    } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Carga una matriz ortogonal desde un archivo JSON.
     * @param nombreArchivo Nombre del archivo desde donde se cargará la matriz
     * @return La matriz ortogonal cargada desde el archivo
     * @throws IOException Si ocurre un error al leer el archivo
     */
    public static MatrizOrtogonal cargarDesdeArchivo(String nombreArchivo) throws IOException {
        Gson gson = new Gson();
        MatrizOrtogonal matriz = null;

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(nombreArchivo));
            matriz = gson.fromJson(reader, MatrizOrtogonal.class);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return matriz;
    }
}
