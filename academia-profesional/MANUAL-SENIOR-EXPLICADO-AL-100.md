# 🧠 Manual Senior Explicado al 100% - Entender TODO como Profesional

> Este documento explica CADA concepto como si estuvieras sentado con un Senior
> que te dice: "Mira, esto funciona ASI y se usa POR ESTO".
> Sin jerga innecesaria. Sin asumir que ya sabes algo.

---

# PARTE 1: CÓMO FUNCIONA UNA APLICACIÓN WEB (el panorama completo)

## La Gran Imagen

Cuando un usuario abre tu aplicación, esto es lo que pasa REALMENTE:

```
USUARIO                    INTERNET                 TU SISTEMA
(navegador)                                         (servidores)

1. Escribe URL ──────────────────────────────────────────────────────────┐
                                                                         │
2. DNS convierte "miapp.com" → IP "192.168.1.100" ◄──────────────────────┘
                                                                         │
3. Navegador conecta a esa IP (TCP + TLS) ──────────────────────────────►│
                                                                         │
4. Nginx recibe la conexión ◄────────────────────────────────────────────┘
   │
   ├── Si pide "/" → sirve index.html (Angular compilado)
   │   El navegador descarga JS/CSS → Angular se ejecuta en el navegador
   │
   └── Si pide "/api/..." → reenvía a Spring Boot
       │
       Spring Boot recibe la petición
       │
       ├── JwtFilter: ¿tiene token válido? Si no → 401
       │
       ├── Controller: recibe los datos, valida formato
       │
       ├── Service: aplica lógica de negocio
       │   (calcular IVA, verificar stock, aplicar descuento)
       │
       ├── Repository: genera SQL y lo envía a PostgreSQL
       │
       ├── PostgreSQL: ejecuta el SQL, devuelve resultados
       │
       └── Spring Boot: convierte resultado a JSON → responde al navegador
           │
           Angular recibe el JSON → actualiza la pantalla
```

**¿Por qué importa entender esto?**
Porque cuando algo falla, necesitas saber DÓNDE buscar:
- ¿El usuario ve pantalla blanca? → Problema en Angular o Nginx
- ¿Ve error 401? → Problema en JWT/Security
- ¿Ve error 500? → Bug en el Service o la BD
- ¿Tarda mucho? → Query lenta en PostgreSQL o falta caché

---

# PARTE 2: BASE DE DATOS - Entender al 100%

## ¿Por qué existe una base de datos?

Sin BD: los datos viven en la RAM del servidor. Si se reinicia → TODO se pierde.
Con BD: los datos se guardan en DISCO. Sobreviven reinicios, crashes, actualizaciones.

## ¿Qué es una TABLA?

Piensa en una hoja de Excel:
```
TABLA: productos
┌─────┬──────────────┬──────────┬───────┬─────────────┐
│ id  │ nombre       │ precio   │ stock │ categoria   │
├─────┼──────────────┼──────────┼───────┼─────────────┤
│ 1   │ Laptop HP    │ 18999.99 │ 25    │ Electrónica │
│ 2   │ Mouse MX     │ 1899.00  │ 50    │ Periféricos │
│ 3   │ Silla Ergo   │ 8999.00  │ 8     │ Mobiliario  │
└─────┴──────────────┴──────────┴───────┴─────────────┘

Cada FILA = un producto (un registro)
Cada COLUMNA = una característica (un campo)
```

## ¿Qué es una PRIMARY KEY y por qué es obligatoria?

```
La PK es el "número de identificación" de cada fila.
Como tu CURP: es ÚNICO, no se repite, identifica exactamente a UNA persona.

Sin PK: ¿cómo distingues dos productos que se llaman igual?
  "Laptop HP" (¿cuál de las 5 que tenemos?)

Con PK: cada uno tiene un ID único
  id=1 → Laptop HP ProBook
  id=2 → Laptop HP Pavilion
  Ahora puedes decir: "actualiza el precio del producto 1" (sin ambigüedad)
```

## ¿Qué es una FOREIGN KEY y por qué importa?

