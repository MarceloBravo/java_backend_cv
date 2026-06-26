package com.mabc.back_cv.web.services.Rol;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.mabc.back_cv.web.dto.RolDTO;
import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.repositories.RolRepository;

import com.mabc.back_cv.common.Utils;

/**
 * Implementación del servicio de roles.
 * Proporciona la lógica de negocio para las operaciones CRUD de roles.
 */
@Service
public class RolServiceImpl implements RolService {
    private final RolRepository rolRepository;
    private final RolMapper RolMapper;

    public RolServiceImpl(RolRepository rolRepository, RolMapper RolMapper) {
        this.rolRepository = rolRepository;
        this.RolMapper = RolMapper;
    }

    @Override
    public RolDTO findById(Long id) {
        Rol rol = rolRepository.findById(id).orElse(null);
        if (rol == null)
            return null;
        return new RolDTO(rol.getId(), rol.getNombre(), rol.getActivo());
    }

    @Override
    public Page<RolDTO> searchBy(String nombre, Boolean activo, int page, int rows) {
        Pageable pageable = Utils.createPageable(page, rows);
        Page<Rol> rolPage;

        if (nombre == null || nombre.trim().isEmpty()) {
            rolPage = rolRepository.findAll(pageable);
        } else {
            rolPage = rolRepository.searchByNombreAndEstado(nombre, activo, pageable);
        }

        return rolPage.map(rol -> RolMapper.mapToRolDTO(rol));
    }

    @Override
    public List<RolDTO> getAll() {
        return rolRepository.findAll()
                .stream()
                .map(rol -> new RolDTO(rol.getId(), rol.getNombre(), rol.getActivo()))
                .collect(Collectors.toList());
    }

    @Override
    public Page<RolDTO> getAll(int page, int rows) {
        Pageable pageable = Utils.createPageable(page, rows);
        Page<Rol> rolPage = rolRepository.findAll(pageable);
        return rolPage.map(rol -> RolMapper.mapToRolDTO(rol));
    }

    @Override
    public List<RolDTO> getActiveRoles() {
        List<Rol> rolPage = rolRepository.findByActiveState();
        return rolPage.stream().map(rol -> RolMapper.mapToRolDTO(rol)).collect(Collectors.toList());
    }

    @Override
    public RolDTO save(RolDTO rolDto) {
        Rol rol = RolMapper.mapToRol(rolDto);
        if (rol == null) {
            return null;
        }
        Rol savedRol = rolRepository.save(rol);
        return RolMapper.mapToRolDTO(savedRol);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Error: El id no puede ser nulo.");
        }
        if (!rolRepository.findById(id).isPresent()) {
            throw new RuntimeException("Error: El rol no existe.");
        }
        rolRepository.deleteById(id);
    }

}
