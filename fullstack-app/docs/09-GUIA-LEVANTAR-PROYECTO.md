# Guía Práctica: Levantar Todo desde Cero hasta Despliegue

## Prerrequisitos (Instalar en tu máquina)

### Opción A: Solo con Docker (más fácil)
```
1. Docker Desktop: https://www.docker.com/products/docker-desktop/
   - Incluye Docker Engine + Docker Compose
   - Windows/Mac: instalador gráfico
   - Asignar mínimo 8GB RAM en Settings → Resources

2. Git: https://git-scm.com/downloads
```

### Opción B: Desarrollo local (sin Docker para los backends)
```
1. Java 17 (JDK): https://adoptium.net/
   Verificar: java -version → "17.x.x"

2. Maven: https://maven.apache.org/download.cgi
   Verificar: mvn -version → "3.9.x"

3. Node.js 20: https://nodejs.org/
   Verificar: node -version → "20.x.x"
   Verificar: npm -version → "10.x.x"

4. Angular CLI: npm install -g @angular/cli
   Verificar: ng version → "17.x.x"

5. Docker Desktop (para las bases de datos)
```

---

## Paso 1: Clonar y Configurar

```bash
# Clonar el repositorio
git clone <tu-repositorio>
cd fullstack-app

# Crear archivo de variables de entorno
cp .env.example .env

# Editar .env con tus valores (especialmente el correo para SMTP)
# En Windows: notepad .env
# En Mac/Linux: nano .env
```

---

## Paso 2: Levantar Bases de Datos con Docker

```bash
# Levantar SOLO las bases de datos (sin los backends ni frontend)
docker compose up mysql oracle sqlserver -d

# Ver el estado (esperar a que digan "healthy")
docker compose ps

# Esto puede tardar 2-5 minutos (especialmente Oracle)
# Puedes ver los logs mientras esperas:
docker compose logs -f mysql
docker compose logs -f oracle
docker compose logs -f sqlserver
```

### Verificar que las BDs están funcionando:

```bash
# MySQL
docker exec -it fullstack-mysql mysql -uroot -prootPassword123! -e "SHOW DATABASES;"
# Debes ver: auth_db

# SQL Server
docker exec -it fullstack-sqlserver /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P 'SqlServer123!' -C -Q "SELECT name FROM sys.databases"
# Debes ver: logs_db
```

---

## Paso 3: Ejecutar Scripts de Base de Datos

### MySQL (se ejecuta automáticamente con Docker)
Los scripts en `docker/init-scripts/mysql/` se ejecutan automáticamente la primera vez que arranca el contenedor.

Si necesitas re-ejecutarlos manualmente:
```bash
docker exec -i fullstack-mysql mysql -uroot -prootPassword123! < docker/init-scripts/mysql/01-schema.sql
docker exec -i fullstack-mysql mysql -uroot -prootPassword123! < docker/init-scripts/mysql/02-seed-data.sql
```

### Oracle (ejecutar manualmente)
```bash
# Conectar al contenedor Oracle
docker exec -it fullstack-oracle sqlplus products_user/productsPass123!@XEPDB1

# Dentro de SQLPlus, ejecutar:
@/container-entrypoint-initdb.d/01-schema.sql
@/container-entrypoint-initdb.d/02-seed-data.sql

# Verificar
SELECT COUNT(*) FROM products;
-- Debe mostrar: 15

EXIT;
```

### SQL Server (ejecutar con script)
```bash
# Ejecutar el inicializador
docker compose up sqlserver-init

# O manualmente:
docker exec -it fullstack-sqlserver /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P 'SqlServer123!' -C -i /scripts/01-schema.sql
docker exec -it fullstack-sqlserver /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P 'SqlServer123!' -C -i /scripts/02-seed-data.sql
```

