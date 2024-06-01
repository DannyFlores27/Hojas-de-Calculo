
package programacioniii.hojasdecalculo;

/**
 * La clase Nodo representa un nodo en una estructura de datos de lista enlazada.
 * Cada nodo contiene una referencia a una celda y a otro nodo siguiente en la lista.
 */
import java.io.Serializable;

public class Nodo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Celda celda;
    private Nodo siguiente;

    /**
     * Constructor de la clase Nodo.
     *
     * @param celda La celda asociada con este nodo.
     */
    public Nodo(Celda celda) {
        this.celda = celda;
        this.siguiente = null;
    }

    /**
     * Obtiene la celda asociada con este nodo.
     *
     * @return La celda asociada con este nodo.
     */
    public Celda getCelda() {
        return celda;
    }

    /**
     * Establece el siguiente nodo en la lista.
     *
     * @param siguiente El siguiente nodo en la lista.
     */
    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }

    /**
     * Obtiene el siguiente nodo en la lista.
     *
     * @return El siguiente nodo en la lista.
     */
    public Nodo getSiguiente() {
        return siguiente;
    }
}