# Módulo 28: Arquitectura Frontend

## 1. Clean Architecture Frontend

```
┌─────────────────────────────────────────────────────────────┐
│  src/app/                                                   │
├─────────────────────────────────────────────────────────────┤
│  core/                    ← Singleton, app-wide             │
│  ├── services/            (AuthService, HttpInterceptors)   │
│  ├── guards/                                                │
│  ├── interceptors/                                          │
│  ├── models/              (interfaces globales)             │
│  └── constants/                                             │
│                                                             │
│  shared/                  ← Reutilizable, sin estado        │
│  ├── components/          (Button, Modal, DataTable)        │
│  ├── directives/                                            │
│  ├── pipes/                                                 │
│  └── utils/                                                 │
│                                                             │
│  features/                ← Módulos de negocio              │
│  ├── auth/                                                  │
│  │   ├── pages/           (LoginPage, RegisterPage)         │
│  │   ├── components/      (LoginForm, SocialButtons)        │
│  │   ├── services/        (AuthService)                     │
│  │   ├── models/          (User, LoginRequest)              │
│  │   └── auth.routes.ts                                     │
│  ├── usuarios/                                              │
│  ├── productos/                                             │
│  └── reportes/                                              │
└─────────────────────────────────────────────────────────────┘
```

## 2. Smart vs Dumb Components

```typescript
// SMART (Container) - tiene lógica, inyecta servicios
@Component({
  selector: 'app-usuario-page',
  standalone: true,
  imports: [UsuarioTableComponent, UsuarioFormDialogComponent],
  template: `
    <app-usuario-table
      [usuarios]="usuarios()"
      [loading]="loading()"
      (editar)="onEditar($event)"
      (eliminar)="onEliminar($event)"
    />
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UsuarioPageComponent {
  private service = inject(UsuarioService);
  usuarios = signal<Usuario[]>([]);
  loading = signal(false);

  ngOnInit() { this.cargar(); }
  cargar() { /* lógica */ }
  onEditar(u: Usuario) { /* lógica */ }
  onEliminar(u: Usuario) { /* lógica */ }
}

// DUMB (Presentational) - solo inputs/outputs, sin servicios
@Component({
  selector: 'app-usuario-table',
  standalone: true,
  imports: [MatTableModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `...`
})
export class UsuarioTableComponent {
  usuarios = input.required<Usuario[]>();
  loading = input(false);
  editar = output<Usuario>();
  eliminar = output<Usuario>();
}
```

## 3. Barrel Exports (index.ts)

```typescript
// features/usuarios/index.ts
export { UsuarioPageComponent } from './pages/usuario-page.component';
export { UsuarioService } from './services/usuario.service';
export { USUARIO_ROUTES } from './usuario.routes';

// Uso desde otro lugar:
import { UsuarioService } from '@features/usuarios';
// En lugar de:
import { UsuarioService } from '../../../features/usuarios/services/usuario.service';
```

```json
// tsconfig.json - path aliases
{
  "compilerOptions": {
    "paths": {
      "@core/*": ["src/app/core/*"],
      "@shared/*": ["src/app/shared/*"],
      "@features/*": ["src/app/features/*"],
      "@env": ["src/environments/environment"]
    }
  }
}
```

## 4. Feature-Based Structure

```
features/productos/
├── pages/
│   ├── producto-list-page.component.ts    ← Smart
│   └── producto-detail-page.component.ts  ← Smart
├── components/
│   ├── producto-table.component.ts        ← Dumb
│   ├── producto-card.component.ts         ← Dumb
│   └── producto-form.component.ts         ← Dumb
├── services/
│   └── producto.service.ts
├── models/
│   └── producto.model.ts
├── producto.routes.ts
└── index.ts                               ← Barrel export
```

## 5. Reglas de Dependencia

```
┌──────────────────────────────────────────┐
│  features/ puede importar de:            │
│    ✅ core/                              │
│    ✅ shared/                            │
│    ❌ otros features/ (acoplamiento)     │
│                                          │
│  shared/ puede importar de:             │
│    ✅ core/ (solo models/constants)      │
│    ❌ features/                          │
│                                          │
│  core/ NO importa de:                   │
│    ❌ features/                          │
│    ❌ shared/                            │
└──────────────────────────────────────────┘
```

## 6. Ejercicios

1. Reestructura un proyecto plano a la arquitectura core/shared/features.
2. Separa un componente grande en Smart (page) + Dumb (table, form, card).
3. Crea barrel exports (index.ts) y configura path aliases en tsconfig.json.
4. Implementa un feature completo (routes, pages, components, services, models).
5. Valida las reglas de dependencia: ningún feature importa de otro feature.

---

## Siguiente Módulo
→ [29-Migración ExtJS](../29-migracion-extjs/README.md)
