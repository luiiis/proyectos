# Git Workflow — Lo que necesitas para trabajar profesionalmente

## ¿Por qué Git?
Git guarda CADA versión de tu código. Si rompes algo, vuelves atrás. Si trabajas en equipo, cada quien trabaja en su rama y se integra sin conflictos.

---

## 1. Conceptos Clave

```
Working Directory → Staging Area → Local Repository → Remote (GitHub)
(tu código)         (git add)       (git commit)        (git push)
```

| Concepto | Qué es | Analogía |
|----------|--------|----------|
| Repository | Carpeta con historial Git | Un álbum de fotos |
| Commit | Una foto del código en un momento | Una página del álbum |
| Branch | Una línea paralela de desarrollo | Una copia del documento |
| Merge | Unir dos ramas | Combinar dos versiones |
| Remote | Copia en la nube (GitHub) | Google Drive de tu código |
| Clone | Descargar un repo remoto | Bajar una copia |
| Pull | Traer cambios de GitHub | Sincronizar |
| Push | Subir cambios a GitHub | Publicar |

---

## 2. Configuración Inicial (una sola vez)

```cmd
git config --global user.name "Tu Nombre"
git config --global user.email "tu@email.com"

:: Ver configuración
git config --list
```

---

## 3. Flujo Básico Diario

```cmd
:: 1. Ver qué cambió
git status

:: 2. Agregar archivos al staging
git add .                    :: todos los cambios
git add src/ProductoService.java   :: un archivo específico

:: 3. Hacer commit (guardar versión)
git commit -m "feat: agregar búsqueda por nombre"

:: 4. Subir a GitHub
git push
```

---

## 4. Commits Convencionales

Cada commit debe tener un mensaje claro que explique QUÉ hiciste:

```
tipo: descripción corta (máx 70 caracteres)
```

| Tipo | Cuándo | Ejemplo |
|------|--------|---------|
| `feat:` | Funcionalidad nueva | `feat: agregar endpoint de categorías` |
| `fix:` | Corregir un bug | `fix: resolver error 500 al buscar por ID nulo` |
| `refactor:` | Mejorar código sin cambiar funcionalidad | `refactor: extraer validaciones a método privado` |
| `docs:` | Documentación | `docs: agregar COMO_EJECUTAR del nivel 5` |
| `test:` | Agregar o modificar tests | `test: agregar tests del ProductoService` |
| `chore:` | Tareas de mantenimiento | `chore: actualizar dependencias del pom.xml` |
| `style:` | Formato (espacios, comas, etc.) | `style: formatear código del controller` |

### Ejemplos buenos vs malos
```
❌ "cambios"
❌ "fix"
❌ "actualización"
❌ "asdfasdf"

✅ "feat: crear CRUD de productos con MyBatis"
✅ "fix: corregir query de búsqueda por categoría"
✅ "docs: documentar endpoints de autenticación"
✅ "refactor: separar lógica de paginación en método"
```

---

## 5. Branches (Ramas)

```cmd
:: Ver ramas
git branch

:: Crear una rama nueva y moverte a ella
git checkout -b feature/crud-categorias

:: Cambiar a una rama existente
git checkout main
git checkout feature/crud-categorias

:: Ver todas (locales + remotas)
git branch -a
```

### Nomenclatura de ramas
```
main                      → código estable (producción)
develop                   → desarrollo activo
feature/crud-productos    → funcionalidad nueva
feature/login-jwt         → otra funcionalidad
fix/error-paginacion      → corrección de bug
hotfix/seguridad-token    → corrección urgente en producción
```

### Flujo de ramas (Git Flow simplificado)
```
main ─────────────────────────────────────────────── (estable)
  └── develop ────────────────────────────────────── (desarrollo)
        ├── feature/crud-productos ──→ merge a develop
        ├── feature/login-jwt ──→ merge a develop
        └── fix/error-500 ──→ merge a develop ──→ merge a main
```

