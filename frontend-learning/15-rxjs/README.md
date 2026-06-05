# Módulo 15: RxJS - Programación Reactiva

## 1. Observable, Observer, Subscription

```typescript
// Observable: flujo de datos en el tiempo
const numeros$ = new Observable<number>(subscriber => {
  subscriber.next(1);
  subscriber.next(2);
  subscriber.next(3);
  subscriber.complete();
});

// Observer: quien recibe los datos
numeros$.subscribe({
  next: valor => console.log(valor),
  error: err => console.error(err),
  complete: () => console.log('Completado'),
});
```

## 2. Subject y BehaviorSubject

```typescript
// Subject: multicast, no tiene valor inicial
const eventos$ = new Subject<string>();
eventos$.next('click');  // se pierde si nadie está suscrito

// BehaviorSubject: tiene valor inicial, emite último valor a nuevos suscriptores
@Injectable({ providedIn: 'root' })
export class AuthStateService {
  private usuarioSubject = new BehaviorSubject<Usuario | null>(null);
  usuario$ = this.usuarioSubject.asObservable();  // exponer solo lectura

  login(usuario: Usuario) {
    this.usuarioSubject.next(usuario);
  }

  logout() {
    this.usuarioSubject.next(null);
  }

  get isLoggedIn(): boolean {
    return this.usuarioSubject.value !== null;
  }
}
```

## 3. Operadores - Marble Diagrams

```
map: transforma cada valor
source:   --1--2--3--4--|
map(x*2): --2--4--6--8--|

filter: filtra valores
source:        --1--2--3--4--5--|
filter(x > 2): --------3--4--5--|

switchMap: cancela anterior, usa el nuevo
clicks:    --c------c------c--|
switchMap:  --[req1]--x     
                     [req2]-x
                            [req3]--|

mergeMap: ejecuta todos en paralelo
clicks:    --c------c------c--|
mergeMap:   --[req1]----------|
                    [req2]----|
                           [req3]--|
```

## 4. Operadores Más Usados

```typescript
// map + filter
this.productos$ = this.productoService.getAll().pipe(
  map(productos => productos.filter(p => p.activo)),
  map(productos => productos.sort((a, b) => a.nombre.localeCompare(b.nombre))),
);

// switchMap: búsqueda con cancelación
this.resultados$ = this.searchControl.valueChanges.pipe(
  debounceTime(300),
  distinctUntilChanged(),
  filter(term => term.length >= 3),
  switchMap(term => this.productoService.buscar(term)),
);

// combineLatest: combinar múltiples fuentes
this.viewModel$ = combineLatest([
  this.usuarios$,
  this.filtro$,
  this.ordenamiento$,
]).pipe(
  map(([usuarios, filtro, orden]) => ({
    datos: this.aplicarFiltro(usuarios, filtro, orden),
    total: usuarios.length,
  })),
);

// forkJoin: esperar que todos completen (como Promise.all)
forkJoin({
  usuarios: this.usuarioService.getAll(),
  roles: this.rolService.getAll(),
  permisos: this.permisoService.getAll(),
}).subscribe(({ usuarios, roles, permisos }) => {
  this.inicializarFormulario(usuarios, roles, permisos);
});
```

## 5. Patrones Comunes en Angular

```typescript
// Patrón: auto-unsubscribe con takeUntilDestroyed
export class DashboardComponent {
  private destroyRef = inject(DestroyRef);

  ngOnInit() {
    this.dataService.getData().pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe(data => this.datos.set(data));
  }
}

// Patrón: loading + error
export class UsuarioListComponent {
  loading = signal(false);
  error = signal<string | null>(null);
  usuarios = signal<Usuario[]>([]);

  cargar() {
    this.loading.set(true);
    this.usuarioService.getAll().pipe(
      finalize(() => this.loading.set(false))
    ).subscribe({
      next: data => this.usuarios.set(data),
      error: err => this.error.set(err.message),
    });
  }
}
```

## 6. Ejercicios

1. Crea un BehaviorSubject para manejar el estado de autenticación (login/logout).
2. Implementa un buscador con `debounceTime`, `distinctUntilChanged` y `switchMap`.
3. Usa `combineLatest` para combinar filtros (texto + categoría + orden) sobre una lista.
4. Implementa `forkJoin` para cargar datos iniciales de un formulario (roles + departamentos).
5. Crea un servicio de notificaciones usando Subject con auto-dismiss (delay + take).

---

## Siguiente Módulo
→ [16-HttpClient](../16-http-client/README.md)
