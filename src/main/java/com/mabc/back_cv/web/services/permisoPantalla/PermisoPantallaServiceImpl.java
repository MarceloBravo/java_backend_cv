package com.mabc.back_cv.web.services.permisoPantalla;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mabc.back_cv.web.dto.PermisoPantallaDTO;
import com.mabc.back_cv.web.entities.Pantalla;
import com.mabc.back_cv.web.entities.PermisoPantalla;
import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.repositories.PantallaRepository;
import com.mabc.back_cv.web.repositories.PermisoPantallaRepository;
import com.mabc.back_cv.web.repositories.RolRepository;

/**
 * Implementación del servicio de permisos de pantalla.
 *
 * Proporciona operaciones para obtener, crear, eliminar y desactivar permisos
 * de pantalla asociados a roles y pantallas.
 */
@Service
public class PermisoPantallaServiceImpl implements PermisoPantallaService {

    private final PermisoPantallaRepository permisoPantallaRepository;
    private final RolRepository rolRepository;
    private final PantallaRepository pantallaRepository;

    /**
     * Constructor para la inyección de dependencias.
     *
     * @param permisoPantallaRepository Repositorio de permisos de pantalla.
     * @param rolRepository Repositorio de roles.
     * @param pantallaRepository Repositorio de pantallas.
     */
    public PermisoPantallaServiceImpl(PermisoPantallaRepository permisoPantallaRepository,
                                      RolRepository rolRepository,
                                      PantallaRepository pantallaRepository) {
        this.permisoPantallaRepository = permisoPantallaRepository;
        this.rolRepository = rolRepository;
        this.pantallaRepository = pantallaRepository;
    }

    /**
     * Obtiene la lista de permisos de pantalla asociados a un rol.
     *
     * @param id_rol Identificador del rol.
     * @return Lista de permisos de pantalla en forma de DTO; lista vacía si el rol es nulo o no existen permisos.
     */
    @Override
    public List<PermisoPantallaDTO> obtenerPermisosPantallaPorRol(Long id_rol) {
        if (id_rol == null) {
            return List.of();
        }

        List<PermisoPantalla[]> permisoPantalla = permisoPantallaRepository.findAllPermisosByRolId(id_rol);
        List<PermisoPantallaDTO> permisosDTO = permisoPantalla
                .stream()
                .map(pp -> PermisoPantallaUtil.mapToDTO(pp[0]))
                .toList();

        return permisosDTO;
    }



    /**
     * Obtiene un permiso de pantalla asociado a un rol y una pantalla.
     *
     * @param id_rol Identificador del rol.
     * @param id_pantalla Identificador de la pantalla.
     * @param activo Indica si el permiso debe estar activo; si es null se considera true.
     * @return DTO del permiso encontrado, o null si no existe.
     */
    @Override
    public PermisoPantallaDTO obtenerPermisoPantallaPorRolYPantalla(Long id_rol, Long id_pantalla, Boolean activo) {
        if (id_rol == null || id_pantalla == null) {
            return null;
        }

        activo = (activo == null) ? true : activo;
        List<PermisoPantalla> permisosPantalla = permisoPantallaRepository.findAllByRolIdAndPantallaIdAndActivo(id_rol, id_pantalla, activo);

        if (permisosPantalla.isEmpty()) {
            return null;
        }

        return PermisoPantallaUtil.mapToDTO(permisosPantalla.get(0));
    }

    /**
     * Almacena una lista de permisos de pantalla.
     *
     * @param permisosPantallaDTO Lista de DTOs de permisos de pantalla.
     * @return Cantidad de permisos procesados.
     */
    @Transactional
    @Override
    public int grabarPermisosPantallaPorRol(List<PermisoPantallaDTO> permisosPantallaDTO) {
        if (permisosPantallaDTO == null || permisosPantallaDTO.isEmpty()) {
            return 0;
        }

        for (PermisoPantallaDTO dto : permisosPantallaDTO) {
            grabarPermisoPantalla(dto);
        }

        return permisosPantallaDTO.size();
    }

    /**
     * Valida y guarda un permiso de pantalla individual.
     *
     * @param dto DTO del permiso de pantalla.
     */
    private void grabarPermisoPantalla(PermisoPantallaDTO dto) {
        if (dto == null || dto.getRol() == null || dto.getPantalla() == null) {
            throw new IllegalArgumentException("El rol y la pantalla son obligatorios");
        }

        Rol rol = rolRepository.findById(dto.getRol().getId()).orElse(null);
        Pantalla pantalla = pantallaRepository.findById(dto.getPantalla().getId()).orElse(null);

        if (rol == null || pantalla == null) {
            throw new IllegalArgumentException("El rol o la pantalla no existen");
        }

        PermisoPantalla permisoPantalla = PermisoPantallaUtil.mapToEntity(dto);
        permisoPantallaRepository.save(permisoPantalla);
    }


    /**
     * Elimina permisos de pantalla por rol.
     *
     * @param id_rol Identificador del rol.
     * @return true si se eliminaron permisos, false si no se encontraron.
     */
    @Override
    public boolean eliminarPermisosPantallaPorRolId(Long id_rol) {
        List<PermisoPantalla> permisosPantalla = permisoPantallaRepository.findByRolId(id_rol);
        if (!permisosPantalla.isEmpty()) {
            permisoPantallaRepository.deleteAll(permisosPantalla);
            return true;
        }
        return false;
    }



    /**
     * Desactiva un permiso de pantalla por su identificador.
     *
     * @param id Identificador del permiso de pantalla.
     * @return true si se desactivó el permiso.
     */
    @Override
    public boolean desactivarPermisosPantallaPorRolId(Long id) {
        int rowsAffected = permisoPantallaRepository.deactivatePermisoPantallaByRolId(id);
        return rowsAffected > 0;
    }


}
