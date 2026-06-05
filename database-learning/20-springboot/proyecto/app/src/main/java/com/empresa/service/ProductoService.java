package com.empresa.service;

import com.empresa.domain.entity.Producto;
import com.empresa.domain.repository.ProductoRepository;
import com.empresa.dto.ProductoDto;
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

    public Page<ProductoDto> listar(Pageable pageable) {
        return repository.findByActivoTrue(pageable).map(this::toDto);
    }

    public ProductoDto buscarPorId(Long id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
    }

    public List<ProductoDto> buscar(String nombre) {
        return repository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::toDto).toList();
    }

    public List<ProductoDto> stockBajo(int minimo) {
        return repository.findConStockBajo(minimo).stream()
                .map(this::toDto).toList();
    }

    public List<ProductoDto> masVendidos(int limite) {
        return repository.findMasVendidos(limite).stream()
                .map(this::toDto).toList();
    }

    @Transactional
    public ProductoDto crear(ProductoDto dto) {
        var producto = new Producto();
        producto.setNombre(dto.nombre());
        producto.setPrecio(dto.precio());
        producto.setStock(dto.stock());
        producto.setDescripcion(dto.descripcion());
        producto.setSku(dto.sku());
        producto.setCategoriaId(dto.categoriaId());
        producto.setProveedorId(dto.proveedorId());
        var guardado = repository.save(producto);
        return toDto(guardado);
    }

    @Transactional
    public ProductoDto actualizar(Long id, ProductoDto dto) {
        var producto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
        if (dto.nombre() != null) producto.setNombre(dto.nombre());
        if (dto.precio() != null) producto.setPrecio(dto.precio());
        if (dto.stock() != null) producto.setStock(dto.stock());
        if (dto.descripcion() != null) producto.setDescripcion(dto.descripcion());
        return toDto(repository.save(producto));
    }

    @Transactional
    public void eliminar(Long id) {
        var producto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
        producto.setActivo(false);
        repository.save(producto);
    }

    private ProductoDto toDto(Producto p) {
        return new ProductoDto(p.getId(), p.getNombre(), p.getDescripcion(),
                p.getPrecio(), p.getStock(), p.getSku(), p.getCategoriaId(), p.getProveedorId());
    }
}
