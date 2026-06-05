package com.academia.graphql.controller;

import com.academia.graphql.entity.Producto;
import com.academia.graphql.repository.ProductoRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

/**
 * GraphQL Controller: resuelve queries y mutations definidas en schema.graphqls
 * 
 * Diferencia con REST:
 * - REST: 1 metodo por endpoint (GET /productos, GET /productos/1, POST /productos)
 * - GraphQL: 1 metodo por CAMPO del schema (productos, producto, crearProducto)
 */
@Controller
public class ProductoGraphQLController {

    private final ProductoRepository repo;

    public ProductoGraphQLController(ProductoRepository repo) {
        this.repo = repo;
    }

    // ═══════ QUERIES (lectura) ═══════

    @QueryMapping  // Resuelve: query { productos(...) }
    public List<Producto> productos(@Argument String categoria, @Argument Integer limit) {
        List<Producto> resultado;
        if (categoria != null) {
            resultado = repo.findByCategoriaAndActivoTrue(categoria);
        } else {
            resultado = repo.findByActivoTrue();
        }
        if (limit != null && limit < resultado.size()) {
            resultado = resultado.subList(0, limit);
        }
        return resultado;
    }

    @QueryMapping  // Resuelve: query { producto(id: 1) }
    public Producto producto(@Argument Long id) {
        return repo.findById(id).orElse(null);
    }

    @QueryMapping  // Resuelve: query { buscarProductos(nombre: "laptop") }
    public List<Producto> buscarProductos(@Argument String nombre) {
        return repo.findByNombreContainingIgnoreCaseAndActivoTrue(nombre);
    }

    // ═══════ MUTATIONS (escritura) ═══════

    @MutationMapping  // Resuelve: mutation { crearProducto(input: {...}) }
    public Producto crearProducto(@Argument Map<String, Object> input) {
        var p = new Producto();
        p.setNombre((String) input.get("nombre"));
        p.setDescripcion((String) input.get("descripcion"));
        p.setPrecio(((Number) input.get("precio")).doubleValue());
        p.setStock(((Number) input.get("stock")).intValue());
        p.setCategoria((String) input.get("categoria"));
        p.setSku((String) input.get("sku"));
        return repo.save(p);
    }

    @MutationMapping
    public Producto actualizarProducto(@Argument Long id, @Argument Map<String, Object> input) {
        var p = repo.findById(id).orElseThrow(() -> new RuntimeException("No encontrado: " + id));
        if (input.containsKey("nombre")) p.setNombre((String) input.get("nombre"));
        if (input.containsKey("precio")) p.setPrecio(((Number) input.get("precio")).doubleValue());
        if (input.containsKey("stock")) p.setStock(((Number) input.get("stock")).intValue());
        if (input.containsKey("categoria")) p.setCategoria((String) input.get("categoria"));
        return repo.save(p);
    }

    @MutationMapping
    public boolean eliminarProducto(@Argument Long id) {
        var p = repo.findById(id).orElseThrow();
        p.setActivo(false);
        repo.save(p);
        return true;
    }
}
