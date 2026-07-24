package com.softwarelee.productos.application.usecase;

import com.softwarelee.productos.domain.model.Producto;
import com.softwarelee.productos.domain.ports.in.CrearProductoUseCase;
import com.softwarelee.productos.domain.ports.out.ProductoRepository;
import org.springframework.stereotype.Service;

/**
 * CASO DE USO: Crear un producto.
 *
 * Implementa el puerto de entrada (CrearProductoUseCase).
 * Usa el puerto de salida (ProductoRepository) para persistir.
 * Orquesta la validación del dominio + la persistencia.
 */
@Service
public class CrearProductoUseCaseImpl implements CrearProductoUseCase {

    private final ProductoRepository productoRepository;

    public CrearProductoUseCaseImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public Producto ejecutar(Producto producto) {
        // 1. Validar reglas de dominio
        producto.validar();

        // 2. Persistir
        return productoRepository.save(producto);
    }
}
