# Módulo 17: Gradle

Alternativa moderna a Maven. Más rápido, más flexible, sintaxis Kotlin/Groovy.

## build.gradle.kts (Kotlin DSL)
```kotlin
plugins {
    java
    id("org.springframework.boot") version "3.3.0"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

java { sourceCompatibility = JavaVersion.VERSION_21 }
```

## Comandos
```bash
./gradlew build      # Compilar + tests
./gradlew bootRun    # Ejecutar Spring Boot
./gradlew test       # Solo tests
./gradlew clean      # Limpiar
```

## Maven vs Gradle
| Aspecto | Maven | Gradle |
|---------|-------|--------|
| Config | XML (verbose) | Kotlin/Groovy (conciso) |
| Velocidad | Lento | Rápido (incremental + cache) |
| Flexibilidad | Rígido | Muy flexible |
| Curva | Más fácil | Más complejo |
| Uso | Enterprise legacy | Proyectos nuevos, Android |
