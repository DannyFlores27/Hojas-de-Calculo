import java.io.Serializable;

class Celda implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private Object valor;

    public Celda(String id) {
        this.id = id;
        this.valor = null;
    }

    public String getId() {
        return id;
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }
}
