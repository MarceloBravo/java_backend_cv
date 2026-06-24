package com.mabc.back_cv.usuarios;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import com.mabc.back_cv.web.controllers.UsuarioController;
import com.mabc.back_cv.web.dto.UsuarioDTO;
import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.entities.UserPresentation;
import com.mabc.back_cv.web.services.usuarios.UsuariosService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de UsuarioController")
public class UsuarioControllerTest {

    @Mock
    private UsuariosService usuariosService;

    @InjectMocks
    private UsuarioController usuarioController;

    private UsuarioDTO usuarioDTO;
    private List<UsuarioDTO> usuariosList;
    private Page<UsuarioDTO> usuarioPage;

    @BeforeEach
    void setUp() {
        usuarioDTO = new UsuarioDTO(
                1L,
                "Juan",
                "Pérez",
                "juan.perez@example.com",
                "123456789",
                "Calle Falsa 123",
                "Santiago",
                "Español",
                "Password123!",
                true,
                new Rol(),
                Collections.<UserPresentation>emptyList());

        usuariosList = List.of(usuarioDTO);
        usuarioPage = new PageImpl<>(usuariosList, PageRequest.of(0, 10), usuariosList.size());
    }

    @Test
    @DisplayName("getAll devuelve lista de usuarios cuando el servicio responde correctamente")
    void getAll_success_returnsOk() {
        when(usuariosService.getAllUsuarios(null)).thenReturn(usuariosList);

        ResponseEntity<List<UsuarioDTO>> response = usuarioController.getAll(null);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(usuariosList);
        verify(usuariosService).getAllUsuarios(null);
    }

    @Test
    @DisplayName("getById devuelve usuario cuando el servicio encuentra el id")
    void getById_success_returnsOk() {
        when(usuariosService.getUsuarioById(1L)).thenReturn(usuarioDTO);

        ResponseEntity<UsuarioDTO> response = usuarioController.getById(1L);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(usuarioDTO);
        verify(usuariosService).getUsuarioById(1L);
    }

    @Test
    @DisplayName("getAllPage devuelve página de usuarios cuando el servicio responde correctamente")
    void getAllPage_success_returnsOk() {
        when(usuariosService.getAllUsuariosPage(null, 0, 10)).thenReturn(usuarioPage);

        ResponseEntity<Page<UsuarioDTO>> response = usuarioController.getAllPage(null, 0, 10);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(usuarioPage);
        verify(usuariosService).getAllUsuariosPage(null, 0, 10);
    }

    @Test
    @DisplayName("saveUsuario guarda el usuario y devuelve ok cuando el servicio responde correctamente")
    void saveUsuario_success_returnsOk() {
        when(usuariosService.saveUsuario(usuarioDTO)).thenReturn(usuarioDTO);

        ResponseEntity<UsuarioDTO> response = usuarioController.saveUsuario(usuarioDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(usuarioDTO);
        verify(usuariosService).saveUsuario(usuarioDTO);
    }

    @Test
    @DisplayName("deleteUsuario devuelve ok cuando el servicio elimina el usuario correctamente")
    void deleteUsuario_success_returnsOk() {
        doNothing().when(usuariosService).deleteUsuario(1L);

        ResponseEntity<String> response = usuarioController.deleteUsuario(1L);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Usuario eliminado correctamente");
        verify(usuariosService).deleteUsuario(1L);
    }

    @Test
    @DisplayName("getAll devuelve bad request cuando el servicio lanza excepción")
    void getAll_serviceException_returnsBadRequest() {
        when(usuariosService.getAllUsuarios("filtro"))
                .thenThrow(new IllegalArgumentException("Filtro inválido"));

        ResponseEntity<List<UsuarioDTO>> response = usuarioController.getAll("filtro");

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("getById devuelve bad request cuando el servicio lanza excepción")
    void getById_serviceException_returnsBadRequest() {
        when(usuariosService.getUsuarioById(999L)).thenThrow(new RuntimeException("Usuario no encontrado"));

        ResponseEntity<UsuarioDTO> response = usuarioController.getById(999L);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("getAllPage devuelve bad request cuando los parámetros de paginación son inválidos")
    void getAllPage_invalidPagination_returnsBadRequest() {
        when(usuariosService.getAllUsuariosPage(eq("filter"), eq(-1), eq(10)))
                .thenThrow(new IllegalArgumentException("Página inválida"));

        ResponseEntity<Page<UsuarioDTO>> response = usuarioController.getAllPage("filter", -1, 10);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("saveUsuario devuelve bad request cuando el cuerpo es nulo o inválido")
    void saveUsuario_nullBody_returnsBadRequest() {
        when(usuariosService.saveUsuario(any())).thenThrow(new IllegalArgumentException("Usuario inválido"));

        ResponseEntity<UsuarioDTO> response = usuarioController.saveUsuario(null);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("deleteUsuario devuelve bad request cuando el servicio lanza excepción")
    void deleteUsuario_serviceException_returnsBadRequest() {
        doThrow(new RuntimeException("Error al eliminar usuario")).when(usuariosService).deleteUsuario(2L);

        ResponseEntity<String> response = usuarioController.deleteUsuario(2L);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isNull();
    }
}
