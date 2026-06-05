# Manual Java: Versiones 23 a 26 (Completo hasta la ultima version)

Complemento del MANUAL-JAVA-COMPLETO.md que cubre hasta Java 22.
Este documento cubre Java 23, 24, 25 (LTS) y 26 (ultima version, marzo 2026).

---

# Java 23 (Septiembre 2024)

## Primitive Types in Patterns (Preview)
```java
// Ahora puedes usar primitivos en pattern matching
switch (codigo) {
    case 200 -> "OK";
    case 404 -> "Not Found";
    case int i when i >= 500 -> "Server Error: " + i;
    case int i -> "Otro: " + i;
}

// Tambien en instanceof
if (obj instanceof int i) {
    System.out.println("Es entero: " + i);
}
```

## Module Import Declarations (Preview)
```java
// Importar todo un modulo con una linea
import module java.base;  // Importa java.util, java.io, java.time, etc.

// En lugar de:
import java.util.List;
import java.util.Map;
import java.io.IOException;
// ... 20 imports mas
```

## Implicitly Declared Classes (Preview)
```java
// Para programas simples, no necesitas declarar clase ni main explicito
// Archivo: HolaMundo.java
void main() {
    System.out.println("Hola sin clase!");
}
// Ejecutar: java HolaMundo.java
// No necesitas: public class, public static void main(String[] args)
// Ideal para scripts rapidos y aprendizaje
```

## Stream Gatherers (Preview)
```java
// Crear operaciones intermedias CUSTOM en streams
// Ejemplo: agrupar elementos en ventanas de N
List<List<Integer>> ventanas = Stream.of(1,2,3,4,5,6,7,8,9)
    .gather(Gatherers.windowFixed(3))
    .toList();
// [[1,2,3], [4,5,6], [7,8,9]]

// Ventana deslizante
List<List<Integer>> sliding = Stream.of(1,2,3,4,5)
    .gather(Gatherers.windowSliding(3))
    .toList();
// [[1,2,3], [2,3,4], [3,4,5]]
```

## Structured Concurrency (Preview)
```java
// Ejecutar tareas en paralelo de forma ESTRUCTURADA
// Si una falla, las demas se cancelan automaticamente
try (var scope = StructuredTaskScope.open()) {
    Subtask<String> usuario = scope.fork(() -> buscarUsuario(id));
    Subtask<List<Pedido>> pedidos = scope.fork(() -> buscarPedidos(id));
    
    scope.join();  // Esperar ambas
    
    return new Perfil(usuario.get(), pedidos.get());
}
// Si buscarUsuario() falla -> buscarPedidos() se cancela automaticamente
// No hay threads huerfanos ni resource leaks
```

## Scoped Values (Preview)
```java
// Compartir datos inmutables a traves de la cadena de llamadas
// Sin pasar como parametro en cada metodo
private static final ScopedValue<Usuario> CURRENT_USER = ScopedValue.newInstance();

// Establecer valor para este scope
ScopedValue.runWhere(CURRENT_USER, usuarioAutenticado, () -> {
    procesarPedido();  // Puede acceder a CURRENT_USER.get()
});

// En cualquier metodo dentro del scope:
void procesarPedido() {
    Usuario user = CURRENT_USER.get();  // Accede sin parametro
    registrarAuditoria(user);
}
// Ventaja sobre ThreadLocal: inmutable, mas seguro, funciona con Virtual Threads
```

---

# Java 24 (Marzo 2025)

## Stream Gatherers (Segunda Preview)
```java
// Gatherers personalizados
Gatherer<Integer, ?, Integer> duplicar = Gatherer.of(
    (state, element, downstream) -> {
        downstream.push(element);
        downstream.push(element);
        return true;
    }
);

List<Integer> resultado = Stream.of(1, 2, 3)
    .gather(duplicar)
    .toList();
// [1, 1, 2, 2, 3, 3]
```

