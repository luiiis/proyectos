package com.softwarelee.productos.domain.ports.in;

import com.softwarelee.productos.domain.model.Producto;

import java.util.List;

/**
 * PUERTO DE ENTRADA: Define los casos de uso de consulta de productos.
 */
public interface ConsultarProductoUseCase {
    List<Producto> listarTodos();
    Producto buscarPorId(Long id);
    List<Producto> buscarPorNombre(String nombre);
    List<Producto> buscarPorCategoria(Long categoriaId);
}
