# Bitácora de Construcción - Proyecto 11: Ventas Full Stack

## ¿Qué integra este proyecto?
Combina TODO lo aprendido en proyectos 01-10:

| Proyecto previo | Qué se reutiliza aquí |
|----------------|----------------------|
| 05 BD Ventas | Schema de tablas (productos, ventas, clientes) |
| 07 API Productos | Spring Boot + JPA + PostgreSQL |
| 08 Auth JWT | Spring Security + JWT |
| 09 Dashboard | Angular + Material + routing |
| 10 PrimeNG | DataTable + Dialog + Forms |

## Arquitectura
```
┌──────────────────┐     ┌──────────────────┐     ┌──────────────┐
│   Angular App    │────→│  Spring Boot API │────→│  PostgreSQL  │
│   (PrimeNG)     │     │  (Security+JWT)  │     │  (16 tablas) │
│   Puerto: 4200  │     │  Puerto: 8080    │     │  Puerto: 5432│
└──────────────────┘     └──────────────────┘     └──────────────┘
```

## Decisiones técnicas

| Decisión | Razón |
|----------|-------|
| Monorepo (backend/ + frontend/) | Más simple para un solo equipo |
| Docker Compose con 3 servicios | Reproducible en cualquier máquina |
| JWT stateless | Escala horizontalmente sin sesiones |
| PrimeNG DataTable | Paginación, sort, filter incluidos |
| Soft delete | Nunca perder datos históricos |

## Flujo: Registrar una Venta
```
1. Frontend: usuario selecciona cliente + productos + cantidades
2. Frontend: POST /api/ventas con JSON del pedido
3. Backend: AuthFilter valida JWT → usuario autenticado
4. Backend: VentaService.registrar():
   a. Genera número de venta
   b. INSERT en tabla ventas
   c. Para cada producto: INSERT en detalle_venta
   d. Para cada producto: UPDATE inventario (descontar stock)
   e. Calcula totales (subtotal + IVA)
   f. COMMIT (transacción atómica)
5. Backend: responde 201 con la venta creada
6. Frontend: muestra confirmación + redirige a historial
```

## Módulos del frontend
- `/login` → Formulario de autenticación
- `/dashboard` → KPIs (ventas hoy, mes, alertas)
- `/productos` → CRUD con DataTable
- `/ventas/nueva` → Stepper para registrar venta
- `/ventas/historial` → Lista de ventas con filtros
- `/reportes` → Ventas por período, top productos
