# Nivel 0: Preparación del Entorno + Fundamentos

## Objetivo
Instalar herramientas + aprender los fundamentos necesarios antes de tocar Spring Boot o Angular.

---

## Guías de Fundamentos (leer en orden)

### Bloque 1: Lenguajes y conceptos base
| # | Documento | Para qué | Leer antes de... |
|---|-----------|----------|-----------------|
| 1 | `01-JAVA-FUNDAMENTOS.md` | Variables, clases, interfaces, streams, Optional | Nivel 1 |
| 2 | `02-SQL-FUNDAMENTOS.md` | SELECT, INSERT, JOIN, GROUP BY, transacciones | Nivel 3 |
| 3 | `03-HTTP-Y-APIS.md` | Métodos HTTP, JSON, status codes, REST | Nivel 1 |
| 4 | `04-TYPESCRIPT-FUNDAMENTOS.md` | Tipos, interfaces, generics, async | Nivel 8 |
| 5 | `05-GIT-WORKFLOW.md` | Branches, commits, pull requests | Todo |

### Bloque 2: Herramientas de validación y calidad
| # | Documento | Para qué | Leer antes de... |
|---|-----------|----------|-----------------|
| 6 | `06-VALIDACION-POSTMAN.md` | Colecciones, tests automáticos, variables, Runner | Nivel 1 |
| 7 | `07-SWAGGER-OPENAPI.md` | Documentación interactiva auto-generada | Nivel 3 |
| 8 | `08-SONARQUBE.md` | Calidad de código, bugs, vulnerabilidades, cobertura | Nivel 4 |
| 9 | `09-VALIDACIONES-SPRING.md` | Bean Validation, @Valid, errores 400 | Nivel 2 |
| 10 | `10-SPRING-SECURITY-PRUEBAS.md` | Probar 401, 403, tokens, roles | Nivel 5 |
| 11 | `11-SPRING-DATA-JPA.md` | JPA vs MyBatis, relaciones, paginación | Referencia |
| 12 | `12-PRUEBAS-E2E-CHECKLIST.md` | Flujos completos, evidencias, checklist por nivel | Todo |

**No necesitas memorizar todo.** Lee una vez, entiende los conceptos, y vuelve a consultar cuando lo necesites.

---

## Instalación de Herramientas

---

## Lista de Herramientas

| # | Herramienta | Versión | Para qué |
|---|-------------|---------|----------|
| 1 | Java JDK | 21 (LTS) | Lenguaje de programación backend |
| 2 | Maven | 3.9+ | Gestión de dependencias y build |
| 3 | Git | 2.40+ | Control de versiones |
| 4 | GitHub | — | Repositorio remoto |
| 5 | IntelliJ IDEA / VS Code | Última | Editor de código |
| 6 | MySQL o PostgreSQL | 8.0 / 16 | Base de datos |
| 7 | DBeaver | Última | GUI para base de datos |
| 8 | Node.js | 22 LTS | Runtime para Angular |
| 9 | Angular CLI | 20 | Framework frontend |
| 10 | Postman | Última | Probar APIs |
| 11 | Docker Desktop | 24+ | Contenedores |
| 12 | Terminal | — | Ejecutar comandos |

---

## Paso 1: Instalar Java JDK 21

### Windows:
```
1. Ir a: https://adoptium.net/
2. Descargar: Temurin JDK 21 (LTS) para Windows x64
3. Ejecutar el instalador
4. IMPORTANTE: Marcar "Set JAVA_HOME" durante la instalación
5. Verificar:
```

```cmd
java -version
javac -version
echo %JAVA_HOME%
```

**Resultado esperado:**
```
openjdk version "21.x.x"
javac 21.x.x
C:\Program Files\Eclipse Adoptium\jdk-21...
```

### Si JAVA_HOME no se configuró:
```
1. Buscar "Variables de entorno" en Windows
2. Variables del sistema → Nueva
3. Nombre: JAVA_HOME
4. Valor: C:\Program Files\Eclipse Adoptium\jdk-21.0.x (tu ruta)
5. Editar PATH → Agregar: %JAVA_HOME%\bin
6. Cerrar y abrir nueva terminal
```

---

## Paso 2: Instalar Maven

### Windows:
```
1. Ir a: https://maven.apache.org/download.cgi
2. Descargar: apache-maven-3.9.x-bin.zip
3. Descomprimir en: C:\tools\apache-maven-3.9.x
4. Agregar a PATH: C:\tools\apache-maven-3.9.x\bin
5. Verificar:
```

```cmd
mvn -version
```

**Resultado esperado:**
```
Apache Maven 3.9.x
Maven home: C:\tools\apache-maven-3.9.x
Java version: 21.x.x
```

---

## Paso 3: Instalar Git

```
1. Ir a: https://git-scm.com/download/win
2. Descargar e instalar (opciones por defecto)
3. Verificar:
```

