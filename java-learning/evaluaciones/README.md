# Evaluaciones por Módulo

## Examen Fase 1: Java Core (Módulos 1-13)

### Teoría (40%)
1. Explica el ciclo de vida de un objeto en Java (creación → GC)
2. ¿Cuál es la diferencia entre abstract class e interface? ¿Cuándo usar cada una?
3. Explica cómo funciona HashMap internamente (hashing, buckets, colisiones)
4. ¿Qué es un deadlock? ¿Cómo prevenirlo?
5. Explica la diferencia entre Checked y Unchecked exceptions

### Código (60%)
6. Implementa una clase `Inventario<T extends Producto>` genérica con: agregar, buscar, eliminar, listarPorPrecio
7. Usando Streams, dado una lista de ventas: agrupa por mes, calcula total por mes, encuentra el mes con mayor venta
8. Implementa un mini sistema bancario thread-safe con transferencias entre cuentas
9. Crea una jerarquía de excepciones para un sistema de pagos
10. Lee un CSV de 10,000 productos y cárgalos en un HashMap<String, Producto> (sku → producto)

---

## Examen Fase 2: Herramientas (Módulos 14-17)

1. Implementa el patrón Strategy para calcular envío (express, estándar, gratis)
2. Escribe tests unitarios para un servicio de carrito de compras (mínimo 10 tests)
3. Configura un proyecto Maven multi-módulo con: core, api, persistence
4. Implementa el patrón Builder para construir objetos Pedido complejos
5. Alcanza 80% de cobertura de tests en un servicio dado

---

## Examen Fase 3: Spring (Módulos 18-23)

1. Construye una API REST completa para gestión de empleados con: CRUD, paginación, filtros, validación, manejo de errores
2. Implementa autenticación JWT con: login, register, refresh token, roles
3. Mapea un modelo de datos con 5 entidades relacionadas (1:N, M:N)
4. Implementa caché Redis en el servicio de productos
5. Escribe tests de integración con Testcontainers

---

## Examen Fase 4: DevOps + Arquitectura (Módulos 24-29)

1. Dockeriza la aplicación y crea docker-compose con app + BD + Redis
2. Divide un monolito en 2 microservicios que se comunican via REST
3. Implementa un evento Kafka: "venta-creada" → consumer que actualiza inventario
4. Refactoriza un servicio a Hexagonal Architecture
5. Despliega en Kubernetes con 3 réplicas y auto-scaling

---

## Examen Final (Módulo 30)

Construye el sistema enterprise completo en 2 semanas.
Criterios en el README del módulo 30.
