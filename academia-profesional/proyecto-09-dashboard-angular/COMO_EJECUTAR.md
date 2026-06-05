# Cómo Ejecutar - Proyecto 09: Dashboard Angular

## 1. Crear el proyecto (primera vez)
```bash
cd academia-profesional/proyecto-09-dashboard-angular
ng new dashboard --standalone --style=scss --routing --skip-tests
cd dashboard
ng add @angular/material
```

## 2. Generar componentes
```bash
ng g c features/login --standalone
ng g c features/dashboard --standalone
ng g c shared/components/sidebar --standalone
ng g s core/services/auth
```

## 3. Ejecutar
```bash
ng serve
# → http://localhost:4200
```

## 4. Estructura del proyecto
```
src/app/
├── core/
│   ├── guards/auth.guard.ts
│   ├── interceptors/auth.interceptor.ts
│   └── services/auth.service.ts
├── features/
│   ├── login/login.component.ts
│   └── dashboard/dashboard.component.ts
├── shared/
│   └── components/sidebar/
├── app.routes.ts
└── app.config.ts
```
