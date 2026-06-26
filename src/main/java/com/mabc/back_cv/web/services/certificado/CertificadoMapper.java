package com.mabc.back_cv.web.services.certificado;

import org.springframework.stereotype.Component;

import com.mabc.back_cv.web.entities.Certificado;
import com.mabc.back_cv.web.dto.CertificadoDTO;
import com.mabc.back_cv.web.services.usuarios.UsuarioMapper;

@Component
public class CertificadoMapper {

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
