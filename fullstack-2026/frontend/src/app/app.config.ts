import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideZonelessChangeDetection } from '@angular/core';
import { routes } from './app.routes';
import { authInterceptor } from './core/interceptors/auth.interceptor';

/**
 * Configuración de la aplicación Angular 21.
 *
 * DIFERENCIAS con Angular 17 (tu proyecto anterior):
 *
 * 1. provideZonelessChangeDetection()
 *    - ANTES: Zone.js parchaba TODOS los eventos async y Angular revisaba TODO el árbol
 *    - AHORA: Sin Zone.js. Angular solo actualiza componentes que leen Signals que cambiaron
 *    - Resultado: menos JavaScript, más rápido, menos bugs de change detection
 *
 * 2. provideAnimationsAsync()
 *    - Carga el módulo de animaciones de forma lazy (no bloquea el arranque)
 *
 * 3. TanStack Query (opcional, se configura por componente)
 *    - Reemplaza el patrón manual de: loading + error + data + subscribe
 *    - Maneja caché, refetch, optimistic updates automáticamente
 */
export const appConfig: ApplicationConfig = {
  providers: [
    // Zoneless: Angular 21 no necesita Zone.js
    // Los Signals notifican directamente qué cambió
    provideZonelessChangeDetection(),

    // Router con lazy loading
    provideRouter(routes),

    // HTTP Client con interceptor de autenticación
    provideHttpClient(withInterceptors([authInterceptor])),

    // Animaciones cargadas async (no bloquean initial bundle)
    provideAnimationsAsync(),
  ]
};
