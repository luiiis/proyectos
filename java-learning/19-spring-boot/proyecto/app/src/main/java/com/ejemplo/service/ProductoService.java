package com.ejemplo.service;

import com.ejemplo.dto.ProductoRequest;
import com.ejemplo.dto.ProductoResponse;
import com.ejemplo.entity.Producto;
import com.ejemplo.repository.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public Page<ProductoResponse> listar(Pageable pageable) {
        return repository.findByActivoTrue(pageable).map(this::toResponse);
    }

    public ProductoResponse buscarPorId(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
    }

    public List<ProductoResponse> buscarPorNombre(String nombre) {
        return repository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::toResponse).toList();
    }

    @Transactional
    public ProductoResponse crear(ProductoRequest request) {
        var producto = new Producto();
        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
        producto.setCategoria(request.categoria());
        return toResponse(repository.save(producto));
    }

    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        var producto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
        if (request.nombre() != null) producto.setNombre(request.nombre());
        if (request.precio() != null) producto.setPrecio(request.precio());
        if (request.descripcion() != null) producto.setDescripcion(request.descripcion());
        producto.setStock(request.stock());
        return toResponse(repository.save(producto));
    }

    @Transactional
    public void eliminar(Long id) {
        var producto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
        producto.setActivo(false);
        repository.save(producto);
    }

    private ProductoResponse toResponse(Producto p) {
        return new ProductoResponse(p.getId(), p.getNombre(), p.getDescripcion(),
                p.getPrecio(), p.getStock(), p.getCategoria(), p.getCreatedAt());
    }
}
