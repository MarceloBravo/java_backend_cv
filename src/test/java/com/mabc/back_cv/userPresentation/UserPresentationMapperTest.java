package com.mabc.back_cv.userPresentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mabc.back_cv.web.dto.UserPresentationDTO;
import com.mabc.back_cv.web.dto.UsuarioDTO;
import com.mabc.back_cv.web.entities.UserPresentation;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.services.userPresentation.UserPresentationMapper;
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
@DisplayName("UserPresentationMapperTest Tests")
public class UserPresentationMapperTest{

    @Mock
    private UserPresentationRepository userPresentationRepository;

    @Mock
    private UserPresentationMapper UserPresentationMapper;

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
    void dtoToEntity_conDTOValido_retornaEntity() throws Exception {
        userPresentationDTO.setUser(usuarioDTO);
        UserPresentation userPresentation = UserPresentationMapper.dtoToEntity(userPresentationDTO);
        assertEquals(userPresentationDTO.getId(), userPresentation.getId());
        assertEquals(userPresentationDTO.getPosicion(), userPresentation.getPosicion());
        assertEquals(userPresentationDTO.getParrafo(), userPresentation.getParrafo());
        assertEquals(userPresentationDTO.getUser().getId(), userPresentation.getUser().getId());
        assertEquals(userPresentationDTO.getUser().getNombre(), userPresentation.getUser().getNombre());
        assertEquals(userPresentationDTO.getUser().getApellido(), userPresentation.getUser().getApellido());    
    }

    @Test
    void dtoToEntity_conDTONulo_retornaNulo() throws Exception {
        UserPresentation userPresentation = UserPresentationMapper.dtoToEntity(null);
        assertNull(userPresentation);
    }

    @Test
    void entityToDTO_conEntityValidao_retornaDTO() throws Exception {
        UserPresentationDTO userPresentationDTO = UserPresentationMapper.entityToDTO(userPresentation);
        assertEquals(userPresentationDTO.getId(), userPresentation.getId());
        assertEquals(userPresentationDTO.getPosicion(), userPresentation.getPosicion());
        assertEquals(userPresentationDTO.getParrafo(), userPresentation.getParrafo());
        assertEquals(userPresentationDTO.getUser().getId(), userPresentation.getUser().getId());
        assertEquals(userPresentationDTO.getUser().getNombre(), userPresentation.getUser().getNombre());
        assertEquals(userPresentationDTO.getUser().getApellido(), userPresentation.getUser().getApellido());    
    }

    @Test
    void entityToDTO_conEntityNulo_retornaNulo() throws Exception {
        UserPresentationDTO userPresentationDTO = UserPresentationMapper.entityToDTO(null);
        assertNull(userPresentationDTO);
    }
}
