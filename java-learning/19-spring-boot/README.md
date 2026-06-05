# Módulo 19: Spring Boot

## ¿Qué agrega Spring Boot sobre Spring?
- **Auto-configuration**: detecta dependencias y configura automáticamente
- **Starters**: paquetes de dependencias pre-configurados
- **Embedded server**: Tomcat incluido (no necesitas instalar servidor aparte)
- **application.yml**: configuración centralizada
- **Actuator**: health checks y métricas out-of-the-box

## Crear proyecto
```bash
# Spring Initializr (web): https://start.spring.io
# O con CLI:
spring init --dependencies=web,data-jpa,postgresql,security mi-proyecto
```

## application.yml
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/empresa_db
    username: postgres
    password: postgres123
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
  profiles:
    active: dev

# Custom properties
app:
  nombre: Mi Aplicación
  version: 1.0.0
  jwt:
    secret: ${JWT_SECRET:defaultSecret}
    expiration: 86400000
```

## Profiles (ambientes)
```yaml
# application-dev.yml (desarrollo)
spring:
  jpa:
    show-sql: true

# application-prod.yml (producción)
spring:
  jpa:
    show-sql: false
```

## Ejercicios
1. Crea un proyecto Spring Boot con web + JPA + PostgreSQL
2. Configura profiles para dev y prod
3. Crea un endpoint /health personalizado
4. Usa @ConfigurationProperties para mapear configuración custom a una clase
