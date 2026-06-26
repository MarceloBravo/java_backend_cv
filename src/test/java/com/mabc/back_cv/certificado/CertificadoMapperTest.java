package com.mabc.back_cv.certificado;

import com.mabc.back_cv.web.dto.CertificadoDTO;
import com.mabc.back_cv.web.dto.UsuarioDTO;
import com.mabc.back_cv.web.entities.Certificado;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.services.certificado.CertificadoMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CertificadoMapperTest {

    private Certificado certificado;
    private CertificadoDTO certificadoDTO;
    private User user;
    private UsuarioDTO usuarioDTO;
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

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNombre("Juan");
        usuarioDTO.setApellido("Perez");
        usuarioDTO.setEmail("juan@example.com");
        usuarioDTO.setPassword("pass123");

        certificado = new Certificado();
        certificado.setId(1L);
        certificado.setName("Certificado Java");
        certificado.setImage("certificado.png");
        certificado.setUrl("https://certificados.com/java");
        certificado.setMouse_move_title("Certificado");
        certificado.setMouse_move_description("Certificado de Java Avanzado");
        certificado.setUser(user);

        certificadoDTO = new CertificadoDTO();
        certificadoDTO.setId(1L);
        certificadoDTO.setName("Certificado Java");
        certificadoDTO.setImage("certificado.png");
        certificadoDTO.setUrl("https://certificados.com/java");
        certificadoDTO.setMouse_move_title("Certificado");
        certificadoDTO.setMouse_move_description("Certificado de Java Avanzado");
        certificadoDTO.setUser(usuarioDTO);
    }

    @Test
    void entityToDTO_conCertificadoValido_retornaDTOCorrecto() {
        CertificadoDTO resultado = CertificadoMapper.entityToDTO(certificado);

        assertNotNull(resultado);
        assertEquals(certificado.getId(), resultado.getId());
        assertEquals(certificado.getName(), resultado.getName());
        assertEquals(certificado.getImage(), resultado.getImage());
        assertEquals(certificado.getUrl(), resultado.getUrl());
        assertEquals(certificado.getMouse_move_title(), resultado.getMouse_move_title());
        assertEquals(certificado.getMouse_move_description(), resultado.getMouse_move_description());
        assertNotNull(resultado.getUser());
        assertEquals(certificado.getUser().getId(), resultado.getUser().getId());
    }

    @Test
    void entityToDTO_conCertificadoNull_retornaNull() {
        CertificadoDTO resultado = CertificadoMapper.entityToDTO(null);
        assertNull(resultado);
    }

    @Test
    void entityToDTO_conCertificadoConCamposNulos_retornaDTOConCamposNulos() {
        certificado.setName(null);
        certificado.setImage(null);
        certificado.setUrl(null);
        certificado.setMouse_move_title(null);
        certificado.setMouse_move_description(null);

        CertificadoDTO resultado = CertificadoMapper.entityToDTO(certificado);

        assertNotNull(resultado);
        assertNull(resultado.getName());
        assertNull(resultado.getImage());
        assertNull(resultado.getUrl());
        assertNull(resultado.getMouse_move_title());
        assertNull(resultado.getMouse_move_description());
    }

    @Test
    void dtoToEntity_conDTOValido_retornaEntityCorrecto() {
        Certificado resultado = CertificadoMapper.dtoToEntity(certificadoDTO);

        assertNotNull(resultado);
        assertEquals(certificadoDTO.getId(), resultado.getId());
        assertEquals(certificadoDTO.getName(), resultado.getName());
        assertEquals(certificadoDTO.getImage(), resultado.getImage());
        assertEquals(certificadoDTO.getUrl(), resultado.getUrl());
        assertEquals(certificadoDTO.getMouse_move_title(), resultado.getMouse_move_title());
        assertEquals(certificadoDTO.getMouse_move_description(), resultado.getMouse_move_description());
        assertNotNull(resultado.getUser());
        assertEquals(certificadoDTO.getUser().getId(), resultado.getUser().getId());
    }

    @Test
    void dtoToEntity_conDTONull_retornaNull() {
        Certificado resultado = CertificadoMapper.dtoToEntity(null);
        assertNull(resultado);
    }

    @Test
    void dtoToEntity_conDTOConIdNull_retornaEntitySinId() {
        certificadoDTO.setId(null);

        Certificado resultado = CertificadoMapper.dtoToEntity(certificadoDTO);

        assertNotNull(resultado);
        assertNull(resultado.getId());
    }

    @Test
    void dtoToEntity_conDTOConCamposNulos_retornaEntityConCamposNulos() {
        certificadoDTO.setName(null);
        certificadoDTO.setImage(null);
        certificadoDTO.setUrl(null);
        certificadoDTO.setMouse_move_title(null);
        certificadoDTO.setMouse_move_description(null);

        Certificado resultado = CertificadoMapper.dtoToEntity(certificadoDTO);

        assertNotNull(resultado);
        assertNull(resultado.getName());
        assertNull(resultado.getImage());
        assertNull(resultado.getUrl());
        assertNull(resultado.getMouse_move_title());
        assertNull(resultado.getMouse_move_description());
    }

    @Test
    void dtoToEntity_conDTOConIdNoNull_retornaEntityConId() {
        certificadoDTO.setId(5L);

        Certificado resultado = CertificadoMapper.dtoToEntity(certificadoDTO);

        assertNotNull(resultado);
        assertEquals(5L, resultado.getId());
    }

}
