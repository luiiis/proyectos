# Módulo 25: Micro Frontends

## 1. Concepto

```
┌─────────────────────────────────────────────────────────────┐
│                    Shell (Host App)                          │
│  ┌──────────┐  ┌──────────────┐  ┌───────────────────────┐ │
│  │  Header  │  │   Sidebar    │  │     Content Area      │ │
│  │  (Shell) │  │   (Shell)    │  │                       │ │
│  └──────────┘  └──────────────┘  │  ┌─────────────────┐  │ │
│                                   │  │  Remote App 1   │  │ │
│                                   │  │  (Productos)    │  │ │
│                                   │  │  Team A         │  │ │
│                                   │  └─────────────────┘  │ │
│                                   │  ┌─────────────────┐  │ │
│                                   │  │  Remote App 2   │  │ │
│                                   │  │  (Ventas)       │  │ │
│                                   │  │  Team B         │  │ │
│                                   │  └─────────────────┘  │ │
│                                   └───────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

## 2. Module Federation (Webpack 5)

```javascript
// webpack.config.js - Remote (Productos App)
const { ModuleFederationPlugin } = require('webpack').container;

module.exports = {
  plugins: [
    new ModuleFederationPlugin({
      name: 'productosApp',
      filename: 'remoteEntry.js',
      exposes: {
        './ProductoModule': './src/app/features/productos/producto.routes.ts',
      },
      shared: {
        '@angular/core': { singleton: true, strictVersion: true },
        '@angular/common': { singleton: true, strictVersion: true },
        '@angular/router': { singleton: true, strictVersion: true },
        rxjs: { singleton: true },
      },
    }),
  ],
};

// webpack.config.js - Host (Shell App)
module.exports = {
  plugins: [
    new ModuleFederationPlugin({
      name: 'shell',
      remotes: {
        productosApp: 'productosApp@http://localhost:4201/remoteEntry.js',
        ventasApp: 'ventasApp@http://localhost:4202/remoteEntry.js',
      },
      shared: { /* mismas dependencias */ },
    }),
  ],
};
```

## 3. Configuración con @angular-architects/module-federation

```bash
# Instalar en ambos proyectos
ng add @angular-architects/module-federation --project shell --port 4200
ng add @angular-architects/module-federation --project productos --port 4201
```

```typescript
// Shell: app.routes.ts - carga remota
export const routes: Routes = [
  { path: '', component: DashboardComponent },
  {
    path: 'productos',
    loadChildren: () => loadRemoteModule({
      type: 'module',
      remoteEntry: 'http://localhost:4201/remoteEntry.js',
      exposedModule: './ProductoModule',
    }).then(m => m.PRODUCTO_ROUTES),
  },
];
```

## 4. Independent Deployment

```yaml
# Cada micro frontend tiene su propio pipeline
# .github/workflows/productos-app.yml
name: Deploy Productos MFE
on:
  push:
    paths: ['apps/productos/**']
jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - run: npm ci
      - run: npm run build:productos
      - run: aws s3 sync dist/productos s3://mfe-bucket/productos/
      - run: aws cloudfront create-invalidation --distribution-id $CDN_ID
```

## 5. Cuándo Usar Micro Frontends

```
✅ Usar cuando:
- Múltiples equipos trabajan en el mismo producto
- Necesitas deploy independiente por módulo
- Diferentes ciclos de release por equipo
- App muy grande (>50 desarrolladores)

❌ NO usar cuando:
- Equipo pequeño (<10 personas)
- App simple o mediana
- No necesitas deploy independiente
- Agrega complejidad innecesaria
```

## 6. Ejercicios

1. Configura un proyecto con Module Federation: 1 shell + 1 remote.
2. Expón un componente standalone desde el remote y cárgalo en el shell.
3. Implementa shared state entre shell y remote usando un servicio compartido.
4. Configura deploy independiente para cada micro frontend.
5. Implementa fallback UI cuando un remote no está disponible.

---

## Siguiente Módulo
→ [26-Docker](../26-docker/README.md)
