/**
 * Interface: define el CONTRATO de algo que se puede prestar.
 * Cualquier clase que implemente Prestable DEBE tener estos métodos.
 */
public interface Prestable {
    boolean estaDisponible();
    void marcarPrestado();
    void marcarDevuelto();
}
