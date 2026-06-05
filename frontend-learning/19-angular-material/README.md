# Módulo 19: Angular Material

## 1. Instalación y Configuración

```bash
ng add @angular/material
# Seleccionar tema, tipografía y animaciones
```

```typescript
// Importar módulos individualmente en cada componente standalone
import { MatTableModule } from '@angular/material/table';
import { MatDialogModule } from '@angular/material/dialog';
import { MatToolbarModule } from '@angular/material/toolbar';
```

## 2. mat-table (Tabla de Datos)

```typescript
@Component({
  standalone: true,
  imports: [MatTableModule, MatPaginatorModule, MatSortModule],
  template: `
    <table mat-table [dataSource]="dataSource" matSort>
      <ng-container matColumnDef="id">
        <th mat-header-cell *matHeaderCellDef mat-sort-header>ID</th>
        <td mat-cell *matCellDef="let row">{{ row.id }}</td>
      </ng-container>

      <ng-container matColumnDef="nombre">
        <th mat-header-cell *matHeaderCellDef mat-sort-header>Nombre</th>
        <td mat-cell *matCellDef="let row">{{ row.nombre }}</td>
      </ng-container>

      <ng-container matColumnDef="acciones">
        <th mat-header-cell *matHeaderCellDef>Acciones</th>
        <td mat-cell *matCellDef="let row">
          <button mat-icon-button (click)="editar(row)">
            <mat-icon>edit</mat-icon>
          </button>
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="columnas"></tr>
      <tr mat-row *matRowDef="let row; columns: columnas"></tr>
    </table>
    <mat-paginator [pageSizeOptions]="[5, 10, 25]" showFirstLastButtons />
  `
})
export class UsuarioTableComponent implements AfterViewInit {
  columnas = ['id', 'nombre', 'acciones'];
  dataSource = new MatTableDataSource<Usuario>();
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }
}
```

## 3. mat-dialog

```typescript
// Abrir dialog
export class UsuarioListComponent {
  private dialog = inject(MatDialog);

  abrirFormulario(usuario?: Usuario) {
    const ref = this.dialog.open(UsuarioFormDialogComponent, {
      width: '500px',
      data: { usuario, modo: usuario ? 'editar' : 'crear' },
    });

    ref.afterClosed().subscribe(resultado => {
      if (resultado) this.cargarUsuarios();
    });
  }
}

// Componente Dialog
@Component({ standalone: true, imports: [MatDialogModule, ReactiveFormsModule], ... })
export class UsuarioFormDialogComponent {
  data = inject<{ usuario: Usuario; modo: string }>(MAT_DIALOG_DATA);
  private dialogRef = inject(MatDialogRef<UsuarioFormDialogComponent>);

  guardar() {
    this.dialogRef.close(this.form.getRawValue());
  }
}
```

## 4. mat-toolbar + mat-menu

```html
<mat-toolbar color="primary">
  <button mat-icon-button (click)="toggleSidebar()">
    <mat-icon>menu</mat-icon>
  </button>
  <span>Mi Aplicación</span>
  <span class="spacer"></span>
  <button mat-icon-button [matMenuTriggerFor]="userMenu">
    <mat-icon>account_circle</mat-icon>
  </button>
  <mat-menu #userMenu="matMenu">
    <button mat-menu-item (click)="perfil()">Perfil</button>
    <button mat-menu-item (click)="logout()">Cerrar sesión</button>
  </mat-menu>
</mat-toolbar>
```

## 5. mat-stepper

```html
<mat-stepper linear>
  <mat-step [stepControl]="datosForm" label="Datos personales">
    <form [formGroup]="datosForm">...</form>
    <button mat-button matStepperNext>Siguiente</button>
  </mat-step>
  <mat-step [stepControl]="direccionForm" label="Dirección">
    <form [formGroup]="direccionForm">...</form>
    <button mat-button matStepperPrevious>Atrás</button>
    <button mat-button matStepperNext>Siguiente</button>
  </mat-step>
  <mat-step label="Confirmar">
    <p>Revise sus datos y confirme.</p>
    <button mat-raised-button color="primary" (click)="confirmar()">Confirmar</button>
  </mat-step>
</mat-stepper>
```

## 6. Ejercicios

1. Crea una tabla con mat-table, paginación, ordenamiento y filtro.
2. Implementa un CRUD usando mat-dialog para crear/editar registros.
3. Crea un layout con mat-toolbar, mat-sidenav y mat-menu.
4. Implementa un wizard de registro con mat-stepper (3 pasos con validación).
5. Crea un formulario con mat-form-field, mat-select, mat-datepicker y mat-autocomplete.

---

## Siguiente Módulo
→ [20-PrimeNG](../20-primeng/README.md)