```
La FK es una REFERENCIA a otra tabla. Crea la RELACIÓN.

Problema sin FK:
  TABLA ventas: | id | producto_nombre | cliente_nombre |
  Si el cliente cambia de nombre → ¿actualizas en todas sus ventas? (inconsistencia)
  Si escribes mal el nombre → datos basura

Solución con FK:
  TABLA ventas: | id | producto_id | cliente_id |
  producto_id APUNTA a la tabla productos (por su PK)
  cliente_id APUNTA a la tabla clientes (por su PK)
  
  Ventajas:
  - Si el cliente cambia de nombre → se actualiza en 1 solo lugar
  - No puedes insertar un producto_id que no existe (la BD lo rechaza)
  - Integridad referencial GARANTIZADA
```

## ¿Qué es un JOIN y por qué se necesita?

```
Las tablas están SEPARADAS (normalización). Para ver datos COMPLETOS necesitas unirlas.

Sin JOIN (solo tabla ventas):
  | id | producto_id | cliente_id | total |
  | 1  | 5           | 23         | 18999 |
  ¿Quién es el cliente 23? ¿Qué producto es el 5? No sabes.

Con JOIN (unir tablas):
  SELECT v.id, p.nombre AS producto, c.nombre AS cliente, v.total
  FROM ventas v
  JOIN productos p ON v.producto_id = p.id
  JOIN clientes c ON v.cliente_id = c.id;
  
  Resultado:
  | id | producto    | cliente       | total |
  | 1  | Laptop HP   | Carlos García | 18999 |
  Ahora SÍ entiendes los datos.
```

## ¿Qué es un ÍNDICE y por qué hace todo más rápido?

```
Analogía: un libro de 500 páginas.

Sin índice: para encontrar "Capítulo 15" → lees página por página (500 páginas).
Con índice: vas al índice al inicio → "Capítulo 15: página 234" → vas directo.

En la BD:
Sin índice: SELECT * FROM ventas WHERE fecha = '2024-06-15'
  PostgreSQL lee las 10,000 filas una por una → 200ms

Con índice: CREATE INDEX idx_ventas_fecha ON ventas(fecha);
  PostgreSQL va directo a las filas de esa fecha → 2ms (100x más rápido)

¿Cuándo crear índice?
  - Columnas que usas en WHERE frecuentemente
  - Columnas que usas en JOIN (las FK)
  - Columnas que usas en ORDER BY

¿Cuándo NO crear índice?
  - Tablas pequeñas (< 1000 filas, no vale la pena)
  - Columnas con pocos valores únicos (boolean: solo true/false)
  - Tablas con muchos INSERT (cada insert actualiza TODOS los índices)
```

## ¿Qué es una TRANSACCIÓN y por qué es crítica?

```
Problema: Transferencia bancaria
  Paso 1: Restar $1000 de cuenta A
  Paso 2: Sumar $1000 a cuenta B
  
  ¿Qué pasa si el servidor se cae ENTRE paso 1 y paso 2?
  → Se restó de A pero NO se sumó a B → $1000 desaparecieron

Solución: TRANSACCIÓN (todo o nada)
  BEGIN;
    UPDATE cuentas SET saldo = saldo - 1000 WHERE id = 1;
    UPDATE cuentas SET saldo = saldo + 1000 WHERE id = 2;
  COMMIT;
  
  Si algo falla entre BEGIN y COMMIT → ROLLBACK automático
  → Ambas cuentas quedan como estaban (nada se perdió)

En Spring Boot:
  @Transactional  // Esta anotación hace todo lo anterior automáticamente
  public void transferir(Long origen, Long destino, double monto) {
      cuentaRepo.restar(origen, monto);
      cuentaRepo.sumar(destino, monto);
  }
  // Si restar() funciona pero sumar() falla → Spring hace ROLLBACK
```

---

# PARTE 3: BACKEND - Entender al 100%

## ¿Qué hace EXACTAMENTE un backend?

