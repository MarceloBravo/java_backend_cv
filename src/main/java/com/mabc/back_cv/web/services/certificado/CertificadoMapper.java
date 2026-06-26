package com.mabc.back_cv.web.services.certificado;

import org.springframework.stereotype.Component;

import com.mabc.back_cv.web.entities.Certificado;
import com.mabc.back_cv.web.dto.CertificadoDTO;
import com.mabc.back_cv.web.services.usuarios.UsuarioMapper;

/**
 * Mapper para la conversión entre entidades Certificado y DTOs CertificadoDTO.
 */
@Component
public class CertificadoMapper {

    /**
     * Convierte una entidad Certificado a un CertificadoDTO.
     *
     * @param certificado Entidad a convertir.
     * @return CertificadoDTO convertido o null si la entidad es null.
     */
    public static CertificadoDTO entityToDTO(Certificado certificado) {
        if (certificado == null) {
            return null;
        }
        CertificadoDTO dto = new CertificadoDTO();
        dto.setId(certificado.getId());
        dto.setName(certificado.getName());
        dto.setImage(certificado.getImage());
        dto.setUrl(certificado.getUrl());
        dto.setMouse_move_title(certificado.getMouse_move_title());
        dto.setMouse_move_description(certificado.getMouse_move_description());
        dto.setUser(UsuarioMapper.userToDTO(certificado.getUser()));
        return dto;
    }

    /**
     * Convierte un CertificadoDTO a una entidad Certificado.
     *
     * @param dto DTO a convertir.
     * @return Entidad Certificado convertida o null si el DTO es null.
     */
    public static Certificado dtoToEntity(CertificadoDTO dto) {
        if (dto == null) {
            return null;
        }
        Certificado certificado = new Certificado();
        if (dto.getId() != null) {
            certificado.setId(dto.getId());
        }
        certificado.setName(dto.getName());
        certificado.setImage(dto.getImage());
        certificado.setUrl(dto.getUrl());
        certificado.setMouse_move_title(dto.getMouse_move_title());
        certificado.setMouse_move_description(dto.getMouse_move_description());
        certificado.setUser(UsuarioMapper.DTOToUser(dto.getUser()));
        return certificado;
    }

}
