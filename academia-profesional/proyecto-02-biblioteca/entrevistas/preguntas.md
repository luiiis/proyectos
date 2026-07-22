# Preguntas de Entrevista - Tema: POO en Java

## Nivel Junior

### 1. ¿Cuáles son los 4 pilares de la POO? Explica cada uno con un ejemplo.
**Respuesta:**
- **Abstracción**: Representar solo lo esencial. `MaterialBiblioteca` abstrae las características comunes de libros y revistas (titulo, autor) sin importar los detalles específicos.
- **Encapsulamiento**: Ocultar datos internos. Los atributos son `private`, solo se acceden con getters. Nadie puede poner `disponible = true` directamente, debe usar `marcarDevuelto()`.
- **Herencia**: Una clase hereda comportamiento de otra. `Libro extends MaterialBiblioteca` → Libro tiene todo lo de Material + páginas.
- **Polimorfismo**: Tratar objetos diferentes de forma genérica. Un `List<MaterialBiblioteca>` puede contener Libros y Revistas mezclados, y llamar `getDescripcion()` funciona diferente para cada uno.

---

### 2. ¿Cuál es la diferencia entre clase abstracta e interface?
**Respuesta:**

| Característica | Clase Abstracta | Interface |
|---------------|----------------|-----------|
| Herencia | Solo una (extends) | Múltiples (implements) |
| Campos | Puede tener atributos | Solo constantes (static final) |
| Constructores | Sí | No |
| Métodos | Abstractos + concretos | Abstractos + default (Java 8+) |
| Cuándo usarla | "Es un tipo de" (Libro ES UN Material) | "Puede hacer" (Material PUEDE ser Prestable) |

---

### 3. ¿Qué es un Record? ¿Cuándo usarlo y cuándo NO?
**Respuesta:**
Record es una clase inmutable que autogenera: constructor, getters (sin get), equals, hashCode, toString.

**Usar cuando:** datos que no cambian (DTOs, eventos, resultados de query).
**NO usar cuando:** necesitas mutabilidad (como `MaterialBiblioteca` que cambia `disponible`).

---

### 4. ¿Por qué `MaterialBiblioteca` es abstracta y no una clase normal?
**Respuesta:**
Porque no tiene sentido crear un "MaterialBiblioteca" genérico. ¿Qué sería su `getDescripcion()`? No se sabe hasta que sea Libro o Revista. El `abstract` obliga a los hijos a implementarlo y prohíbe instanciar la clase padre directamente.

---

### 5. ¿Qué pasa si quito `@Override` de `getDescripcion()` en Libro?
**Respuesta:**
Compila igual, pero pierdes validación en compilación. Si escribes mal el nombre del método (ej: `getdescripcion`), sin `@Override` Java lo trata como un método NUEVO en vez de darte error. Con `@Override`, el compilador verifica que realmente estás sobreescribiendo algo del padre.

---

## Nivel Mid

### 6. Si necesitas agregar "DVD" y "Videojuego" como materiales prestables, ¿qué cambios necesitas?
**Respuesta:**
1. Crear `DVD extends MaterialBiblioteca` (implementar getDescripcion, getTipo)
2. Crear `Videojuego extends MaterialBiblioteca` (lo mismo)
3. **CERO cambios** en Biblioteca, Prestamo, o Main (ya trabajan con MaterialBiblioteca genérico)

Esto es el **Open/Closed Principle**: abierto para extensión, cerrado para modificación.

---

### 7. ¿Por qué se usa `Map<String, MaterialBiblioteca>` y no `List<MaterialBiblioteca>` para el catálogo?
**Respuesta:**
- Map: búsqueda por ISBN/identificador en O(1) → `catalogo.get("978-0132350884")`
- List: buscar requiere recorrer todo → O(n) → con 100,000 libros es 100,000x más lento
- El catálogo se consulta constantemente (cada préstamo, cada devolución), la performance importa.

---

### 8. ¿Qué problema tiene este código?
```java
for (Prestamo p : prestamosActivos) {
    if (p.estaVencido()) {
        prestamosActivos.remove(p);  // ← PROBLEMA
    }
}
```
**Respuesta:**
`ConcurrentModificationException`. No puedes modificar una lista mientras la recorres con for-each. Soluciones:
- `prestamosActivos.removeIf(Prestamo::estaVencido);`
- Usar Iterator con `iterator.remove()`
- Crear nueva lista filtrada con Stream

---

### 9. ¿Qué es "composición sobre herencia"? ¿Cuándo preferirla?
**Respuesta:**
En vez de heredar (`class A extends B`), contener (`class A { private B b; }`).

Preferir composición cuando:
- La relación no es "es un" sino "tiene un" (Biblioteca TIENE materiales, no ES un material)
- Necesitas cambiar comportamiento en runtime
- La herencia crea jerarquías profundas difíciles de mantener

En este proyecto: `Biblioteca` usa composición (tiene un HashMap de materiales), no herencia.

---

### 10. ¿Cómo implementarías un sistema de "plugins" para nuevos tipos de material sin modificar código existente?
**Respuesta:**
Con interfaces y el patrón Strategy/Factory:
```java
// Registrar tipos dinámicamente
Map<String, Supplier<MaterialBiblioteca>> fabricas = new HashMap<>();
fabricas.put("LIBRO", () -> new Libro(...));
fabricas.put("DVD", () -> new DVD(...));

// Crear material según tipo (sin if/else ni switch)
MaterialBiblioteca material = fabricas.get(tipo).get();
```
Esto es **programar contra interfaces, no contra implementaciones**.
