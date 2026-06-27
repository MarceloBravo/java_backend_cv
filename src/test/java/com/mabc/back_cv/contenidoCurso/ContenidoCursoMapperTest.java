package com.mabc.back_cv.contenidoCurso;

import com.mabc.back_cv.web.dto.ContenidoCursoDTO;
import com.mabc.back_cv.web.entities.ContenidoCurso;
import com.mabc.back_cv.web.services.contenidoCurso.ContenidoCursoMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContenidoCursoMapperTest {

    private ContenidoCurso contenidoCurso;
    private ContenidoCursoDTO contenidoCursoDTO;

    @BeforeEach
    void setUp() {
        contenidoCurso = new ContenidoCurso();
        contenidoCurso.setId(1L);
        contenidoCurso.setTitle("Introducción a Java");
        contenidoCurso.setDescription("Conceptos básicos de Java");
        contenidoCurso.setActivo(true);

        contenidoCursoDTO = new ContenidoCursoDTO();
        contenidoCursoDTO.setId(1L);
        contenidoCursoDTO.setTitle("Introducción a Java");
        contenidoCursoDTO.setDescription("Conceptos básicos de Java");
        contenidoCursoDTO.setActivo(true);
    }

    @Test
    void entityToDTO_conEntidadValida_retornaDTOCorrecto() {
        ContenidoCursoDTO resultado = ContenidoCursoMapper.entityToDTO(contenidoCurso);

        assertNotNull(resultado);
        assertEquals(contenidoCurso.getId(), resultado.getId());
        assertEquals(contenidoCurso.getTitle(), resultado.getTitle());
        assertEquals(contenidoCurso.getDescription(), resultado.getDescription());
        assertEquals(contenidoCurso.getActivo(), resultado.getActivo());
    }

    @Test
    void entityToDTO_conEntidadNull_retornaNull() {
        ContenidoCursoDTO resultado = ContenidoCursoMapper.entityToDTO(null);
        assertNull(resultado);
    }

    @Test
    void entityToDTO_conCamposNulos_retornaDTOConCamposNulos() {
        contenidoCurso.setTitle(null);
        contenidoCurso.setDescription(null);
        contenidoCurso.setActivo(null);

        ContenidoCursoDTO resultado = ContenidoCursoMapper.entityToDTO(contenidoCurso);

        assertNotNull(resultado);
        assertNull(resultado.getTitle());
        assertNull(resultado.getDescription());
        assertNull(resultado.getActivo());
    }

    @Test
    void dtoToEntity_conDTOValido_retornaEntityCorrecto() {
        ContenidoCurso resultado = ContenidoCursoMapper.dtoToEntity(contenidoCursoDTO);

        assertNotNull(resultado);
        assertEquals(contenidoCursoDTO.getId(), resultado.getId());
        assertEquals(contenidoCursoDTO.getTitle(), resultado.getTitle());
        assertEquals(contenidoCursoDTO.getDescription(), resultado.getDescription());
        assertEquals(contenidoCursoDTO.getActivo(), resultado.getActivo());
    }

    @Test
    void dtoToEntity_conDTONull_retornaNull() {
        ContenidoCurso resultado = ContenidoCursoMapper.dtoToEntity(null);
        assertNull(resultado);
    }

    @Test
    void dtoToEntity_conDTOConIdNull_retornaEntitySinId() {
        contenidoCursoDTO.setId(null);

        ContenidoCurso resultado = ContenidoCursoMapper.dtoToEntity(contenidoCursoDTO);

        assertNotNull(resultado);
        assertNull(resultado.getId());
    }

    @Test
    void dtoToEntity_conDTOConIdNoNull_retornaEntityConId() {
        contenidoCursoDTO.setId(5L);

        ContenidoCurso resultado = ContenidoCursoMapper.dtoToEntity(contenidoCursoDTO);

        assertNotNull(resultado);
        assertEquals(5L, resultado.getId());
    }

    @Test
    void dtoToEntity_conDTOConCamposNulos_retornaEntityConCamposNulos() {
        contenidoCursoDTO.setTitle(null);
        contenidoCursoDTO.setDescription(null);
        contenidoCursoDTO.setActivo(null);

        ContenidoCurso resultado = ContenidoCursoMapper.dtoToEntity(contenidoCursoDTO);

        assertNotNull(resultado);
        assertNull(resultado.getTitle());
        assertNull(resultado.getDescription());
        assertNull(resultado.getActivo());
    }
}
