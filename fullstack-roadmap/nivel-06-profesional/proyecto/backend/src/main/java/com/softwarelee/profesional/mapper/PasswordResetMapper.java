package com.softwarelee.profesional.mapper;

import com.softwarelee.profesional.model.PasswordResetToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PasswordResetMapper {
    void insert(PasswordResetToken token);
    PasswordResetToken findByToken(@Param("token") String token);
    void marcarComoUsado(@Param("id") Long id);
    void invalidarTokensPorUsuario(@Param("usuarioId") Long usuarioId);
    void eliminarExpirados();
}
