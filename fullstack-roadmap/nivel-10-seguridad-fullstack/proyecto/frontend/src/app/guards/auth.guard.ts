import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * Route Guard: protege rutas que requieren autenticación.
 *
 * Si el usuario NO tiene token → redirige a /login.
 * Se usa en las rutas: canActivate: [authGuard]
 */
export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAuthenticated()) {
    return true;
  }

  router.navigate(['/login']);
  return false;
};

/**
 * Role Guard: protege rutas según el rol del usuario.
 * Ejemplo: canActivate: [roleGuard('ROLE_ADMIN')]
 */
export function roleGuard(requiredRole: string): CanActivateFn {
  return () => {
    const authService = inject(AuthService);
    const router = inject(Router);

    if (authService.hasRole(requiredRole)) {
      return true;
    }

    router.navigate(['/acceso-denegado']);
    return false;
  };
}
