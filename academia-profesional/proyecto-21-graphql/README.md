# Proyecto 21: GraphQL API

## ¿Qué construimos?
API GraphQL con Spring Boot que permite al cliente pedir EXACTAMENTE los datos que necesita.

## REST vs GraphQL
```
REST (lo que ya sabes):
  GET /api/productos       → devuelve TODOS los campos de TODOS los productos
  GET /api/productos/1     → devuelve TODOS los campos de 1 producto
  Problema: over-fetching (recibes datos que no necesitas)
  Problema: under-fetching (necesitas 3 requests para 1 pantalla)

GraphQL:
  POST /graphql (1 solo endpoint)
  Body: { query: "{ productos { nombre precio } }" }
  Respuesta: SOLO nombre y precio (exactamente lo que pediste)
  
  Puedes pedir datos de MULTIPLES entidades en 1 request:
  { 
    productos(categoria: "Laptops") { nombre precio }
    clientes(tipo: "VIP") { nombre email }
    ventasHoy { total }
  }
  → 1 request en lugar de 3
```

## Ejecutar
```bash
cd academia-profesional/proyecto-21-graphql
docker compose up -d    # PostgreSQL
mvn spring-boot:run     # API en :8080

# GraphQL Playground: http://localhost:8080/graphiql
# Escribir queries interactivamente
```

## Ejemplo de queries
```graphql
# Listar productos (solo campos que necesito)
query {
  productos(limit: 5) {
    id
    nombre
    precio
  }
}

# Producto con categoria (JOIN automatico)
query {
  producto(id: 1) {
    nombre
    precio
    stock
    categoria {
      nombre
    }
  }
}

# Crear producto (mutation)
mutation {
  crearProducto(input: {
    nombre: "Nuevo Producto"
    precio: 999.99
    stock: 10
  }) {
    id
    nombre
  }
}
```
