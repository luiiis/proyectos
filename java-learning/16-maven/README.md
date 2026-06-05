# Módulo 16: Maven

Maven gestiona dependencias, compilación, testing y empaquetado de proyectos Java.

## pom.xml explicado
```xml
<project>
    <groupId>com.empresa</groupId>      <!-- Organización -->
    <artifactId>mi-app</artifactId>     <!-- Nombre del proyecto -->
    <version>1.0.0</version>            <!-- Versión -->
    <packaging>jar</packaging>          <!-- Tipo de empaquetado -->

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>3.3.0</version>
        </dependency>
    </dependencies>
</project>
```

## Comandos principales
```bash
mvn clean           # Limpiar target/
mvn compile         # Compilar
mvn test            # Ejecutar tests
mvn package         # Generar .jar
mvn install         # Instalar en repo local
mvn spring-boot:run # Ejecutar Spring Boot
```

## Ejercicios
1. Crea un proyecto Maven desde cero con 3 dependencias
2. Configura profiles para dev/prod con diferentes properties
3. Crea un proyecto multi-módulo (parent + 2 hijos)
