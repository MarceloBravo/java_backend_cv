package com.mabc.back_cv.userPresentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mabc.back_cv.web.dto.UserPresentationDTO;
import com.mabc.back_cv.web.dto.UsuarioDTO;
import com.mabc.back_cv.web.entities.UserPresentation;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.services.userPresentation.UserPresentationUtils;
import com.mabc.back_cv.web.repositories.UserPresentationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserPresentationUtilsTest Tests")
public class UserPresentationUtilsTest{

    @Mock
    private UserPresentationRepository userPresentationRepository;

    @Mock
    private UserPresentationUtils userPresentationUtils;

    private ObjectMapper objectMapper;
    private UserPresentation userPresentation;
    private UserPresentationDTO userPresentationDTO;
    private UsuarioDTO usuarioDTO;

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
        
        User user = new User();
        user.setId(1L);
        user.setNombre("Juan");
        user.setApellido("Perez");

        userPresentation = new UserPresentation(
            userPresentationDTO.getId(),
            userPresentationDTO.getPosicion(),
            userPresentationDTO.getParrafo(),
            user
        );
    }

    @Test
    void createPageable_conParametrosValidos_retornaPageable() throws Exception {
        Pageable pageable = UserPresentationUtils.createPageable(0, 10);
        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
    }

    @Test
    void createPageable_conParametrosNulos_retornaPageable() throws Exception {
        Pageable pageable = UserPresentationUtils.createPageable(null, null);
        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());

        Pageable pageable2 = UserPresentationUtils.createPageable(null, 10);
        assertEquals(0, pageable2.getPageNumber());
        assertEquals(10, pageable2.getPageSize());

        Pageable pageable3 = UserPresentationUtils.createPageable(0, null);
        assertEquals(0, pageable3.getPageNumber());
        assertEquals(10, pageable3.getPageSize());
    }

    @Test
    void createPageable_conParametrosNegativos_retornaPageable() throws Exception {
        Pageable pageable = UserPresentationUtils.createPageable(-1, -10);
        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());

        Pageable pageable2 = UserPresentationUtils.createPageable(0, -10);
        assertEquals(0, pageable2.getPageNumber());
        assertEquals(10, pageable2.getPageSize());

        Pageable pageable3 = UserPresentationUtils.createPageable(-1, 10);
        assertEquals(0, pageable3.getPageNumber());
        assertEquals(10, pageable3.getPageSize());
    }


    @Test
    void createPageable_conParametrosNulosYNegativos_retornaPageable() throws Exception {
        Pageable pageable = UserPresentationUtils.createPageable(null, -10);
        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());

        Pageable pageable2 = UserPresentationUtils.createPageable(0, null);
        assertEquals(0, pageable2.getPageNumber());
        assertEquals(10, pageable2.getPageSize());

        Pageable pageable3 = UserPresentationUtils.createPageable(null, 10);
        assertEquals(0, pageable3.getPageNumber());
        assertEquals(10, pageable3.getPageSize());
    }

    @Test
    void dtoToEntity_conDTOValido_retornaEntity() throws Exception {
        userPresentationDTO.setUser(usuarioDTO);
        UserPresentation userPresentation = UserPresentationUtils.dtoToEntity(userPresentationDTO);
        assertEquals(userPresentationDTO.getId(), userPresentation.getId());
        assertEquals(userPresentationDTO.getPosicion(), userPresentation.getPosicion());
        assertEquals(userPresentationDTO.getParrafo(), userPresentation.getParrafo());
        assertEquals(userPresentationDTO.getUser().getId(), userPresentation.getUser().getId());
        assertEquals(userPresentationDTO.getUser().getNombre(), userPresentation.getUser().getNombre());
        assertEquals(userPresentationDTO.getUser().getApellido(), userPresentation.getUser().getApellido());    
    }

    @Test
    void dtoToEntity_conDTONulo_retornaNulo() throws Exception {
        UserPresentation userPresentation = UserPresentationUtils.dtoToEntity(null);
        assertNull(userPresentation);
    }

    @Test
    void entityToDTO_conEntityValidao_retornaDTO() throws Exception {
        UserPresentationDTO userPresentationDTO = UserPresentationUtils.entityToDTO(userPresentation);
        assertEquals(userPresentationDTO.getId(), userPresentation.getId());
        assertEquals(userPresentationDTO.getPosicion(), userPresentation.getPosicion());
        assertEquals(userPresentationDTO.getParrafo(), userPresentation.getParrafo());
        assertEquals(userPresentationDTO.getUser().getId(), userPresentation.getUser().getId());
        assertEquals(userPresentationDTO.getUser().getNombre(), userPresentation.getUser().getNombre());
        assertEquals(userPresentationDTO.getUser().getApellido(), userPresentation.getUser().getApellido());    
    }

    @Test
    void entityToDTO_conEntityNulo_retornaNulo() throws Exception {
        UserPresentationDTO userPresentationDTO = UserPresentationUtils.entityToDTO(null);
        assertNull(userPresentationDTO);
    }
}
