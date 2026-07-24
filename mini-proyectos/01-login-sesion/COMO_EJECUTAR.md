# Cómo Ejecutar — Mini-Proyecto 01: Login con Sesión

## Requisitos
- Java 21 (`java -version`)
- Maven 3.9+ (`mvn -version`)

## Ejecutar
```cmd
cd mini-proyectos/01-login-sesion/backend
mvn spring-boot:run
```

## Probar en el navegador
1. Abrir: http://localhost:8080
2. Te redirige a: http://localhost:8080/login
3. Escribir: **admin** / **Admin123!**
4. Debes ver el Dashboard con tu nombre y roles
5. Click en "Admin" → ves el panel de administración
6. Click en "Cerrar Sesión" → vuelves al login

## Probar con usuario sin permisos
1. Login con: **usuario** / **User123!**
2. Intentar ir a: http://localhost:8080/admin
3. Resultado: error 403 Forbidden (no tiene rol ADMIN)

## Probar con credenciales incorrectas
1. Escribir: **admin** / **wrongpassword**
2. Resultado: redirige a /login?error=true con mensaje de error

## Archivos del proyecto
```
01-login-sesion/
├── README.md              ← Descripción y conceptos
├── MANUAL-TECNICO.md      ← Arquitectura y explicación del código
├── COMO_EJECUTAR.md       ← Este archivo
└── backend/
    ├── pom.xml
    └── src/main/
        ├── java/.../
        │   ├── Application.java      ← Main
        │   ├── SecurityConfig.java   ← Reglas de seguridad
        │   └── WebController.java    ← Páginas
        └── resources/
            ├── application.properties
            └── templates/
                ├── login.html        ← Formulario
                ├── dashboard.html    ← Página protegida
                └── admin.html        ← Solo ADMIN
```