```cmd
git --version
git config --global user.name "Tu Nombre"
git config --global user.email "tu@email.com"
```

---

## Paso 4: Crear cuenta en GitHub

```
1. Ir a: https://github.com
2. Sign up → crear cuenta
3. Crear un repositorio de prueba
4. Clonar con: git clone https://github.com/tu-usuario/tu-repo.git
```

---

## Paso 5: Instalar IntelliJ IDEA (o VS Code)

### IntelliJ IDEA Community (gratis):
```
1. Ir a: https://www.jetbrains.com/idea/download/
2. Descargar Community Edition (gratis)
3. Instalar
```

### VS Code (alternativa):
```
1. Ir a: https://code.visualstudio.com/
2. Instalar extensiones:
   - Extension Pack for Java
   - Spring Boot Extension Pack
   - Angular Language Service
   - Prettier
```

---

## Paso 6: Instalar MySQL (o PostgreSQL)

### Opción A — MySQL:
```
1. Ir a: https://dev.mysql.com/downloads/installer/
2. Descargar MySQL Installer
3. Elegir: Server + Workbench
4. Password root: Root123! (para desarrollo)
5. Puerto: 3306
6. Verificar:
```
```cmd
mysql -u root -p
```

### Opción B — PostgreSQL:
```
1. Ir a: https://www.postgresql.org/download/
2. Instalar con Stack Builder
3. Password: postgres123
4. Puerto: 5432
5. Verificar:
```
```cmd
psql -U postgres
```

### Opción C — Con Docker (la más limpia):
```cmd
docker run --name mysql-dev -e MYSQL_ROOT_PASSWORD=Root123! -p 3306:3306 -d mysql:8.0
```

---

## Paso 7: Instalar DBeaver

```
1. Ir a: https://dbeaver.io/download/
2. Descargar Community Edition
3. Instalar
4. Conectar a MySQL/PostgreSQL con las credenciales del paso 6
```

---

## Paso 8: Instalar Node.js

```
1. Ir a: https://nodejs.org/
2. Descargar versión LTS (22.x)
3. Instalar
4. Verificar:
```

```cmd
node -v
npm -v
```

---

## Paso 9: Instalar Angular CLI

```cmd
npm install -g @angular/cli@20
ng version
```

---

## Paso 10: Instalar Postman

```
1. Ir a: https://www.postman.com/downloads/
2. Descargar e instalar
3. Crear cuenta (gratis)
```

---

## Paso 11: Instalar Docker Desktop

```
1. Ir a: https://www.docker.com/products/docker-desktop/
2. Descargar e instalar
3. Reiniciar computadora
4. Abrir Docker Desktop
5. Verificar:
```

```cmd
docker --version
docker compose version
docker run hello-world
```

---

## Paso 12: Comandos Básicos de Terminal

### Navegación:
```cmd
cd carpeta              → Entrar a carpeta
cd ..                   → Subir un nivel
dir                     → Listar archivos (Windows)
mkdir nueva-carpeta     → Crear carpeta
```

### Git básico:
```cmd
git init                → Iniciar repositorio
git add .               → Preparar cambios
git commit -m "mensaje" → Guardar cambios
git push                → Subir a GitHub
git pull                → Bajar cambios
git status              → Ver estado
git log --oneline       → Ver historial
```

### Maven básico:
```cmd
mvn spring-boot:run     → Ejecutar Spring Boot
mvn clean package       → Compilar JAR
mvn test                → Ejecutar tests
```

### Angular básico:
```cmd
ng new mi-app           → Crear proyecto
ng serve                → Ejecutar (localhost:4200)
ng generate component X → Crear componente
ng build                → Compilar para producción
```

---

## Checklist Final ✅

Ejecuta todos estos comandos. Si TODOS funcionan, estás listo:

```cmd
java -version           → Java 21+
mvn -version            → Maven 3.9+
git --version           → Git 2.40+
node -v                 → Node 22+
ng version              → Angular CLI 20
docker --version        → Docker 24+
docker compose version  → Compose v2+
mysql -u root -p -e "SELECT 1;"   → MySQL funciona
```

✅ Si todo pasa → estás listo para el Proyecto 1.
❌ Si algo falla → revisa los pasos de arriba.

---

## Errores Frecuentes

| Error | Causa | Solución |
|-------|-------|----------|
| 'java' no se reconoce | JAVA_HOME no configurado | Paso 1: configurar variable |
| 'mvn' no se reconoce | Maven no está en PATH | Paso 2: agregar a PATH |
| 'ng' no se reconoce | Angular CLI no instalado | `npm install -g @angular/cli` |
| Docker: "Cannot connect" | Docker Desktop no está corriendo | Abrir Docker Desktop primero |
| MySQL: "Access denied" | Password incorrecta | Verificar password del paso 6 |
| Puerto en uso | Otro programa usa ese puerto | Cambiar puerto o cerrar el programa |
