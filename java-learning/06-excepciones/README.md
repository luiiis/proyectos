# Módulo 06: Excepciones

## Jerarquía
```
Throwable
├── Error (no manejar: OutOfMemoryError, StackOverflowError)
└── Exception
    ├── Checked (DEBES manejar: IOException, SQLException)
    └── RuntimeException / Unchecked (puedes manejar: NullPointerException, IllegalArgumentException)
```

## Manejo de Excepciones
```java
try {
    int resultado = 10 / 0;
} catch (ArithmeticException e) {
    System.out.println("Error: " + e.getMessage());
} catch (Exception e) {
    System.out.println("Error genérico: " + e.getMessage());
} finally {
    System.out.println("Siempre se ejecuta");
}
```

## Custom Exceptions (Patrón Enterprise)
```java
public class BusinessException extends RuntimeException {
    private final String code;
    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }
    public String getCode() { return code; }
}

public class ProductoNoEncontradoException extends BusinessException {
    public ProductoNoEncontradoException(Long id) {
        super("PROD_NOT_FOUND", "Producto no encontrado con id: " + id);
    }
}
```

## Ejercicios
1. Crea un validador de edad que lance excepción si es negativa o > 150
2. Implementa try-with-resources para leer un archivo
3. Crea una jerarquía de excepciones para un sistema bancario
4. ¿Cuándo usar checked vs unchecked exceptions?
