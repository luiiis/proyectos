# Módulo 15: RxJS - Manual Técnico

## ¿Qué es RxJS?
Librería de programación reactiva. Maneja flujos de datos asíncronos (HTTP, eventos, timers) de forma declarativa.

## ¿Por qué es importante para Angular?
- HttpClient devuelve Observables (no Promises)
- Router events son Observables
- Reactive Forms usan Observables (valueChanges)
- Es el "pegamento" entre componentes y servicios

## Analogía
```
Promise = Pedir una pizza (1 valor, 1 vez)
Observable = Suscribirse a Netflix (múltiples valores en el tiempo)
```

## Operadores más usados en Angular

| Operador | Qué hace | Cuándo usarlo |
|----------|----------|---------------|
| map | Transforma cada valor | Mapear respuesta HTTP |
| filter | Filtra valores | Ignorar valores vacíos |
| switchMap | Cancela anterior, usa nuevo | Búsqueda con debounce |
| mergeMap | Ejecuta en paralelo | Múltiples requests independientes |
| concatMap | Ejecuta en secuencia | Requests que dependen del anterior |
| combineLatest | Combina últimos valores | Filtros múltiples |
| forkJoin | Espera a que todos terminen | Cargar datos iniciales |
| debounceTime | Espera X ms sin cambios | Input de búsqueda |
| distinctUntilChanged | Ignora si no cambió | Evitar requests duplicados |
| takeUntil | Cancela cuando otro emite | Unsubscribe en ngOnDestroy |
| catchError | Maneja errores | Error handling en HTTP |
| tap | Efecto secundario | Logging, loading state |

## Cómo ejecutar los ejemplos
```bash
cd frontend-learning/15-rxjs/proyecto
npm install rxjs
ts-node ejemplos-rxjs.ts
```
