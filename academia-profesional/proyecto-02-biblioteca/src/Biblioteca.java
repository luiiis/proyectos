import java.util.*;
import java.util.stream.Collectors;

public class Biblioteca {
    private final String nombre;
    private final Map<String, MaterialBiblioteca> catalogo = new HashMap<>();
    private final Map<Integer, Usuario> usuarios = new HashMap<>();
    private final List<Prestamo> prestamosActivos = new ArrayList<>();

    public Biblioteca(String nombre) { this.nombre = nombre; }

    public void agregar(MaterialBiblioteca material) {
        catalogo.put(material.getIdentificador(), material);
    }

    public void registrarUsuario(Usuario usuario) {
        usuarios.put(usuario.id(), usuario);
    }

    public void prestar(String identificador, int usuarioId) {
        MaterialBiblioteca material = catalogo.get(identificador);
        Usuario usuario = usuarios.get(usuarioId);

        if (material == null) { System.out.println("  ✗ Material no encontrado: " + identificador); return; }
        if (usuario == null) { System.out.println("  ✗ Usuario no encontrado: " + usuarioId); return; }
        if (!material.estaDisponible()) { System.out.println("  ✗ '" + material.getTitulo() + "' no está disponible"); return; }

        material.marcarPrestado();
        prestamosActivos.add(new Prestamo(material, usuario));
        System.out.printf("  ✓ '%s' prestado a %s%n", material.getTitulo(), usuario.nombre());
    }

    public void devolver(String identificador, int usuarioId) {
        var prestamo = prestamosActivos.stream()
            .filter(p -> p.material().getIdentificador().equals(identificador) && p.usuario().id() == usuarioId)
            .findFirst();

        if (prestamo.isPresent()) {
            prestamo.get().material().marcarDevuelto();
            prestamosActivos.remove(prestamo.get());
            System.out.printf("  ✓ '%s' devuelto por %s%n", prestamo.get().material().getTitulo(), prestamo.get().usuario().nombre());
        } else {
            System.out.println("  ✗ Préstamo no encontrado");
        }
    }

    public void buscar(String termino) {
        var resultados = catalogo.values().stream()
            .filter(m -> m.getTitulo().toLowerCase().contains(termino.toLowerCase()) ||
                         m.getAutor().toLowerCase().contains(termino.toLowerCase()))
            .toList();
        System.out.println("Resultados para '" + termino + "': " + resultados.size());
        resultados.forEach(m -> System.out.println("  " + m));
    }

    public void mostrarCatalogo() {
        System.out.println("── Catálogo: " + nombre + " (" + catalogo.size() + " materiales) ──");
        catalogo.values().forEach(m -> System.out.println("  " + m));
    }

    public void mostrarPrestamos() {
        System.out.println("\n── Préstamos activos: " + prestamosActivos.size() + " ──");
        prestamosActivos.forEach(System.out::println);
    }

    public void estadisticas() {
        long libros = catalogo.values().stream().filter(m -> m instanceof Libro).count();
        long revistas = catalogo.values().stream().filter(m -> m instanceof Revista).count();
        long prestados = catalogo.values().stream().filter(m -> !m.estaDisponible()).count();
        System.out.printf("%n── Estadísticas ──%nLibros: %d | Revistas: %d | Prestados: %d | Usuarios: %d%n",
            libros, revistas, prestados, usuarios.size());
    }
}
