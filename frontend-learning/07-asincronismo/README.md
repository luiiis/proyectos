# Módulo 07: Asincronismo en JavaScript

## 1. Event Loop

```
┌───────────────────────────────────────────────┐
│                  Call Stack                     │
│  (ejecuta funciones una a una - LIFO)         │
└───────────────────────┬───────────────────────┘
                        │
                        ▼
┌───────────────────────────────────────────────┐
│              Web APIs / Node APIs              │
│  (setTimeout, fetch, addEventListener)        │
└───────────────────────┬───────────────────────┘
                        │ cuando termina...
                        ▼
┌──────────────────┐  ┌────────────────────────┐
│  Microtask Queue │  │   Macrotask Queue      │
│  (Promises)      │  │   (setTimeout, I/O)    │
│  ⚡ Prioridad    │  │   Después de micros    │
└────────┬─────────┘  └───────────┬────────────┘
         │                        │
         └────────┬───────────────┘
                  ▼
         Event Loop: "¿Stack vacío? → Ejecuto siguiente tarea"
```

## 2. Callbacks (el pasado)

```javascript
// Callback Hell ❌
getUsuario(1, (usuario) => {
  getPedidos(usuario.id, (pedidos) => {
    getDetalle(pedidos[0].id, (detalle) => {
      console.log(detalle); // Pirámide de la muerte
    });
  });
});
```

## 3. Promises

```javascript
// Crear una Promise
function getUsuario(id) {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      if (id > 0) resolve({ id, nombre: 'Juan' });
      else reject(new Error('ID inválido'));
    }, 1000);
  });
}

// Encadenar Promises
getUsuario(1)
  .then(usuario => getPedidos(usuario.id))
  .then(pedidos => getDetalle(pedidos[0].id))
  .then(detalle => console.log(detalle))
  .catch(error => console.error('Error:', error.message));

// Promise.all → ejecutar en paralelo
const [usuarios, productos] = await Promise.all([
  fetch('/api/usuarios').then(r => r.json()),
  fetch('/api/productos').then(r => r.json()),
]);
```

## 4. Async/Await (el presente)

```javascript
// Limpio y legible ✅
async function cargarDashboard() {
  try {
    const usuario = await getUsuario(1);
    const pedidos = await getPedidos(usuario.id);
    const detalle = await getDetalle(pedidos[0].id);
    return detalle;
  } catch (error) {
    console.error('Error cargando dashboard:', error.message);
    throw error;
  }
}

// Patrón real con fetch
async function getProductos(): Promise<Producto[]> {
  const response = await fetch('/api/productos');
  if (!response.ok) {
    throw new Error(`HTTP ${response.status}: ${response.statusText}`);
  }
  return response.json();
}
```

## 5. Orden de Ejecución

```javascript
console.log('1 - Síncrono');

setTimeout(() => console.log('2 - Macrotask'), 0);

Promise.resolve().then(() => console.log('3 - Microtask'));

console.log('4 - Síncrono');

// Resultado: 1, 4, 3, 2
// Síncrono → Microtasks → Macrotasks
```

## 6. Ejercicios

1. Implementa una función `delay(ms)` que retorne una Promise que se resuelve después de ms.
2. Convierte una función con callbacks a async/await.
3. Usa `Promise.all` para cargar 3 APIs en paralelo y mostrar los resultados.
4. Explica el orden de ejecución de un código con setTimeout, Promise y console.log.
5. Implementa un retry: función que reintenta una operación async N veces antes de fallar.

---

## Siguiente Módulo
→ [08-ES6+](../08-es6/README.md)
