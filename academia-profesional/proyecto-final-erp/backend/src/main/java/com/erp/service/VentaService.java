package com.erp.service;

import com.erp.entity.DetalleVenta;
import com.erp.entity.Producto;
import com.erp.entity.Venta;
import com.erp.repository.DetalleVentaRepository;
import com.erp.repository.ProductoRepository;
import com.erp.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class VentaService {
    private final VentaRepository ventaRepo;
    private final DetalleVentaRepository detalleRepo;
    private final ProductoRepository productoRepo;

    public VentaService(VentaRepository ventaRepo, DetalleVentaRepository detalleRepo, ProductoRepository productoRepo) {
        this.ventaRepo = ventaRepo;
        this.detalleRepo = detalleRepo;
        this.productoRepo = productoRepo;
    }

    public List<Venta> listar() { return ventaRepo.findAll(); }

    @Transactional
    public Venta registrar(Integer clienteId, Integer usuarioId, List<Map<String, Object>> items) {
        // Crear venta
        var venta = new Venta();
        venta.setNumero("V-" + DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").withZone(ZoneId.systemDefault()).format(Instant.now()));
        venta.setClienteId(clienteId);
        venta.setUsuarioId(usuarioId);
        ventaRepo.save(venta);

        BigDecimal subtotal = BigDecimal.ZERO;

        // Procesar cada item
        for (var item : items) {
            Long productoId = ((Number) item.get("productoId")).longValue();
            int cantidad = ((Number) item.get("cantidad")).intValue();

            Producto producto = productoRepo.findById(productoId)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + productoId));

            producto.removeStock(cantidad); // Valida stock suficiente
            productoRepo.save(producto);

            var detalle = new DetalleVenta();
            detalle.setVentaId(venta.getId());
            detalle.setProductoId(productoId);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(producto.getPrecio().multiply(BigDecimal.valueOf(cantidad)));
            detalleRepo.save(detalle);

            subtotal = subtotal.add(detalle.getSubtotal());
        }

        // Calcular totales
        venta.setSubtotal(subtotal);
        venta.setImpuesto(subtotal.multiply(BigDecimal.valueOf(0.16)));
        venta.setTotal(subtotal.multiply(BigDecimal.valueOf(1.16)));
        return ventaRepo.save(venta);
    }

    public List<Object[]> reporteMensual() { return ventaRepo.reporteMensual(); }
}
