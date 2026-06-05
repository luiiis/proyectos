# Módulo 02: HTML5 - Estructura y Semántica

## 1. Etiquetas Semánticas

```html
<!-- ❌ Antes: todo con divs -->
<div class="header">...</div>
<div class="nav">...</div>
<div class="content">...</div>

<!-- ✅ Ahora: HTML5 semántico -->
<header>
  <nav>
    <ul>
      <li><a href="/">Inicio</a></li>
      <li><a href="/productos">Productos</a></li>
    </ul>
  </nav>
</header>
<main>
  <article>
    <h1>Título del artículo</h1>
    <section>
      <h2>Sección 1</h2>
      <p>Contenido...</p>
    </section>
  </article>
  <aside>Contenido lateral</aside>
</main>
<footer>&copy; 2025 Mi Empresa</footer>
```

## 2. Formularios y Inputs

```html
<form action="/api/registro" method="POST">
  <label for="nombre">Nombre:</label>
  <input type="text" id="nombre" name="nombre" required minlength="3">

  <label for="email">Email:</label>
  <input type="email" id="email" name="email" required>

  <label for="edad">Edad:</label>
  <input type="number" id="edad" name="edad" min="18" max="99">

  <label for="fecha">Fecha nacimiento:</label>
  <input type="date" id="fecha" name="fecha">

  <label for="password">Contraseña:</label>
  <input type="password" id="password" name="password" 
         pattern="(?=.*\d)(?=.*[a-z]).{8,}" title="Mínimo 8 caracteres">

  <label for="rol">Rol:</label>
  <select id="rol" name="rol">
    <option value="">Seleccione...</option>
    <option value="admin">Administrador</option>
    <option value="user">Usuario</option>
  </select>

  <button type="submit">Registrar</button>
</form>
```

## 3. Tablas

```html
<table>
  <thead>
    <tr>
      <th>ID</th><th>Nombre</th><th>Email</th><th>Acciones</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>1</td><td>Juan</td><td>juan@mail.com</td>
      <td><button>Editar</button></td>
    </tr>
  </tbody>
</table>
```

## 4. Multimedia

```html
<figure>
  <img src="producto.jpg" alt="Laptop HP 15 pulgadas" loading="lazy">
  <figcaption>Laptop HP - $999</figcaption>
</figure>

<video controls width="600">
  <source src="demo.mp4" type="video/mp4">
  Tu navegador no soporta video.
</video>
```

## 5. Ejercicios

1. Crea una página con estructura semántica completa (header, nav, main, aside, footer).
2. Construye un formulario de registro con validación HTML5 (required, pattern, min/max).
3. Crea una tabla de productos con thead, tbody y al menos 5 filas.
4. Implementa una galería de imágenes usando `<figure>` y `<figcaption>` con lazy loading.
5. Crea un formulario de contacto accesible con labels, fieldset y legend.

---

## Siguiente Módulo
→ [03-CSS](../03-css/README.md)
