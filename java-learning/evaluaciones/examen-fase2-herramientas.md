# Examen Fase 2: Herramientas (Módulos 14-17)

## Instrucciones
- Tiempo: 60 minutos
- Puedes usar IDE

---

## Sección A: Teoría (30 puntos, 3 pts cada una)

1. Nombra 5 patrones de diseño GoF y cuándo usarías cada uno.
2. ¿Cuál es la diferencia entre un unit test y un test de integración?
3. ¿Qué es Maven y qué problema resuelve? ¿Qué es el pom.xml?
4. ¿Cuál es la diferencia entre `@Mock` y `@Spy` en Mockito?
5. Explica el ciclo de vida de Maven: compile → test → package → install → deploy
6. ¿Qué es TDD? Describe el ciclo Red-Green-Refactor.
7. ¿Cuándo usarías Gradle en vez de Maven?
8. ¿Qué es el patrón Repository y por qué es importante?
9. ¿Qué cobertura de tests es adecuada y por qué 100% no es necesariamente bueno?
10. ¿Cuál es la diferencia entre el patrón Factory y el patrón Builder?

---

## Sección B: Práctica (70 puntos)

### B1. Patrones de Diseño (20 pts)
Implementa un sistema de notificaciones usando el **patrón Strategy**:
- `NotificationStrategy` (interface): `void enviar(String destinatario, String mensaje)`
- `EmailNotification`: imprime "📧 Email a {destinatario}: {mensaje}"
- `SmsNotification`: imprime "📱 SMS a {destinatario}: {mensaje}"
- `PushNotification`: imprime "🔔 Push a {destinatario}: {mensaje}"
- `NotificationService`: recibe una lista de strategies y envía por TODAS

### B2. Testing con JUnit 5 (25 pts)
Dada esta clase:
```java
public class CarritoCompras {
    private final List<Item> items = new ArrayList<>();
    
    public void agregar(Item item) { items.add(item); }
    public void remover(String productoId) { items.removeIf(i -> i.id().equals(productoId)); }
    public double getTotal() { return items.stream().mapToDouble(i -> i.precio() * i.cantidad()).sum(); }
    public int getCantidadItems() { return items.size(); }
    public void vaciar() { items.clear(); }
}
record Item(String id, String nombre, double precio, int cantidad) {}
```

Escribe **al menos 8 tests** que cubran:
- Agregar item → total y cantidad correctos
- Agregar múltiples items
- Remover item existente
- Remover item que no existe (no debe fallar)
- Total con carrito vacío = 0
- Vaciar carrito
- Edge case: precio = 0
- Edge case: cantidad = 0

### B3. Maven POM (10 pts)
Escribe un `pom.xml` para un proyecto que necesita:
- Java 21
- Spring Boot 3.3 parent
- Dependencias: spring-web, spring-data-jpa, postgresql, jjwt, spring-test
- Plugin: spring-boot-maven-plugin

### B4. Patrón Builder + Validación (15 pts)
Implementa un Builder para `ConfiguracionApp` con:
- `host` (obligatorio, no vacío)
- `puerto` (obligatorio, entre 1 y 65535)
- `baseDatos` (obligatorio)
- `maxConexiones` (opcional, default 10)
- `timeout` (opcional, default 30 segundos)
- `ssl` (opcional, default false)
- `build()` debe lanzar `IllegalStateException` si faltan campos obligatorios

---

## Aprobación
- 50+ = Aprobado
- 70+ = Sólido en herramientas
- 85+ = Productivo en equipo enterprise