## Flexible Constructor Bodies (Preview)
```java
// Ahora puedes ejecutar codigo ANTES de super()
public class Empleado extends Persona {
    public Empleado(String nombre, double salario) {
        // Validar ANTES de llamar al constructor padre
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Nombre requerido");
        }
        if (salario < 0) {
            throw new IllegalArgumentException("Salario no puede ser negativo");
        }
        super(nombre);  // Ahora si, llamar al padre
        this.salario = salario;
    }
}
// ANTES de Java 24: super() DEBIA ser la primera linea (no podias validar antes)
```

## Class-File API (Preview)
```java
// API para leer y escribir archivos .class programaticamente
// Reemplaza ASM y otras librerias de manipulacion de bytecode
ClassFile cf = ClassFile.of();
byte[] bytes = cf.build(ClassDesc.of("com.ejemplo.MiClase"), classBuilder -> {
    classBuilder.withMethod("saludar", MethodTypeDesc.of(CD_String), 
        ACC_PUBLIC, methodBuilder -> {
            methodBuilder.withCode(codeBuilder -> {
                codeBuilder.ldc("Hola desde bytecode!")
                           .areturn();
            });
        });
});
```

---

# Java 25 (Septiembre 2025 - LTS)

## Esta es la version LTS recomendada para produccion en 2026.

## Scoped Values (Estable - ya no preview)
```java
// Finalmente estable despues de 4 previews
private static final ScopedValue<RequestContext> CTX = ScopedValue.newInstance();

// En el filtro HTTP:
ScopedValue.runWhere(CTX, new RequestContext(usuario, ip, timestamp), () -> {
    controller.handleRequest();
});

// En CUALQUIER metodo de la cadena (sin pasar como parametro):
void registrarAuditoria() {
    var ctx = CTX.get();
    log.info("Usuario: {} desde IP: {}", ctx.usuario(), ctx.ip());
}

// vs ThreadLocal:
// - ScopedValue es INMUTABLE (mas seguro)
// - Funciona correctamente con Virtual Threads
// - Se limpia automaticamente al salir del scope
// - Mas eficiente (no hay mapa por thread)
```

## Structured Concurrency (Preview avanzado)
```java
// Patron: ejecutar N tareas, usar la primera que responda
try (var scope = StructuredTaskScope.open(
        StructuredTaskScope.Joiner.anySuccessfulResultOrThrow())) {
    
    scope.fork(() -> buscarEnCache(id));      // Rapido si esta en cache
    scope.fork(() -> buscarEnBD(id));         // Mas lento pero siempre funciona
    scope.fork(() -> buscarEnServicioExterno(id));
    
    Producto resultado = scope.join();  // Retorna el PRIMERO que responda
    // Los demas se cancelan automaticamente
}
```

## Compact Source Files and Instance Main Methods (Estable)
```java
// Archivo: Calculadora.java (sin class declaration)
void main() {
    var scanner = new java.util.Scanner(System.in);
    System.out.print("Numero: ");
    int n = scanner.nextInt();
    System.out.println("Doble: " + n * 2);
}
// Ejecutar: java Calculadora.java
// Perfecto para scripts, prototipos, ensenanza
```

## Primitive Types in Patterns (Estable)
```java
// Pattern matching con primitivos - ya no preview
Object valor = obtenerValor();

String descripcion = switch (valor) {
    case int i when i > 0    -> "Entero positivo: " + i;
    case int i when i < 0    -> "Entero negativo: " + i;
    case int i               -> "Cero";
    case double d when d > 0 -> "Decimal positivo: " + d;
    case double d            -> "Decimal: " + d;
    case String s            -> "Texto: " + s;
    case null                -> "Nulo";
    default                  -> "Otro tipo";
};
```

## Key Encapsulation Mechanism API
```java
// Nueva API para criptografia post-cuantica
// Preparacion para cuando las computadoras cuanticas rompan RSA
KeyPairGenerator kpg = KeyPairGenerator.getInstance("ML-KEM");
kpg.initialize(NamedParameterSpec.ML_KEM_768);
KeyPair kp = kpg.generateKeyPair();

// Encapsular (generar secreto compartido)
KEM kem = KEM.getInstance("ML-KEM");
KEM.Encapsulator enc = kem.newEncapsulator(kp.getPublic());
KEM.Encapsulated encapsulated = enc.encapsulate();
SecretKey sharedSecret = encapsulated.key();
```

