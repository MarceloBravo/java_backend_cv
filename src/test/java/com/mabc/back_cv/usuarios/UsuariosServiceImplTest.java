package com.mabc.back_cv.usuarios;

import com.mabc.back_cv.web.dto.UsuarioDTO;
import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.repositories.RolRepository;
import com.mabc.back_cv.web.repositories.UserRepository;
import com.mabc.back_cv.web.services.usuarios.UsuariosServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de UsuariosServiceImpl")
class UsuariosServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private UsuariosServiceImpl service;

    private Rol rolBase;
    private User userBase;
    private UsuarioDTO usuarioDTOBase;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        rolBase = new Rol();
        rolBase.setId(1L);
        rolBase.setNombre("ROLE_USER");
        rolBase.setActivo(true);

        userBase = new User();
        userBase.setId(1L);
        userBase.setNombre("Juan");
        userBase.setApellido("Pérez");
        userBase.setEmail("juan@example.com");
        userBase.setPassword("pass123");
        userBase.setActivo(true);
        userBase.setRol(rolBase);

        usuarioDTOBase = new UsuarioDTO();
        usuarioDTOBase.setNombre("Juan");
        usuarioDTOBase.setApellido("Pérez");
        usuarioDTOBase.setEmail("juan@example.com");
        usuarioDTOBase.setPassword("pass123");
        usuarioDTOBase.setActivo(true);
        usuarioDTOBase.setRol(rolBase);

        pageable = PageRequest.of(0, 10);
    }

    // =========================================================================
    // getAllUsuarios
    // =========================================================================
    @Nested
    @DisplayName("getAllUsuarios")
    class GetAllUsuariosTests {

        @Test
        @DisplayName("Éxito: retorna lista de usuarios activos con filtro válido")
        void exitoCon_filtroValido() {
            when(userRepository.findAllFilteres("Juan", true)).thenReturn(List.of(userBase));

            List<UsuarioDTO> result = service.getAllUsuarios("Juan");

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Juan", result.get(0).getNombre());
            verify(userRepository).findAllFilteres("Juan", true);
        }

        @Test
        @DisplayName("Éxito: retorna lista vacía cuando no hay coincidencias")
        void exitoConListaVacia() {
            when(userRepository.findAllFilteres("inexistente", true)).thenReturn(List.of());

            List<UsuarioDTO> result = service.getAllUsuarios("inexistente");

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Parámetro nulo: se pasa null como filtro, el repositorio es invocado con null")
        void filtroNulo() {
            when(userRepository.findAllFilteres(null, true)).thenReturn(List.of(userBase));

            List<UsuarioDTO> result = service.getAllUsuarios(null);

            assertNotNull(result);
            assertEquals(1, result.size());
            verify(userRepository).findAllFilteres(null, true);
        }

        @Test
        @DisplayName("Parámetro inválido: filtro vacío, el repositorio es invocado con cadena vacía")
        void filtroVacio() {
            when(userRepository.findAllFilteres("", true)).thenReturn(List.of());

            List<UsuarioDTO> result = service.getAllUsuarios("");

            assertNotNull(result);
            verify(userRepository).findAllFilteres("", true);
        }
    }

    // =========================================================================
    // getAllUsuariosPage
    // =========================================================================
    @Nested
    @DisplayName("getAllUsuariosPage")
    class GetAllUsuariosPageTests {

        @Test
        @DisplayName("Éxito: retorna página con parámetros válidos")
        void exitoConParametrosValidos() {
            Page<User> pageUser = new PageImpl<>(List.of(userBase), pageable, 1);
            when(userRepository.findByFilter(eq("Juan"), any(Pageable.class))).thenReturn(pageUser);

            Page<UsuarioDTO> result = service.getAllUsuariosPage("Juan", 0, 10);

            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            assertEquals("Juan", result.getContent().get(0).getNombre());
        }

        @Test
        @DisplayName("Éxito: retorna página vacía cuando no hay resultados")
        void exitoConPaginaVacia() {
            Page<User> pageVacia = new PageImpl<>(List.of(), pageable, 0);
            when(userRepository.findByFilter(eq("sinresultado"), any(Pageable.class))).thenReturn(pageVacia);

            Page<UsuarioDTO> result = service.getAllUsuariosPage("sinresultado", 0, 10);

            assertNotNull(result);
            assertTrue(result.getContent().isEmpty());
        }

        @Test
        @DisplayName("Parámetros nulos: page y size null deben usar valores por defecto (0 y 10)")
        void pageYSizeNulos() {
            Page<User> pageUser = new PageImpl<>(List.of(userBase), pageable, 1);
            when(userRepository.findByFilter(isNull(), any(Pageable.class))).thenReturn(pageUser);

            Page<UsuarioDTO> result = service.getAllUsuariosPage(null, null, null);

            assertNotNull(result);
            verify(userRepository).findByFilter(isNull(),
                    argThat(p -> p.getPageNumber() == 0 && p.getPageSize() == 10));
        }

        @Test
        @DisplayName("Parámetros inválidos: page negativo debe corregirse a 0")
        void pageNegativa() {
            Page<User> pageUser = new PageImpl<>(List.of(userBase), pageable, 1);
            when(userRepository.findByFilter(any(), any(Pageable.class))).thenReturn(pageUser);

            Page<UsuarioDTO> result = service.getAllUsuariosPage("Juan", -5, 10);

            assertNotNull(result);
            verify(userRepository).findByFilter(eq("Juan"), argThat(p -> p.getPageNumber() == 0));
        }

        @Test
        @DisplayName("Parámetros inválidos: size <= 0 debe corregirse a 10")
        void sizeCero() {
            Page<User> pageUser = new PageImpl<>(List.of(userBase), pageable, 1);
            when(userRepository.findByFilter(any(), any(Pageable.class))).thenReturn(pageUser);

            Page<UsuarioDTO> result = service.getAllUsuariosPage("Juan", 0, 0);

            assertNotNull(result);
            verify(userRepository).findByFilter(eq("Juan"), argThat(p -> p.getPageSize() == 10));
        }

        @Test
        @DisplayName("Parámetros inválidos: filter vacío debe tratarse como null")
        void filtroVacioSeConvierteEnNull() {
            Page<User> pageUser = new PageImpl<>(List.of(userBase), pageable, 1);
            when(userRepository.findByFilter(isNull(), any(Pageable.class))).thenReturn(pageUser);

            Page<UsuarioDTO> result = service.getAllUsuariosPage("", 0, 10);

            assertNotNull(result);
            verify(userRepository).findByFilter(isNull(), any(Pageable.class));
        }
    }

    // =========================================================================
    // getUsuarioById
    // =========================================================================
    @Nested
    @DisplayName("getUsuarioById")
    class GetUsuarioByIdTests {

        @Test
        @DisplayName("Éxito: retorna UsuarioDTO cuando el id existe")
        void exitoCuandoIdExiste() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(userBase));

            UsuarioDTO result = service.getUsuarioById(1L);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("Juan", result.getNombre());
        }

        @Test
        @DisplayName("Error: retorna null cuando el usuario no existe")
        void errorCuandoIdNoExiste() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            UsuarioDTO result = service.getUsuarioById(99L);

            assertNull(result);
        }

        @Test
        @DisplayName("Parámetro nulo: retorna null sin invocar el repositorio")
        void idNulo() {
            UsuarioDTO result = service.getUsuarioById(null);

            assertNull(result);
            verifyNoInteractions(userRepository);
        }
    }

    // =========================================================================
    // saveUsuario
    // =========================================================================
    @Nested
    @DisplayName("saveUsuario")
    class SaveUsuarioTests {

        @Test
        @DisplayName("Éxito: guarda y retorna UsuarioDTO cuando los datos son válidos y el rol existe")
        void exitoGuardaUsuarioNuevo() {
            User userSinId = new User();
            userSinId.setNombre("Juan");
            userSinId.setApellido("Pérez");
            userSinId.setEmail("juan@example.com");
            userSinId.setPassword("pass123");
            userSinId.setActivo(true);
            userSinId.setRol(rolBase);

            when(rolRepository.findById(1L)).thenReturn(Optional.of(rolBase));
            when(userRepository.save(any(User.class))).thenReturn(userBase);

            UsuarioDTO result = service.saveUsuario(usuarioDTOBase);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("Juan", result.getNombre());
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("Error: lanza RuntimeException cuando el rol no existe en la base de datos")
        void errorRolNoEncontrado() {
            when(rolRepository.findById(1L)).thenReturn(Optional.empty());

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> service.saveUsuario(usuarioDTOBase));

            assertEquals("Rol no encontrado", ex.getMessage());
            verifyNoInteractions(userRepository);
        }

        @Test
        @DisplayName("Error: lanza RuntimeException cuando el DTO tiene id (usuario ya existe)")
        void errorUsuarioConId() {
            usuarioDTOBase.setId(1L);
            when(rolRepository.findById(1L)).thenReturn(Optional.of(rolBase));

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> service.saveUsuario(usuarioDTOBase));

            assertEquals("Error: El usuario no existe.", ex.getMessage());
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Parámetros inválidos: DTO con rol de id null omite la validación del rol")
        void dtoConRolSinId() {
            Rol rolSinId = new Rol();
            rolSinId.setId(null);
            rolSinId.setNombre("ROLE_GUEST");

            UsuarioDTO dtoConRolSinId = new UsuarioDTO();
            dtoConRolSinId.setNombre("Ana");
            dtoConRolSinId.setApellido("García");
            dtoConRolSinId.setEmail("ana@example.com");
            dtoConRolSinId.setPassword("pass");
            dtoConRolSinId.setActivo(true);
            dtoConRolSinId.setRol(rolSinId);

            User userGuardado = new User();
            userGuardado.setId(2L);
            userGuardado.setNombre("Ana");
            userGuardado.setApellido("García");
            userGuardado.setEmail("ana@example.com");
            userGuardado.setRol(rolSinId);
            userGuardado.setActivo(true);

            when(userRepository.save(any(User.class))).thenReturn(userGuardado);

            UsuarioDTO result = service.saveUsuario(dtoConRolSinId);

            assertNotNull(result);
            verify(userRepository).save(any(User.class));
        }
    }

    // =========================================================================
    // deleteUsuario
    // =========================================================================
    @Nested
    @DisplayName("deleteUsuario")
    class DeleteUsuarioTests {

        @Test
        @DisplayName("Éxito: elimina el usuario cuando el id existe en la base de datos")
        void exitoEliminaUsuario() {
            when(userRepository.existsById(1L)).thenReturn(true);
            doNothing().when(userRepository).deleteById(1L);

            assertDoesNotThrow(() -> service.deleteUsuario(1L));

            verify(userRepository).existsById(1L);
            verify(userRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Error: lanza RuntimeException cuando el usuario no existe")
        void errorUsuarioNoExiste() {
            when(userRepository.existsById(99L)).thenReturn(false);

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> service.deleteUsuario(99L));

            assertEquals("Error: El usuario no existe.", ex.getMessage());
            verify(userRepository, never()).deleteById(any());
        }

        @Test
        @DisplayName("Parámetro nulo: lanza RuntimeException cuando el id es null")
        void idNulo() {
            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> service.deleteUsuario(null));

            assertEquals("Error: El usuario no existe.", ex.getMessage());
            verifyNoInteractions(userRepository);
        }
    }
}
