package com.mabc.back_cv.educacion;

import com.mabc.back_cv.web.dto.EducacionDTO;
import com.mabc.back_cv.web.dto.UsuarioDTO;
import com.mabc.back_cv.web.entities.Educacion;
import com.mabc.back_cv.web.services.educacion.EducacionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.entities.Rol;

class EducacionMapperTest {

    private Educacion educacion;
    private EducacionDTO educacionDTO;
    private UsuarioDTO usuario;
    private User user;
    private Rol rol;


    @BeforeEach
    void setUp() {
        rol = new Rol();
        rol.setId(1L);
        rol.setNombre("USER");


        user = new User();
        user.setId(1L);
        user.setNombre("Juan");
        user.setApellido("Perez");
        user.setEmail("juan@example.com");
        user.setPassword("pass123");
        user.setFono("123456789");
        user.setDireccion("Calle 123");
        user.setCiudad("Santiago");
        user.setIdioma("es");
        user.setRol(rol);



        usuario = new UsuarioDTO();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setApellido("Perez");
        usuario.setEmail("juan@example.com");
        usuario.setPassword("pass123");

        educacion = new Educacion();
        educacion.setId(1L);
        educacion.setInstitution("Universidad de Chile");
        educacion.setTitle("Ingeniería Civil");
        educacion.setShortTitle("Ing. Civil");
        educacion.setName("Juan Pérez");
        educacion.setDescription("Carrera de ingeniería civil");
        educacion.setYearFrom(2018);
        educacion.setYearTo(2023);
        educacion.setDuration(10);
        educacion.setImage("logo.png");
        educacion.setUrl("https://uchile.cl");
        educacion.setStyles("style1");
        educacion.setUsuario(user);

        educacionDTO = new EducacionDTO();
        educacionDTO.setId(1L);
        educacionDTO.setInstitution("Universidad de Chile");
        educacionDTO.setTitle("Ingeniería Civil");
        educacionDTO.setShortTitle("Ing. Civil");
        educacionDTO.setName("Juan Pérez");
        educacionDTO.setDescription("Carrera de ingeniería civil");
        educacionDTO.setYearFrom(2018);
        educacionDTO.setYearTo(2023);
        educacionDTO.setDuration(10);
        educacionDTO.setImage("logo.png");
        educacionDTO.setUrl("https://uchile.cl");
        educacionDTO.setStyles("style1");
        educacionDTO.setUsuario(usuario);
    }

    // Tests para entityToDTO

    @Test
    void entityToDTO_conEducacionValida_retornaDTOCorrecto() {
        EducacionDTO resultado = EducacionMapper.entityToDTO(educacion);
        
        assertNotNull(resultado);
        assertEquals(educacion.getId(), resultado.getId());
        assertEquals(educacion.getInstitution(), resultado.getInstitution());
        assertEquals(educacion.getTitle(), resultado.getTitle());
        assertEquals(educacion.getShortTitle(), resultado.getShortTitle());
        assertEquals(educacion.getName(), resultado.getName());
        assertEquals(educacion.getDescription(), resultado.getDescription());
        assertEquals(educacion.getYearFrom(), resultado.getYearFrom());
        assertEquals(educacion.getYearTo(), resultado.getYearTo());
        assertEquals(educacion.getDuration(), resultado.getDuration());
        assertEquals(educacion.getImage(), resultado.getImage());
        assertEquals(educacion.getUrl(), resultado.getUrl());
        assertEquals(educacion.getStyles(), resultado.getStyles());
        assertNotNull(resultado.getUsuario());
        assertEquals(educacion.getUsuario().getId(), resultado.getUsuario().getId());
    }

    @Test
    void entityToDTO_conEducacionNull_retornaNull() {
        EducacionDTO resultado = EducacionMapper.entityToDTO(null);
        
        assertNull(resultado);
    }

    @Test
    void entityToDTO_conEducacionConCamposNulos_retornaDTOConCamposNulos() {
        educacion.setInstitution(null);
        educacion.setTitle(null);
        educacion.setDescription(null);
        educacion.setImage(null);
        educacion.setUrl(null);
        educacion.setStyles(null);
        
        EducacionDTO resultado = EducacionMapper.entityToDTO(educacion);
        
        assertNotNull(resultado);
        assertNull(resultado.getInstitution());
        assertNull(resultado.getTitle());
        assertNull(resultado.getDescription());
        assertNull(resultado.getImage());
        assertNull(resultado.getUrl());
        assertNull(resultado.getStyles());
    }

    // Tests para dtoToEntity

    @Test
    void dtoToEntity_conDTOValido_retornaEntityCorrecto() {
        Educacion resultado = EducacionMapper.dtoToEntity(educacionDTO);
        
        assertNotNull(resultado);
        assertEquals(educacionDTO.getId(), resultado.getId());
        assertEquals(educacionDTO.getInstitution(), resultado.getInstitution());
        assertEquals(educacionDTO.getTitle(), resultado.getTitle());
        assertEquals(educacionDTO.getShortTitle(), resultado.getShortTitle());
        assertEquals(educacionDTO.getName(), resultado.getName());
        assertEquals(educacionDTO.getDescription(), resultado.getDescription());
        assertEquals(educacionDTO.getYearFrom(), resultado.getYearFrom());
        assertEquals(educacionDTO.getYearTo(), resultado.getYearTo());
        assertEquals(educacionDTO.getDuration(), resultado.getDuration());
        assertEquals(educacionDTO.getImage(), resultado.getImage());
        assertEquals(educacionDTO.getUrl(), resultado.getUrl());
        assertEquals(educacionDTO.getStyles(), resultado.getStyles());
        assertNotNull(resultado.getUsuario());
        assertEquals(educacionDTO.getUsuario().getId(), resultado.getUsuario().getId());
    }

    @Test
    void dtoToEntity_conDTONull_retornaNull() {
        Educacion resultado = EducacionMapper.dtoToEntity(null);
        
        assertNull(resultado);
    }

    @Test
    void dtoToEntity_conDTOConIdNull_retornaEntitySinId() {
        educacionDTO.setId(null);
        
        Educacion resultado = EducacionMapper.dtoToEntity(educacionDTO);
        
        assertNotNull(resultado);
        assertNull(resultado.getId());
    }

    @Test
    void dtoToEntity_conDTOConCamposNulos_retornaEntityConCamposNulos() {
        educacionDTO.setInstitution(null);
        educacionDTO.setTitle(null);
        educacionDTO.setDescription(null);
        educacionDTO.setImage(null);
        educacionDTO.setUrl(null);
        educacionDTO.setStyles(null);
        
        Educacion resultado = EducacionMapper.dtoToEntity(educacionDTO);
        
        assertNotNull(resultado);
        assertNull(resultado.getInstitution());
        assertNull(resultado.getTitle());
        assertNull(resultado.getDescription());
        assertNull(resultado.getImage());
        assertNull(resultado.getUrl());
        assertNull(resultado.getStyles());
    }

    @Test
    void dtoToEntity_conDTOConIdNoNull_retornaEntityConId() {
        educacionDTO.setId(5L);
        
        Educacion resultado = EducacionMapper.dtoToEntity(educacionDTO);
        
        assertNotNull(resultado);
        assertEquals(5L, resultado.getId());
    }
}
