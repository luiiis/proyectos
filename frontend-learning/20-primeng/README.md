# Módulo 20: PrimeNG - Componentes Enterprise

## 1. Instalación

```bash
npm install primeng primeicons
# En styles.scss:
# @import "primeng/resources/themes/lara-light-blue/theme.css";
# @import "primeng/resources/primeng.css";
# @import "primeicons/primeicons.css";
```

## 2. DataTable (p-table)

```typescript
@Component({
  standalone: true,
  imports: [TableModule, ButtonModule, InputTextModule],
  template: `
    <p-table
      [value]="usuarios()"
      [paginator]="true"
      [rows]="10"
      [rowsPerPageOptions]="[5, 10, 25]"
      [globalFilterFields]="['nombre', 'email', 'rol']"
      [loading]="loading()"
      selectionMode="single"
      [(selection)]="seleccionado"
      dataKey="id">

      <ng-template pTemplate="caption">
        <input pInputText type="text" (input)="dt.filterGlobal($event.target.value, 'contains')"
               placeholder="Buscar..." />
      </ng-template>

      <ng-template pTemplate="header">
        <tr>
          <th pSortableColumn="nombre">Nombre <p-sortIcon field="nombre" /></th>
          <th pSortableColumn="email">Email <p-sortIcon field="email" /></th>
          <th>Acciones</th>
        </tr>
      </ng-template>

      <ng-template pTemplate="body" let-usuario>
        <tr>
          <td>{{ usuario.nombre }}</td>
          <td>{{ usuario.email }}</td>
          <td>
            <p-button icon="pi pi-pencil" (click)="editar(usuario)" />
            <p-button icon="pi pi-trash" severity="danger" (click)="confirmarEliminar(usuario)" />
          </td>
        </tr>
      </ng-template>
    </p-table>
  `
})
export class UsuarioTableComponent { }
```

## 3. TreeTable

```typescript
@Component({
  standalone: true,
  imports: [TreeTableModule],
  template: `
    <p-treeTable [value]="archivos" [columns]="columnas">
      <ng-template pTemplate="header" let-columns>
        <tr>
          <th *ngFor="let col of columns">{{ col.header }}</th>
        </tr>
      </ng-template>
      <ng-template pTemplate="body" let-rowNode let-rowData="rowData" let-columns="columns">
        <tr>
          <td *ngFor="let col of columns; let i = index">
            <p-treeTableToggler [rowNode]="rowNode" *ngIf="i === 0" />
            {{ rowData[col.field] }}
          </td>
        </tr>
      </ng-template>
    </p-treeTable>
  `
})
export class ArchivoTreeComponent { }
```

## 4. Dialog, ConfirmDialog, DynamicDialog

```typescript
// ConfirmDialog
export class UsuarioListComponent {
  private confirmService = inject(ConfirmationService);

  confirmarEliminar(usuario: Usuario) {
    this.confirmService.confirm({
      message: `¿Eliminar a ${usuario.nombre}?`,
      header: 'Confirmar eliminación',
      icon: 'pi pi-exclamation-triangle',
      accept: () => this.eliminar(usuario.id),
    });
  }
}

// DynamicDialog (abrir componentes dinámicamente)
export class UsuarioListComponent {
  private dialogService = inject(DialogService);

  abrirFormulario(usuario?: Usuario) {
    const ref = this.dialogService.open(UsuarioFormComponent, {
      header: usuario ? 'Editar Usuario' : 'Nuevo Usuario',
      width: '50vw',
      data: { usuario },
    });

    ref.onClose.subscribe((resultado) => {
      if (resultado) this.cargar();
    });
  }
}
```

## 5. TabView y Sidebar

```html
<!-- TabView -->
<p-tabView>
  <p-tabPanel header="Datos Generales">
    <app-datos-generales [usuario]="usuario()" />
  </p-tabPanel>
  <p-tabPanel header="Permisos">
    <app-permisos [usuario]="usuario()" />
  </p-tabPanel>
  <p-tabPanel header="Historial">
    <app-historial [usuario]="usuario()" />
  </p-tabPanel>
</p-tabView>

<!-- Sidebar -->
<p-sidebar [(visible)]="sidebarVisible" position="right" [style]="{width:'400px'}">
  <h3>Filtros Avanzados</h3>
  <app-filtros (aplicar)="aplicarFiltros($event); sidebarVisible = false" />
</p-sidebar>
```

## 6. Ejercicios

1. Crea un DataTable con paginación, ordenamiento, filtro global y selección.
2. Implementa un CRUD completo usando DynamicDialog para el formulario.
3. Crea un TreeTable para mostrar una estructura jerárquica (departamentos/empleados).
4. Usa ConfirmDialog antes de eliminar registros.
5. Implementa un layout con TabView para organizar secciones de un formulario complejo.

---

## Siguiente Módulo
→ [21-State Management](../21-state-management/README.md)
