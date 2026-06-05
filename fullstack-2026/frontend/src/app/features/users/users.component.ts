import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [MatCardModule],
  template: `
    <div style="padding: 24px;">
      <h2>Gestión de Usuarios</h2>
      <mat-card>
        <mat-card-content>
          <p>Los usuarios se gestionan directamente en Keycloak.</p>
          <p>Accede a: <a href="http://localhost:8180" target="_blank">Keycloak Admin Console</a></p>
          <p>Desde ahí puedes: crear usuarios, asignar roles, configurar MFA, etc.</p>
        </mat-card-content>
      </mat-card>
    </div>
  `
})
export class UsersComponent {}
