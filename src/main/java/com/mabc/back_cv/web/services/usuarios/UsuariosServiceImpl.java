package com.mabc.back_cv.web.services.usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mabc.back_cv.web.repositories.UserRepository;
import com.mabc.back_cv.web.repositories.RolRepository;

import com.mabc.back_cv.web.dto.UsuarioDTO;
import com.mabc.back_cv.web.entities.User;

import java.util.stream.Collectors;
import java.util.List;

import com.mabc.back_cv.common.Utils;

/**
 * Implementación del servicio de usuario.
 * Proporciona la lógica de negocio para las operaciones CRUD de usuarios.
 * 
 * @author MaBC
 * @version 1.0
 */
@Service
public class UsuariosServiceImpl implements UsuariosService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolRepository rolRepository;
    

    /**
     * Obtiene la lista de todos los usuarios aplicando un filtro opcional.
     * 
     * @param filter Filtro opcional para buscar usuarios
     * @return Lista de UsuarioDTO
     */
    @Override
    public List<UsuarioDTO> getAllUsuarios(String filter) {
        List<User> userList = userRepository.findAllFilteres(filter, true);
        return userList.stream().map(user -> UsuarioMapper.userToDTO(user)).collect(Collectors.toList());
    }

    /**
     * Obtiene una página de usuarios con filtro, número de página y tamaño.
     * 
     * @param filter Filtro opcional para buscar usuarios
     * @param page Número de página (basado en cero)
     * @param size Cantidad de registros por página
     * @return Page de UsuarioDTO
     */
    @Override
    public Page<UsuarioDTO> getAllUsuariosPage(String filter, Integer page, Integer size) {
        filter = (filter == null || filter.isEmpty()) ? null : filter;        
        Pageable pageable = Utils.createPageable(page, size);
        Page<User> userPage = userRepository.findByFilter(filter, pageable);
        return userPage.map(user -> UsuarioMapper.userToDTO(user));
    }

    /**
     * Obtiene un usuario por su identificador.
     * 
     * @param id Identificador único del usuario
     * @return UsuarioDTO o null si no existe o el id es nulo
     */
    @Override
    public UsuarioDTO getUsuarioById(Long id) {
        if (id == null) {
            return null;
        }
        return userRepository.findById(id).map(user -> UsuarioMapper.userToDTO(user)).orElse(null);
    }

    /**
     * Crea o actualiza un usuario.
     * Valida que el rol exista y que el usuario sea nuevo antes de guardar.
     * 
     * @param usuarioDTO Datos del usuario a guardar
     * @return UsuarioDTO guardado
     * @throws RuntimeException Si el rol no existe o el usuario ya existe
     */
    @Override
    public UsuarioDTO saveUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioDTO.getRol().getId() != null && rolRepository.findById(usuarioDTO.getRol().getId()).isEmpty()) {
            throw new RuntimeException("Rol no encontrado");
        }
        User user = UsuarioMapper.DTOToUser(usuarioDTO);
        if (user.getId() != null) {
            throw new RuntimeException("Error: El usuario no existe.");
        }
        User userSaved = userRepository.save(user);
        return UsuarioMapper.userToDTO(userSaved);
    }

    /**
     * Elimina un usuario por su identificador.
     * Valida que el usuario exista antes de eliminarlo.
     * 
     * @param id Identificador único del usuario a eliminar
     * @throws RuntimeException Si el usuario no existe o no se pudo eliminar
     */
    @Override
    public void deleteUsuario(Long id) {
        if (id == null || !userRepository.existsById(id)) {
            throw new RuntimeException("Error: El usuario no existe.");
        }
        userRepository.deleteById(id);
        if (userRepository.findById(id).isPresent()) {
            throw new RuntimeException("Error: El usuario no se pudo eliminar.");
        }
    }

}
