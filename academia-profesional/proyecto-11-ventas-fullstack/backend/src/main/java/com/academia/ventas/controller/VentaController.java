package com.academia.ventas.controller;

import com.academia.ventas.entity.*;
import com.academia.ventas.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaRepository ventaRepo;
    private final ProductoRepository productoRepo;
    private final ClienteRepository clienteRepo;
    private final UsuarioRepository usuarioRepo;

    public VentaController(VentaRepository ventaRepo, ProductoRepository productoRepo,
                           ClienteRepository clienteRepo, UsuarioRepository usuarioRepo) {
        this.ventaRepo = ventaRepo;
        this.productoRepo = productoRepo;
        this.clienteRepo = clienteRepo;
        this.usuarioRepo = usuarioRepo;
    }

    record ItemRequest(Long productoId, int cantidad) {}
    record VentaRequest(Long clienteId, List<ItemRequest> items) {}

    @GetMapping
    public List<Venta> listar() { return ventaRepo.findTop10ByOrderByFechaDesc(); }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody VentaRequest request, Authentication auth) {
        Usuario usuario = usuarioRepo.findByUsername(auth.getName()).orElseThrow();
        Cliente cliente = clienteRepo.findById(request.clienteId()).orElseThrow();

        Venta venta = new Venta();
        venta.setNumero("V-" + System.currentTimeMillis());
        venta.setCliente(cliente);
        venta.setUsuario(usuario);

        BigDecimal subtotal = BigDecimal.ZERO;

        for (ItemRequest item : request.items()) {
            Producto producto = productoRepo.findById(item.productoId()).orElseThrow();
            if (producto.getStock() < item.cantidad()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Stock insuficiente para " + producto.getNombre(),
                    "disponible", producto.getStock()
                ));
            }

            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(producto);
            detalle.setCantidad(item.cantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(producto.getPrecio().multiply(BigDecimal.valueOf(item.cantidad())));
            venta.addDetalle(detalle);

            subtotal = subtotal.add(detalle.getSubtotal());
            producto.setStock(producto.getStock() - item.cantidad());
            productoRepo.save(producto);
        }

        BigDecimal impuesto = subtotal.multiply(new BigDecimal("0.16"));
        venta.setSubtotal(subtotal);
        venta.setImpuesto(impuesto);
        venta.setTotal(subtotal.add(impuesto));

        return ResponseEntity.ok(ventaRepo.save(venta));
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        return Map.of(
            "ventasMes", ventaRepo.ventasDelMes(),
            "pedidosMes", ventaRepo.pedidosDelMes(),
            "productosStockBajo", productoRepo.findByStockLessThan(5).size(),
            "totalProductos", productoRepo.findByActivoTrue().size()
        );
    }
}
