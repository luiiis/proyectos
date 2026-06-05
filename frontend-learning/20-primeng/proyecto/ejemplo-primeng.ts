// PrimeNG Components - Reference File
import { Component } from '@angular/core';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';

interface Product {
  id: number;
  name: string;
  price: number;
  category: string;
}

@Component({
  selector: 'app-primeng-demo',
  standalone: true,
  imports: [TableModule, DialogModule, ConfirmDialogModule],
  providers: [ConfirmationService],
  template: `
    <!-- p-table with sorting and filtering -->
    <p-table [value]="products" [paginator]="true" [rows]="5"
             [globalFilterFields]="['name','category']" sortMode="multiple">
      <ng-template pTemplate="header">
        <tr>
          <th pSortableColumn="name">Name <p-sortIcon field="name"/></th>
          <th pSortableColumn="price">Price <p-sortIcon field="price"/></th>
          <th>Category</th>
          <th>Actions</th>
        </tr>
        <tr>
          <th><input pInputText type="text" (input)="dt.filter($event.target.value, 'name', 'contains')"></th>
        </tr>
      </ng-template>
      <ng-template pTemplate="body" let-product>
        <tr>
          <td>{{ product.name }}</td>
          <td>{{ product.price | currency }}</td>
          <td>{{ product.category }}</td>
          <td>
            <button (click)="showDialog(product)">Edit</button>
            <button (click)="confirmDelete(product)">Delete</button>
          </td>
        </tr>
      </ng-template>
    </p-table>

    <!-- p-dialog -->
    <p-dialog header="Edit Product" [(visible)]="dialogVisible" [modal]="true">
      <p>Editing: {{ selectedProduct?.name }}</p>
    </p-dialog>

    <!-- p-confirmDialog -->
    <p-confirmDialog />
  `
})
export class PrimengDemoComponent {
  products: Product[] = [
    { id: 1, name: 'Widget', price: 25.99, category: 'Tools' },
    { id: 2, name: 'Gadget', price: 49.99, category: 'Electronics' }
  ];
  dialogVisible = false;
  selectedProduct: Product | null = null;

  constructor(private confirmService: ConfirmationService) {}

  showDialog(product: Product) {
    this.selectedProduct = product;
    this.dialogVisible = true;
  }

  confirmDelete(product: Product) {
    this.confirmService.confirm({
      message: `Delete ${product.name}?`,
      accept: () => { this.products = this.products.filter(p => p.id !== product.id); }
    });
  }
}

console.log('Reference file: ejemplo-primeng.ts - PrimeNG table, dialog & confirm for use in an Angular project');