### Verificar datos poblados:
```bash
# MySQL - Usuarios
docker exec -it fullstack-mysql mysql -uroot -prootPassword123! -e "USE auth_db; SELECT username, email FROM users;"
# Resultado: admin, manager1, user1, user2, user3

# Oracle - Productos
docker exec -it fullstack-oracle sqlplus products_user/productsPass123!@XEPDB1 -s <<EOF
SELECT name, price, stock FROM products WHERE ROWNUM <= 5;
EOF

# SQL Server - Audit logs
docker exec -it fullstack-sqlserver /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P 'SqlServer123!' -C -Q "USE logs_db; SELECT TOP 5 action, username FROM audit_logs;"
```

---

## Paso 4: Levantar Backend Auth (Desarrollo Local)

```bash
cd backend-auth

# Configurar variables de entorno (Windows PowerShell)
$env:MYSQL_HOST="localhost"
$env:MYSQL_PORT="3306"
$env:MYSQL_DB="auth_db"
$env:MYSQL_USER="root"
$env:MYSQL_PASSWORD="rootPassword123!"
$env:ORACLE_HOST="localhost"
$env:ORACLE_PORT="1521"
$env:ORACLE_SID="XEPDB1"
$env:ORACLE_USER="products_user"
$env:ORACLE_PASSWORD="productsPass123!"
$env:SQLSERVER_HOST="localhost"
$env:SQLSERVER_PORT="1433"
$env:SQLSERVER_DB="logs_db"
$env:SQLSERVER_USER="sa"
$env:SQLSERVER_PASSWORD="SqlServer123!"
$env:JWT_SECRET="bXlTZWNyZXRLZXlGb3JKV1RUb2tlbkdlbmVyYXRpb25NdXN0QmUyNTZCaXRzTG9uZyEh"
$env:REDIS_HOST="localhost"

# Compilar y ejecutar
mvn spring-boot:run

# Debes ver: "Started BackendAuthApplication in X seconds"
# El servidor está en: http://localhost:8080
```

### Verificar que funciona:
```bash
# Probar registro
curl -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d "{\"username\":\"testuser\",\"email\":\"test@test.com\",\"password\":\"test123\"}"

# Probar login
curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"admin123\"}"

# Debes recibir un JSON con accessToken

# Swagger UI (documentación interactiva):
# Abrir en navegador: http://localhost:8080/swagger-ui.html
```

---

## Paso 5: Levantar Backend Mail (Desarrollo Local)

```bash
cd backend-mail

# Variables de entorno
$env:MYSQL_HOST="localhost"
$env:MYSQL_PASSWORD="rootPassword123!"
$env:MAIL_HOST="smtp.gmail.com"
$env:MAIL_PORT="587"
$env:MAIL_USERNAME="tu-correo@gmail.com"
$env:MAIL_PASSWORD="tu-app-password-16-chars"
$env:JWT_SECRET="bXlTZWNyZXRLZXlGb3JKV1RUb2tlbkdlbmVyYXRpb25NdXN0QmUyNTZCaXRzTG9uZyEh"

# Ejecutar
mvn spring-boot:run

# Servidor en: http://localhost:8081
```

### Configurar Gmail para envío de correos:
```
1. Ir a https://myaccount.google.com/security
2. Activar "Verificación en 2 pasos"
3. Ir a https://myaccount.google.com/apppasswords
4. Crear contraseña de aplicación para "Correo" → "Otro (nombre personalizado)"
5. Google te da una contraseña de 16 caracteres (ej: "abcd efgh ijkl mnop")
6. Usar esa contraseña como MAIL_PASSWORD (sin espacios)
```

---

## Paso 6: Levantar Frontend Angular (Desarrollo Local)

```bash
cd frontend

# Instalar dependencias (primera vez)
npm install

# Ejecutar en modo desarrollo
ng serve

# Servidor en: http://localhost:4200
# Se recarga automáticamente cuando cambias código
```

### Verificar:
```
1. Abrir http://localhost:4200 en el navegador
2. Debes ver la pantalla de login
3. Ingresar: admin / admin123
4. Debes ver el dashboard
5. Navegar a Productos → ver la lista de 15 productos
```

