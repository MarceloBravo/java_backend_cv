package com.mabc.back_cv.usuarios;

import com.mabc.back_cv.web.dto.UsuarioDTO;
import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.services.usuarios.UsuarioUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas unitarias de UsuarioUtils")
class UsuarioUtilsTest {

    private Rol rol;
    private User userBase;
    private UsuarioDTO dtoBases;

    @BeforeEach
    void setUp() {
        rol = new Rol();
        rol.setId(1L);
        rol.setNombre("ROLE_USER");
        rol.setActivo(true);

        userBase = new User();
        userBase.setId(10L);
        userBase.setNombre("María");
        userBase.setApellido("López");
        userBase.setEmail("maria@example.com");
        userBase.setPassword("secret");
        userBase.setActivo(true);
        userBase.setRol(rol);
        userBase.setParrafos(new ArrayList<>());

        dtoBases = new UsuarioDTO();
        dtoBases.setId(10L);
        dtoBases.setNombre("María");
        dtoBases.setApellido("López");
        dtoBases.setEmail("maria@example.com");
        dtoBases.setPassword("secret");
        dtoBases.setActivo(true);
        dtoBases.setRol(rol);
        dtoBases.setParrafos(new ArrayList<>());
    }

    // =========================================================================
    // userToDTO
    // =========================================================================
    @Nested
    @DisplayName("userToDTO")
    class UserToDTOTests {

        @Test
        @DisplayName("Éxito: mapea correctamente todos los campos de User a UsuarioDTO")
        void exitoMapeoCompleto() {
            UsuarioDTO result = UsuarioUtils.userToDTO(userBase);

            assertNotNull(result);
            assertEquals(10L, result.getId());
            assertEquals("María", result.getNombre());
            assertEquals("López", result.getApellido());
            assertEquals("maria@example.com", result.getEmail());
            assertEquals(rol, result.getRol());
            assertTrue(result.getActivo());
            assertNotNull(result.getParrafos());
        }

        @Test
        @DisplayName("Éxito: el campo password NO se copia al DTO (seguridad)")
        void passwordNoSeCopiaalDTO() {
            UsuarioDTO result = UsuarioUtils.userToDTO(userBase);

            assertNull(result.getPassword());
        }

        @Test
        @DisplayName("Éxito: mapea usuario con campos opcionales nulos sin lanzar excepción")
        void exitoConCamposOpcionalesNulos() {
            User userMinimo = new User();
            userMinimo.setId(5L);
            userMinimo.setNombre("Pedro");
            userMinimo.setApellido("Soto");
            userMinimo.setEmail("pedro@example.com");
            userMinimo.setPassword("pass");
            userMinimo.setActivo(false);
            userMinimo.setRol(rol);
            userMinimo.setParrafos(new ArrayList<>());

            UsuarioDTO result = UsuarioUtils.userToDTO(userMinimo);

            assertNotNull(result);
            assertEquals(5L, result.getId());
            assertFalse(result.getActivo());
        }

        @Test
        @DisplayName("Parámetro nulo: lanza NullPointerException al recibir null")
        void userNuloLanzaExcepcion() {
            assertThrows(NullPointerException.class, () -> UsuarioUtils.userToDTO(null));
        }
    }

    // =========================================================================
    // DTOToUser
    // =========================================================================
    @Nested
    @DisplayName("DTOToUser")
    class DTOToUserTests {

        @Test
        @DisplayName("Éxito: mapea correctamente todos los campos de UsuarioDTO a User")
        void exitoMapeoCompleto() {
            User result = UsuarioUtils.DTOToUser(dtoBases);

            assertNotNull(result);
            assertEquals(10L, result.getId());
            assertEquals("María", result.getNombre());
            assertEquals("López", result.getApellido());
            assertEquals("maria@example.com", result.getEmail());
            assertEquals("secret", result.getPassword());
            assertEquals(rol, result.getRol());
            assertTrue(result.getActivo());
        }

        @Test
        @DisplayName("Éxito: cuando el DTO tiene id null, el User resultante tampoco tiene id")
        void exitoSinId() {
            dtoBases.setId(null);

            User result = UsuarioUtils.DTOToUser(dtoBases);

            assertNotNull(result);
            assertNull(result.getId());
        }

        @Test
        @DisplayName("Éxito: mapea DTO con activo en false")
        void exitoActivoFalse() {
            dtoBases.setActivo(false);

            User result = UsuarioUtils.DTOToUser(dtoBases);

            assertFalse(result.getActivo());
        }

        @Test
        @DisplayName("Parámetros inválidos: campos de texto vacíos se mapean tal cual al User")
        void camposVaciosSeMapeam() {
            dtoBases.setNombre("");
            dtoBases.setApellido("");

            User result = UsuarioUtils.DTOToUser(dtoBases);

            assertEquals("", result.getNombre());
            assertEquals("", result.getApellido());
        }

        @Test
        @DisplayName("Parámetro nulo: lanza NullPointerException al recibir null")
        void dtoNuloLanzaExcepcion() {
            assertNull(UsuarioUtils.DTOToUser(null));
        }
    }

    // =========================================================================
    // Ciclo de ida y vuelta User → DTO → User
    // =========================================================================
    @Nested
    @DisplayName("Ciclo de conversión User ↔ DTO")
    class CicloConversionTests {

        @Test
        @DisplayName("User convertido a DTO y de vuelta a User mantiene los mismos campos de negocio")
        void cicloUserDTOUser() {
            UsuarioDTO dto = UsuarioUtils.userToDTO(userBase);
            dto.setPassword("secret"); // restaurar password para la conversión inversa
            User userReconstruido = UsuarioUtils.DTOToUser(dto);

            assertEquals(userBase.getId(), userReconstruido.getId());
            assertEquals(userBase.getNombre(), userReconstruido.getNombre());
            assertEquals(userBase.getApellido(), userReconstruido.getApellido());
            assertEquals(userBase.getEmail(), userReconstruido.getEmail());
            assertEquals(userBase.getRol(), userReconstruido.getRol());
            assertEquals(userBase.getActivo(), userReconstruido.getActivo());
        }
    }
}
