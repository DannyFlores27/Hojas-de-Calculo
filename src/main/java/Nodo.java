import java.io.Serializable;

public class Nodo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Celda celda;
    private Nodo siguiente;

    public Nodo(Celda celda) {
        this.celda = celda;
        this.siguiente = null;
    }

    public Celda getCelda() {
        return celda;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }

    public Nodo getSiguiente() {
        return siguiente;
    }
}
