# Ejercicios y Retos - Proyecto 02: Sistema Biblioteca

## Reto 1: Agregar tipo DVD
Crea una clase `DVD` que extienda `MaterialBiblioteca`:
- Atributos propios: duracionMinutos, director
- Implementa `getDescripcion()` y `getTipo()`
- Agrégalo al Main y préstalo

**Lo que practicas:** Herencia, implementar métodos abstractos

---

## Reto 2: Límite de préstamos
Modifica `Biblioteca.prestar()` para que un usuario no pueda tener más de 3 préstamos activos simultáneamente.

**Pista:** Usa Stream para contar cuántos préstamos tiene el usuario:
```java
long prestamosUsuario = prestamosActivos.stream()
    .filter(p -> p.usuario().id() == usuarioId)
    .count();
```

**Lo que practicas:** Validaciones con Stream API

---

## Reto 3: Multas por retraso
Agrega un método `calcularMulta(String identificador, int usuarioId)`:
- Si el préstamo está vencido, cobrar $10 por cada día de retraso
- Usar `ChronoUnit.DAYS.between(vencimiento, LocalDate.now())`

**Lo que practicas:** java.time, cálculos con fechas

---

## Reto 4: Búsqueda avanzada
Crea un método `buscarPorCategoria(String tipo)` que devuelva solo Libros o solo Revistas:
```java
// Usar instanceof pattern matching (Java 16+)
if (material instanceof Libro libro) {
    // libro.getPaginas() disponible aquí
}
```

**Lo que practicas:** Pattern matching, instanceof, polimorfismo

---

## Reto 5: Interface Buscable
Crea una nueva interface `Buscable`:
```java
public interface Buscable {
    boolean coincideCon(String termino);
}
```
Haz que `MaterialBiblioteca` la implemente. Cada hijo define su propia lógica de búsqueda:
- Libro busca por título, autor o ISBN
- Revista busca por título, editorial o edición

**Lo que practicas:** Múltiples interfaces, polimorfismo

---

## Reto 6: Historial de préstamos
Crea un `List<Prestamo> historialCompleto` que guarde TODOS los préstamos (activos + devueltos).
Agrega un método `historialUsuario(int usuarioId)` que muestre todo lo que un usuario ha pedido prestado.

**Lo que practicas:** Colecciones, filtrado con Stream

---

## Reto 7: Exportar catálogo
Agrega un método que imprima el catálogo en formato CSV:
```
TIPO,TITULO,AUTOR,IDENTIFICADOR,DISPONIBLE
LIBRO,Clean Code,Robert Martin,978-0132350884,true
REVISTA,IEEE Software,IEEE,REV-Ene2026,false
```

**Lo que practicas:** String.format, iteración de colecciones

---

## Reto 8 (Avanzado): Reservas
Implementa un sistema de reservas:
- Si un material está prestado, el usuario puede "reservarlo"
- Cuando se devuelve, se asigna automáticamente al primer usuario en la cola de reservas
- Usa `Queue<Usuario>` (LinkedList) por cada material reservado

**Lo que practicas:** Queue, Map<String, Queue<Usuario>>, lógica de negocio compleja
