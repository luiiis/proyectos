# SonarQube — Calidad de Código Profesional

## ¿Qué es SonarQube?
SonarQube analiza tu código y detecta:
- 🐛 **Bugs**: código que probablemente falle
- 🔒 **Vulnerabilidades**: problemas de seguridad
- 💩 **Code Smells**: código que funciona pero es difícil de mantener
- 📊 **Cobertura**: % del código cubierto por tests
- 📐 **Duplicación**: código repetido

---

## 1. Instalar SonarQube con Docker

```cmd
:: Levantar SonarQube
docker run -d --name sonarqube -p 9000:9000 sonarqube:community

:: Esperar ~1 minuto a que arranque
:: Abrir: http://localhost:9000
:: Login: admin / admin (te pedirá cambiar la contraseña)
```

---

## 2. Configurar tu proyecto Maven

### pom.xml — Agregar plugin de SonarQube + JaCoCo
```xml
<properties>
    <sonar.organization>softwarelee</sonar.organization>
    <sonar.projectKey>api-productos</sonar.projectKey>
    <sonar.host.url>http://localhost:9000</sonar.host.url>
    <sonar.coverage.jacoco.xmlReportPaths>
        ${project.build.directory}/site/jacoco/jacoco.xml
    </sonar.coverage.jacoco.xmlReportPaths>
</properties>

<build>
    <plugins>
        <!-- JaCoCo: genera reporte de cobertura -->
        <plugin>
            <groupId>org.jacoco</groupId>
            <artifactId>jacoco-maven-plugin</artifactId>
            <version>0.8.12</version>
            <executions>
                <execution>
                    <goals><goal>prepare-agent</goal></goals>
                </execution>
                <execution>
                    <id>report</id>
                    <phase>test</phase>
                    <goals><goal>report</goal></goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

---

## 3. Ejecutar análisis

```cmd
:: Paso 1: Ejecutar tests (genera reporte JaCoCo)
mvn clean test

:: Paso 2: Enviar a SonarQube
mvn sonar:sonar -Dsonar.token=TU_TOKEN_DE_SONAR
```

### Obtener token de SonarQube:
```
1. http://localhost:9000
2. My Account → Security → Generate Tokens
3. Nombre: "local-analysis"
4. Copiar el token generado
```

---

## 4. Lo que ves en el Dashboard

```
┌─────────────────────────────────────────────────────────────┐
│  API Productos — Quality Gate: ✅ PASSED                      │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  Bugs: 0          Vulnerabilities: 0       Code Smells: 3    │
│  Coverage: 82%    Duplications: 1.2%                          │
│                                                               │
│  ──────────────────────────────────────────────────           │
│  Reliability: A   Security: A   Maintainability: A            │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

---

## 5. Problemas comunes que detecta

### 🐛 Bug: NullPointerException potencial
```java
// ❌ SonarQube marca esto
public String getNombre(Producto p) {
    return p.getNombre().toUpperCase();  // p podría ser null
}

// ✅ Corrección
public String getNombre(Producto p) {
    if (p == null || p.getNombre() == null) return "";
    return p.getNombre().toUpperCase();
}
```

### 🔒 Vulnerabilidad: SQL Injection
```java
// ❌ NUNCA concatenar valores en SQL
String sql = "SELECT * FROM productos WHERE nombre = '" + nombre + "'";

// ✅ Usar parámetros (MyBatis lo hace automáticamente con #{})
// En MyBatis XML:
// WHERE nombre = #{nombre}
```

### 💩 Code Smell: Método muy largo
```java
// ❌ Método de 80 líneas
public void procesarVenta(...) {
    // validar cliente...
    // validar productos...
    // calcular totales...
    // actualizar stock...
    // registrar movimiento...
    // generar ticket...
    // enviar correo...
}

// ✅ Dividir en métodos pequeños
public void procesarVenta(...) {
    validarCliente(cliente);
    validarProductos(productos);
    BigDecimal total = calcularTotal(productos);
    actualizarStock(productos);
    registrarMovimiento(venta);
    enviarConfirmacion(cliente, venta);
}
```

### 💩 Code Smell: Variable no usada
```java
// ❌ SonarQube marca variables sin usar
String resultado = "ok";  // nunca se usa
return ResponseEntity.ok(Map.of("success", true));

// ✅ Eliminar la variable
return ResponseEntity.ok(Map.of("success", true));
```

### 📐 Duplicación
```java
// ❌ Código repetido en 3 controllers
Map.of("success", false, "error", ex.getMessage(), "timestamp", LocalDateTime.now().toString())

// ✅ Extraer a un método utilitario o al GlobalExceptionHandler
```

---

## 6. Quality Gates (umbral de calidad)

Un Quality Gate es el MÍNIMO que tu código debe cumplir:

| Métrica | Umbral recomendado |
|---------|-------------------|
| Cobertura | ≥ 80% |
| Duplicación | ≤ 3% |
| Bugs | 0 |
| Vulnerabilidades | 0 |
| Code Smells nuevos | Calificación A |
| Reliability Rating | A |
| Security Rating | A |

Si tu código NO pasa el Quality Gate → no se puede mergear a main.

---

## 7. Integrar con CI/CD (GitHub Actions)

```yaml
# .github/workflows/sonar.yml
name: SonarQube Analysis

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  sonar:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'
      - name: Cache Maven packages
        uses: actions/cache@v4
        with:
          path: ~/.m2
          key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}
      - name: Build and analyze
        env:
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
          SONAR_HOST_URL: ${{ secrets.SONAR_HOST_URL }}
        run: mvn clean verify sonar:sonar
```

---

## 8. Reglas importantes para tu roadmap

| Nivel | Meta de SonarQube |
|-------|------------------|
| 1-3 | Familiarizarte (0 bugs, 0 vulnerabilidades) |
| 4 | Cobertura ≥ 70% con los tests que creaste |
| 5-7 | 0 vulnerabilidades de seguridad |
| 11-12 | Quality Gate PASS completo |
| 14 | Integrado en CI/CD (análisis automático en cada push) |

---

## 9. Alternativa: SonarLint (en tu editor)

SonarLint es una extensión para IntelliJ/VS Code que te muestra problemas EN TIEMPO REAL mientras escribes:

```
IntelliJ: Settings → Plugins → SonarLint → Install
VS Code: Extensions → SonarLint → Install
```

Te marca los problemas con subrayado amarillo/rojo directamente en el editor.

---

## Siguiente paso
Instala SonarQube con Docker y ejecuta tu primer análisis en el proyecto del nivel 3 o 4.
