import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class MatrizOrtogonal implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int CAPACIDAD_INICIAL = 200;

    private Nodo[] tablaHash;

    public MatrizOrtogonal() {
        this.tablaHash = new Nodo[CAPACIDAD_INICIAL];
    }

    private int hash(int fila, int columna) {
        int hash = 17;
        hash = 31 * hash + fila;
        hash = 31 * hash + columna;
        return Math.abs(hash) % CAPACIDAD_INICIAL;
    }

    private String generarId(int fila, int columna) {
        return "F" + fila + "C" + columna;
    }

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
