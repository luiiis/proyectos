# Examen Fase 2: Angular Core (Módulos 10-18)

## Instrucciones
- Tiempo: 120 minutos
- Puedes usar Angular CLI y VS Code
- El resultado debe compilar con `ng build`

---

## Sección A: Teoría (20 puntos)

1. ¿Qué es un standalone component y por qué se prefiere sobre NgModules?
2. Explica el lifecycle de un componente (ngOnInit, ngOnChanges, ngOnDestroy)
3. ¿Cuál es la diferencia entre `@Input()` y `input()` (signal-based)?
4. ¿Cómo funciona la inyección de dependencias en Angular?
5. ¿Cuál es la diferencia entre un Observable frío y uno caliente?

---

## Sección B: Proyecto Práctico (80 puntos)

### Construir: "Lista de Tareas" (Todo App) con Angular

**Requerimientos:**

### B1. Componentes (15 pts)
- `TodoListComponent` (smart): maneja estado, inyecta servicio
- `TodoItemComponent` (dumb): recibe @Input, emite @Output
- `TodoFormComponent`: formulario reactivo para agregar
- `TodoFilterComponent`: filtrar por estado (todas/pendientes/completadas)

### B2. Servicio + Estado con Signals (15 pts)
```typescript
@Injectable({ providedIn: 'root' })
export class TodoService {
  // Signals para estado reactivo:
  todos = signal<Todo[]>([]);
  filtro = signal<'todas' | 'pendientes' | 'completadas'>('todas');
  
  // Computed derivados:
  todosFiltrados = computed(() => ...);
  pendientes = computed(() => ...);
  completadas = computed(() => ...);
  
  // Métodos:
  agregar(titulo: string): void;
  completar(id: number): void;
  eliminar(id: number): void;
}
```

### B3. Formulario Reactivo (15 pts)
- Input con validación (mínimo 3 caracteres)
- Mostrar error de validación
- Deshabilitar botón si inválido
- Limpiar después de agregar

### B4. Routing (15 pts)
```
/              → redirige a /todos
/todos         → lista completa
/todos/activas → solo pendientes
/todos/completadas → solo completadas
/about         → página estática "Acerca de"
/**            → 404 Not Found
```
Implementar con lazy loading y guards.

### B5. HTTP Client (10 pts)
- Simular con `json-server` o mock interceptor
- GET /api/todos → cargar lista
- POST /api/todos → crear
- PATCH /api/todos/:id → toggle completado
- DELETE /api/todos/:id → eliminar
- Manejar errores con retry(2)

### B6. Estilos (10 pts)
- Diseño limpio y funcional
- Responsive (funciona en mobile)
- Animaciones en agregar/eliminar (Angular Animations o CSS)
- Item completado: tachado y gris

---

## Estructura esperada:
```
src/app/
├── features/
│   └── todos/
│       ├── components/
│       │   ├── todo-list.component.ts
│       │   ├── todo-item.component.ts
│       │   ├── todo-form.component.ts
│       │   └── todo-filter.component.ts
│       ├── services/
│       │   └── todo.service.ts
│       ├── models/
│       │   └── todo.model.ts
│       └── todos.routes.ts
├── core/
│   └── interceptors/
│       └── error.interceptor.ts
├── app.routes.ts
└── app.config.ts
```

---

## Aprobación
- 50+ = Entiende Angular básico
- 70+ = Puede construir features independientemente
- 85+ = Nivel junior-mid frontend Angular
