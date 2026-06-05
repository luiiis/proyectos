# Módulo 01: Fundamentos Web - Cómo Funciona Internet

## 1. ¿Cómo funciona Internet?

```
TU COMPUTADORA          INTERNET              SERVIDOR
┌──────────┐           ┌────────┐           ┌──────────┐
│Navegador │──────────>│ Router │──────────>│ Web      │
│(Chrome)  │  Request  │  ISP   │           │ Server   │
│          │<──────────│  DNS   │<──────────│(Nginx)   │
│          │  Response │        │           │          │
└──────────┘           └────────┘           └──────────┘
```

### Flujo completo cuando escribes una URL:

```
1. Escribes: www.google.com
2. DNS: "¿Cuál es la IP de google.com?" → 142.250.80.46
3. TCP: Tu navegador establece conexión con esa IP (puerto 443)
4. TLS: Se negocia encriptación (HTTPS)
5. HTTP: Navegador envía: GET / HTTP/2
6. Servidor: Procesa y responde con HTML
7. Navegador: Parsea HTML → Descarga CSS/JS → Renderiza
8. Tú ves: La página de Google
```

## 2. Cliente vs Servidor

| Concepto | Cliente (Frontend) | Servidor (Backend) |
|----------|-------------------|-------------------|
| Dónde corre | En TU navegador | En una máquina remota |
| Lenguajes | HTML, CSS, JavaScript | Java, Python, Node.js |
| Qué hace | Muestra la interfaz | Procesa datos, BD |
| Ejemplo | La página que ves | La API que responde JSON |

## 3. HTTP/HTTPS

```
HTTP Request:
┌─────────────────────────────────────┐
│ GET /api/productos HTTP/1.1         │  ← Método + Ruta + Versión
│ Host: api.empresa.com               │  ← Headers
│ Authorization: Bearer eyJ...        │
│ Accept: application/json            │
│                                     │
│ (body vacío en GET)                 │
└─────────────────────────────────────┘

HTTP Response:
┌─────────────────────────────────────┐
│ HTTP/1.1 200 OK                     │  ← Status code
│ Content-Type: application/json      │  ← Headers
│                                     │
│ {"data": [{"id": 1, "nombre":...}]} │  ← Body (JSON)
└─────────────────────────────────────┘
```

### Status Codes:
| Código | Significado | Ejemplo |
|--------|-------------|---------|
| 200 | OK | Petición exitosa |
| 201 | Created | Recurso creado |
| 400 | Bad Request | Datos inválidos |
| 401 | Unauthorized | No autenticado |
| 403 | Forbidden | Sin permisos |
| 404 | Not Found | No existe |
| 500 | Server Error | Bug en el servidor |

## 4. DNS (Domain Name System)

```
"www.empresa.com" → DNS → "192.168.1.100"

Es como la agenda de contactos de Internet:
  Nombre: "google.com"  → Teléfono: "142.250.80.46"
  Nombre: "github.com"  → Teléfono: "140.82.121.3"
```

## 5. Cómo Renderiza el Navegador

```
HTML recibido
     │
     ▼
┌─────────┐     ┌─────────┐     ┌─────────────┐     ┌──────────┐
│  Parse  │────>│  DOM    │────>│ Render Tree │────>│  Paint   │
│  HTML   │     │  Tree   │     │ (DOM + CSS) │     │ (Pixels) │
└─────────┘     └─────────┘     └─────────────┘     └──────────┘
                     ▲
                     │
              ┌─────────┐
              │  Parse  │
              │  CSS    │
              └─────────┘
```

## 6. Ejercicios

1. Abre DevTools (F12) → Network. Recarga una página y observa todas las peticiones HTTP.
2. ¿Cuántos archivos descarga tu navegador al cargar google.com?
3. Identifica: método HTTP, status code, content-type de cada petición.
4. ¿Qué es la diferencia entre HTTP y HTTPS? ¿Por qué importa?
5. Usa `curl` o Postman para hacer un GET a una API pública.

---

## Siguiente Módulo
→ [02-HTML](../02-html/README.md)