## Mejoras de Performance
```java
// Compact Object Headers (experimental)
// Reduce el tamano del header de cada objeto de 12 bytes a 8 bytes
// En aplicaciones con millones de objetos: ahorro significativo de RAM
// Activar: -XX:+UseCompactObjectHeaders

// Generational ZGC mejorado
// Pausas < 1ms incluso con heaps de 16TB
// Default en Java 25 (no necesitas activarlo)
```

---

# Java 26 (Marzo 2026 - Ultima version)

## AOT (Ahead-of-Time) Object Caching
```java
// Los objetos creados durante el arranque se cachean en disco
// Siguiente arranque: se cargan directamente (no se recrean)
// Resultado: arranque 2-3x mas rapido

// Activar:
// java -XX:AOTCache=app-cache.aot -jar mi-app.jar  (primera vez: crea cache)
// java -XX:AOTCache=app-cache.aot -jar mi-app.jar  (siguiente: usa cache)

// Ahora funciona con CUALQUIER garbage collector (antes solo con G1)
```

## Lazy Constants
```java
// Inicializacion diferida de constantes (solo se calculan cuando se usan)
// Util para constantes costosas de crear

// ANTES: se calcula al cargar la clase (aunque nunca se use)
static final ExpensiveObject INSTANCE = new ExpensiveObject();

// AHORA (Java 26): se calcula la PRIMERA vez que se accede
static final ExpensiveObject INSTANCE = StableValue.of(() -> new ExpensiveObject());
// Si nunca se usa -> nunca se crea (ahorra tiempo de arranque)
```

## HTTP/3 Support
```java
// El HttpClient ahora soporta HTTP/3 (basado en QUIC/UDP)
// Mas rapido que HTTP/2 en redes con perdida de paquetes
HttpClient client = HttpClient.newBuilder()
    .version(HttpClient.Version.HTTP_3)  // Nuevo!
    .build();

HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://api.ejemplo.com/datos"))
    .build();

HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
// HTTP/3 usa UDP (QUIC) en lugar de TCP
// Ventaja: si se pierde 1 paquete, no bloquea los demas (vs HTTP/2 sobre TCP)
```

## Vector API (Incubator avanzado)
```java
// Operaciones SIMD (Single Instruction, Multiple Data)
// Procesar multiples datos en UNA instruccion de CPU
// 4-16x mas rapido para calculos numericos masivos

static final VectorSpecies<Float> SPECIES = FloatVector.SPECIES_256;

void sumarArrays(float[] a, float[] b, float[] resultado) {
    int i = 0;
    // Procesar 8 floats a la vez (256 bits / 32 bits por float)
    for (; i < SPECIES.loopBound(a.length); i += SPECIES.length()) {
        var va = FloatVector.fromArray(SPECIES, a, i);
        var vb = FloatVector.fromArray(SPECIES, b, i);
        va.add(vb).intoArray(resultado, i);
    }
    // Procesar los restantes uno por uno
    for (; i < a.length; i++) {
        resultado[i] = a[i] + b[i];
    }
}
// Uso: procesamiento de imagenes, ML, simulaciones fisicas
```

## Structured Concurrency (Estable en Java 26)
```java
// Finalmente ESTABLE despues de 6 previews (desde Java 19)
try (var scope = StructuredTaskScope.open()) {
    var usuario = scope.fork(() -> userService.findById(id));
    var pedidos = scope.fork(() -> orderService.findByUser(id));
    var saldo = scope.fork(() -> walletService.getBalance(id));
    
    scope.join();
    
    return new Dashboard(usuario.get(), pedidos.get(), saldo.get());
}
// Garantias:
// - Si una tarea falla -> las demas se cancelan
// - No hay threads huerfanos
// - El scope se cierra al salir del try
```

