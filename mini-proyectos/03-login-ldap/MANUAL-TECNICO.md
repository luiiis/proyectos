# Manual Técnico — Mini-Proyecto 03: Login LDAP

## Arquitectura
```
Cliente → Spring Security → LDAP Server (OpenLDAP)
                                ↓
                         Validar credenciales
                                ↓
                         Devolver grupos/roles
```

## Stack
| Componente | Tecnología |
|-----------|-----------|
| Backend | Spring Boot 3.3 + Spring Security LDAP |
| LDAP Server | OpenLDAP (Docker) |
| Admin GUI | phpLDAPadmin |

## Estructura
```
03-login-ldap/
├── docker-compose.yml
├── backend/
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/com/softwarelee/ldap/
│       ├── Application.java
│       ├── SecurityConfig.java
│       └── AuthController.java
```

## Puertos
| Servicio | Puerto |
|----------|--------|
| Backend | 8080 |
| LDAP | 389 |
| LDAP Admin UI | 8090 |