---

## Paso 7: Levantar TODO con Docker (Producción)

```bash
cd fullstack-app

# Construir y levantar todo
docker compose up --build -d

# Ver progreso
docker compose logs -f

# Esperar a que todo esté "healthy" y "started"
docker compose ps

# Acceder:
# Frontend: http://localhost
# Backend: http://localhost:8080
# Swagger: http://localhost:8080/swagger-ui.html
```

---

## Paso 8: Levantar Monitoreo (Opcional)

```bash
# Levantar Redis + Prometheus + Grafana
docker compose -f docker-compose.yml -f docker-compose.monitoring.yml up -d

# Acceder:
# Grafana: http://localhost:3000 (admin/admin123)
# Prometheus: http://localhost:9090
```

---

## Paso 9: Ejecutar Tests

```bash
cd backend-auth

# Ejecutar todos los tests
mvn test

# Ejecutar un test específico
mvn test -Dtest="AuthServiceTest"

# Ejecutar con reporte detallado
mvn test -Dsurefire.reportFormat=plain

# Ver reporte HTML (después de ejecutar tests)
# Abrir: target/surefire-reports/
```

---

## Paso 10: Despliegue en Producción

### Opción A: Servidor con Docker Compose
```bash
# En tu servidor (VPS, EC2, etc.)
1. Instalar Docker
2. Clonar repositorio
3. Configurar .env con valores de producción
4. Configurar dominio DNS apuntando al servidor
5. Obtener certificado SSL:
   chmod +x docker/ssl/init-letsencrypt.sh
   ./docker/ssl/init-letsencrypt.sh
6. docker compose up -d
```

### Opción B: Kubernetes
```bash
# Requisito: cluster Kubernetes (EKS, GKE, AKS, o minikube local)
kubectl apply -f k8s/namespace.yml
kubectl apply -f k8s/secrets.yml
kubectl apply -f k8s/backend-auth-deployment.yml
kubectl apply -f k8s/ingress.yml
```

---

## Troubleshooting (Problemas Comunes)

### "Connection refused" al backend
```
Causa: El backend no ha terminado de arrancar
Solución: Esperar 30-60 segundos, verificar logs:
  docker compose logs backend-auth
```

### "Access denied" en MySQL
```
Causa: Password incorrecto o usuario no existe
Solución: Verificar variables de entorno en .env
  docker exec -it fullstack-mysql mysql -uroot -p
```

### Oracle tarda mucho
```
Causa: Normal, Oracle XE necesita 2-5 minutos para inicializar
Solución: Esperar. Verificar con:
  docker compose logs oracle | grep "DATABASE IS READY"
```

### Frontend muestra pantalla en blanco
```
Causa: Error de JavaScript en consola
Solución: Abrir DevTools (F12) → Console → ver error
  Común: CORS error → verificar que backend está corriendo
```

### "Too Many Requests" (429)
```
Causa: Rate limiting activado (más de 5 logins/min o 100 requests/min)
Solución: Esperar 1 minuto. En desarrollo, puedes desactivar RateLimitFilter
```

---

## Herramientas Recomendadas para Desarrollo

| Herramienta | Para qué | Descarga |
|-------------|----------|----------|
| IntelliJ IDEA | Desarrollo Java | jetbrains.com |
| VS Code | Frontend Angular | code.visualstudio.com |
| DBeaver | Conectar a las 3 BDs | dbeaver.io |
| Postman | Probar APIs REST | postman.com |
| Docker Desktop | Gestionar contenedores | docker.com |
| Git | Control de versiones | git-scm.com |

### Conexiones en DBeaver:
```
MySQL:
  Host: localhost, Port: 3306, Database: auth_db
  User: root, Password: rootPassword123!

Oracle:
  Host: localhost, Port: 1521, Database: XEPDB1
  User: products_user, Password: productsPass123!

SQL Server:
  Host: localhost, Port: 1433, Database: logs_db
  User: sa, Password: SqlServer123!
```
