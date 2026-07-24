# Mini-Proyecto 01: Login Básico con Sesión HTTP

## Qué aprenderás
- Spring Security con formulario de login
- Sesiones HTTP (stateful, el servidor recuerda quién eres)
- UserDetailsService (cargar usuario de BD)
- BCrypt (hashear passwords)
- Proteger endpoints por rol

## Diferencia con JWT
| Sesión (este proyecto) | JWT (proyecto 02) |
|----------------------|-------------------|
| Servidor GUARDA la sesión | Servidor NO guarda nada |
| Cookie JSESSIONID | Header Authorization: Bearer |
| Funciona con 1 servidor | Funciona con N servidores |
| Más simple | Más escalable |
| Para apps tradicionales | Para APIs + SPAs |

## Endpoints
```
GET  /login         → Formulario de login (HTML)
POST /login         → Procesar credenciales
GET  /dashboard     → Página protegida (requiere login)
GET  /admin         → Solo ADMIN puede acceder
POST /logout        → Cerrar sesión
```

## Ejecutar
```bash
cd 01-login-sesion/backend
mvn spring-boot:run
# → http://localhost:8080/login
# Usuario: admin / Admin123!
```
