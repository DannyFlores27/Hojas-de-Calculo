import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        MatrizOrtogonal matriz = new MatrizOrtogonal();
        matriz.insertarCelda(1, 1, 10);
        matriz.insertarCelda(1, 2, 20.5);
        matriz.insertarCelda(2, 1, "Texto no numérico");

        // Guardar la matriz en un archivo
        try {
            matriz.guardarEnArchivo("matriz.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Cargar la matriz desde el archivo
        MatrizOrtogonal matrizCargada = null;
        try {
            matrizCargada = MatrizOrtogonal.cargarDesdeArchivo("matriz.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Realizar operaciones con la matriz cargada
        if (matrizCargada != null) {
            System.out.println(matrizCargada.realizarOperacion(1, 1, 1, 2, "sumar")); // 30.5
            System.out.println(matrizCargada.realizarOperacion(1, 1, 2, 1, "sumar")); // Error: Ambos valores deben ser numéricos
            System.out.println(matrizCargada.realizarOperacion(1, 1, 1, 2, "dividir")); // 0.4878048780487805
        }

        matriz.insertarCelda(11, 11, 110);
        matriz.insertarCelda(1, 2, "Texto no numérico");
        matriz.insertarCelda(12, 11, "Texto no numérico");

        // Guardar la matriz en un archivo
        try {
            matriz.guardarEnArchivo("matriz.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Cargar la matriz desde el archivo
        matrizCargada = null;
        try {
            matrizCargada = MatrizOrtogonal.cargarDesdeArchivo("matriz.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Realizar operaciones con la matriz cargada
        if (matrizCargada != null) {
            System.out.println(matrizCargada.realizarOperacion(11, 11, 1, 2, "sumar")); // 160.0
            System.out.println(matrizCargada.realizarOperacion(11, 11, 12, 11, "sumar")); // Error: Ambos valores deben ser numéricos
            System.out.println(matrizCargada.realizarOperacion(11, 11, 1, 2, "dividir")); // 2.2
        }
    }
}
