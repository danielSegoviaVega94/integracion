package com.ejercicio.integracion.mapper;

import com.ejercicio.integracion.dto.TelefonoDTO;
import com.ejercicio.integracion.entity.Telefono;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TelefonoMapper {
    TelefonoMapper INSTANCE = Mappers.getMapper(TelefonoMapper.class);

    TelefonoDTO toDto(Telefono telefono);
    Telefono toEntity(TelefonoDTO telefonoDTO);
}
