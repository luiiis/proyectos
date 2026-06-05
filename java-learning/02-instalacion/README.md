# Módulo 02: Instalación del Entorno de Desarrollo

## 1. Instalar JDK 21+

### Windows
```
1. Ir a: https://adoptium.net/
2. Descargar: Temurin JDK 21 (o 25) para Windows x64
3. Ejecutar instalador → Next → Next → Install
4. Verificar en terminal:
   java -version    → "openjdk 21.0.x"
   javac -version   → "javac 21.0.x"
```

### Mac
```bash
# Con Homebrew (recomendado)
brew install --cask temurin@21

# Verificar
java -version
```

### Linux
```bash
sudo apt update
sudo apt install openjdk-21-jdk
java -version
```

### Configurar JAVA_HOME (Windows)
```
1. Buscar "Variables de entorno" en Windows
2. Variables del sistema → Nueva:
   Nombre: JAVA_HOME
   Valor: C:\Program Files\Eclipse Adoptium\jdk-21.0.x
3. Editar PATH → Agregar: %JAVA_HOME%\bin
```

---

## 2. Instalar IDE

### IntelliJ IDEA (RECOMENDADO para Java)
```
1. Descargar: https://www.jetbrains.com/idea/download/
2. Community Edition = GRATIS (suficiente para este curso)
3. Instalar → Abrir → Configurar JDK:
   File → Project Structure → SDKs → Add JDK → Seleccionar carpeta del JDK
```

### VS Code (alternativa ligera)
```
1. Instalar VS Code: https://code.visualstudio.com/
2. Instalar extensiones:
   - Extension Pack for Java (Microsoft)
   - Spring Boot Extension Pack
   - Lombok Annotations Support
```

---

## 3. Instalar Maven

```bash
# Windows (con Chocolatey)
choco install maven

# Mac
brew install maven

# Linux
sudo apt install maven

# Verificar
mvn -version
# Apache Maven 3.9.x
```

---

## 4. Instalar Docker Desktop

```
1. Descargar: https://www.docker.com/products/docker-desktop/
2. Instalar y reiniciar
3. Verificar:
   docker --version
   docker compose version
```

---

## 5. Primer Proyecto con IntelliJ

```
1. File → New → Project
2. Language: Java
3. Build system: Maven (o IntelliJ para empezar simple)
4. JDK: 21
5. Nombre: java-learning-exercises
6. Create

7. Crear clase: src/main/java/HolaMundo.java
8. Escribir el código del módulo 01
9. Clic derecho → Run 'HolaMundo.main()'
10. Ver output en la consola inferior
```

---

## 6. JShell (REPL interactivo)

```bash
# JShell permite ejecutar Java línea por línea (sin crear archivos)
jshell

# Dentro de jshell:
jshell> System.out.println("Hola desde JShell")
Hola desde JShell

jshell> int x = 42
x ==> 42

jshell> x * 2
$3 ==> 84

jshell> /exit
```

Útil para experimentar rápidamente sin crear un proyecto completo.

---

## 7. Ejercicios

1. Instala JDK 21+ y verifica con `java -version`
2. Crea un proyecto en IntelliJ y ejecuta "Hola Mundo"
3. Abre JShell y calcula: 2^10, la raíz cuadrada de 144, y concatena dos strings
4. Crea un programa que reciba tu nombre por argumento y lo imprima:
   `java Saludo Carlos` → "¡Hola, Carlos!"

---

## Siguiente Módulo
→ [03-Java Básico](../03-java-basico/README.md)
