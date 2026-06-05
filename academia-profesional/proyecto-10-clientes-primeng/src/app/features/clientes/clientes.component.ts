import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DialogModule } from 'primeng/dialog';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';

interface Cliente {
  id: number; nombre: string; apellido: string;
  email: string; telefono: string; ciudad: string;
}

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule, TableModule, ButtonModule,
            InputTextModule, DialogModule, ConfirmDialogModule, ToastModule],
  providers: [ConfirmationService, MessageService],
  template: `
    <p-toast />
    <p-confirmDialog />

    <div style="padding:24px;">
      <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:16px;">
        <h2>Gestión de Clientes</h2>
        <button pButton label="Nuevo Cliente" icon="pi pi-plus" (click)="abrirDialog()"></button>
      </div>

      <p-table [value]="clientes" [paginator]="true" [rows]="10"
               [globalFilterFields]="['nombre','apellido','email','ciudad']"
               [tableStyle]="{'min-width':'60rem'}">
        <ng-template pTemplate="header">
          <tr>
            <th pSortableColumn="nombre">Nombre <p-sortIcon field="nombre"/></th>
            <th pSortableColumn="apellido">Apellido <p-sortIcon field="apellido"/></th>
            <th>Email</th>
            <th pSortableColumn="ciudad">Ciudad <p-sortIcon field="ciudad"/></th>
            <th>Acciones</th>
          </tr>
        </ng-template>
        <ng-template pTemplate="body" let-cliente>
          <tr>
            <td>{{cliente.nombre}}</td>
            <td>{{cliente.apellido}}</td>
            <td>{{cliente.email}}</td>
            <td>{{cliente.ciudad}}</td>
            <td>
              <button pButton icon="pi pi-pencil" class="p-button-sm p-button-text" (click)="editar(cliente)"></button>
              <button pButton icon="pi pi-trash" class="p-button-sm p-button-text p-button-danger" (click)="confirmarEliminar(cliente)"></button>
            </td>
          </tr>
        </ng-template>
      </p-table>
    </div>

    <!-- Dialog para crear/editar -->
    <p-dialog [(visible)]="dialogVisible" [header]="editando ? 'Editar Cliente' : 'Nuevo Cliente'" [modal]="true" [style]="{width:'450px'}">
      <div style="display:flex;flex-direction:column;gap:12px;">
        <input pInputText [(ngModel)]="clienteForm.nombre" placeholder="Nombre">
        <input pInputText [(ngModel)]="clienteForm.apellido" placeholder="Apellido">
        <input pInputText [(ngModel)]="clienteForm.email" placeholder="Email">
        <input pInputText [(ngModel)]="clienteForm.ciudad" placeholder="Ciudad">
      </div>
      <ng-template pTemplate="footer">
        <button pButton label="Cancelar" class="p-button-text" (click)="dialogVisible=false"></button>
        <button pButton label="Guardar" (click)="guardar()"></button>
      </ng-template>
    </p-dialog>
  `
})
export class ClientesComponent implements OnInit {
  private http = inject(HttpClient);
  private confirmService = inject(ConfirmationService);
  private msgService = inject(MessageService);

  clientes: Cliente[] = [];
  clienteForm: Partial<Cliente> = {};
  dialogVisible = false;
  editando = false;
  private apiUrl = 'http://localhost:8080/api/clientes';

  ngOnInit() { this.cargar(); }

  cargar() {
    this.http.get<Cliente[]>(this.apiUrl).subscribe(data => this.clientes = data);
  }

  abrirDialog() { this.clienteForm = {}; this.editando = false; this.dialogVisible = true; }

  editar(c: Cliente) { this.clienteForm = {...c}; this.editando = true; this.dialogVisible = true; }

  guardar() {
    const obs = this.editando
      ? this.http.put(`${this.apiUrl}/${this.clienteForm.id}`, this.clienteForm)
      : this.http.post(this.apiUrl, this.clienteForm);
    obs.subscribe(() => {
      this.cargar();
      this.dialogVisible = false;
      this.msgService.add({severity:'success', summary:'Éxito', detail:'Cliente guardado'});
    });
  }

  confirmarEliminar(c: Cliente) {
    this.confirmService.confirm({
      message: `¿Eliminar a ${c.nombre} ${c.apellido}?`,
      accept: () => {
        this.http.delete(`${this.apiUrl}/${c.id}`).subscribe(() => {
          this.cargar();
          this.msgService.add({severity:'info', summary:'Eliminado'});
        });
      }
    });
  }
}
