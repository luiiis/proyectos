# Proyecto 10: Clientes con PrimeNG

## ¿Qué construimos?
CRUD completo de clientes usando PrimeNG: DataTable con sort/filter/paginate, Dialog para crear/editar, ConfirmDialog para eliminar.

## Tecnologías
- Angular 17 + PrimeNG 17
- p-table (DataTable con sorting, filtering, pagination)
- p-dialog (modal para formularios)
- p-confirmDialog (confirmación antes de eliminar)
- p-toast (notificaciones)

## Funcionalidades
- Listar clientes en tabla con paginación
- Buscar/filtrar en tiempo real
- Ordenar por cualquier columna
- Crear cliente (dialog con formulario)
- Editar cliente (mismo dialog, pre-llenado)
- Eliminar con confirmación

## Ejecutar
```bash
# Backend necesario (proyecto 06 o 07):
cd ../proyecto-06-api-clientes && mvn spring-boot:run

# Frontend:
cd academia-profesional/proyecto-10-clientes-primeng
ng serve
# → http://localhost:4200
```

## Cómo se conecta al backend
El componente usa HttpClient para llamar a `http://localhost:8080/api/clientes`.
El interceptor agrega el token JWT si existe.
