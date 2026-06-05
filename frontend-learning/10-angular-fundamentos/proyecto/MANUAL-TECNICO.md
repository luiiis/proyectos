# Módulo 10: Angular Fundamentos - Manual Técnico

## ¿Qué construimos?
Una aplicación Angular desde cero con el CLI, demostrando la arquitectura y standalone components.

## Cómo se construyó paso a paso

### Paso 1: Crear proyecto con Angular CLI
```bash
# Instalar Angular CLI (una sola vez)
npm install -g @angular/cli

# Crear proyecto nuevo
ng new mi-primera-app --standalone --style=scss --routing

# Opciones elegidas:
# --standalone: sin NgModules (patrón moderno 2024+)
# --style=scss: preprocesador CSS
# --routing: incluir router
```

### Paso 2: Entender la estructura generada
```
mi-primera-app/
├── src/
│   ├── app/
│   │   ├── app.component.ts      ← Componente raíz
│   │   ├── app.config.ts         ← Configuración (providers)
│   │   └── app.routes.ts         ← Rutas
│   ├── index.html                ← HTML base
│   ├── main.ts                   ← Bootstrap de la app
│   └── styles.scss               ← Estilos globales
├── angular.json                  ← Configuración del proyecto
├── package.json                  ← Dependencias
└── tsconfig.json                 ← Configuración TypeScript
```

### Paso 3: Crear componentes
```bash
ng generate component features/productos --standalone
ng generate component features/dashboard --standalone
ng generate service core/services/producto
```

### Paso 4: Ejecutar
```bash
cd mi-primera-app
npm install
ng serve
# → http://localhost:4200
```

## Arquitectura Angular

```
┌─────────────────────────────────────────────────┐
│                  Angular App                      │
│                                                  │
│  ┌──────────────────────────────────────────┐   │
│  │            app.config.ts                  │   │
│  │  providers: [provideRouter, provideHttp]  │   │
│  └──────────────────────────────────────────┘   │
│                      │                           │
│  ┌──────────────────────────────────────────┐   │
│  │            app.routes.ts                  │   │
│  │  /dashboard → DashboardComponent         │   │
│  │  /productos → ProductosComponent         │   │
│  └──────────────────────────────────────────┘   │
│                      │                           │
│  ┌─────────┐  ┌─────────┐  ┌─────────────┐    │
│  │Dashboard│  │Productos│  │  Servicios  │    │
│  │Component│  │Component│  │(HttpClient) │    │
│  └─────────┘  └─────────┘  └─────────────┘    │
└─────────────────────────────────────────────────┘
```

## Standalone Components vs NgModules

```typescript
// ANTES (NgModules - legacy):
@NgModule({
  declarations: [ProductosComponent],
  imports: [CommonModule, MatTableModule],
  exports: [ProductosComponent]
})
export class ProductosModule {}

// AHORA (Standalone - moderno):
@Component({
  standalone: true,
  imports: [CommonModule, MatTableModule],  // Importa directamente
  template: `...`
})
export class ProductosComponent {}
// No necesitas módulo. El componente es auto-contenido.
```
