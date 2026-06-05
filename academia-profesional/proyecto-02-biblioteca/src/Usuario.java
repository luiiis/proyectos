public record Usuario(int id, String nombre, String email) {
    @Override
    public String toString() {
        return String.format("[%d] %s (%s)", id, nombre, email);
    }
}
