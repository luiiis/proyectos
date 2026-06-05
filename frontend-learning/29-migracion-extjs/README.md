# Módulo 29: Migración de ExtJS a Angular

## 1. Tabla de Equivalencias

```
┌─────────────────────────┬──────────────────────────────────────┐
│ ExtJS                   │ Angular Equivalente                  │
├─────────────────────────┼──────────────────────────────────────┤
│ Ext.grid.Panel          │ mat-table / PrimeNG p-table          │
│ Ext.data.Store          │ Service + RxJS (BehaviorSubject)     │
│ Ext.panel.Panel         │ Component                            │
│ Ext.window.Window       │ MatDialog / DynamicDialog            │
│ Ext.form.ComboBox       │ mat-select / p-dropdown              │
│ Ext.tree.Panel          │ mat-tree / PrimeNG p-treeTable       │
│ Ext.tab.Panel           │ mat-tab-group / p-tabView            │
│ Ext.form.Panel          │ Reactive Forms + mat-form-field      │
│ Ext.toolbar.Toolbar     │ mat-toolbar                          │
│ Ext.menu.Menu           │ mat-menu                             │
│ Ext.data.Model          │ TypeScript interface                 │
│ Ext.Ajax                │ HttpClient                           │
│ Ext.MessageBox          │ MatDialog / ConfirmDialog            │
│ Ext.container.Viewport  │ AppComponent + Router                │
│ MVC Controller          │ Component + Service                  │
│ ViewModel               │ Signals / RxJS                       │
└─────────────────────────┴──────────────────────────────────────┘
```

## 2. Grid → mat-table / p-table

```javascript
// ExtJS Grid
Ext.create('Ext.grid.Panel', {
  store: usuarioStore,
  columns: [
    { text: 'Nombre', dataIndex: 'nombre', flex: 1 },
    { text: 'Email', dataIndex: 'email', flex: 1 },
  ],
  dockedItems: [{ xtype: 'pagingtoolbar', store: usuarioStore, dock: 'bottom' }]
});
```

```typescript
// Angular equivalente
@Component({
  standalone: true,
  imports: [TableModule],
  template: `
    <p-table [value]="usuarios()" [paginator]="true" [rows]="10">
      <ng-template pTemplate="header">
        <tr>
          <th pSortableColumn="nombre">Nombre</th>
          <th pSortableColumn="email">Email</th>
        </tr>
      </ng-template>
      <ng-template pTemplate="body" let-usuario>
        <tr>
          <td>{{ usuario.nombre }}</td>
          <td>{{ usuario.email }}</td>
        </tr>
      </ng-template>
    </p-table>
  `
})
export class UsuarioListComponent {
  private service = inject(UsuarioService);
  usuarios = signal<Usuario[]>([]);
}
```

## 3. Store → Service + RxJS/Signals

```javascript
// ExtJS Store
Ext.create('Ext.data.Store', {
  model: 'App.model.Usuario',
  proxy: { type: 'ajax', url: '/api/usuarios', reader: { rootProperty: 'data' } },
  autoLoad: true,
});
```

```typescript
// Angular Service (equivalente)
@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private http = inject(HttpClient);
  private usuarios = signal<Usuario[]>([]);

  readonly lista = this.usuarios.asReadonly();

  cargar(): void {
    this.http.get<ApiResponse<Usuario[]>>('/api/usuarios')
      .subscribe(res => this.usuarios.set(res.data));
  }

  agregar(usuario: Usuario): void {
    this.http.post<Usuario>('/api/usuarios', usuario)
      .subscribe(nuevo => this.usuarios.update(list => [...list, nuevo]));
  }
}
```

## 4. Window → MatDialog

```javascript
// ExtJS Window
Ext.create('Ext.window.Window', {
  title: 'Editar Usuario',
  width: 500, modal: true,
  items: [{ xtype: 'form', items: [...] }],
  buttons: [{ text: 'Guardar', handler: function() { ... } }]
});
```

```typescript
// Angular Dialog
this.dialog.open(UsuarioFormComponent, {
  width: '500px',
  data: { usuario, modo: 'editar' },
}).afterClosed().subscribe(result => {
  if (result) this.cargar();
});
```

## 5. Estrategia de Migración

```
Fase 1: Coexistencia
├── ExtJS app existente (iframe o micro-frontend)
└── Nueva app Angular (nuevas features)

Fase 2: Migración gradual
├── Migrar módulo por módulo (menos críticos primero)
├── Mantener misma API backend
└── Replicar UX existente

Fase 3: Reemplazo completo
├── Eliminar ExtJS
├── Angular como app principal
└── Optimizar y refactorizar

Regla: NO reescribir todo de una vez. Migrar incrementalmente.
```

## 6. Ejercicios

1. Migra un Grid de ExtJS a PrimeNG p-table con paginación y ordenamiento.
2. Convierte un Store de ExtJS a un Service Angular con signals.
3. Reemplaza un Window de ExtJS por un MatDialog con formulario reactivo.
4. Migra un ComboBox con carga remota a mat-select con HttpClient.
5. Crea un plan de migración para un módulo completo (Grid + Form + Store → Angular).

---

## Siguiente Módulo
→ [30-Proyecto Final](../30-proyecto-final/README.md)
