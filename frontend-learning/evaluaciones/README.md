# Sistema de Evaluaciones - Frontend Learning

## Estructura de Evaluaciones

```
┌─────────────────────────────────────────────────────────────┐
│  Fase 1: Fundamentos Web        (Módulos 01-09)            │
│  Fase 2: Angular Core           (Módulos 10-18)            │
│  Fase 3: Angular Avanzado       (Módulos 19-23)            │
│  Fase 4: Arquitectura & DevOps  (Módulos 24-30)            │
└─────────────────────────────────────────────────────────────┘
```

## Fase 1: Fundamentos Web (Módulos 01-09)

### Examen Teórico (40%)
1. ¿Qué diferencia hay entre HTML semántico y divs genéricos? Menciona 5 etiquetas semánticas.
2. Explica el box model y por qué usamos `box-sizing: border-box`.
3. ¿Cuál es la diferencia entre Flexbox y Grid? ¿Cuándo usar cada uno?
4. Explica closures con un ejemplo práctico.
5. ¿Qué es el event loop? Ordena: setTimeout, Promise.then, console.log.
6. ¿Cuál es la diferencia entre `==` y `===`?
7. Explica la diferencia entre `interface` y `type` en TypeScript.
8. ¿Qué es event delegation y por qué es mejor que múltiples listeners?
9. ¿Qué es async/await y cómo maneja errores?
10. Describe el patrón AAA en testing.

### Examen Práctico (60%)
1. Crea una página responsive con HTML semántico + CSS Grid (30 min).
2. Implementa un carrito de compras con JavaScript puro (45 min).
3. Escribe tests con Jest para una función de validación (20 min).
4. Convierte código JavaScript a TypeScript con tipos estrictos (15 min).

**Criterios de aprobación: 70% mínimo**

## Fase 2: Angular Core (Módulos 10-18)

### Examen Teórico (40%)
1. ¿Qué es un componente standalone y cómo difiere de uno con NgModule?
2. Explica el nuevo control flow (@if, @for, @switch) vs las directivas antiguas.
3. ¿Cuál es la diferencia entre `input()` y `@Input()`?
4. ¿Qué es un signal? ¿Qué es computed? ¿Qué es effect?
5. Explica el ciclo de vida de un componente (hooks principales).
6. ¿Cuál es la diferencia entre template-driven y reactive forms?
7. ¿Qué es un interceptor y para qué se usa?
8. Explica lazy loading y cómo se configura.
9. ¿Qué es un guard y qué tipos existen?
10. ¿Qué es dependency injection y cómo funciona `providedIn: 'root'`?

### Examen Práctico (60%)
1. Crea un CRUD de productos con Angular (service + component + routing) (60 min).
2. Implementa un formulario reactivo con validaciones custom (30 min).
3. Configura rutas con lazy loading y guards (20 min).
4. Crea un interceptor de autenticación (15 min).

**Criterios de aprobación: 70% mínimo**

## Fase 3: Angular Avanzado (Módulos 19-23)

### Examen Teórico (40%)
1. ¿Cuál es la diferencia entre `switchMap`, `mergeMap` y `concatMap`?
2. Explica `BehaviorSubject` vs `Subject`. ¿Cuándo usar cada uno?
3. ¿Cómo funciona Change Detection? ¿Qué es OnPush?
4. ¿Qué es `@defer` y qué triggers tiene?
5. Explica el patrón de state management con signals.
6. ¿Cómo testeas un servicio que usa HttpClient?
7. ¿Qué es virtual scroll y cuándo usarlo?
8. Compara Angular Material vs PrimeNG.
9. ¿Cuándo usarías NgRx vs Signals + Services?
10. ¿Qué es WCAG 2.2 y qué nivel debe cumplir una app enterprise?

### Examen Práctico (60%)
1. Implementa un buscador con debounce + switchMap + loading state (30 min).
2. Crea un DataTable con PrimeNG: paginación, filtros, CRUD con dialog (45 min).
3. Implementa state management con signals para un módulo (30 min).
4. Escribe tests para un componente y un servicio (30 min).

**Criterios de aprobación: 75% mínimo**

## Fase 4: Arquitectura & DevOps (Módulos 24-30)

### Examen Teórico (40%)
1. Describe la arquitectura Clean Architecture para frontend.
2. ¿Qué son Smart vs Dumb components? Da ejemplos.
3. Explica Module Federation para micro frontends.
4. ¿Cómo configurarías Docker multi-stage para Angular?
5. Describe un pipeline CI/CD completo para Angular.
6. ¿Cómo migrarías un módulo de ExtJS a Angular?
7. ¿Qué son barrel exports y path aliases?
8. ¿Cuáles son las reglas de dependencia entre core/shared/features?
9. ¿Cuándo usarías micro frontends y cuándo no?
10. Describe las fases de un proyecto enterprise Angular.

### Examen Práctico (60%)
1. Diseña la arquitectura de un sistema (diagrama + estructura de carpetas) (30 min).
2. Configura Docker + nginx para una app Angular (30 min).
3. Crea un GitHub Actions workflow (lint + test + build + deploy) (30 min).
4. Implementa un módulo completo siguiendo clean architecture (60 min).

**Criterios de aprobación: 80% mínimo**

## Sistema de Calificación

```
┌──────────────────────────────────────────┐
│  90-100%  │  Excelente (A)              │
│  80-89%   │  Muy Bueno (B)             │
│  70-79%   │  Aprobado (C)              │
│  60-69%   │  Necesita refuerzo (D)     │
│  < 60%    │  No aprobado (F)           │
└──────────────────────────────────────────┘

Regla: No avanzar a la siguiente fase sin aprobar la actual.
Reintentos: Máximo 2 por fase, con 1 semana entre intentos.
```
