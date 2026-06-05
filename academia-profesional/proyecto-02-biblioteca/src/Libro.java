/**
 * HERENCIA: Libro extiende MaterialBiblioteca.
 * Agrega: páginas. Implementa getDescripcion() de forma específica.
 */
public class Libro extends MaterialBiblioteca {
    private final int paginas;

    public Libro(String titulo, String autor, String isbn, int paginas) {
        super(titulo, autor, isbn);
        this.paginas = paginas;
    }

    @Override
    public String getDescripcion() {
        return String.format("Libro: %s por %s (%d págs)", getTitulo(), getAutor(), paginas);
    }

    @Override
    public String getTipo() { return "LIBRO"; }

    public int getPaginas() { return paginas; }
}
