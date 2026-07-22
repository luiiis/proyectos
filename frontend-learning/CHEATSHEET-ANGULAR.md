# Angular Cheatsheet — Referencia Rápida

## CLI Commands
```bash
ng new mi-app --standalone --style=scss --routing
ng generate component features/productos/lista --standalone
ng generate service core/services/auth
ng serve                    # → localhost:4200
ng build --configuration production
ng test --run               # Unit tests
```

## Componente Standalone
```typescript
@Component({
  selector: 'app-producto',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <h2>{{ producto().nombre }}</h2>
    <p>{{ producto().precio | currency:'MXN' }}</p>
    <button (click)="comprar.emit(producto())">Comprar</button>
  `
})
export class ProductoComponent {
  producto = input.required<Producto>();
  comprar = output<Producto>();
}
```

## Signals
```typescript
// Estado
count = signal(0);
items = signal<Producto[]>([]);

// Derivado (se recalcula automáticamente)
total = computed(() => this.items().reduce((s, i) => s + i.precio, 0));
isEmpty = computed(() => this.items().length === 0);

// Modificar
this.count.set(5);
this.count.update(c => c + 1);
this.items.update(list => [...list, nuevoItem]);
```

## Servicio
```typescript
@Injectable({ providedIn: 'root' })
export class ProductoService {
  private http = inject(HttpClient);
  private apiUrl = '/api/productos';

  getAll(): Observable<Producto[]> { return this.http.get<Producto[]>(this.apiUrl); }
  getById(id: number): Observable<Producto> { return this.http.get<Producto>(`${this.apiUrl}/${id}`); }
  create(p: ProductoCreate): Observable<Producto> { return this.http.post<Producto>(this.apiUrl, p); }
  update(id: number, p: ProductoUpdate): Observable<Producto> { return this.http.put<Producto>(`${this.apiUrl}/${id}`, p); }
  delete(id: number): Observable<void> { return this.http.delete<void>(`${this.apiUrl}/${id}`); }
}
```

## Routing
```typescript
export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'login', loadComponent: () => import('./login.component').then(m => m.LoginComponent) },
  { path: 'dashboard', canActivate: [authGuard], loadComponent: () => import('./dashboard.component').then(m => m.DashboardComponent) },
  { path: '**', loadComponent: () => import('./not-found.component').then(m => m.NotFoundComponent) }
];
```

## Interceptor
```typescript
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = inject(AuthService).getToken();
  if (token) req = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
  return next(req).pipe(
    catchError(err => { if (err.status === 401) inject(Router).navigate(['/login']); return throwError(() => err); })
  );
};
```

## Guard
```typescript
export const authGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  return auth.isAuthenticated() || inject(Router).createUrlTree(['/login']);
};
```

## Reactive Forms
```typescript
form = new FormGroup({
  nombre: new FormControl('', [Validators.required, Validators.minLength(3)]),
  email: new FormControl('', [Validators.required, Validators.email]),
  precio: new FormControl(0, [Validators.required, Validators.min(1)])
});

// Template:
// <form [formGroup]="form" (ngSubmit)="guardar()">
//   <input formControlName="nombre">
//   @if (form.get('nombre')?.hasError('required')) { <span>Obligatorio</span> }
// </form>
```

## Template Syntax (Angular 17+)
```html
<!-- Control flow -->
@if (loading()) { <spinner /> }
@else if (error()) { <p>Error: {{ error() }}</p> }
@else { <app-lista [items]="items()" /> }

@for (item of items(); track item.id) {
  <app-card [data]="item" />
} @empty {
  <p>No hay elementos</p>
}

@switch (estado()) {
  @case ('activo') { <badge color="green">Activo</badge> }
  @case ('inactivo') { <badge color="red">Inactivo</badge> }
  @default { <badge>Desconocido</badge> }
}

<!-- Defer (lazy component) -->
@defer (on viewport) { <app-grafica-pesada /> }
@placeholder { <p>Cargando...</p> }
```

## RxJS Operators
```typescript
// Búsqueda con debounce:
this.searchInput.valueChanges.pipe(
  debounceTime(300),
  distinctUntilChanged(),
  filter(term => term.length >= 3),
  switchMap(term => this.service.buscar(term))
).subscribe(results => this.results.set(results));

// Combinar filtros:
combineLatest([this.categoria$, this.orden$]).pipe(
  switchMap(([cat, orden]) => this.service.getFiltered(cat, orden))
);

// Retry con backoff:
this.http.get('/api/x').pipe(
  retry({ count: 3, delay: (err, count) => timer(count * 1000) })
);
```

## Testing
```typescript
describe('ProductoService', () => {
  let service: ProductoService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideHttpClientTesting()] });
    service = TestBed.inject(ProductoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should get all productos', () => {
    service.getAll().subscribe(data => expect(data.length).toBe(2));
    const req = httpMock.expectOne('/api/productos');
    expect(req.request.method).toBe('GET');
    req.flush([{ id: 1 }, { id: 2 }]);
  });
});
```

## PrimeNG (componentes más usados)
```html
<p-table [value]="items" [paginator]="true" [rows]="10" [sortField]="'nombre'">
  <ng-template pTemplate="header">
    <tr><th pSortableColumn="nombre">Nombre</th><th>Precio</th></tr>
  </ng-template>
  <ng-template pTemplate="body" let-item>
    <tr><td>{{item.nombre}}</td><td>{{item.precio | currency}}</td></tr>
  </ng-template>
</p-table>

<p-dialog [(visible)]="show" header="Título" [modal]="true">Contenido</p-dialog>
<p-toast />
<p-confirmDialog />
<button pButton label="Guardar" (click)="save()" [loading]="saving" />
```
