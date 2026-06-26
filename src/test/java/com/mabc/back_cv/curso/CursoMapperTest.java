package com.mabc.back_cv.curso;

import com.mabc.back_cv.web.dto.CursoDTO;
import com.mabc.back_cv.web.dto.CertificadoDTO;
import com.mabc.back_cv.web.dto.ContenidoCursoDTO;
import com.mabc.back_cv.web.dto.UsuarioDTO;
import com.mabc.back_cv.web.entities.Curso;
import com.mabc.back_cv.web.entities.Certificado;
import com.mabc.back_cv.web.entities.ContenidoCurso;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.services.curso.CursoMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CursoMapperTest {

    private Curso curso;
    private CursoDTO cursoDTO;
    private User user;
    private UsuarioDTO usuarioDTO;
    private Rol rol;
    private Certificado certificado;
    private CertificadoDTO certificadoDTO;
    private ContenidoCurso contenidoCurso;
    private ContenidoCursoDTO contenidoCursoDTO;

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

        certificadoDTO = new CertificadoDTO();
        certificadoDTO.setId(1L);
        certificadoDTO.setName("Certificado Java");
        certificadoDTO.setImage("certificado.png");
        certificadoDTO.setUrl("https://certificados.com/java");
        certificadoDTO.setMouse_move_title("Certificado");
        certificadoDTO.setMouse_move_description("Certificado de Java Avanzado");

        contenidoCurso = new ContenidoCurso();
        contenidoCurso.setId(1L);
        contenidoCurso.setTitle("Introducción a Java");
        contenidoCurso.setDescription("Conceptos básicos");
        contenidoCurso.setActivo(true);

        contenidoCursoDTO = new ContenidoCursoDTO();
        contenidoCursoDTO.setId(1L);
        contenidoCursoDTO.setTitle("Introducción a Java");
        contenidoCursoDTO.setDescription("Conceptos básicos");
        contenidoCursoDTO.setActivo(true);

        curso = new Curso();
        curso.setId(1L);
        curso.setName("Curso de Java");
        curso.setTitle("Java Avanzado");
        curso.setInstitute("Oracle University");
        curso.setStartDate(new Date());
        curso.setEndDate(new Date());
        curso.setActivo(true);
        curso.setUsuario(user);
        curso.setCertificate(certificado);
        curso.setContenidos(Arrays.asList(contenidoCurso));

        cursoDTO = new CursoDTO();
        cursoDTO.setId(1L);
        cursoDTO.setName("Curso de Java");
        cursoDTO.setTitle("Java Avanzado");
        cursoDTO.setInstitute("Oracle University");
        cursoDTO.setStartDate(new Date());
        cursoDTO.setEndDate(new Date());
        cursoDTO.setActivo(true);
        cursoDTO.setUsuario(usuarioDTO);
        cursoDTO.setCertificate(certificadoDTO);
        cursoDTO.setContenidos(Arrays.asList(contenidoCursoDTO));
    }

    @Test
    void entityToDTO_conCursoValido_retornaDTOCorrecto() {
        CursoDTO resultado = CursoMapper.entityToDTO(curso);

        assertNotNull(resultado);
        assertEquals(curso.getId(), resultado.getId());
        assertEquals(curso.getName(), resultado.getName());
        assertEquals(curso.getTitle(), resultado.getTitle());
        assertEquals(curso.getInstitute(), resultado.getInstitute());
        assertEquals(curso.getStartDate(), resultado.getStartDate());
        assertEquals(curso.getEndDate(), resultado.getEndDate());
        assertEquals(curso.getActivo(), resultado.getActivo());
        assertNotNull(resultado.getUsuario());
        assertEquals(curso.getUsuario().getId(), resultado.getUsuario().getId());

        assertNotNull(resultado.getCertificate());
        assertEquals(curso.getCertificate().getName(), resultado.getCertificate().getName());

        assertNotNull(resultado.getContenidos());
        assertEquals(1, resultado.getContenidos().size());
        assertEquals(curso.getContenidos().get(0).getTitle(), resultado.getContenidos().get(0).getTitle());
    }

    @Test
    void entityToDTO_conCursoNull_retornaNull() {
        CursoDTO resultado = CursoMapper.entityToDTO(null);
        assertNull(resultado);
    }

    @Test
    void entityToDTO_conCursoSinCertificado_retornaDTOSinCertificado() {
        curso.setCertificate(null);

        CursoDTO resultado = CursoMapper.entityToDTO(curso);

        assertNotNull(resultado);
        assertNull(resultado.getCertificate());
    }

    @Test
    void entityToDTO_conCursoSinContenidos_retornaDTOSinContenidos() {
        curso.setContenidos(null);

        CursoDTO resultado = CursoMapper.entityToDTO(curso);

        assertNotNull(resultado);
        assertNull(resultado.getContenidos());
    }

    @Test
    void entityToDTO_conCursoConCamposNulos_retornaDTOConCamposNulos() {
        curso.setName(null);
        curso.setTitle(null);
        curso.setInstitute(null);
        curso.setStartDate(null);
        curso.setEndDate(null);

        CursoDTO resultado = CursoMapper.entityToDTO(curso);

        assertNotNull(resultado);
        assertNull(resultado.getName());
        assertNull(resultado.getTitle());
        assertNull(resultado.getInstitute());
        assertNull(resultado.getStartDate());
        assertNull(resultado.getEndDate());
    }

    @Test
    void dtoToEntity_conDTOValido_retornaEntityCorrecto() {
        Curso resultado = CursoMapper.dtoToEntity(cursoDTO);

        assertNotNull(resultado);
        assertEquals(cursoDTO.getId(), resultado.getId());
        assertEquals(cursoDTO.getName(), resultado.getName());
        assertEquals(cursoDTO.getTitle(), resultado.getTitle());
        assertEquals(cursoDTO.getInstitute(), resultado.getInstitute());
        assertEquals(cursoDTO.getStartDate(), resultado.getStartDate());
        assertEquals(cursoDTO.getEndDate(), resultado.getEndDate());
        assertEquals(cursoDTO.getActivo(), resultado.getActivo());
        assertNotNull(resultado.getUsuario());
        assertEquals(cursoDTO.getUsuario().getId(), resultado.getUsuario().getId());

        assertNotNull(resultado.getCertificate());
        assertEquals(cursoDTO.getCertificate().getName(), resultado.getCertificate().getName());
    }

    @Test
    void dtoToEntity_conDTONull_retornaNull() {
        Curso resultado = CursoMapper.dtoToEntity(null);
        assertNull(resultado);
    }

    @Test
    void dtoToEntity_conDTOConIdNull_retornaEntitySinId() {
        cursoDTO.setId(null);

        Curso resultado = CursoMapper.dtoToEntity(cursoDTO);

        assertNotNull(resultado);
        assertNull(resultado.getId());
    }

    @Test
    void dtoToEntity_conDTOSinCertificado_retornaEntitySinCertificado() {
        cursoDTO.setCertificate(null);

        Curso resultado = CursoMapper.dtoToEntity(cursoDTO);

        assertNotNull(resultado);
        assertNull(resultado.getCertificate());
    }

    @Test
    void dtoToEntity_conDTOConCamposNulos_retornaEntityConCamposNulos() {
        cursoDTO.setName(null);
        cursoDTO.setTitle(null);
        cursoDTO.setInstitute(null);
        cursoDTO.setStartDate(null);
        cursoDTO.setEndDate(null);

        Curso resultado = CursoMapper.dtoToEntity(cursoDTO);

        assertNotNull(resultado);
        assertNull(resultado.getName());
        assertNull(resultado.getTitle());
        assertNull(resultado.getInstitute());
        assertNull(resultado.getStartDate());
        assertNull(resultado.getEndDate());
    }

    @Test
    void dtoToEntity_conDTOConIdNoNull_retornaEntityConId() {
        cursoDTO.setId(5L);

        Curso resultado = CursoMapper.dtoToEntity(cursoDTO);

        assertNotNull(resultado);
        assertEquals(5L, resultado.getId());
    }

    @Test
    void dtoToEntity_conDTOCertificadoSinId_retornaEntityConCertificadoSinId() {
        certificadoDTO.setId(null);

        Curso resultado = CursoMapper.dtoToEntity(cursoDTO);

        assertNotNull(resultado);
        assertNotNull(resultado.getCertificate());
        assertNull(resultado.getCertificate().getId());
    }
}
