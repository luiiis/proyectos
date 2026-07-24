package com.softwarelee.productos.application.usecase;

import com.softwarelee.productos.domain.exception.ProductoNotFoundException;
import com.softwarelee.productos.domain.model.Producto;
import com.softwarelee.productos.domain.ports.in.ConsultarProductoUseCase;
import com.softwarelee.productos.domain.ports.out.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultarProductoUseCaseImpl implements ConsultarProductoUseCase {

    private final ProductoRepository productoRepository;

    public ConsultarProductoUseCaseImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException(id));
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombre(nombre);
    }

    @Override
    public List<Producto> buscarPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }
}
