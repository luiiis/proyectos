# Sistema de Ejercicios - Frontend Learning

## Estructura: 900 Ejercicios

```
┌─────────────────────────────────────────────────────────────┐
│  300 Básicos (B001-B300)                                    │
│  300 Intermedios (I001-I300)                                │
│  300 Avanzados (A001-A300)                                  │
└─────────────────────────────────────────────────────────────┘
```

## Distribución por Módulo

| Módulo | Básico | Intermedio | Avanzado |
|--------|--------|------------|----------|
| HTML/CSS | B001-B040 | I001-I030 | A001-A020 |
| JavaScript | B041-B090 | I031-I070 | A021-A050 |
| TypeScript | B091-B120 | I071-I100 | A051-A070 |
| DOM + Async | B121-B150 | I101-I120 | A071-A085 |
| Testing JS | B151-B170 | I121-I140 | A086-A100 |
| Angular Core | B171-B220 | I141-I190 | A101-A150 |
| RxJS + HTTP | B221-B250 | I191-I220 | A151-A180 |
| Forms + Material | B251-B270 | I221-I250 | A181-A210 |
| State + Performance | B271-B285 | I251-I275 | A211-A250 |
| Architecture + DevOps | B286-B300 | I276-I300 | A251-A300 |

## Nivel Básico (B001-B300)

```
B001: Crear una página HTML con estructura semántica completa
B002: Estilizar un navbar con Flexbox
B003: Crear un grid de cards responsive
B041: Declarar variables con let/const y explicar la diferencia
B042: Crear funciones arrow con diferentes parámetros
B043: Usar map, filter y reduce sobre un array de objetos
B091: Definir interfaces para un sistema de usuarios
B092: Crear un enum para estados de pedido
B171: Generar un componente standalone con Angular CLI
B172: Crear un servicio con inject() y consumirlo
B221: Crear un Observable simple y suscribirse
B251: Crear un formulario reactivo con validaciones básicas
```

## Nivel Intermedio (I001-I300)

```
I001: Crear un layout completo con CSS Grid (header, sidebar, main, footer)
I031: Implementar un closure para un carrito de compras
I032: Crear una función debounce desde cero
I071: Implementar un Repository<T> genérico con TypeScript
I141: Crear un CRUD completo con Angular (list + form + service)
I142: Implementar lazy loading para 3 features
I191: Usar switchMap para un buscador con cancelación
I192: Implementar combineLatest para filtros múltiples
I221: Crear un formulario con FormArray dinámico
I251: Implementar state management con signals para un carrito
I276: Configurar Docker multi-stage para Angular
```

## Nivel Avanzado (A001-A300)

```
A001: Crear un design system con CSS custom properties y componentes
A021: Implementar un event bus con TypeScript y generics
A051: Crear un type-safe HTTP client con generics avanzados
A101: Implementar un DataTable genérico reutilizable con paginación server-side
A102: Crear un sistema de permisos con directivas y guards
A151: Implementar retry con backoff exponencial usando RxJS
A181: Crear un form builder dinámico que genere formularios desde JSON
A211: Implementar NgRx para un módulo completo (actions, reducers, effects, selectors)
A251: Configurar Module Federation para micro frontends
A252: Implementar CI/CD completo con GitHub Actions
A280: Migrar un módulo de ExtJS a Angular manteniendo la funcionalidad
A300: Proyecto final: sistema empresarial completo
```

## Formato de Cada Ejercicio

```
[CÓDIGO] Título del ejercicio
Nivel: Básico | Intermedio | Avanzado
Módulo: XX - Nombre del módulo
Tiempo estimado: XX minutos

Descripción:
  Explicación clara de lo que se debe implementar.

Requisitos:
  1. Requisito específico 1
  2. Requisito específico 2
  3. Requisito específico 3

Criterios de aceptación:
  - [ ] El código compila sin errores
  - [ ] Cumple todos los requisitos
  - [ ] Tiene tests (intermedio/avanzado)
  - [ ] Sigue las convenciones del proyecto

Pistas:
  - Pista 1 (si se necesita ayuda)
  - Pista 2
```

## Cómo Usar

1. Empieza por los ejercicios básicos de cada módulo
2. No avances al siguiente nivel sin completar el 80% del actual
3. Los ejercicios avanzados combinan múltiples módulos
4. El proyecto final (A300) integra TODO lo aprendido
5. Tiempo estimado total: ~200 horas de práctica
