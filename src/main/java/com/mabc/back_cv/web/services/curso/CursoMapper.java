package com.mabc.back_cv.web.services.curso;

import org.springframework.stereotype.Component;

import com.mabc.back_cv.web.entities.Curso;
import com.mabc.back_cv.web.entities.Certificado;
import com.mabc.back_cv.web.entities.ContenidoCurso;
import com.mabc.back_cv.web.dto.CursoDTO;
import com.mabc.back_cv.web.dto.CertificadoDTO;
import com.mabc.back_cv.web.dto.ContenidoCursoDTO;
import com.mabc.back_cv.web.services.usuarios.UsuarioMapper;

import java.util.stream.Collectors;

@Component
public class CursoMapper {

    public static CursoDTO entityToDTO(Curso curso) {
        if (curso == null) {
            return null;
        }
        CursoDTO cursoDTO = new CursoDTO();
        cursoDTO.setId(curso.getId());
        cursoDTO.setName(curso.getName());
        cursoDTO.setTitle(curso.getTitle());
        cursoDTO.setInstitute(curso.getInstitute());
        cursoDTO.setStartDate(curso.getStartDate());
        cursoDTO.setEndDate(curso.getEndDate());
        cursoDTO.setActivo(curso.getActivo());
        cursoDTO.setUsuario(UsuarioMapper.userToDTO(curso.getUsuario()));

        if (curso.getCertificate() != null) {
            CertificadoDTO certDTO = new CertificadoDTO();
            certDTO.setId(curso.getCertificate().getId());
            certDTO.setName(curso.getCertificate().getName());
            certDTO.setImage(curso.getCertificate().getImage());
            certDTO.setUrl(curso.getCertificate().getUrl());
            certDTO.setMouse_move_title(curso.getCertificate().getMouse_move_title());
            certDTO.setMouse_move_description(curso.getCertificate().getMouse_move_description());
            cursoDTO.setCertificate(certDTO);
        }

        if (curso.getContenidos() != null) {
            cursoDTO.setContenidos(
                curso.getContenidos().stream()
                    .map(c -> {
                        ContenidoCursoDTO dto = new ContenidoCursoDTO();
                        dto.setId(c.getId());
                        dto.setTitle(c.getTitle());
                        dto.setDescription(c.getDescription());
                        dto.setActivo(c.getActivo());
                        return dto;
                    })
                    .collect(Collectors.toList())
            );
        }

        return cursoDTO;
    }

    public static Curso dtoToEntity(CursoDTO cursoDTO) {
        if (cursoDTO == null) {
            return null;
        }
        Curso curso = new Curso();
        if (cursoDTO.getId() != null) {
            curso.setId(cursoDTO.getId());
        }
        curso.setName(cursoDTO.getName());
        curso.setTitle(cursoDTO.getTitle());
        curso.setInstitute(cursoDTO.getInstitute());
        curso.setStartDate(cursoDTO.getStartDate());
        curso.setEndDate(cursoDTO.getEndDate());
        curso.setActivo(cursoDTO.getActivo());
        curso.setUsuario(UsuarioMapper.DTOToUser(cursoDTO.getUsuario()));

        if (cursoDTO.getCertificate() != null) {
            Certificado cert = new Certificado();
            if (cursoDTO.getCertificate().getId() != null) {
                cert.setId(cursoDTO.getCertificate().getId());
            }
            cert.setName(cursoDTO.getCertificate().getName());
            cert.setImage(cursoDTO.getCertificate().getImage());
            cert.setUrl(cursoDTO.getCertificate().getUrl());
            cert.setMouse_move_title(cursoDTO.getCertificate().getMouse_move_title());
            cert.setMouse_move_description(cursoDTO.getCertificate().getMouse_move_description());
            curso.setCertificate(cert);
        }

        return curso;
    }

}
