# Ejercicios y Retos - Proyecto 21: GraphQL

## Reto 1: Mutation para actualizar producto
Implementa una mutation que actualice solo los campos proporcionados:
```graphql
mutation {
  actualizarProducto(id: 1, input: { precio: 19999.99 }) {
    id
    nombre
    precio
  }
}
```

**Lo que practicas:** Mutations, input types, actualización parcial

---

## Reto 2: Relaciones N:M
Agrega la relación Producto ↔ Tags (muchos a muchos):
```graphql
type Producto {
  id: ID!
  nombre: String!
  tags: [Tag!]!
}
type Tag {
  id: ID!
  nombre: String!
  productos: [Producto!]!
}
```

**Lo que practicas:** DataLoader, N+1 problem en GraphQL, relaciones

---

## Reto 3: Paginación con Cursor
Implementa paginación cursor-based (estándar en GraphQL):
```graphql
query {
  productos(first: 10, after: "cursor123") {
    edges {
      node { id, nombre, precio }
      cursor
    }
    pageInfo {
      hasNextPage
      endCursor
    }
  }
}
```

**Lo que practicas:** Relay-style pagination, cursors, Connection pattern

---

## Reto 4: Subscriptions (tiempo real)
Implementa subscriptions para notificar cuando se crea un producto:
```graphql
subscription {
  productoCreado {
    id
    nombre
    precio
  }
}
```
El cliente recibe datos automáticamente cuando hay cambios (WebSocket).

**Lo que practicas:** GraphQL subscriptions, WebSocket, reactive streams

---

## Reto 5: Validación y errores custom
Maneja errores de forma clara:
```json
{
  "errors": [{
    "message": "Producto no encontrado",
    "extensions": { "code": "NOT_FOUND", "productoId": 999 }
  }]
}
```

**Lo que practicas:** Error handling en GraphQL, extensions, typed errors

---

## Reto 6 (Avanzado): Schema Stitching
Combina schemas de múltiples microservicios en un solo endpoint GraphQL:
- productos-service: tipo Producto
- ventas-service: tipo Venta (con referencia a Producto)
- Un Gateway GraphQL que combina ambos

**Lo que practicas:** Federation, schema composition, microservicios con GraphQL
