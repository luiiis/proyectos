# Módulo 06: Manipulación del DOM

## 1. Seleccionar Elementos

```javascript
// Selectores modernos (usar siempre estos)
const boton = document.querySelector('#btn-guardar');         // uno
const cards = document.querySelectorAll('.card');              // todos
const input = document.querySelector('input[name="email"]');  // por atributo

// Recorrer NodeList
cards.forEach(card => card.classList.add('visible'));

// Convertir a array para usar map/filter
const activas = [...cards].filter(c => c.dataset.activo === 'true');
```

## 2. Crear y Modificar Elementos

```javascript
// Crear elemento
function crearCard(producto) {
  const card = document.createElement('div');
  card.className = 'card';
  card.innerHTML = `
    <h3>${producto.nombre}</h3>
    <p class="precio">$${producto.precio}</p>
    <button data-id="${producto.id}">Agregar</button>
  `;
  return card;
}

// Insertar en el DOM
const contenedor = document.querySelector('#productos');
contenedor.appendChild(crearCard({ id: 1, nombre: 'Laptop', precio: 999 }));

// Modificar
const titulo = document.querySelector('h1');
titulo.textContent = 'Nuevo título';       // solo texto
titulo.classList.toggle('destacado');       // toggle clase
titulo.style.color = '#1976d2';            // estilo inline (evitar)
titulo.setAttribute('aria-label', 'Título principal');
```

## 3. Eventos

```javascript
// addEventListener (siempre usar este)
const form = document.querySelector('#form-login');

form.addEventListener('submit', (event) => {
  event.preventDefault();
  const formData = new FormData(form);
  const datos = Object.fromEntries(formData);
  console.log(datos); // { email: '...', password: '...' }
});

// Remover evento
const handler = () => console.log('click');
boton.addEventListener('click', handler);
boton.removeEventListener('click', handler);
```

## 4. Event Delegation

```javascript
// ❌ Malo: un listener por cada botón
document.querySelectorAll('.btn-eliminar').forEach(btn => {
  btn.addEventListener('click', () => eliminar(btn.dataset.id));
});

// ✅ Bueno: un solo listener en el padre (Event Delegation)
document.querySelector('#tabla-usuarios').addEventListener('click', (e) => {
  const btn = e.target.closest('[data-action]');
  if (!btn) return;

  const id = btn.dataset.id;
  const action = btn.dataset.action;

  switch (action) {
    case 'editar':  editarUsuario(id); break;
    case 'eliminar': eliminarUsuario(id); break;
  }
});
```

```
Evento "click" en botón dentro de tabla:
                                    
  ┌─ document ──────────────────┐   Capturing ↓
  │  ┌─ #tabla-usuarios ─────┐  │   
  │  │  ┌─ <tr> ──────────┐  │  │   ← Listener aquí
  │  │  │  ┌─ <button> ─┐ │  │  │   
  │  │  │  │   CLICK    │ │  │  │   ← Origen del evento
  │  │  │  └────────────┘ │  │  │   
  │  │  └─────────────────┘  │  │   Bubbling ↑
  │  └────────────────────────┘  │   
  └──────────────────────────────┘   
```

## 5. Ejercicios

1. Crea una lista de tareas (TODO) usando solo DOM: agregar, eliminar, marcar completada.
2. Implementa una tabla dinámica que se llene desde un array de objetos.
3. Usa Event Delegation para manejar clicks en una tabla con botones editar/eliminar.
4. Crea un buscador en tiempo real que filtre una lista mientras escribes (input event).
5. Implementa un modal reutilizable (abrir, cerrar, contenido dinámico).

---

## Siguiente Módulo
→ [07-Asincronismo](../07-asincronismo/README.md)