---

## 6. Merge (unir ramas)

```cmd
:: Estás en feature/crud-categorias y terminaste
:: Primero: volver a main
git checkout main

:: Traer últimos cambios de GitHub
git pull

:: Unir tu rama a main
git merge feature/crud-categorias

:: Subir a GitHub
git push

:: Borrar la rama (ya no la necesitas)
git branch -d feature/crud-categorias
```

---

## 7. Resolver Conflictos

Un conflicto ocurre cuando 2 personas modificaron la MISMA línea del MISMO archivo.

```
<<<<<<< HEAD
    private BigDecimal precio;     ← TU versión
=======
    private double precio;          ← versión del OTRO
>>>>>>> feature/otra-rama
```

### Cómo resolverlo:
1. Abrir el archivo conflictivo
2. Elegir qué versión quieres (o combinar ambas)
3. Borrar las marcas `<<<`, `===`, `>>>`
4. Guardar
5. `git add .`
6. `git commit -m "fix: resolver conflicto en Producto.java"`

---

## 8. Pull Requests (GitHub)

En equipo, NUNCA haces merge directo a main. Usas Pull Requests:

```
1. Crear rama: git checkout -b feature/nueva-funcionalidad
2. Trabajar: hacer commits
3. Subir: git push -u origin feature/nueva-funcionalidad
4. En GitHub: abrir Pull Request
5. Alguien revisa tu código
6. Si está bien → Merge
7. Si no → te dejan comentarios → corriges → push → se actualiza
```

### Estructura de un Pull Request
```
Título: feat: agregar CRUD de categorías

Descripción:
## Qué hice
- Creé CategoriaMapper + XML con queries
- Creé CategoriaService con validación de duplicados
- Creé CategoriaController con 6 endpoints

## Cómo probar
1. Ejecutar la app
2. curl http://localhost:8080/api/categorias
3. Crear: curl -X POST ... (ver COMO_EJECUTAR)

## Screenshots (si aplica)
(captura de Postman)
```

---

## 9. .gitignore

Archivos que NO deben subirse a GitHub:

```gitignore
# Compilados Java
target/
*.class
*.jar

# IDE
.idea/
*.iml
.vscode/

# Node
node_modules/
dist/

# Configuración local
.env
application-local.yml

# Logs
*.log

# OS
.DS_Store
Thumbs.db
```

---

## 10. Comandos Útiles

```cmd
:: Ver historial bonito
git log --oneline --graph

:: Ver cambios antes de commitear
git diff

:: Deshacer cambios en un archivo (antes de add)
git checkout -- archivo.java

:: Deshacer el último commit (mantiene los cambios)
git reset --soft HEAD~1

:: Ver quién modificó cada línea
git blame archivo.java

:: Guardar cambios temporalmente (sin commit)
git stash
:: ... haces otra cosa ...
git stash pop   :: recuperar los cambios

:: Clonar un repositorio
git clone https://github.com/usuario/repo.git
```

---

## 11. Flujo para Este Roadmap

Recomendación para tu aprendizaje:

```cmd
:: Al empezar cada nivel:
git checkout -b nivel-03-base-datos

:: Después de cada versión funcional:
git add .
git commit -m "feat: nivel 03 V1 - crear tablas manualmente"

:: Cuando terminas el nivel:
git checkout main
git merge nivel-03-base-datos
git push
git branch -d nivel-03-base-datos
```

---

## Resumen del flujo diario

```
1. git pull              ← Traer lo último
2. git checkout -b ...   ← Crear rama (si es nuevo)
3. (trabajar, escribir código)
4. git status            ← Ver qué cambió
5. git add .             ← Preparar cambios
6. git commit -m "..."   ← Guardar con mensaje claro
7. git push              ← Subir a GitHub
```

---

## Siguiente paso
Practica este flujo con los proyectos del roadmap. Cada nivel = una rama. Cada versión funcional = un commit.
