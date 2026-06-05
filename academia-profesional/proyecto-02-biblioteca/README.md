# Proyecto 02: Sistema de Biblioteca

## ¿Qué problema resuelve?
Aprender Programación Orientada a Objetos construyendo un sistema de gestión de biblioteca que maneja libros, revistas, préstamos y usuarios con herencia, polimorfismo e interfaces.

## Tecnologías
- Java 21
- POO: Herencia y Polimorfismo
- Interfaces (Prestable, Buscable)
- Clases abstractas
- Collections (ArrayList, HashMap)
- Enums para estados

## Funcionalidades
- Registro y búsqueda de libros y revistas
- Gestión de usuarios (alta, baja, consulta)
- Sistema de préstamos y devoluciones
- Control de disponibilidad de materiales
- Historial de préstamos por usuario
- Multas por devolución tardía
- Búsqueda por título, autor o ISBN

## Estructura de Clases
- `MaterialBiblioteca` (clase abstracta padre)
- `Libro` extends MaterialBiblioteca
- `Revista` extends MaterialBiblioteca
- `Usuario` con lista de préstamos activos
- `Prestamo` con fechas y estado
- `Biblioteca` como controlador principal
- `Prestable` (interface)
- `Buscable` (interface)

## Conceptos Clave
- Herencia: Libro y Revista heredan de MaterialBiblioteca
- Polimorfismo: tratar materiales de forma genérica
- Interfaces: contratos para préstamo y búsqueda
- Encapsulamiento: atributos privados con getters/setters
