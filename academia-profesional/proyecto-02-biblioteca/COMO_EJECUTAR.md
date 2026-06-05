# Cómo Ejecutar - Proyecto 02: Sistema de Biblioteca

## Requisitos
- Java 21+ instalado (`java -version`)

## Compilar y Ejecutar

```bash
cd academia-profesional/proyecto-02-biblioteca/src

# Compilar todas las clases
javac *.java

# Ejecutar
java Main
```

## Compilación alternativa (con paquetes)

```bash
cd academia-profesional/proyecto-02-biblioteca

# Compilar desde raíz del proyecto
javac -d bin src/**/*.java

# Ejecutar
java -cp bin Main
```

## Uso
```
═══ SISTEMA DE BIBLIOTECA ═══
1. Registrar libro
2. Registrar revista
3. Registrar usuario
4. Realizar préstamo
5. Devolver material
6. Buscar material
7. Ver préstamos de usuario
8. Salir
Opción: 4
ID Usuario: U001
ID Material: L003
Préstamo registrado. Fecha devolución: 2025-02-15
```

## Solución de errores

| Error | Causa | Solución |
|-------|-------|----------|
| `javac: command not found` | Java no instalado | Instalar JDK 21 |
| `cannot find symbol` | Falta compilar dependencias | Usar `javac *.java` para compilar todo |
| `ClassNotFoundException` | Clase no compilada | Verificar que todos los .class existen |
