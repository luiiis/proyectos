# Módulo 17: Routing en Angular

## 1. Configuración de Rutas

```typescript
// app.routes.ts
export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [authGuard],
  },
  // Lazy loading por componente
  {
    path: 'usuarios',
    loadComponent: () => import('./features/usuarios/usuario-list.component')
      .then(m => m.UsuarioListComponent),
    canActivate: [authGuard],
  },
  // Lazy loading por rutas hijas
  {
    path: 'productos',
    loadChildren: () => import('./features/productos/producto.routes')
      .then(m => m.PRODUCTO_ROUTES),
  },
  { path: '**', component: NotFoundComponent },
];
```

## 2. Rutas Anidadas

```typescript
// producto.routes.ts
export const PRODUCTO_ROUTES: Routes = [
  { path: '', component: ProductoListComponent },
  { path: 'nuevo', component: ProductoFormComponent },
  { path: ':id', component: ProductoDetalleComponent },
  { path: ':id/editar', component: ProductoFormComponent },
];

// Acceder a parámetros
export class ProductoDetalleComponent {
  private route = inject(ActivatedRoute);
  private productoService = inject(ProductoService);

  producto = signal<Producto | null>(null);

  ngOnInit() {
    const id = +this.route.snapshot.params['id'];
    this.productoService.getById(id).subscribe(p => this.producto.set(p));
  }
}
```

## 3. Guards

```typescript
// auth.guard.ts (funcional - Angular 15+)
export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAuthenticated()) {
    return true;
  }
  return router.createUrlTree(['/login'], {
    queryParams: { returnUrl: state.url }
  });
};

// role.guard.ts
export const roleGuard: CanActivateFn = (route) => {
  const authService = inject(AuthService);
  const requiredRoles = route.data['roles'] as string[];
  return requiredRoles.includes(authService.getCurrentRole());
};

// Uso en rutas
{ path: 'admin', component: AdminComponent, 
  canActivate: [authGuard, roleGuard], data: { roles: ['admin'] } }
```

## 4. Resolvers

```typescript
// usuario.resolver.ts
export const usuarioResolver: ResolveFn<Usuario> = (route) => {
  const service = inject(UsuarioService);
  const id = +route.params['id'];
  return service.getById(id);
};

// En la ruta
{ path: ':id', component: UsuarioDetalleComponent, resolve: { usuario: usuarioResolver } }

// En el componente
export class UsuarioDetalleComponent {
  private route = inject(ActivatedRoute);
  usuario = this.route.snapshot.data['usuario'] as Usuario;
}
```

## 5. Navegación Programática

```typescript
export class UsuarioFormComponent {
  private router = inject(Router);

  onGuardar(usuario: Usuario) {
    this.usuarioService.crear(usuario).subscribe(creado => {
      this.router.navigate(['/usuarios', creado.id]);
    });
  }

  onCancelar() {
    this.router.navigate(['/usuarios']);
  }
}
```

## 6. Ejercicios

1. Configura rutas con lazy loading para 3 features (usuarios, productos, reportes).
2. Implementa un `authGuard` que redirija a login si no hay token.
3. Crea un `roleGuard` que valide permisos por ruta.
4. Implementa rutas anidadas para un CRUD (list, detail, create, edit).
5. Crea un resolver que cargue datos antes de mostrar un componente.

---

## Siguiente Módulo
→ [18-Forms](../18-forms/README.md)
