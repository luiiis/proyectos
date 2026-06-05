# Módulo 14: Servicios e Inyección de Dependencias

## 1. @Injectable y DI

```typescript
@Injectable({
  providedIn: 'root'  // Singleton en toda la app
})
export class UsuarioService {
  private http = inject(HttpClient);
  private apiUrl = '/api/usuarios';

  getUsuarios(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(this.apiUrl);
  }

  getUsuario(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.apiUrl}/${id}`);
  }

  crear(usuario: UsuarioCrear): Observable<Usuario> {
    return this.http.post<Usuario>(this.apiUrl, usuario);
  }

  actualizar(id: number, datos: Partial<Usuario>): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.apiUrl}/${id}`, datos);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
```

## 2. Inyección con inject()

```typescript
// ✅ Moderno: inject() (Angular 14+)
@Component({ ... })
export class UsuarioListComponent {
  private usuarioService = inject(UsuarioService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  usuarios = signal<Usuario[]>([]);

  ngOnInit() {
    this.usuarioService.getUsuarios().subscribe(data => {
      this.usuarios.set(data);
    });
  }
}

// ❌ Antiguo: constructor injection
constructor(
  private usuarioService: UsuarioService,
  private router: Router
) {}
```

## 3. Singleton vs Scoped

```typescript
// Singleton (una instancia para toda la app)
@Injectable({ providedIn: 'root' })
export class AuthService { }

// Scoped (una instancia por componente)
@Component({
  providers: [FormValidatorService]  // nueva instancia por componente
})
export class UsuarioFormComponent { }
```

## 4. Service Layer Architecture

```
┌─────────────────────────────────────────────┐
│  Component (Vista)                          │
│  - Solo lógica de presentación              │
│  - Llama al service                         │
└──────────────────┬──────────────────────────┘
                   │ inject()
┌──────────────────▼──────────────────────────┐
│  Service (Lógica de negocio)                │
│  - Transformar datos                        │
│  - Validaciones                             │
│  - Orquestar llamadas                       │
└──────────────────┬──────────────────────────┘
                   │ inject(HttpClient)
┌──────────────────▼──────────────────────────┐
│  HttpClient (Comunicación)                  │
│  - GET, POST, PUT, DELETE                   │
│  - Interceptors                             │
└─────────────────────────────────────────────┘
```

```typescript
// Servicio con lógica de negocio
@Injectable({ providedIn: 'root' })
export class PedidoService {
  private http = inject(HttpClient);
  private authService = inject(AuthService);

  crearPedido(items: ItemCarrito[]): Observable<Pedido> {
    const pedido = {
      usuarioId: this.authService.getCurrentUserId(),
      items: items.map(i => ({ productoId: i.id, cantidad: i.cantidad })),
      total: items.reduce((sum, i) => sum + i.precio * i.cantidad, 0),
      fecha: new Date().toISOString(),
    };
    return this.http.post<Pedido>('/api/pedidos', pedido);
  }
}
```

## 5. Ejercicios

1. Crea un `ProductoService` con métodos CRUD que use HttpClient.
2. Implementa un `AuthService` singleton con login, logout y getCurrentUser.
3. Crea un servicio scoped `FormStateService` que maneje el estado de un formulario.
4. Implementa un `NotificationService` que encapsule MatSnackBar.
5. Crea una arquitectura de 3 capas: Component → Service → HttpClient para un CRUD.

---

## Siguiente Módulo
→ [15-RxJS](../15-rxjs/README.md)
