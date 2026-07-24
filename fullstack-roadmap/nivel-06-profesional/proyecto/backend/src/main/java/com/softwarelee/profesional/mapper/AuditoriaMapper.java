package com.softwarelee.profesional.mapper;

import com.softwarelee.profesional.model.RegistroAuditoria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuditoriaMapper {
    void insert(RegistroAuditoria registro);
    List<RegistroAuditoria> findByUsuarioId(@Param("usuarioId") Long usuarioId);
    List<RegistroAuditoria> findRecientes(@Param("limite") int limite);
}
