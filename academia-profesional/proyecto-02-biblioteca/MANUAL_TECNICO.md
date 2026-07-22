# Manual Técnico - Proyecto 02: Sistema Biblioteca

## Objetivo de Aprendizaje
Dominar los 4 pilares de la Programación Orientada a Objetos (POO) construyendo un sistema real:
- **Abstracción**: ModelMaterialBiblioteca define QUÉ es un material, sin importar si es Libro o Revista
- **Encapsulamiento**: Atributos privados, acceso controlado con getters
- **Herencia**: Libro y Revista heredan de MaterialBiblioteca
- **Polimorfismo**: Tratar Libro y Revista como "MaterialBiblioteca" genérico

---

## Arquitectura

```
┌─────────────────────────────────────────────────────────────┐
│                         Main.java                            │
│  (Punto de entrada: crea objetos y ejecuta operaciones)     │
└───────────────────────────┬─────────────────────────────────┘
                            │ usa
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                      Biblioteca.java                         │
│  (Controlador: orquesta catálogo, usuarios, préstamos)      │
│                                                             │
│  HashMap<String, MaterialBiblioteca> catalogo               │
│  HashMap<Integer, Usuario> usuarios                         │
│  ArrayList<Prestamo> prestamosActivos                       │
└───────┬───────────────────┬─────────────────┬───────────────┘
        │                   │                 │
        ▼                   ▼                 ▼
┌──────────────┐    ┌──────────────┐   ┌──────────────┐
│   Libro.java │    │ Revista.java │   │ Prestamo.java│
│  extends     │    │  extends     │   │  (record)    │
│  Material    │    │  Material    │   │  material    │
│  Biblioteca  │    │  Biblioteca  │   │  usuario     │
│  + paginas   │    │  + edicion   │   │  fecha       │
│              │    │  + articulos │   │  vencimiento │
└──────┬───────┘    └──────┬───────┘   └──────────────┘
       │                   │
       ▼                   ▼
┌─────────────────────────────────────────────────────────────┐
│              MaterialBiblioteca.java (ABSTRACTA)            │
│  titulo, autor, identificador, disponible                   │
│  abstract getDescripcion()                                  │
│  abstract getTipo()                                         │
│  implements Prestable                                       │
└───────────────────────────┬─────────────────────────────────┘
                            │ implements
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                   Prestable.java (INTERFACE)                 │
│  estaDisponible()                                           │
│  marcarPrestado()                                           │
│  marcarDevuelto()                                           │
└─────────────────────────────────────────────────────────────┘

┌──────────────┐
│ Usuario.java │  (record: id, nombre, email)
└──────────────┘
```

---

## Descripción de Cada Archivo

| Archivo | Tipo | Responsabilidad |
|---------|------|-----------------|
| `Prestable.java` | Interface | Contrato: define qué métodos debe tener algo "prestable" |
| `MaterialBiblioteca.java` | Clase abstracta | Estructura común de libros y revistas, implementa Prestable |
| `Libro.java` | Clase concreta | Hereda de Material, agrega: páginas |
| `Revista.java` | Clase concreta | Hereda de Material, agrega: edición, artículos |
| `Usuario.java` | Record | Datos inmutables de un usuario (id, nombre, email) |
| `Prestamo.java` | Record | Registro de préstamo (material, usuario, fecha, vencimiento) |
| `Biblioteca.java` | Clase | Controlador: administra catálogo, usuarios y préstamos |
| `Main.java` | Clase | Punto de entrada, demuestra toda la funcionalidad |

---

## Flujo de Ejecución

```
1. Main crea una Biblioteca("Biblioteca Central")
2. Se agregan materiales al catálogo (HashMap por identificador)
3. Se registran usuarios (HashMap por ID)
4. Se realizan préstamos:
   - Verificar que el material existe
   - Verificar que el usuario existe
   - Verificar que está disponible
   - Marcar como prestado → agregar Prestamo a la lista
5. Se realizan devoluciones:
   - Buscar el préstamo activo (Stream + filter)
   - Marcar material como devuelto
   - Remover de la lista de activos
6. Búsqueda por título o autor (Stream + filter + contains)
7. Estadísticas: contar por tipo con instanceof
```

---

## Conceptos Clave Explicados

### ¿Por qué interface y NO clase abstracta para Prestable?
```
Interface = CONTRATO puro (dice QUÉ, no CÓMO)
  → Cualquier cosa puede ser "Prestable": un libro, un DVD, un juego de mesa
  → No impone estructura (no dice "tienes que tener título y autor")

Clase abstracta = ESTRUCTURA base con código compartido
  → MaterialBiblioteca comparte: titulo, autor, identificador, disponible
  → Define comportamiento por defecto (estaDisponible, marcarPrestado)
```

### ¿Por qué Record para Usuario y Prestamo?
```
Record = clase inmutable autogenerada
  → Genera: constructor, getters, equals(), hashCode(), toString()
  → Perfecto para datos que NO cambian después de crearse
  → Usuario: una vez creado con id/nombre/email, esos datos no se modifican
  → Prestamo: una vez registrado, la fecha no cambia
```

### ¿Por qué HashMap y no ArrayList para el catálogo?
```
HashMap<String, Material>:
  → Buscar por ISBN es O(1) (instantáneo)
  → catalogo.get("978-0132350884") → directo al resultado

ArrayList<Material>:
  → Buscar por ISBN sería O(n) (recorrer toda la lista)
  → Con 10,000 libros, HashMap es 10,000x más rápido
```

---

## Estructura de Archivos
```
proyecto-02-biblioteca/
├── src/
│   ├── Prestable.java           ← Interface (contrato)
│   ├── MaterialBiblioteca.java  ← Clase abstracta (padre)
│   ├── Libro.java               ← Hereda de Material
│   ├── Revista.java             ← Hereda de Material
│   ├── Usuario.java             ← Record (datos usuario)
│   ├── Prestamo.java            ← Record (datos préstamo)
│   ├── Biblioteca.java          ← Controlador
│   └── Main.java                ← Punto de entrada
├── ejercicios/
│   └── retos.md                 ← Ejercicios prácticos
├── entrevistas/
│   └── preguntas.md             ← Preguntas de entrevista
├── README.md                    ← Descripción general
├── CONSTRUCCION.md              ← Bitácora de decisiones
├── COMO_EJECUTAR.md             ← Instrucciones de ejecución
└── MANUAL_TECNICO.md            ← Este archivo
```
