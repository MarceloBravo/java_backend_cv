package com.mabc.back_cv.userPresentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mabc.back_cv.web.controllers.UserPresentationController;
import com.mabc.back_cv.web.dto.UserPresentationDTO;
import com.mabc.back_cv.web.dto.UsuarioDTO;
import com.mabc.back_cv.web.services.userPresentation.UserPresentationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;
import org.mockito.MockedStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.DisplayName;

import com.mabc.back_cv.web.repositories.UserPresentationRepository;
import com.mabc.back_cv.web.entities.UserPresentation;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.services.userPresentation.UserPresentationUtils;
import com.mabc.back_cv.web.services.userPresentation.UserPresentationServiceImpl;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mabc.back_cv.common.Utils;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserPresentationServiceImpl Test")
public class UserPresentationServiceImplTest{

@Mock
    private UserPresentationRepository userPresentationRepository;

    @Mock
    private UserPresentationUtils userPresentationUtils;

    @Mock
    private Utils utils;

    @InjectMocks
    private UserPresentationServiceImpl userPresentationServiceImpl;

    private ObjectMapper objectMapper;
    private UserPresentation userPresentation;
    private UserPresentationDTO userPresentationDTO;
    private UsuarioDTO usuarioDTO;
    private User user;
    private Page<UserPresentationDTO> userPresentationPage;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNombre("Juan");
        usuarioDTO.setApellido("Perez");
        
        userPresentationDTO = new UserPresentationDTO();
        userPresentationDTO.setId(1L);
        userPresentationDTO.setPosicion(1);
        userPresentationDTO.setParrafo("Este es un párrafo de prueba");
        userPresentationDTO.setUser(null); // Usuario null para evitar problemas de serialización en tests de Page
        
        user = new User();
        user.setId(1L);
        user.setNombre("Juan");
        user.setApellido("Perez");

        userPresentation = new UserPresentation(
            userPresentationDTO.getId(),
            userPresentationDTO.getPosicion(),
            userPresentationDTO.getParrafo(),
            user
        );

