# 📋 Cómo Ejecutar Cada Módulo - java-learning

## Módulos Java Core (03-15): Ejecutar con javac + java

```bash
cd java-learning/XX-nombre/proyecto
javac *.java
java NombreClase
```

| Módulo | Comando | Qué demuestra |
|--------|---------|---------------|
| 03 | `cd 03-java-basico/proyecto && javac *.java && java Ejercicio01_Variables` | Variables, tipos, casting |
| 03 | `java Ejercicio02_Calculadora` | Scanner, switch moderno |
| 03 | `java Ejercicio03_FizzBuzz` | Ciclos, streams |
| 04 | `cd 04-poo/proyecto && javac *.java && java SistemaBancario` | Herencia, polimorfismo, abstracción |
| 05 | `cd 05-colecciones/proyecto && javac *.java && java ColeccionesDemo` | List, Map, Set, Queue |
| 06 | `cd 06-excepciones/proyecto && javac *.java && java ExcepcionesDemo` | Try/catch, custom exceptions |
| 07 | `cd 07-generics/proyecto && javac *.java && java GenericsDemo` | Clases genéricas, bounded types |
| 08 | `cd 08-streams/proyecto && javac *.java && java StreamsDemo` | filter, map, reduce, groupingBy |
| 09 | `cd 09-lambdas/proyecto && javac *.java && java LambdasDemo` | Predicate, Function, Consumer |
| 10 | `cd 10-fechas/proyecto && javac *.java && java FechasDemo` | LocalDate, Duration, formateo |
| 11 | `cd 11-archivos/proyecto && javac *.java && java ArchivosDemo` | Leer/escribir archivos NIO |
| 12 | `cd 12-concurrencia/proyecto && javac *.java && java ConcurrenciaDemo` | Virtual Threads, CompletableFuture |
| 13 | `cd 13-jdbc/proyecto && javac *.java && java JdbcDemo` | Conexión directa a PostgreSQL* |
| 14 | `cd 14-patrones/proyecto && javac *.java && java PatronesDemo` | Strategy, Builder, Factory |
| 15 | `cd 15-testing/proyecto && javac *.java && java -ea CalculadoraTest` | Tests con assertions |

*Módulo 13 requiere PostgreSQL corriendo: `docker compose up postgres -d` (desde database-learning/)

---

## Módulos Spring Boot (19-30): Ejecutar con Maven

```bash
cd java-learning/19-spring-boot/proyecto/app
mvn spring-boot:run
# → http://localhost:8080
# → http://localhost:8080/swagger-ui.html
```

| Módulo | Comando | Qué demuestra |
|--------|---------|---------------|
| 19 | `cd 19-spring-boot/proyecto/app && mvn spring-boot:run` | API REST completa con H2 |
| 24 | `cd 24-docker/proyecto && docker compose up --build -d` | App dockerizada + PostgreSQL |
| 30 | `cd 30-proyecto-final/proyecto && docker compose up --build -d` | Sistema enterprise completo |

---

## Módulos de Configuración (16-18, 20-23, 25-29): Solo README

Estos módulos son de CONFIGURACIÓN y CONCEPTO. No tienen un programa ejecutable independiente porque se aplican DENTRO del proyecto Spring Boot (módulos 19 y 30):

| Módulo | Qué contiene | Dónde se aplica |
|--------|-------------|-----------------|
| 16 Maven | Explicación de pom.xml | Ver `19-spring-boot/proyecto/app/pom.xml` |
| 17 Gradle | Explicación de build.gradle | Alternativa a Maven |
| 18 Spring Core | IoC, DI, Beans | Ver código del módulo 19 |
| 20 JPA | Entities, Repository | Ver `19-spring-boot/proyecto/app/src/.../entity/` |
| 21 REST API | Controllers, DTOs | Ver `19-spring-boot/proyecto/app/src/.../controller/` |
| 22 Security | Spring Security config | Se aplica en módulo 30 |
| 23 JWT | Token generation/validation | Se aplica en módulo 30 |
| 25 Microservicios | Arquitectura distribuida | Concepto (requiere múltiples proyectos) |
| 26 Kafka | Event-driven | Concepto + config |
| 27 Redis | Cache | Se aplica en módulo 30 |
| 28 Kubernetes | Deployment manifests | Concepto + YAML |
| 29 Arquitectura | Hexagonal, Clean | Se aplica en módulo 30 |

---

## Prerrequisitos

```bash
# Para módulos 03-15 (Java puro):
java -version   # Java 21+

# Para módulos 19-30 (Spring Boot):
java -version   # Java 21+
mvn -version    # Maven 3.9+
docker --version # Docker 24+ (para módulos con BD)
```

---

## Flujo recomendado de aprendizaje

```
1. Ejecutar demos 03-15 (Java Core) → entender el lenguaje
2. Leer READMEs 16-18 (herramientas) → entender Maven y Spring
3. Ejecutar módulo 19 (Spring Boot) → tu primera API REST
4. Leer READMEs 20-23 (JPA, Security, JWT) → entender las capas
5. Ejecutar módulo 24 (Docker) → dockerizar la app
6. Leer READMEs 25-29 (avanzado) → arquitectura y DevOps
7. Ejecutar módulo 30 (Proyecto Final) → integrar TODO
```
