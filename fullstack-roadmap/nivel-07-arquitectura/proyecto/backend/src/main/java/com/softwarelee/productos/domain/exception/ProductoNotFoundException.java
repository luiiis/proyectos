package com.softwarelee.productos.domain.exception;

/**
 * Excepción de DOMINIO: el producto no fue encontrado.
 * No depende de Spring ni de HTTP. Es puro Java.
 */
public class ProductoNotFoundException extends RuntimeException {
    public ProductoNotFoundException(Long id) {
        super("Producto no encontrado con ID: " + id);
    }
}