```
El backend es el CEREBRO de la aplicación:
1. Recibe peticiones HTTP del frontend
2. Valida que los datos sean correctos
3. Aplica REGLAS DE NEGOCIO (lógica que el frontend NO debe conocer)
4. Accede a la base de datos
5. Responde con datos en formato JSON

Ejemplo de regla de negocio:
  "Un cliente VIP tiene 20% de descuento, pero máximo $5000 de descuento,
   y solo si la compra es mayor a $10,000, y solo los martes y jueves"
  
  Esto NO va en el frontend (el usuario podría modificarlo).
  Va en el backend donde nadie puede manipularlo.
```

## ¿Por qué Spring Boot tiene CAPAS?

```
Imagina un restaurante:

  MESERO (Controller):
    - Recibe el pedido del cliente
    - Verifica que el pedido tiene sentido ("¿pizza de chocolate? No tenemos eso")
    - Pasa el pedido a la cocina
    - Entrega el plato al cliente
    
  CHEF (Service):
    - Recibe el pedido del mesero
    - Decide CÓMO prepararlo (receta, ingredientes, tiempos)
    - Pide ingredientes al almacén
    - Cocina y devuelve el plato
    
  ALMACÉN (Repository):
    - Tiene todos los ingredientes organizados
    - El chef pide: "dame 200g de queso mozzarella"
    - El almacén lo busca y lo entrega

¿Por qué separar?
  - Si cambias la receta (Service) → el mesero (Controller) no se entera
  - Si cambias de proveedor (Repository) → el chef (Service) no se entera
  - Cada uno tiene UNA responsabilidad
  - Puedes testear cada capa INDEPENDIENTEMENTE
```

## ¿Qué es Dependency Injection y por qué Spring lo usa?

```
Sin DI (tú creas todo):
  class VentaService {
      private ProductoRepository repo = new ProductoRepositoryImpl();
      // ¿Y si quiero cambiar la implementación? Modificar ESTA clase.
      // ¿Y si quiero testear con un mock? No puedo (está hardcodeado).
  }

Con DI (Spring crea e inyecta):
  @Service
  class VentaService {
      private final ProductoRepository repo;  // Spring inyecta automáticamente
      
      VentaService(ProductoRepository repo) {
          this.repo = repo;  // Recibe la dependencia desde FUERA
      }
  }
  
  Ventajas:
  - Para testear: inyectas un MOCK (no necesitas BD real)
  - Para cambiar implementación: solo cambias la configuración (no el código)
  - Spring maneja el ciclo de vida (crear, destruir, singleton)
```

## ¿Qué es JWT y cómo funciona la autenticación?

```
Problema: HTTP es STATELESS (cada petición es independiente).
  El servidor NO recuerda quién eres entre peticiones.
  
Solución antigua: SESIONES en el servidor
  Servidor guarda: "sesión ABC123 = usuario Carlos"
  Problema: si tienes 5 servidores, ¿cuál tiene tu sesión?

Solución moderna: JWT (token que el CLIENTE guarda)
  1. Login: envías username + password
  2. Servidor valida → genera un TOKEN firmado:
     eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTczNTY4OTYwMH0.firma
     
     Decodificado:
     HEADER: {"alg":"HS256"}
     PAYLOAD: {"sub":"admin", "rol":"ADMIN", "exp":1735689600}
     FIRMA: HMAC-SHA256(header+payload, claveSecreta)
  
  3. Cliente guarda el token (localStorage)
  4. Cada petición incluye: Authorization: Bearer eyJ...
  5. Servidor VALIDA la firma (no necesita consultar BD ni sesión)
     - ¿La firma coincide? → el token no fue modificado
     - ¿No ha expirado? → sigue siendo válido
     - Si ambas: el usuario es quien dice ser

¿Por qué es seguro?
  - Sin la clave secreta, NADIE puede generar un token válido
  - Si alguien modifica el payload → la firma ya no coincide → rechazado
  - Tiene expiración (24h típicamente) → si lo roban, expira pronto
```

---

# PARTE 4: FRONTEND - Entender al 100%

## ¿Qué es una SPA (Single Page Application)?

```
Aplicación TRADICIONAL (MPA):
  Clic en "Productos" → navegador pide nueva página al servidor → RECARGA completa
  Clic en "Ventas" → otra recarga completa
  Cada clic = pantalla blanca por 1-2 segundos

SPA (Angular, React, Vue):
  Se carga UNA sola vez (index.html + JavaScript)
  Clic en "Productos" → JavaScript cambia lo que se muestra (SIN recargar)
  Clic en "Ventas" → mismo, solo cambia el contenido
  Solo pide DATOS (JSON) al servidor cuando necesita información nueva
  
  Resultado: la app se siente como una app de escritorio (fluida, sin parpadeos)
```

