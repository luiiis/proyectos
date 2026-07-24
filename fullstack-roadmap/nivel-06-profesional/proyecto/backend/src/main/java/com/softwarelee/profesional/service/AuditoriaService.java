package com.softwarelee.profesional.service;

import com.softwarelee.profesional.mapper.AuditoriaMapper;
import com.softwarelee.profesional.model.RegistroAuditoria;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de auditoría: registra quién hizo qué y cuándo.
 */
@Service
public class AuditoriaService {

    private final AuditoriaMapper auditoriaMapper;

    public AuditoriaService(AuditoriaMapper auditoriaMapper) {
        this.auditoriaMapper = auditoriaMapper;
    }

    public void registrar(Long usuarioId, String accion, String detalle, String ip) {
        RegistroAuditoria registro = new RegistroAuditoria();
        registro.setUsuarioId(usuarioId);
        registro.setAccion(accion);
        registro.setDetalle(detalle);
        registro.setIp(ip);
        auditoriaMapper.insert(registro);
    }

    public List<RegistroAuditoria> listarPorUsuario(Long usuarioId) {
        return auditoriaMapper.findByUsuarioId(usuarioId);
    }

    public List<RegistroAuditoria> listarRecientes(int limite) {
        return auditoriaMapper.findRecientes(limite);
    }
}
