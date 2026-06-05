public class Revista extends MaterialBiblioteca {
    private final String edicion;
    private final int articulos;

    public Revista(String titulo, String editorial, String edicion, int articulos) {
        super(titulo, editorial, "REV-" + edicion.replace(" ", ""));
        this.edicion = edicion;
        this.articulos = articulos;
    }

    @Override
    public String getDescripcion() {
        return String.format("Revista: %s, Edición %s (%d artículos)", getTitulo(), edicion, articulos);
    }

    @Override
    public String getTipo() { return "REVISTA"; }
}
