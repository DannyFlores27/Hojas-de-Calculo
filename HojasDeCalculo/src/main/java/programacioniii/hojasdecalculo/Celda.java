
package programacioniii.hojasdecalculo;

/**
 * La clase Celda representa una celda en una estructura de datos de lista enlazada.
 * Cada celda tiene un identificador único y un valor asociado.
 */
import java.io.Serializable;

class Celda implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private Object valor;

    /**
     * Constructor de la clase Celda.
     *
     * @param id El identificador único de la celda.
     */
    public Celda(String id) {
        this.id = id;
        this.valor = null;
    }

    /**
     * Obtiene el identificador único de la celda.
     *
     * @return El identificador único de la celda.
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el valor almacenado en la celda.
     *
     * @return El valor almacenado en la celda.
     */
    public Object getValor() {
        return valor;
    }

    /**
     * Establece el valor de la celda.
     *
     * @param valor El valor a ser almacenado en la celda.
     */
    public void setValor(Object valor) {
        this.valor = valor;
    }
}
