import java.time.LocalDate;

public record Prestamo(MaterialBiblioteca material, Usuario usuario, LocalDate fecha, LocalDate vencimiento) {
    public Prestamo(MaterialBiblioteca material, Usuario usuario) {
        this(material, usuario, LocalDate.now(), LocalDate.now().plusDays(14));
    }

    public boolean estaVencido() { return LocalDate.now().isAfter(vencimiento); }

    @Override
    public String toString() {
        String estado = estaVencido() ? "⚠️ VENCIDO" : "✓ Vigente";
        return String.format("  %s → %s (vence: %s) %s", material.getTitulo(), usuario.nombre(), vencimiento, estado);
    }
}
