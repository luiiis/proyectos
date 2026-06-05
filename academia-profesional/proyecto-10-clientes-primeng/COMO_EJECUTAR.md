# Cómo Ejecutar - Proyecto 10: Clientes PrimeNG

## 1. Crear proyecto
```bash
cd academia-profesional/proyecto-10-clientes-primeng
ng new clientes-primeng --standalone --style=scss --routing
cd clientes-primeng
npm install primeng primeicons
ng add @angular/cdk
```

## 2. Configurar PrimeNG en styles.scss
```scss
@import "primeng/resources/themes/lara-light-blue/theme.css";
@import "primeng/resources/primeng.css";
@import "primeicons/primeicons.css";
```

## 3. Ejecutar
```bash
ng serve
# → http://localhost:4200
```

## 4. Backend necesario
Este proyecto consume la API del proyecto 06 o 07:
```bash
cd ../proyecto-07-api-productos
docker compose up --build -d
# API en http://localhost:8080
```
