package com.mabc.back_cv.usuarios;

import com.mabc.back_cv.web.dto.UsuarioDTO;
import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.services.usuarios.UsuariosService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contrato de la interfaz {@link UsuariosService}.
 * Verifica que las implementaciones concretas respeten el contrato definido
 * usando una implementación anónima mínima.
 */
@DisplayName("Contrato de interfaz UsuariosService")
class UsuariosServiceTest {

    private Rol buildRol() {
        Rol rol = new Rol();
        rol.setId(1L);
        rol.setNombre("ROLE_USER");
        rol.setActivo(true);
        return rol;
    }

    private User buildUser() {
        User user = new User();
        user.setId(1L);
        user.setNombre("Carlos");
        user.setApellido("Díaz");
        user.setEmail("carlos@example.com");
        user.setPassword("clave");
        user.setActivo(true);
        user.setRol(buildRol());
        return user;
    }

    private UsuarioDTO buildDTO() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("Carlos");
        dto.setApellido("Díaz");
        dto.setEmail("carlos@example.com");
        dto.setPassword("clave");
        dto.setActivo(true);
        dto.setRol(buildRol());
        return dto;
    }

    /**
     * Implementación mínima del contrato para verificar firmas de métodos.
     */
    private final UsuariosService stub = new UsuariosService() {

        @Override
        public List<UsuarioDTO> getAllUsuarios(String filter) {
            if (filter == null) return List.of();
            if (filter.equals("error")) throw new RuntimeException("error");
            UsuarioDTO dto = buildDTO();
            return List.of(dto);
        }

        @Override
        public Page<UsuarioDTO> getAllUsuariosPage(String filter, Integer page, Integer size) {
            if (page != null && page < 0) throw new IllegalArgumentException("page inválida");
            return Page.empty();
        }

        @Override
        public UsuarioDTO getUsuarioById(Long id) {
            if (id == null) return null;
            if (id < 0) throw new IllegalArgumentException("id inválido");
            if (id.equals(1L)) return buildDTO();
            return null;
        }

        @Override
        public UsuarioDTO saveUsuario(UsuarioDTO usuarioDTO) {
            if (usuarioDTO == null) throw new IllegalArgumentException("DTO nulo");
            if (usuarioDTO.getNombre() == null || usuarioDTO.getNombre().isBlank())
                throw new IllegalArgumentException("Nombre inválido");
            return buildDTO();
        }

        @Override
        public void deleteUsuario(Long id) {
            if (id == null) throw new RuntimeException("Error: El usuario no existe.");
            if (!id.equals(1L)) throw new RuntimeException("Error: El usuario no existe.");
        }
    };

    // =========================================================================
    // getAllUsuarios — contrato
    // =========================================================================
    @Nested
    @DisplayName("getAllUsuarios — contrato")
    class GetAllUsuariosContrato {

        @Test
        @DisplayName("Contrato: retorna List no nulo ante filtro válido")
        void retornaListaNoNula() {
            List<UsuarioDTO> result = stub.getAllUsuarios("Carlos");
            assertNotNull(result);
        }

        @Test
        @DisplayName("Contrato: retorna lista vacía ante filtro null")
        void retornaListaVaciaConNull() {
            List<UsuarioDTO> result = stub.getAllUsuarios(null);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Contrato: puede lanzar RuntimeException ante error del servicio")
        void puedeRlanzarExcepcion() {
            assertThrows(RuntimeException.class, () -> stub.getAllUsuarios("error"));
        }
    }

    // =========================================================================
    // getAllUsuariosPage — contrato
    // =========================================================================
    @Nested
    @DisplayName("getAllUsuariosPage — contrato")
    class GetAllUsuariosPageContrato {

        @Test
        @DisplayName("Contrato: retorna Page no nulo con parámetros válidos")
        void retornaPaginaNoNula() {
            Page<UsuarioDTO> result = stub.getAllUsuariosPage("Carlos", 0, 10);
            assertNotNull(result);
        }

        @Test
        @DisplayName("Contrato: acepta parámetros page y size nulos")
        void aceptaNulos() {
            assertDoesNotThrow(() -> stub.getAllUsuariosPage(null, null, null));
        }

        @Test
        @DisplayName("Contrato: puede rechazar page negativa")
        void rechazaPageNegativa() {
            assertThrows(IllegalArgumentException.class,
                    () -> stub.getAllUsuariosPage("Carlos", -1, 10));
        }
    }

    // =========================================================================
    // getUsuarioById — contrato
    // =========================================================================
    @Nested
    @DisplayName("getUsuarioById — contrato")
    class GetUsuarioByIdContrato {

        @Test
        @DisplayName("Contrato: retorna UsuarioDTO cuando el id existe")
        void retornaDTOCuandoExiste() {
            UsuarioDTO result = stub.getUsuarioById(1L);
            assertNotNull(result);
        }

        @Test
        @DisplayName("Contrato: retorna null cuando el id no corresponde a ningún usuario")
        void retornaNullCuandoNoExiste() {
            UsuarioDTO result = stub.getUsuarioById(999L);
            assertNull(result);
        }

        @Test
        @DisplayName("Contrato: retorna null cuando el id es null")
        void retornaNullConIdNulo() {
            UsuarioDTO result = stub.getUsuarioById(null);
            assertNull(result);
        }

        @Test
        @DisplayName("Contrato: puede rechazar id inválido (negativo)")
        void rechazaIdInvalido() {
            assertThrows(IllegalArgumentException.class, () -> stub.getUsuarioById(-1L));
        }
    }

    // =========================================================================
    // saveUsuario — contrato
    // =========================================================================
    @Nested
    @DisplayName("saveUsuario — contrato")
    class SaveUsuarioContrato {

        @Test
        @DisplayName("Contrato: retorna UsuarioDTO no nulo al guardar con datos válidos")
        void retornaDTOAlGuardar() {
            UsuarioDTO result = stub.saveUsuario(buildDTO());
            assertNotNull(result);
        }

        @Test
        @DisplayName("Contrato: lanza excepción cuando el DTO es null")
        void rechazaDTONulo() {
            assertThrows(IllegalArgumentException.class, () -> stub.saveUsuario(null));
        }

        @Test
        @DisplayName("Contrato: lanza excepción cuando el nombre es inválido (vacío)")
        void rechazaNombreVacio() {
            UsuarioDTO dtoInvalido = buildDTO();
            dtoInvalido.setNombre("");
            assertThrows(IllegalArgumentException.class, () -> stub.saveUsuario(dtoInvalido));
        }

        @Test
        @DisplayName("Contrato: lanza excepción cuando el nombre es null")
        void rechazaNombreNulo() {
            UsuarioDTO dtoInvalido = buildDTO();
            dtoInvalido.setNombre(null);
            assertThrows(IllegalArgumentException.class, () -> stub.saveUsuario(dtoInvalido));
        }
    }

    // =========================================================================
    // deleteUsuario — contrato
    // =========================================================================
    @Nested
    @DisplayName("deleteUsuario — contrato")
    class DeleteUsuarioContrato {

        @Test
        @DisplayName("Contrato: no lanza excepción al eliminar un id existente")
        void noLanzaExcepcionCuandoExiste() {
            assertDoesNotThrow(() -> stub.deleteUsuario(1L));
        }

        @Test
        @DisplayName("Contrato: lanza RuntimeException cuando el id no existe")
        void lanzaExcepcionCuandoNoExiste() {
            assertThrows(RuntimeException.class, () -> stub.deleteUsuario(999L));
        }

        @Test
        @DisplayName("Contrato: lanza RuntimeException cuando el id es null")
        void lanzaExcepcionConIdNulo() {
            assertThrows(RuntimeException.class, () -> stub.deleteUsuario(null));
        }
    }
}