## Removal of Applet API
```java
// java.applet.Applet ELIMINADO completamente
// Deprecated desde Java 9, removed en Java 26
// Si tienes codigo legacy con Applets -> migrar a JavaFX o web
```

## Warnings for Restricted Method Calls
```java
// Java 26 emite warnings cuando usas metodos que seran restringidos
// Preparacion para futuras versiones que bloquearan reflection en campos final
// Ejemplo: Field.setAccessible(true) en campos final -> warning
```

---

# Resumen: Que version usar

| Version | Tipo | Fecha | Recomendacion |
|---------|------|-------|---------------|
| Java 8 | LTS | 2014 | Solo si es legacy (migrar!) |
| Java 11 | LTS | 2018 | Minimo aceptable |
| Java 17 | LTS | 2021 | Buena opcion estable |
| **Java 25** | **LTS** | **Sep 2025** | **RECOMENDADA para produccion** |
| Java 26 | Short-term | Mar 2026 | Para experimentar (no LTS) |

## Features mas importantes por version (resumen ejecutivo)

```
Java 8:  Lambdas, Streams, Optional, java.time
Java 9:  Modules, List.of(), private interface methods
Java 10: var (inferencia de tipo local)
Java 11: String.strip(), HTTP Client, Files.readString [LTS]
Java 14: Switch expressions, Records (preview)
Java 16: Records (estable), Pattern matching instanceof
Java 17: Sealed classes, text blocks [LTS]
Java 21: Virtual Threads, Sequenced Collections, Pattern switch [LTS]
Java 22: Unnamed variables, Statements before super (preview)
Java 23: Stream Gatherers (preview), Module imports (preview)
Java 24: Flexible constructors (preview), Class-File API
Java 25: Scoped Values (estable), Compact source files [LTS]
Java 26: AOT caching, Lazy constants, HTTP/3, Structured Concurrency (estable)
```

---

# Ejercicios para practicar features modernas

## Ejercicio 1: Records + Sealed + Pattern Matching
```java
// Implementa un sistema de pagos con:
// - Sealed interface MetodoPago
// - Records: Efectivo, Tarjeta, Transferencia, Crypto
// - Metodo procesarPago() con switch exhaustivo
// - Cada tipo calcula comision diferente
```

## Ejercicio 2: Virtual Threads + Structured Concurrency
```java
// Implementa un servicio que:
// - Recibe un userId
// - En PARALELO busca: perfil, pedidos, recomendaciones
// - Si alguno falla, cancela los demas
// - Retorna un Dashboard con los 3 resultados
// - Usa StructuredTaskScope
```

## Ejercicio 3: Streams + Gatherers
```java
// Dado un stream de transacciones bancarias:
// - Agrupar en ventanas de 5 minutos
// - Calcular el total por ventana
// - Detectar ventanas con monto > $100,000 (sospechosas)
// - Usar Stream Gatherers para las ventanas
```

## Ejercicio 4: Scoped Values
```java
// Implementa un sistema donde:
// - Un filtro HTTP establece el usuario autenticado como ScopedValue
// - El controller, service y repository pueden acceder al usuario
// - SIN pasarlo como parametro en cada metodo
// - Registrar auditoria con el usuario del scope
```

## Ejercicio 5: Compact Source Files
```java
// Crea 5 scripts Java sin declaracion de clase:
// 1. Calculadora interactiva
// 2. Generador de passwords
// 3. Lector de CSV que imprime estadisticas
// 4. Cliente HTTP que consulta una API publica
// 5. Mini servidor HTTP que responde JSON
// Ejecutar cada uno con: java Script.java
```

---

*Fuentes: [oracle.com/java](https://blogs.oracle.com/java/the-arrival-of-java-26), [openjdk.org](https://openjdk.org), [happycoders.eu](https://www.happycoders.eu/java/java-25-features/), [infoworld.com](https://www.infoworld.com/article/4050993/jdk-26-the-new-features-in-java-26.html). Content rephrased for compliance.*
