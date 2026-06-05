# Cómo Ejecutar - Proyecto 21: GraphQL

## Ejecutar (usa H2 en memoria, no necesita Docker)
```bash
cd academia-profesional/proyecto-21-graphql
mvn spring-boot:run
```

## Probar con GraphiQL (interfaz web interactiva)
Abrir: http://localhost:8080/graphiql

## Queries de ejemplo (copiar en GraphiQL)

```graphql
# Listar todos los productos (solo nombre y precio)
query {
  productos {
    nombre
    precio
  }
}

# Filtrar por categoria
query {
  productos(categoria: "Laptops") {
    nombre
    precio
    stock
  }
}

# Buscar por nombre
query {
  buscarProductos(nombre: "Mac") {
    id
    nombre
    precio
    categoria
  }
}

# Buscar por ID (con todos los campos)
query {
  producto(id: 1) {
    id
    nombre
    descripcion
    precio
    stock
    categoria
    sku
    activo
  }
}

# Crear producto
mutation {
  crearProducto(input: {
    nombre: "Nuevo Producto GraphQL"
    precio: 4999.99
    stock: 15
    categoria: "Test"
    sku: "GQL-001"
  }) {
    id
    nombre
    precio
  }
}

# Actualizar precio
mutation {
  actualizarProducto(id: 1, input: {
    precio: 19999.99
    stock: 20
  }) {
    id
    nombre
    precio
    stock
  }
}

# Eliminar
mutation {
  eliminarProducto(id: 1)
}
```

## Probar con curl
```bash
curl -X POST http://localhost:8080/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "{ productos(limit: 3) { nombre precio } }"}'
```
