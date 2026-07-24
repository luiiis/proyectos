# Mini-Proyecto 03: Login con LDAP / Active Directory

## Qué aprenderás
- Qué es LDAP y cómo funciona
- Configurar Spring Security con LDAP
- Autenticar contra Active Directory corporativo
- Mapear grupos LDAP a roles de tu aplicación
- Usar Docker para simular un LDAP local (OpenLDAP)

## ¿Qué es LDAP?
```
LDAP = Lightweight Directory Access Protocol
Es un "directorio de usuarios" corporativo. 
En empresas grandes, los usuarios NO están en tu BD.
Están en Active Directory (Microsoft) o OpenLDAP.

Tu app NO guarda passwords. Le pregunta al LDAP:
"¿Este usuario con esta contraseña es válido?" → Sí/No
```

## Flujo
```
1. Usuario escribe usuario/password en tu app
2. Tu app envía las credenciales al servidor LDAP
3. LDAP responde: "Sí, es válido" + sus grupos (ej: CN=Developers)
4. Tu app mapea: grupo "Developers" → rol "DEVELOPER"
5. Usuario autenticado con roles
```

## Docker para simular LDAP local
```yaml
# docker-compose.yml
services:
  ldap:
    image: osixia/openldap:1.5.0
    container_name: mini-ldap
    environment:
      LDAP_ORGANISATION: "Mi Empresa"
      LDAP_DOMAIN: "miempresa.com"
      LDAP_ADMIN_PASSWORD: "admin123"
    ports: ["389:389"]

  ldap-admin:
    image: osixia/phpldapadmin:0.9.0
    container_name: mini-ldap-admin
    environment:
      PHPLDAPADMIN_LDAP_HOSTS: ldap
    ports: ["8090:80"]
    depends_on: [ldap]
```

## Configuración Spring Boot
```yaml
spring:
  ldap:
    urls: ldap://localhost:389
    base: dc=miempresa,dc=com
    username: cn=admin,dc=miempresa,dc=com
    password: admin123
```

## Ejecutar
```bash
# 1. Levantar LDAP simulado
docker compose up -d
# Admin GUI: http://localhost:8090 (Login DN: cn=admin,dc=miempresa,dc=com)

# 2. Levantar backend
cd backend && mvn spring-boot:run

# 3. Login
curl -X POST http://localhost:8080/api/auth/login \
  -d '{"username":"jgarcia","password":"Pass123!"}'
```

## Cuándo se usa LDAP en la vida real
- Empresas grandes (bancos, gobierno, telecomunicaciones)
- Cuando los usuarios ya están en Active Directory
- Single Sign-On corporativo
- No quieres que cada app tenga su propia tabla de usuarios
