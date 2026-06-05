# Simulación de Entrevistas Frontend

## Estructura por Nivel

```
┌─────────────────────────────────────────────────────────────┐
│  Junior (0-2 años)      → Fundamentos, sintaxis, conceptos │
│  Semi-Senior (2-4 años) → Patrones, Angular, RxJS          │
│  Senior (4-7 años)      → Arquitectura, performance, DX    │
│  Architect (7+ años)    → Decisiones, trade-offs, escala   │
└─────────────────────────────────────────────────────────────┘
```

## Junior - Preguntas y Respuestas

**P1: ¿Cuál es la diferencia entre `let`, `const` y `var`?**
> `const`: no se puede reasignar, scope de bloque. Usar por defecto.
> `let`: se puede reasignar, scope de bloque. Usar cuando el valor cambia.
> `var`: scope de función, se eleva (hoisting). No usar nunca.

**P2: ¿Qué es el box model en CSS?**
> Cada elemento tiene: content + padding + border + margin. Con `box-sizing: border-box`, width incluye padding y border.

**P3: ¿Qué es una Promise?**
> Un objeto que representa un valor futuro. Puede estar pending, fulfilled o rejected. Se consume con `.then()/.catch()` o `async/await`.

**P4: ¿Qué es un componente en Angular?**
> Una clase TypeScript decorada con `@Component` que tiene un template HTML, estilos y lógica. Es la unidad básica de UI.

**P5: ¿Qué es two-way binding?**
> Sincronización bidireccional entre el modelo (componente) y la vista (template). Se usa con `[(ngModel)]` o signals con `model()`.

## Semi-Senior - Preguntas y Respuestas

**P1: ¿Cuál es la diferencia entre `switchMap`, `mergeMap` y `concatMap`?**
> `switchMap`: cancela la suscripción anterior (búsquedas, autocomplete).
> `mergeMap`: ejecuta todas en paralelo (descargas múltiples).
> `concatMap`: ejecuta en orden, una tras otra (operaciones secuenciales).

**P2: ¿Cómo funciona Change Detection en Angular?**
> Angular revisa el árbol de componentes buscando cambios. Con `Default` revisa todo. Con `OnPush` solo revisa si cambian inputs, eventos del template o signals.

**P3: ¿Qué son los Signals en Angular?**
> Primitivas reactivas que notifican cuando su valor cambia. `signal()` para estado mutable, `computed()` para derivados, `effect()` para side effects.

**P4: Explica el patrón Smart/Dumb components.**
> Smart (container): inyecta servicios, maneja estado, orquesta lógica.
> Dumb (presentational): solo inputs/outputs, sin servicios, reutilizable, fácil de testear.

**P5: ¿Cómo manejas errores HTTP globalmente?**
> Con un HttpInterceptor que captura errores, muestra notificaciones al usuario, redirige en 401, y loguea en 500.

## Senior - Preguntas y Respuestas

**P1: ¿Cómo diseñarías la arquitectura de una app Angular enterprise?**
> Core/Shared/Features. Core para singletons (auth, interceptors). Shared para componentes reutilizables. Features por dominio con lazy loading. Path aliases, barrel exports, reglas de dependencia estrictas.

**P2: ¿Signals o NgRx? ¿Cuándo usar cada uno?**
> Signals + Services para el 90% de apps. NgRx solo si necesitas: time-travel debugging, undo/redo, estado muy complejo compartido entre features desacoplados, o el equipo ya lo domina.

**P3: ¿Cómo optimizas el performance de una app Angular?**
> OnPush en todos los componentes, lazy loading, @defer, virtual scroll para listas grandes, trackBy en loops, preload strategies, bundle analysis, tree shaking.

**P4: ¿Cómo implementarías micro frontends?**
> Module Federation con Webpack 5. Shell app que carga remotes dinámicamente. Shared dependencies como singletons. Deploy independiente por equipo. Solo si hay +50 devs y necesidad real.

**P5: ¿Cómo migrarías una app ExtJS a Angular?**
> Migración incremental, nunca big-bang. Coexistencia inicial (iframe/micro-frontend). Migrar módulo por módulo empezando por los menos críticos. Mantener misma API. Replicar UX.

## Architect - Preguntas y Respuestas

**P1: ¿Cómo defines estándares para un equipo de 20 devs frontend?**
> ESLint + Prettier configurados. Arquitectura documentada con ADRs. Code reviews obligatorios. Shared libraries internas. CI que valide estándares. Templates de componentes. Onboarding documentado.

**P2: ¿Monorepo o multirepo para micro frontends?**
> Monorepo (Nx) si un equipo mantiene todo: mejor DX, refactoring global, versiones consistentes. Multirepo si equipos son autónomos con diferentes ciclos de release.

**P3: ¿Cómo manejas la deuda técnica en frontend?**
> Medir con métricas (bundle size, coverage, lint errors). Dedicar 20% del sprint a tech debt. Priorizar por impacto en velocidad del equipo. Documentar decisiones y trade-offs.

## Tips para Entrevistas

```
1. Explica tu razonamiento, no solo la respuesta
2. Menciona trade-offs (no hay solución perfecta)
3. Da ejemplos de proyectos reales
4. Si no sabes algo, dilo honestamente
5. Pregunta por el contexto antes de responder
```
