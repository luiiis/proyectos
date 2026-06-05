import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

/**
 * Interceptor de autenticación - Angular 21.
 *
 * Mismo concepto que tu proyecto anterior, pero ahora el token
 * viene de Keycloak (no de un endpoint /api/auth/login custom).
 *
 * Keycloak JS adapter maneja:
 * - Obtener el token
 * - Refrescar el token cuando expira
 * - Redirigir a login si no hay sesión
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  if (token) {
    req = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
  }

  return next(req);
};
