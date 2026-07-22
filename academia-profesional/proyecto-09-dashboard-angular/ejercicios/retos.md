# Ejercicios y Retos - Proyecto 09: Dashboard Angular

## Reto 1: Agregar gráficas
Instala `ngx-charts` o `chart.js` y agrega:
- Gráfica de barras: ventas por mes
- Gráfica de pie: ventas por categoría
- Gráfica de línea: evolución de ventas últimos 7 días

**Lo que practicas:** Librerías de terceros en Angular, data binding

---

## Reto 2: Tema oscuro (Dark Mode)
Implementa toggle de tema claro/oscuro:
- Guardar preferencia en localStorage
- Usar CSS variables para colores
- Signal para el estado del tema

```typescript
theme = signal<'light' | 'dark'>('light');
toggleTheme() { this.theme.update(t => t === 'light' ? 'dark' : 'light'); }
```

**Lo que practicas:** Signals, CSS variables, localStorage, preferencias de usuario

---

## Reto 3: KPIs en tiempo real (simulado)
Usa `interval()` de RxJS para simular datos que cambian cada 5 segundos:
```typescript
ventas$ = interval(5000).pipe(
  switchMap(() => this.http.get('/api/dashboard/kpis'))
);
```
Los números deben actualizarse automáticamente sin recargar.

**Lo que practicas:** RxJS observables, interval, switchMap, async pipe

---

## Reto 4: Lazy Loading de módulos
Agrega 3 secciones nuevas con lazy loading:
- `/productos` → ProductosModule (se carga solo al navegar)
- `/clientes` → ClientesModule
- `/reportes` → ReportesModule

Verifica en Network (DevTools) que cada módulo se descarga solo cuando navegas a él.

**Lo que practicas:** Lazy loading, routing, code splitting

---

## Reto 5: Sidebar responsive
Crea un sidebar de navegación que:
- En desktop (>768px): siempre visible al lado izquierdo
- En mobile (<768px): se oculta y aparece con botón hamburguesa
- Marcar la ruta activa con `routerLinkActive`

**Lo que practicas:** Responsive design, Angular Material sidenav, media queries

---

## Reto 6: Interceptor de loading
Crea un interceptor que muestre un spinner global mientras haya peticiones HTTP en curso:
```typescript
export const loadingInterceptor: HttpInterceptorFn = (req, next) => {
  loadingService.show();
  return next(req).pipe(finalize(() => loadingService.hide()));
};
```

**Lo que practicas:** HttpInterceptorFn, servicios globales, UX

---

## Reto 7: Manejo de sesión expirada
Cuando el backend responde 401:
1. El interceptor detecta el error
2. Limpia el token de localStorage
3. Redirige a /login
4. Muestra mensaje "Tu sesión expiró"

**Lo que practicas:** Error handling en interceptors, Router, notificaciones

---

## Reto 8 (Avanzado): Dashboard con WebSocket
Conecta el dashboard al WebSocket del proyecto 22:
- Cuando se registra una venta → el KPI se actualiza instantáneamente
- Sin polling, sin recargar

**Lo que practicas:** WebSocket + STOMP en Angular, eventos en tiempo real
