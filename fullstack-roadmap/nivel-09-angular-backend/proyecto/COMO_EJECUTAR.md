# Cómo Ejecutar — Proyecto 9: Angular + Backend

## Requisitos
- Java 21 + Maven (backend)
- Node.js 22 + Angular CLI 20 (frontend)
- MySQL corriendo

## Paso 1: Backend

```cmd
cd nivel-03-base-datos/proyecto/backend
mvn spring-boot:run
```
(Usa el backend del proyecto 3 que ya tiene el CRUD completo)

## Paso 2: Frontend

```cmd
cd nivel-09-angular-backend/proyecto/frontend
npm install
ng serve
```
→ http://localhost:4200

## CORS (backend)
Si recibes error de CORS, agrega en el backend:

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }
}
```

## Estructura Frontend

```
src/app/
├── app.component.ts
├── app.routes.ts
├── components/
│   ├── producto-lista/
│   ├── producto-form/
│   └── producto-detalle/
├── services/
│   └── producto.service.ts
└── environments/
    ├── environment.ts          ← desarrollo (localhost:8080)
    └── environment.prod.ts     ← producción
```
