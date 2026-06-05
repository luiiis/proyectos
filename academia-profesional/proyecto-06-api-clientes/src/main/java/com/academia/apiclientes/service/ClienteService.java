package com.academia.apiclientes.service;

import com.academia.apiclientes.dto.ClienteRequest;
import com.academia.apiclientes.dto.ClienteResponse;
import com.academia.apiclientes.model.Cliente;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ClienteService {

    // Almacenamiento en memoria (en proyecto 07 será PostgreSQL)
    private final Map<Long, Cliente> clientes = new ConcurrentHashMap<>();

    @PostConstruct
    void init() {
        // Datos iniciales
        crear(new ClienteRequest("Carlos", "García", "carlos@mail.com", "55-1234-5678", "CDMX"));
        crear(new ClienteRequest("María", "López", "maria@mail.com", "81-2345-6789", "Monterrey"));
        crear(new ClienteRequest("Juan", "Martínez", "juan@mail.com", "33-3456-7890", "Guadalajara"));
    }

    public List<ClienteResponse> listar() {
        return clientes.values().stream()
                .filter(Cliente::isActivo)
                .map(this::toResponse)
                .toList();
    }

    public ClienteResponse buscarPorId(Long id) {
        return Optional.ofNullable(clientes.get(id))
                .filter(Cliente::isActivo)
                .map(this::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Cliente no encontrado: " + id));
    }

    public List<ClienteResponse> buscarPorCiudad(String ciudad) {
        return clientes.values().stream()
                .filter(c -> c.isActivo() && c.getCiudad().equalsIgnoreCase(ciudad))
                .map(this::toResponse)
                .toList();
    }

    public ClienteResponse crear(ClienteRequest request) {
        var cliente = new Cliente(request.nombre(), request.apellido(),
                request.email(), request.telefono(), request.ciudad());
        clientes.put(cliente.getId(), cliente);
        return toResponse(cliente);
    }

    public ClienteResponse actualizar(Long id, ClienteRequest request) {
        var cliente = Optional.ofNullable(clientes.get(id))
                .orElseThrow(() -> new NoSuchElementException("Cliente no encontrado: " + id));
        if (request.nombre() != null) cliente.setNombre(request.nombre());
        if (request.apellido() != null) cliente.setApellido(request.apellido());
        if (request.email() != null) cliente.setEmail(request.email());
        if (request.telefono() != null) cliente.setTelefono(request.telefono());
        if (request.ciudad() != null) cliente.setCiudad(request.ciudad());
        return toResponse(cliente);
    }

    public void eliminar(Long id) {
        var cliente = Optional.ofNullable(clientes.get(id))
                .orElseThrow(() -> new NoSuchElementException("Cliente no encontrado: " + id));
        cliente.setActivo(false);
    }

    private ClienteResponse toResponse(Cliente c) {
        return new ClienteResponse(c.getId(), c.getNombre(), c.getApellido(),
                c.getEmail(), c.getTelefono(), c.getCiudad(), c.getCreatedAt());
    }
}