## ¿Qué es un COMPONENTE en Angular?

```
Un componente = un PEDAZO de la pantalla que se puede reutilizar.

Ejemplo: una tarjeta de producto
  ┌─────────────────────┐
  │ 📷 Imagen           │
  │ Laptop HP           │  ← Este "bloque" es UN componente
  │ $18,999.99          │
  │ [Comprar]           │
  └─────────────────────┘

Si tienes 50 productos → usas el MISMO componente 50 veces (con datos diferentes).

En código:
  @Component({
    selector: 'app-producto-card',  // Nombre HTML: <app-producto-card>
    template: `
      <div class="card">
        <h3>{{ producto.nombre }}</h3>
        <p>{{ producto.precio | currency }}</p>
        <button (click)="comprar()">Comprar</button>
      </div>
    `
  })
  export class ProductoCardComponent {
    producto = input.required<Producto>();  // Recibe datos del padre
    comprar = output<number>();             // Emite evento al padre
  }
```

## ¿Qué son los SIGNALS en Angular?

```
Problema antiguo (Zone.js):
  Angular NO sabía qué cambió. Entonces revisaba TODO el árbol de componentes.
  100 componentes → revisa los 100 → lento en apps grandes.

Solución moderna (Signals):
  Un Signal es un valor que AVISA cuando cambia.
  Angular solo actualiza los componentes que LEEN ese signal.

  // Crear signal
  const contador = signal(0);
  
  // Leer (Angular sabe que este componente depende de "contador")
  template: `<p>{{ contador() }}</p>`
  
  // Modificar (Angular SOLO re-renderiza este componente)
  contador.set(5);
  contador.update(v => v + 1);
  
  // Computed: se recalcula automáticamente cuando sus dependencias cambian
  const doble = computed(() => contador() * 2);
  // Si contador cambia de 5 a 6 → doble automáticamente es 12

Resultado: apps más rápidas, menos bugs, código más simple.
```

---

# PARTE 5: DOCKER - Entender al 100%

## ¿Qué problema resuelve Docker?

```
Escenario sin Docker:
  Desarrollador: "En mi máquina funciona" (tiene Java 21, PostgreSQL 16, Node 20)
  Servidor: tiene Java 17, PostgreSQL 14, Node 18 → NO funciona
  Otro dev: tiene Mac, tú tienes Windows → configuración diferente

Escenario con Docker:
  Empaquetas tu app + TODAS sus dependencias en un "contenedor"
  El contenedor funciona IGUAL en cualquier máquina que tenga Docker
  No importa si es Windows, Mac, Linux, AWS, Azure → MISMO resultado
```

## ¿Cuál es la diferencia entre IMAGEN y CONTENEDOR?

```
IMAGEN = la receta (Dockerfile)
  Es un archivo estático. No se ejecuta. Es como un .iso de un SO.
  Se construye UNA vez. Se puede compartir (Docker Hub).

CONTENEDOR = el plato servido (docker run)
  Es una instancia EN EJECUCIÓN de una imagen.
  Puedes tener 10 contenedores de la misma imagen (10 copias corriendo).
  
  Analogía:
  Imagen = molde de galleta (siempre igual)
  Contenedor = galleta horneada (puedes hacer muchas del mismo molde)
```

## ¿Qué es Docker Compose y por qué se necesita?

```
Tu app necesita: Spring Boot + PostgreSQL + Redis + Angular
Eso son 4 contenedores que deben:
  - Arrancar en orden (BD primero, luego backend, luego frontend)
  - Comunicarse entre sí (backend necesita hablar con PostgreSQL)
  - Compartir una red interna

Sin Compose: 4 comandos docker run largos, configurar red manual, recordar orden.
Con Compose: UN archivo (docker-compose.yml) + UN comando (docker compose up).

Docker Compose es el "director de orquesta" que coordina múltiples contenedores.
```

