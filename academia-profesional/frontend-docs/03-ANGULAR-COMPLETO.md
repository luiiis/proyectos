# Frontend Completo - Parte 3: Angular al 100%

---

# 1. QUE ES ANGULAR

Angular es un framework para construir SPAs (Single Page Applications).
Proporciona TODO lo que necesitas: componentes, routing, HTTP, forms, testing.

## Por que Angular (vs React/Vue)

```
Angular: framework COMPLETO (todo incluido, estructura opinada)
  ✅ Ideal para: enterprise, equipos grandes, apps complejas
  ✅ TypeScript obligatorio (menos bugs)
  ✅ Estructura consistente (todos los proyectos se ven igual)
  ❌ Curva de aprendizaje mas alta

React: LIBRERIA de UI (tu eliges el resto)
  ✅ Mas flexible, ecosistema enorme
  ❌ Cada proyecto es diferente (fatiga de decisiones)

Vue: framework PROGRESIVO (simple al inicio, complejo si necesitas)
  ✅ Facil de aprender, buena documentacion
  ❌ Menos demanda enterprise que Angular
```

---

# 2. COMPONENTES (el bloque fundamental)

```typescript
import { Component, signal, computed, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
    selector: 'app-productos',      // Nombre HTML: <app-productos>
    standalone: true,                // No necesita NgModule
    imports: [CommonModule],         // Dependencias del template
    template: `
        <h2>Productos ({{ total() }})</h2>
        
        @if (loading()) {
            <p>Cargando...</p>
        } @else {
            @for (producto of productos(); track producto.id) {
                <div class="card">
                    <h3>{{ producto.nombre }}</h3>
                    <p>{{ producto.precio | currency:'MXN' }}</p>
                    <button (click)="eliminar(producto.id)">Eliminar</button>
                </div>
            } @empty {
                <p>No hay productos</p>
            }
        }
    `,
    styles: [`
        .card { border: 1px solid #ddd; padding: 16px; margin: 8px 0; border-radius: 8px; }
    `]
})
export class ProductosComponent {
    private http = inject(HttpClient);

    // SIGNALS: estado reactivo
    productos = signal<Producto[]>([]);
    loading = signal(true);

    // COMPUTED: se recalcula automaticamente cuando productos() cambia
    total = computed(() => this.productos().length);

    constructor() {
        this.cargar();
    }

    cargar() {
        this.loading.set(true);
        this.http.get<Producto[]>('/api/productos').subscribe({
            next: (data) => { this.productos.set(data); this.loading.set(false); },
            error: () => this.loading.set(false)
        });
    }

    eliminar(id: number) {
        this.http.delete(`/api/productos/${id}`).subscribe(() => this.cargar());
    }
}
```

---

# 3. SERVICIOS (logica compartida)

```typescript
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })  // Singleton: 1 instancia para toda la app
export class ProductoService {
    private http = inject(HttpClient);
    private url = '/api/productos';

    listar(): Observable<Producto[]> {
        return this.http.get<Producto[]>(this.url);
    }

    buscar(id: number): Observable<Producto> {
        return this.http.get<Producto>(`${this.url}/${id}`);
    }

    crear(producto: CrearProducto): Observable<Producto> {
        return this.http.post<Producto>(this.url, producto);
    }

    actualizar(id: number, datos: Partial<Producto>): Observable<Producto> {
        return this.http.put<Producto>(`${this.url}/${id}`, datos);
    }

    eliminar(id: number): Observable<void> {
        return this.http.delete<void>(`${this.url}/${id}`);
    }
}

// USO en componente:
export class MiComponent {
    private service = inject(ProductoService);
    
    ngOnInit() {
        this.service.listar().subscribe(data => this.productos.set(data));
    }
}
```

---

# 4. ROUTING (navegacion)

```typescript
// app.routes.ts
import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
    // Ruta publica
    { path: 'login', loadComponent: () => import('./features/login/login.component').then(m => m.LoginComponent) },
    
    // Rutas protegidas (requieren autenticacion)
    {
        path: '',
        canActivate: [authGuard],  // Guard verifica token
        children: [
            { path: 'dashboard', loadComponent: () => import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent) },
            { path: 'productos', loadComponent: () => import('./features/productos/productos.component').then(m => m.ProductosComponent) },
            { path: 'productos/:id', loadComponent: () => import('./features/productos/detalle.component').then(m => m.DetalleComponent) },
            { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
        ]
    },
    
    // Ruta 404
    { path: '**', redirectTo: 'login' }
];

// loadComponent = LAZY LOADING: solo descarga el codigo cuando navegas a esa ruta
// Resultado: la app carga mas rapido (no descarga TODO al inicio)
```

---

# 5. FORMULARIOS REACTIVOS

```typescript
import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';

@Component({
    standalone: true,
    imports: [ReactiveFormsModule],
    template: `
        <form [formGroup]="form" (ngSubmit)="guardar()">
            <label>Nombre</label>
            <input formControlName="nombre">
            @if (form.get('nombre')?.errors?.['required'] && form.get('nombre')?.touched) {
                <span class="error">Nombre es requerido</span>
            }

            <label>Precio</label>
            <input type="number" formControlName="precio">
            @if (form.get('precio')?.errors?.['min']) {
                <span class="error">Precio debe ser mayor a 0</span>
            }

            <label>Email</label>
            <input formControlName="email">
            @if (form.get('email')?.errors?.['email']) {
                <span class="error">Email invalido</span>
            }

            <button type="submit" [disabled]="form.invalid">Guardar</button>
        </form>
    `
})
export class ProductoFormComponent {
    private fb = inject(FormBuilder);
    private service = inject(ProductoService);

    form: FormGroup = this.fb.group({
        nombre: ['', [Validators.required, Validators.minLength(3)]],
        precio: [0, [Validators.required, Validators.min(0.01)]],
        stock: [0, [Validators.min(0)]],
        email: ['', [Validators.email]],
        categoria: ['']
    });

    guardar() {
        if (this.form.valid) {
            this.service.crear(this.form.value).subscribe({
                next: () => alert('Producto creado!'),
                error: (err) => alert('Error: ' + err.message)
            });
        }
    }
}
```

---

# 6. INTERCEPTORS (modificar TODAS las peticiones HTTP)

```typescript
// auth.interceptor.ts
import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
    const auth = inject(AuthService);
    const router = inject(Router);
    const token = auth.getToken();

    // Agregar token a TODAS las peticiones
    if (token) {
        req = req.clone({
            setHeaders: { Authorization: `Bearer ${token}` }
        });
    }

    return next(req).pipe(
        catchError((error: HttpErrorResponse) => {
            if (error.status === 401) {
                // Token expirado o invalido → logout
                auth.logout();
                router.navigate(['/login']);
            }
            return throwError(() => error);
        })
    );
};

// Registrar en app.config.ts:
export const appConfig: ApplicationConfig = {
    providers: [
        provideHttpClient(withInterceptors([authInterceptor])),
    ]
};
```

---

# 7. SIGNALS (estado reactivo moderno)

```typescript
import { signal, computed, effect } from '@angular/core';

// signal(): valor reactivo que Angular RASTREA
const contador = signal(0);
contador();        // Leer: 0
contador.set(5);   // Escribir: 5
contador.update(v => v + 1);  // Actualizar basado en valor actual: 6

// computed(): valor DERIVADO (se recalcula automaticamente)
const doble = computed(() => contador() * 2);  // Siempre es contador * 2
// Si contador cambia de 6 a 10 → doble automaticamente es 20

// effect(): ejecutar codigo cuando un signal cambia
effect(() => {
    console.log('Contador cambio a:', contador());
    // Se ejecuta cada vez que contador() cambia
});

// Ejemplo real en componente:
export class ProductosComponent {
    productos = signal<Producto[]>([]);
    busqueda = signal('');
    
    // Se recalcula cuando productos() O busqueda() cambian
    filtrados = computed(() => {
        const term = this.busqueda().toLowerCase();
        if (!term) return this.productos();
        return this.productos().filter(p => 
            p.nombre.toLowerCase().includes(term)
        );
    });
    
    total = computed(() => this.filtrados().length);
    valorInventario = computed(() => 
        this.productos().reduce((sum, p) => sum + p.precio * p.stock, 0)
    );
}
```

---

# 8. GUARDS (proteger rutas)

```typescript
import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

// Guard funcional (Angular 15+)
export const authGuard: CanActivateFn = () => {
    const auth = inject(AuthService);
    const router = inject(Router);

    if (auth.isAuthenticated()) {
        return true;  // Permitir acceso
    }

    router.navigate(['/login']);
    return false;  // Bloquear acceso
};

// Guard por rol
export const adminGuard: CanActivateFn = () => {
    const auth = inject(AuthService);
    if (auth.isAdmin()) return true;
    return false;  // Solo admins
};

// Uso en rutas:
{ path: 'admin', canActivate: [authGuard, adminGuard], component: AdminComponent }
```

---

# 9. COMO EJECUTAR UN PROYECTO ANGULAR

```bash
# Crear proyecto nuevo
ng new mi-app --standalone --style=scss --routing

# Instalar dependencias
cd mi-app
npm install

# Agregar Angular Material
ng add @angular/material

# Generar componentes/servicios
ng generate component features/productos --standalone
ng generate service core/services/producto

# Ejecutar en desarrollo (hot reload)
ng serve
# → http://localhost:4200

# Compilar para produccion
ng build --configuration production
# Genera: dist/mi-app/ (archivos estaticos para Nginx)

# Ejecutar tests
ng test
```

---

# 10. RESUMEN: FLUJO COMPLETO FRONTEND

```
1. Usuario abre http://localhost:4200
2. Nginx sirve index.html + main.js (Angular compilado)
3. Angular se inicializa en el navegador
4. Router lee la URL → authGuard verifica token
5. Si no hay token → redirige a /login
6. Usuario escribe credenciales → POST /api/auth/login
7. Interceptor: no hay token aun (login es publico)
8. Backend valida → responde con JWT
9. AuthService guarda token en localStorage + actualiza signal
10. Router navega a /dashboard
11. DashboardComponent se carga (lazy loading)
12. Llama a GET /api/productos
13. Interceptor agrega: Authorization: Bearer eyJ...
14. Backend valida token → responde con JSON
15. Componente actualiza signal → Angular re-renderiza la tabla
16. Usuario ve los productos en pantalla
```
