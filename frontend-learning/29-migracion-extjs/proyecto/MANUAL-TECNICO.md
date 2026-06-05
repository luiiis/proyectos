# Módulo 29: Migración ExtJS → Angular - Manual Técnico

## ¿Por qué migrar de ExtJS a Angular?
- ExtJS tiene licencia costosa ($$$)
- Comunidad pequeña y decreciente
- Difícil encontrar desarrolladores ExtJS
- Angular tiene ecosistema moderno, tooling superior, mejor performance

## Tabla de Equivalencias ExtJS → Angular

| ExtJS | Angular Equivalente | Librería |
|-------|-------------------|----------|
| `Ext.grid.Panel` | `mat-table` / `p-table` | Angular Material / PrimeNG |
| `Ext.data.Store` | `Service + HttpClient + BehaviorSubject` | RxJS |
| `Ext.form.Panel` | `Reactive Forms (FormGroup)` | @angular/forms |
| `Ext.window.Window` | `MatDialog` / `DynamicDialog` | Material / PrimeNG |
| `Ext.form.ComboBox` | `mat-select` / `p-dropdown` | Material / PrimeNG |
| `Ext.tree.Panel` | `mat-tree` / `p-tree` | Material / PrimeNG |
| `Ext.tab.Panel` | `mat-tab-group` / `p-tabView` | Material / PrimeNG |
| `Ext.panel.Panel` | `mat-card` / `mat-expansion-panel` | Material |
| `Ext.toolbar.Toolbar` | `mat-toolbar` | Material |
| `Ext.menu.Menu` | `mat-menu` | Material |
| `Ext.MessageBox` | `MatSnackBar` / `ConfirmDialog` | Material / PrimeNG |
| `Ext.Ajax` | `HttpClient` | @angular/common/http |
| `Ext.util.Observable` | `Subject / BehaviorSubject` | RxJS |
| `Ext.Component` | `@Component` | @angular/core |
| `Ext.app.ViewController` | Component class methods | - |
| `Ext.app.ViewModel` | Signals / Service state | - |

## Ejemplo: Grid ExtJS → mat-table Angular

### ExtJS (antes):
```javascript
Ext.create('Ext.grid.Panel', {
    title: 'Productos',
    store: {
        type: 'ajax',
        url: '/api/productos',
        reader: { type: 'json', rootProperty: 'data' }
    },
    columns: [
        { text: 'Nombre', dataIndex: 'nombre', flex: 1 },
        { text: 'Precio', dataIndex: 'precio', renderer: Ext.util.Format.usMoney },
        { text: 'Stock', dataIndex: 'stock' }
    ],
    bbar: { xtype: 'pagingtoolbar' }
});
```

### Angular (después):
```typescript
@Component({
  standalone: true,
  imports: [MatTableModule, MatPaginatorModule, MatSortModule],
  template: `
    <table mat-table [dataSource]="dataSource" matSort>
      <ng-container matColumnDef="nombre">
        <th mat-header-cell *matHeaderCellDef mat-sort-header>Nombre</th>
        <td mat-cell *matCellDef="let row">{{row.nombre}}</td>
      </ng-container>
      <ng-container matColumnDef="precio">
        <th mat-header-cell *matHeaderCellDef mat-sort-header>Precio</th>
        <td mat-cell *matCellDef="let row">{{row.precio | currency}}</td>
      </ng-container>
      <ng-container matColumnDef="stock">
        <th mat-header-cell *matHeaderCellDef>Stock</th>
        <td mat-cell *matCellDef="let row">{{row.stock}}</td>
      </ng-container>
      <tr mat-header-row *matHeaderRowDef="['nombre','precio','stock']"></tr>
      <tr mat-row *matRowDef="let row; columns: ['nombre','precio','stock']"></tr>
    </table>
    <mat-paginator [pageSizeOptions]="[10, 25, 50]"></mat-paginator>
  `
})
export class ProductosComponent implements AfterViewInit {
  dataSource = new MatTableDataSource<Producto>();
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private productoService: ProductoService) {
    this.productoService.getAll().subscribe(data => this.dataSource.data = data);
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }
}
```

## Estrategia de Migración Recomendada

```
Fase 1 (2-4 semanas): Setup
  - Crear proyecto Angular paralelo
  - Configurar routing equivalente
  - Instalar Angular Material / PrimeNG

Fase 2 (4-8 semanas): Migrar pantalla por pantalla
  - Empezar por pantallas SIMPLES (catálogos, listados)
  - Cada pantalla ExtJS → componente Angular
  - Mantener ambos sistemas corriendo (iframe o micro-frontend)

Fase 3 (2-4 semanas): Migrar pantallas complejas
  - Formularios con validaciones
  - Grids editables
  - Trees y paneles anidados

Fase 4 (2 semanas): Cutover
  - Redirigir todo el tráfico a Angular
  - Descomisionar ExtJS
  - Eliminar licencia ExtJS ($$$)
```
