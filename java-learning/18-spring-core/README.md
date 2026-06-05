# Módulo 18: Spring Core - IoC y Dependency Injection

## Inversión de Control (IoC)
TÚ no creas los objetos. Spring los crea y los inyecta donde se necesitan.

```java
// SIN Spring: tú creas todo manualmente
ProductoRepository repo = new ProductoRepositoryImpl();
ProductoService service = new ProductoService(repo);
ProductoController controller = new ProductoController(service);

// CON Spring: Spring crea y conecta todo automáticamente
@Service
public class ProductoService {
    private final ProductoRepository repo;  // Spring inyecta automáticamente
    public ProductoService(ProductoRepository repo) { this.repo = repo; }
}
```

## Anotaciones principales
```java
@Component     // "Spring, gestiona esta clase"
@Service       // Igual que @Component pero semántico (lógica de negocio)
@Repository    // Igual que @Component pero para acceso a datos
@Controller    // Igual que @Component pero para endpoints web
@Configuration // Clase de configuración (define @Beans)
@Bean          // "Spring, este método crea un objeto que debes gestionar"
@Autowired     // "Spring, inyecta aquí la dependencia" (preferir constructor)
@Value         // Inyectar valor de properties
@Qualifier     // Especificar cuál implementación inyectar cuando hay varias
```

## Scopes
```java
@Scope("singleton")  // DEFAULT: una instancia para toda la app
@Scope("prototype")  // Nueva instancia cada vez que se solicita
@Scope("request")    // Una por request HTTP
@Scope("session")    // Una por sesión HTTP
```

## Ejercicios
1. Crea un servicio con 2 implementaciones y usa @Qualifier para elegir
2. Implementa un @Configuration que cree beans condicionales (@ConditionalOnProperty)
3. Inyecta valores de application.yml con @Value y @ConfigurationProperties
