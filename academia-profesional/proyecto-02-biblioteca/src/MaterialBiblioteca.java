/**
 * Clase ABSTRACTA: define la estructura base de cualquier material.
 * No se puede instanciar directamente.
 * Las clases hijas (Libro, Revista) DEBEN implementar getDescripcion().
 */
public abstract class MaterialBiblioteca implements Prestable {
    private final String titulo;
    private final String autor;
    private final String identificador;
    private boolean disponible;

    protected MaterialBiblioteca(String titulo, String autor, String identificador) {
        this.titulo = titulo;
        this.autor = autor;
        this.identificador = identificador;
        this.disponible = true;
    }

    // Método abstracto: cada tipo de material lo implementa diferente
    public abstract String getDescripcion();
    public abstract String getTipo();

    // Implementación de interface Prestable
    @Override
    public boolean estaDisponible() { return disponible; }

    @Override
    public void marcarPrestado() { this.disponible = false; }

    @Override
    public void marcarDevuelto() { this.disponible = true; }

    // Getters
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getIdentificador() { return identificador; }

    @Override
    public String toString() {
        String estado = disponible ? "✓ Disponible" : "✗ Prestado";
        return String.format("[%s] %s - %s (%s)", getTipo(), titulo, autor, estado);
    }
}