        pageable = PageRequest.of(0, 10);
    }


    // -------------------------------------------------------------
    //                       findAll
    // -------------------------------------------------------------
    @Test
    void obtieneTodosLosUserPresentation_conParametrosValidos_retornaPageable() throws Exception {
        userPresentationDTO.setUser(usuarioDTO);
        when(utils.createPageable(0, 10)).thenReturn(pageable);
        Page<UserPresentation> userPresentationPage = new PageImpl<>(List.of(userPresentation), pageable, 1);

        when(userPresentationRepository.findAll("párrafo", 1L, pageable)).thenReturn(userPresentationPage);

        Page<UserPresentationDTO> result = userPresentationServiceImpl.getAll("párrafo", 1L, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().get(0).getPosicion());
        assertEquals("Este es un párrafo de prueba", result.getContent().get(0).getParrafo());
        assertEquals(1L, result.getContent().get(0).getUser().getId());
        assertEquals("Juan", result.getContent().get(0).getUser().getNombre());
        verify(userPresentationRepository, times(1)).findAll("párrafo", 1L, pageable);

    }
    
    @Test
    void obtieneUnaListaVaciaDeUserPresentation_conParametrosValidos_retornaPageableVacia() throws Exception {
        when(utils.createPageable(0, 10)).thenReturn(pageable);
        Page<UserPresentation> userPresentationPage = new PageImpl<>(List.of(), pageable, 0);

        when(userPresentationRepository.findAll("párrafo", 1L, pageable)).thenReturn(userPresentationPage);

        Page<UserPresentationDTO> result = userPresentationServiceImpl.getAll("párrafo", 1L, 0, 10);

        assertNotNull(result, "El resultado no debería ser null");
    
        assertTrue(result.isEmpty(), "La página debería estar vacía");
        assertEquals(0, result.getTotalElements(), "El total de elementos debería ser 0");
        assertEquals(0, result.getContent().size(), "El tamaño de la lista de contenido debería ser 0");
        
        // 5. Verificación de interacciones
        verify(userPresentationRepository, times(1)).findAll("párrafo", 1L, pageable);

    }

    @Test
    void getAll_cuandouserPresentationDTOPageEsNull_retornaPageEmpty() {
        when(utils.createPageable(null, 10)).thenReturn(pageable);
        
        Page<UserPresentation> userPresentationPageMock = mock(Page.class);
        
        when(userPresentationRepository.findAll("párrafo", 1L, pageable)).thenReturn(userPresentationPageMock);
        
        Page<UserPresentationDTO> result = userPresentationServiceImpl.getAll("párrafo", 1L, null, 10);

        assertNotNull(result);
        assertEquals(Page.empty(), result); // Verifica que sea exactamente la misma instancia vacía de Spring
    }


    // -------------------------------------------------------------
    //                       getById
    // -------------------------------------------------------------
    @Test
    void getByIdexistente_cuandoIdEsValido_retornaUserPresentationDTO() {
        when(userPresentationRepository.findById(1L)).thenReturn(Optional.of(userPresentation));
        UserPresentationDTO result = userPresentationServiceImpl.findById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getByIdInexistente_cuandoIdEsValido_retornaNull() {
        when(userPresentationRepository.findById(9999L)).thenReturn(Optional.empty());
        UserPresentationDTO result = userPresentationServiceImpl.findById(9999L);
        assertNull(result);
    }

    @Test
    void getById_cuandoIdEsNulo_retornaNull() {
        UserPresentationDTO result = userPresentationServiceImpl.findById(null);
        assertNull(result);
    }

    // -------------------------------------------------------------
    //                       Save y Update
    // -------------------------------------------------------------
    @Test
    void saveNuevo_cuandoUserPresentationDTOEsValido_retornaUserPresentationDTO(){

        userPresentationDTO.setId(null); //Configura el objeto como un nuevo registro
        
        when(userPresentationRepository.save(any(UserPresentation.class))).thenReturn(userPresentation);
        UserPresentationDTO result = userPresentationServiceImpl.save(userPresentationDTO);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(userPresentationDTO.getParrafo(), result.getParrafo());
    }

    @Test
    void save_cuandoUserPresentationDTOEsIncompleto_retornaError() throws Exception {
        when(userPresentationRepository.save(userPresentation))
                .thenThrow(new RuntimeException("Error al grabar el registro"));

        assertThrows(RuntimeException.class, () -> userPresentationServiceImpl.save(userPresentationDTO));
    }

    @Test
    void save_cuandoUserPresentationDTOEsNull_retornanULL() {
        UserPresentationDTO result = userPresentationServiceImpl.save(null);
        assertNull(result);
    }

    @Test
    void saveActualiza_cuandoUserPresentationDTOEsValido_retornaUserPresentationDTO(){
        userPresentationDTO.setUser(usuarioDTO);
        when(userPresentationRepository.save(userPresentation)).thenReturn(userPresentation);
        UserPresentationDTO result = userPresentationServiceImpl.save(userPresentationDTO);
        assertNotNull(result);
        assertEquals(userPresentationDTO.getId(), result.getId());
    }

    // -------------------------------------------------------------
    //                       Delete
    // -------------------------------------------------------------

    @Test
    void deleteByIdexistente_cuandoIdEsValido() {
        when(userPresentationRepository.findById(1L)).thenReturn(Optional.of(userPresentation));
        userPresentationServiceImpl.delete(1L);

        verify(userPresentationRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteByIdInexistente_cuandoIdNoExiste_retornaError() {
        when(userPresentationRepository.findById(9999L)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userPresentationServiceImpl.delete(9999L));

        assertEquals("Error: El registro no existe.", exception.getMessage());
        verify(userPresentationRepository, times(1)).findById(9999L);
        verify(userPresentationRepository, never()).delete(any(UserPresentation.class));
    }

    @Test
    void deleteById_cuandoIdEsNulo_retornaError() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userPresentationServiceImpl.delete(null));

        assertEquals("Error: El id no puede ser nulo.", exception.getMessage());
    }

}