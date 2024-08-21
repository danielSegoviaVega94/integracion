package com.ejercicio.integracion.mapper;

import com.ejercicio.integracion.dto.UsuarioDTO;
import com.ejercicio.integracion.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    @Mapping(source = "nombre", target = "name")
    @Mapping(source = "email", target = "email")
    UsuarioDTO toDto(Usuario usuario);

    @Mapping(source = "name", target = "nombre")
    @Mapping(source = "email", target = "email")
    Usuario toEntity(UsuarioDTO usuarioDTO);
}