# Módulo 19: Angular Material - Manual Técnico

## ¿Qué construimos?
Componentes empresariales con Angular Material: tabla con paginación/filtro/sort, diálogos, formularios con stepper, menús y toolbar.

## Cómo instalar Angular Material
```bash
ng add @angular/material
# Elige: tema Indigo/Pink, tipografía global: Yes, animaciones: Yes
```

## Componentes más usados en enterprise

| Componente | Uso | Import |
|-----------|-----|--------|
| mat-table | Tablas de datos con sort/filter/paginate | MatTableModule |
| mat-dialog | Modales (crear/editar/confirmar) | MatDialogModule |
| mat-form-field | Inputs estilizados | MatFormFieldModule |
| mat-toolbar | Barra superior | MatToolbarModule |
| mat-sidenav | Menú lateral | MatSidenavModule |
| mat-stepper | Formularios multi-paso | MatStepperModule |
| mat-snackbar | Notificaciones toast | MatSnackBarModule |
| mat-menu | Menús desplegables | MatMenuModule |
| mat-paginator | Paginación | MatPaginatorModule |
| mat-sort | Ordenamiento de columnas | MatSortModule |

## Ejemplo: Tabla con todo (el componente más usado)

```typescript
@Component({
  standalone: true,
  imports: [MatTableModule, MatPaginatorModule, MatSortModule, MatInputModule],
  template: `
    <mat-form-field>
      <mat-label>Buscar</mat-label>
      <input matInput (keyup)="filtrar($event)">
    </mat-form-field>

    <table mat-table [dataSource]="dataSource" matSort>
      <ng-container matColumnDef="nombre">
        <th mat-header-cell *matHeaderCellDef mat-sort-header>Nombre</th>
        <td mat-cell *matCellDef="let row">{{row.nombre}}</td>
      </ng-container>
      <!-- más columnas... -->
      <tr mat-header-row *matHeaderRowDef="columnas"></tr>
      <tr mat-row *matRowDef="let row; columns: columnas"></tr>
    </table>

    <mat-paginator [pageSizeOptions]="[5, 10, 25]"></mat-paginator>
  `
})
export class TablaProductosComponent {
  dataSource = new MatTableDataSource(productos);
  columnas = ['nombre', 'precio', 'stock', 'acciones'];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  filtrar(event: Event) {
    const valor = (event.target as HTMLInputElement).value;
    this.dataSource.filter = valor.trim().toLowerCase();
  }
}
```