---

# PARTE 6: ¿CÓMO SE CONECTA TODO?

## Flujo completo: Usuario compra un producto

```
1. FRONTEND (Angular en el navegador del usuario)
   - Usuario ve lista de productos (datos que vinieron del backend)
   - Hace clic en "Comprar" en el producto "Laptop HP"
   - Angular ejecuta: this.http.post('/api/ventas', {productoId: 1, cantidad: 1})
   - El interceptor agrega: Authorization: Bearer eyJ... (token JWT)

2. NGINX (reverse proxy en el servidor)
   - Recibe: POST /api/ventas
   - Ve que empieza con /api/ → reenvía a Spring Boot (puerto 8080)

3. SPRING BOOT (backend)
   a. JwtFilter: extrae token → valida firma → usuario = "admin" → OK
   b. VentaController: recibe el JSON → @Valid valida formato
   c. VentaService.registrar():
      - Busca producto en BD: SELECT * FROM productos WHERE id = 1
      - Verifica stock: ¿stock >= cantidad? (25 >= 1 → OK)
      - Descuenta stock: UPDATE productos SET stock = stock - 1 WHERE id = 1
      - Crea venta: INSERT INTO ventas (numero, total, ...) VALUES (...)
      - Crea detalle: INSERT INTO detalle_venta (venta_id, producto_id, ...)
      - Todo en @Transactional (si algo falla → ROLLBACK)
   d. Responde: HTTP 201 {id: 5001, numero: "V-20260530-001", total: 22039.99}

4. POSTGRESQL (base de datos)
   - Ejecutó los INSERT/UPDATE que Spring le envió
   - Los datos están persistidos en disco (sobreviven reinicios)
   - El trigger de auditoría registró: "admin creó venta 5001"

5. REDIS (caché)
   - El caché de productos se INVALIDÓ (@CacheEvict)
   - La próxima petición GET /api/productos consultará la BD (stock actualizado)

6. ANGULAR (de vuelta en el navegador)
   - Recibe la respuesta 201
   - Muestra notificación: "Venta registrada: $22,039.99"
   - Redirige al historial de ventas
   - El usuario ve su nueva venta en la lista
```

---

# PARTE 7: ¿CÓMO APRENDO TODO ESTO?

## Orden correcto (no saltar pasos)

```
MES 1-2: Fundamentos
  □ Java básico (variables, if/else, ciclos, métodos)
  □ POO (clases, herencia, interfaces)
  □ SQL básico (SELECT, INSERT, UPDATE, JOIN)
  □ Git (commit, push, pull, branches)

MES 3-4: Intermedio
  □ Colecciones (List, Map, Set)
  □ Streams y Lambdas
  □ Excepciones y manejo de errores
  □ SQL avanzado (subconsultas, funciones, índices)

MES 5-6: Frameworks
  □ Spring Boot (crear API REST)
  □ JPA/Hibernate (conectar a PostgreSQL)
  □ Angular básico (componentes, servicios, routing)
  □ Docker básico (Dockerfile, docker-compose)

MES 7-9: Integración
  □ Spring Security + JWT
  □ Angular + Material/PrimeNG
  □ Full Stack (Angular ↔ Spring Boot ↔ PostgreSQL)
  □ Testing (JUnit, Mockito)

MES 10-12: Avanzado
  □ Redis (caché)
  □ Kafka (eventos)
  □ Microservicios
  □ CI/CD (GitHub Actions)

MES 13+: Senior
  □ Kubernetes
  □ Observabilidad (Prometheus + Grafana)
  □ Arquitectura (Hexagonal, DDD)
  □ System Design
  □ Mentoría y liderazgo técnico
```

## Regla de oro para aprender

```
1. NO memorizar → ENTENDER (¿por qué existe? ¿qué problema resuelve?)
2. NO solo leer → HACER (ejecutar cada ejemplo, romperlo, arreglarlo)
3. NO avanzar sin dominar → cada tema es BASE del siguiente
4. Cuando algo falla → es la MEJOR oportunidad de aprender
5. Preguntar "¿por qué?" siempre (no "¿cómo?" solamente)
```
