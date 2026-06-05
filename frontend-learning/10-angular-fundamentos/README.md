# Módulo 10: Angular - Fundamentos

## 1. Angular CLI

```bash
# Instalar
npm install -g @angular/cli

# Crear proyecto (standalone por defecto en Angular 17+)
ng new mi-app --style=scss --routing

# Generar componentes y servicios
ng generate component features/usuarios/usuario-list
ng generate service core/services/usuario
ng generate guard core/guards/auth

# Desarrollo
ng serve                    # http://localhost:4200
ng build --configuration=production
ng test                     # unit tests con Karma
```

## 2. Arquitectura Angular

```
┌─────────────────────────────────────────────────┐
│                  Angular App                     │
├─────────────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────┐              │
│  │  Component  │  │  Component  │  ← Vista      │
│  │  (Template) │  │  (Template) │              │
│  └──────┬──────┘  └──────┬──────┘              │
│         │                 │                     │
│  ┌──────▼─────────────────▼──────┐              │
│  │         Services              │  ← Lógica    │
│  │  (Injectable, Singleton)      │              │
│  └──────────────┬────────────────┘              │
│                 │                               │
│  ┌──────────────▼────────────────┐              │
│  │      HttpClient + RxJS        │  ← Datos     │
│  │   (Comunicación con API)      │              │
│  └───────────────────────────────┘              │
├─────────────────────────────────────────────────┤
│  Router → Lazy Loading → Guards → Resolvers     │
└─────────────────────────────────────────────────┘
```

## 3. Standalone Components (Angular 17+)

```typescript
// ✅ Standalone (moderno - preferir siempre)
@Component({
  selector: 'app-usuario-list',
  standalone: true,
  imports: [CommonModule, RouterLink, MatTableModule],
  templateUrl: './usuario-list.component.html',
})
export class UsuarioListComponent {
  usuarios = signal<Usuario[]>([]);
}

// Configuración en main.ts (sin AppModule)
bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([authInterceptor])),
    provideAnimations(),
  ],
});
```

## 4. Modules vs Standalone

```typescript
// ❌ NgModule (legacy - proyectos antiguos)
@NgModule({
  declarations: [UsuarioListComponent, UsuarioFormComponent],
  imports: [CommonModule, SharedModule],
  exports: [UsuarioListComponent],
})
export class UsuariosModule {}

// ✅ Standalone (Angular 17+) - sin módulos
// Cada componente declara sus propias dependencias
// Más tree-shakeable, más simple, lazy loading por componente
```

## 5. Estructura de Proyecto Recomendada

```
src/app/
├── core/                    # Singleton services, guards, interceptors
│   ├── services/
│   ├── guards/
│   └── interceptors/
├── shared/                  # Componentes reutilizables
│   ├── components/
│   └── pipes/
├── features/                # Módulos de negocio
│   ├── auth/
│   ├── usuarios/
│   ├── productos/
│   └── dashboard/
├── app.component.ts
├── app.config.ts
└── app.routes.ts
```

## 6. Ejercicios

1. Crea un proyecto Angular con CLI y explora la estructura generada.
2. Genera 3 componentes standalone y configura rutas básicas entre ellos.
3. Crea un servicio que retorne datos mock y consúmelo desde un componente.
4. Configura `provideHttpClient` y `provideRouter` en `app.config.ts`.
5. Implementa la estructura de carpetas recomendada (core, shared, features).

---

## Siguiente Módulo
→ [11-Componentes](../11-componentes/README.md)
