# Módulo 28: Arquitectura Frontend - Manual Técnico

## ¿Qué construimos?
Estructura de proyecto Angular enterprise con Clean Architecture aplicada al frontend.

## Arquitectura Feature-Based (recomendada para enterprise)

```
src/app/
├── core/                    ← SINGLETON: 1 instancia para toda la app
│   │                          Se importa UNA vez en app.config.ts
│   ├── guards/              ← Protección de rutas
│   ├── interceptors/        ← Modificar HTTP requests/responses
│   ├── services/            ← Servicios globales (auth, notification)
│   └── models/              ← Interfaces/types globales
│
├── shared/                  ← REUTILIZABLE: se importa en múltiples features
│   │                          Componentes genéricos sin lógica de negocio
│   ├── components/          ← Botones, tablas, modales genéricos
│   ├── pipes/               ← Transformaciones de datos
│   ├── directives/          ← Comportamientos reutilizables
│   └── utils/               ← Funciones helper
│
├── features/                ← MÓDULOS DE NEGOCIO: cada uno independiente
│   │                          Lazy loaded, puede desarrollarse en paralelo
│   ├── productos/
│   │   ├── pages/           ← Componentes "smart" (conectan con servicios)
│   │   │   ├── lista/
│   │   │   └── detalle/
│   │   ├── components/      ← Componentes "dumb" (solo @Input/@Output)
│   │   │   ├── producto-card/
│   │   │   └── producto-form/
│   │   ├── services/        ← Servicios específicos de este feature
│   │   │   └── producto.service.ts
│   │   ├── models/          ← Interfaces de este feature
│   │   └── productos.routes.ts ← Rutas del feature
│   │
│   ├── ventas/
│   ├── inventario/
│   └── reportes/
│
├── app.component.ts         ← Shell (layout: sidebar + content)
├── app.config.ts            ← Providers globales
└── app.routes.ts            ← Rutas principales (lazy loading)
```

## Smart vs Dumb Components

```
SMART (Container/Page):
- Conoce los servicios
- Maneja estado
- Hace HTTP calls
- Orquesta dumb components

DUMB (Presentational):
- Solo @Input() y @Output()
- No conoce servicios
- No hace HTTP
- Solo muestra datos y emite eventos
- REUTILIZABLE en cualquier contexto
```

### Ejemplo:
```typescript
// SMART: sabe de dónde vienen los datos
@Component({
  template: `
    <app-producto-form
      [producto]="productoSeleccionado()"
      (guardar)="onGuardar($event)"
      (cancelar)="onCancelar()">
    </app-producto-form>
  `
})
export class ProductoDetallePage {
  private service = inject(ProductoService);
  productoSeleccionado = signal<Producto | null>(null);

  onGuardar(producto: Producto) {
    this.service.guardar(producto).subscribe();
  }
}

// DUMB: no sabe nada del mundo exterior
@Component({
  selector: 'app-producto-form',
  template: `<form [formGroup]="form">...</form>`
})
export class ProductoFormComponent {
  producto = input<Producto | null>();
  guardar = output<Producto>();
  cancelar = output<void>();
}
```

## Barrel Exports (index.ts)

```typescript
// features/productos/index.ts
export * from './pages/lista/lista.component';
export * from './pages/detalle/detalle.component';
export * from './services/producto.service';
export * from './models/producto.model';

// Uso desde otro lugar:
import { ProductoService, Producto } from '@features/productos';
// En lugar de:
import { ProductoService } from '../../../features/productos/services/producto.service';
```

## Reglas de Arquitectura

1. **core/** → NUNCA importa de features/
2. **shared/** → NUNCA importa de features/ ni de core/services
3. **features/X** → NUNCA importa de features/Y (independientes)
4. **features/** → Puede importar de core/ y shared/
5. Cada feature tiene sus propias rutas (lazy loaded)
6. Comunicación entre features → via servicios en core/ o state management
