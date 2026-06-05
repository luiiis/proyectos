import { Injectable, signal, computed } from '@angular/core';
import { Router } from '@angular/router';
import Keycloak from 'keycloak-js';
import { environment } from '../../../environments/environment';

/**
 * Servicio de autenticación con Keycloak - Angular 21.
 *
 * DIFERENCIA FUNDAMENTAL con tu proyecto anterior:
 *
 * ANTES (JWT custom):
 *   1. Frontend tiene formulario de login
 *   2. Frontend envía username/password al backend
 *   3. Backend valida y genera JWT
 *   4. Frontend almacena JWT en localStorage
 *
 * AHORA (Keycloak OAuth2):
 *   1. Frontend REDIRIGE al login de Keycloak (página externa)
 *   2. Usuario se autentica EN Keycloak (puede ser con MFA, social login, etc.)
 *   3. Keycloak redirige de vuelta al frontend CON un token
 *   4. Frontend usa ese token para llamar al backend
 *   5. Backend VALIDA el token con la clave pública de Keycloak
 *
 * Ventajas:
 * - No manejas passwords en tu código
 * - MFA, social login, password policies GRATIS
 * - Single Sign-On entre múltiples apps
 * - Estándar OAuth2/OIDC (compatible con cualquier IdP)
 */
@Injectable({ providedIn: 'root' })
export class AuthService {
  private keycloak: Keycloak;

  // Signals para estado reactivo
  private _isAuthenticated = signal(false);
  private _username = signal<string | null>(null);
  private _roles = signal<string[]>([]);

  // Computed signals (públicos, solo lectura)
  isAuthenticated = this._isAuthenticated.asReadonly();
  username = this._username.asReadonly();
  roles = this._roles.asReadonly();
  isAdmin = computed(() => this._roles().includes('ADMIN'));
  isManager = computed(() => this._roles().includes('MANAGER'));

  constructor(private router: Router) {
    this.keycloak = new Keycloak({
      url: environment.keycloakUrl,
      realm: environment.keycloakRealm,
      clientId: environment.keycloakClientId,
    });
  }

  /**
   * Inicializar Keycloak (se llama al arrancar la app).
   * check-sso: verifica si ya hay sesión sin forzar login.
   */
  async init(): Promise<boolean> {
    try {
      const authenticated = await this.keycloak.init({
        onLoad: 'check-sso',
        silentCheckSsoRedirectUri: window.location.origin + '/assets/silent-check-sso.html',
      });

      this._isAuthenticated.set(authenticated);

      if (authenticated) {
        this._username.set(this.keycloak.tokenParsed?.preferred_username ?? null);
        this._roles.set(this.keycloak.tokenParsed?.realm_access?.roles ?? []);

        // Refrescar token automáticamente antes de que expire
        setInterval(() => this.keycloak.updateToken(30), 30000);
      }

      return authenticated;
    } catch (error) {
      console.error('Keycloak init failed:', error);
      return false;
    }
  }

  login(): void {
    this.keycloak.login();
  }

  logout(): void {
    this.keycloak.logout({ redirectUri: window.location.origin });
  }

  getToken(): string | undefined {
    return this.keycloak.token;
  }
}
