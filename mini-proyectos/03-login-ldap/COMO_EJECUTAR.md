# Cómo Ejecutar — Mini-Proyecto 03: Login LDAP

## Con Docker (recomendado)
```cmd
cd mini-proyectos/03-login-ldap
docker compose up -d
```

## URLs
- Backend: http://localhost:8080
- LDAP Admin: http://localhost:8090 (Login: cn=admin,dc=softwarelee,dc=com / admin123)

## Probar
```bash
curl http://localhost:8080/api/public/health
curl -u admin:admin123 http://localhost:8080/api/me
```

## Parar
```cmd
docker compose down
```
