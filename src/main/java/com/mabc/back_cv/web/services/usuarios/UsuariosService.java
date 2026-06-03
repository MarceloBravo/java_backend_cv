package com.mabc.back_cv.web.services.usuarios;

import org.springframework.data.domain.Page;
import com.mabc.back_cv.web.dto.UsuarioDTO;

import java.util.List;

/**
 * Interfaz de servicio para la gestión de usuarios.
 * Define los métodos que deben ser implementados para operaciones CRUD de usuarios.
 * 
 * @author MaBC
 * @version 1.0
 */
public interface UsuariosService {

    /**
     * Obtiene la lista de todos los usuarios aplicando un filtro opcional.
     * 
     * @param filter Filtro opcional para buscar usuarios
     * @return Lista de UsuarioDTO
     */
    List<UsuarioDTO> getAllUsuarios(String filter);

    /**
     * Obtiene una página de usuarios con filtro, número de página y tamaño.
     * 
     * @param filter Filtro opcional para buscar usuarios
     * @param page Número de página (basado en cero)
     * @param size Cantidad de registros por página
     * @return Page de UsuarioDTO
     */
    Page<UsuarioDTO> getAllUsuariosPage(String filter, Long page, Long size);

    /**
     * Obtiene un usuario por su identificador.
     * 
     * @param id Identificador único del usuario
     * @return UsuarioDTO o null si no existe
     */
    UsuarioDTO getUsuarioById(Long id);

    /**
     * Crea o actualiza un usuario.
     * 
     * @param usuarioDTO Datos del usuario a guardar
     * @return UsuarioDTO guardado
     */
    UsuarioDTO saveUsuario(UsuarioDTO usuarioDTO);

    /**
     * Elimina un usuario por su identificador.
     * 
     * @param id Identificador único del usuario a eliminar
     */
    void deleteUsuario(Long id);
}
