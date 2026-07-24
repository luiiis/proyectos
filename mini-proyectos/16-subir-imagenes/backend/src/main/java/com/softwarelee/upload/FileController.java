package com.softwarelee.upload;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/api/archivos")
public class FileController {

    @PostMapping("/subir")
    public ResponseEntity<Map<String, Object>> subir(@RequestParam("file") MultipartFile file) throws Exception {
        String tipo = file.getContentType();
        if (!List.of("image/jpeg", "image/png", "image/webp").contains(tipo)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Solo JPG, PNG o WebP"));
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            return ResponseEntity.badRequest().body(Map.of("error", "Máximo 5MB"));
        }

        String extension = tipo.split("/")[1];
        String nombre = UUID.randomUUID() + "." + extension;
        Path destino = Paths.get("uploads", nombre);
        Files.createDirectories(destino.getParent());
        file.transferTo(destino.toFile());

        return ResponseEntity.ok(Map.of(
            "success", true, "url", "/uploads/" + nombre,
            "nombre", nombre, "tamano", file.getSize()
        ));
    }

    @GetMapping("/listar")
    public ResponseEntity<Map<String, Object>> listar() throws Exception {
        Path dir = Paths.get("uploads");
        if (!Files.exists(dir)) return ResponseEntity.ok(Map.of("archivos", List.of()));
        List<String> archivos = Files.list(dir).map(p -> p.getFileName().toString()).toList();
        return ResponseEntity.ok(Map.of("archivos", archivos, "total", archivos.size()));
    }
}
