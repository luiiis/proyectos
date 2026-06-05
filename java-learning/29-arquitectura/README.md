# Módulo 29: Arquitectura de Software

## Clean Architecture
```
┌─────────────────────────────────────────────┐
│              Frameworks & Drivers            │  ← Controllers, BD, UI
│  ┌─────────────────────────────────────┐    │
│  │         Interface Adapters          │    │  ← Repositories impl, DTOs
│  │  ┌─────────────────────────────┐    │    │
│  │  │       Application           │    │    │  ← Use Cases (Services)
│  │  │  ┌─────────────────────┐    │    │    │
│  │  │  │      Domain         │    │    │    │  ← Entities, Value Objects
│  │  │  │  (Business Rules)   │    │    │    │
│  │  │  └─────────────────────┘    │    │    │
│  │  └─────────────────────────────┘    │    │
│  └─────────────────────────────────────┘    │
└─────────────────────────────────────────────┘
Regla: las dependencias apuntan HACIA ADENTRO (dominio no depende de nada externo)
```

## Hexagonal Architecture (Ports & Adapters)
```
                    ┌─────────────────┐
    HTTP ──────────>│   Port (Input)  │
    CLI  ──────────>│                 │
    Kafka ─────────>│   APPLICATION   │
                    │                 │
                    │   DOMAIN        │
                    │   (Business)    │
                    │                 │
    PostgreSQL <────│   Port (Output) │
    Redis      <────│                 │
    Email      <────│                 │
                    └─────────────────┘
```

```java
// Puerto (interface en el dominio)
public interface ProductoRepository {  // El dominio define QUÉ necesita
    Producto guardar(Producto producto);
    Optional<Producto> buscarPorId(Long id);
}

// Adaptador (implementación en infraestructura)
@Repository
public class ProductoJpaAdapter implements ProductoRepository {
    private final ProductoJpaRepository jpaRepo;  // Spring Data

    @Override
    public Producto guardar(Producto producto) {
        var entity = toEntity(producto);
        return toDomain(jpaRepo.save(entity));
    }
}
// Si mañana cambias de PostgreSQL a MongoDB, solo cambias el adaptador
// El dominio NO se entera
```

## DDD (Domain-Driven Design)
```java
// Value Object (inmutable, se compara por valor)
public record Dinero(BigDecimal monto, String moneda) {
    public Dinero sumar(Dinero otro) {
        if (!this.moneda.equals(otro.moneda)) throw new MonedaIncompatibleException();
        return new Dinero(this.monto.add(otro.monto), this.moneda);
    }
}

// Aggregate Root (entidad principal que controla consistencia)
public class Pedido {
    private PedidoId id;
    private List<LineaPedido> lineas;
    private EstadoPedido estado;

    public void agregarProducto(Producto producto, int cantidad) {
        if (estado != EstadoPedido.BORRADOR) throw new PedidoNoModificableException();
        lineas.add(new LineaPedido(producto, cantidad));
    }

    public void confirmar() {
        if (lineas.isEmpty()) throw new PedidoVacioException();
        this.estado = EstadoPedido.CONFIRMADO;
        // Publicar evento: PedidoConfirmadoEvent
    }
}
```

## CQRS (Command Query Responsibility Segregation)
```
WRITE (Commands):  POST/PUT/DELETE → Command Handler → Write DB (normalizada)
READ (Queries):    GET → Query Handler → Read DB (desnormalizada, optimizada)
```

## SOLID
- **S**ingle Responsibility: cada clase tiene UNA razón para cambiar
- **O**pen/Closed: abierto a extensión, cerrado a modificación
- **L**iskov Substitution: subclases reemplazan a la padre sin romper
- **I**nterface Segregation: interfaces pequeñas y específicas
- **D**ependency Inversion: depender de abstracciones, no de implementaciones

## Ejercicios
1. Refactoriza el proyecto de ventas a Hexagonal Architecture
2. Implementa un Aggregate Root para Pedido con reglas de negocio
3. Separa lectura y escritura (CQRS) para el módulo de reportes
4. Identifica violaciones de SOLID en código legacy y corrígelas
