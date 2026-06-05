import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

/**
 * Auth Guard con Signals (Angular 21).
 *
 * DIFERENCIA: isAuthenticated() es un Signal, no un Observable.
 * Se lee como una función, no necesita subscribe.
 */
export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Signal se lee como función: authService.isAuthenticated()
  if (authService.isAuthenticated()) {
    return true;
  }

  // No autenticado → redirigir a login de Keycloak
  authService.login();
  return false;
};
