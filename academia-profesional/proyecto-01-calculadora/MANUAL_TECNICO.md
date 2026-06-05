# Manual Técnico - Proyecto 01: Calculadora

## Arquitectura
```
┌─────────────────────────────────┐
│         Calculadora.java         │
├─────────────────────────────────┤
│ main()          → Menú y flujo  │
│ calcular()      → Switch de ops │
│ validarEntrada() → Manejo error │
│ mostrarHistorial() → Listar    │
├─────────────────────────────────┤
│ Record: Operacion               │
│  (num1, operador, num2, result) │
├─────────────────────────────────┤
│ ArrayList<Operacion> historial  │
└─────────────────────────────────┘
```

## Tecnologías
| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| Java | 21+ | Lenguaje |
| Scanner | java.util | Entrada de consola |
| ArrayList | java.util | Almacenar historial |
| Record | Java 16+ | Representar operaciones |

## Estructura de archivos
```
proyecto-01-calculadora/
├── src/
│   └── Calculadora.java    ← Todo el código
├── README.md
├── CONSTRUCCION.md
├── COMO_EJECUTAR.md
├── MANUAL_TECNICO.md
├── ejercicios/
│   └── retos.md
└── entrevistas/
    └── preguntas.md
```

## Flujo de ejecución
```
1. main() inicia
2. Muestra menú
3. Lee opción del usuario
4. Si es operación: pide 2 números → calcula → guarda en historial
5. Si es historial: muestra todas las operaciones previas
6. Si es salir: termina el programa
7. Si es inválido: muestra error y vuelve al menú
```
