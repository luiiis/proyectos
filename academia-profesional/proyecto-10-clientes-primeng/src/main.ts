import { bootstrapApplication } from '@angular/platform-browser';
import { Component } from '@angular/core';
import { provideHttpClient } from '@angular/common/http';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { ClientesComponent } from './app/features/clientes/clientes.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [ClientesComponent],
  template: `<app-clientes />`
})
export class AppComponent {}

bootstrapApplication(AppComponent, {
  providers: [
    provideHttpClient(),
    provideAnimationsAsync()
  ]
}).catch(err => console.error(err));
