package com.mabc.back_cv.web.services.Rol;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.mabc.back_cv.web.dto.RolDTO;
import com.mabc.back_cv.web.entities.Rol;
import com.mabc.back_cv.web.repositories.RolRepository;

@Service
public class RolServiceImpl implements RolService {
    private final RolRepository rolRepository;
    private final RolUtils rolUtils;

    public RolServiceImpl(RolRepository rolRepository, RolUtils rolUtils) {
        this.rolRepository = rolRepository;
        this.rolUtils = rolUtils;
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
        Pageable pageable = rolUtils.createPageable(page, rows);
        Page<Rol> rolPage;

        if (nombre == null || nombre.trim().isEmpty()) {
            rolPage = rolRepository.searchByNombreAndEstado(nombre, activo, pageable);
        } else {
            rolPage = rolRepository.findAll(pageable);
        }

        return rolPage.map(rol -> rolUtils.mapToRolDTO(rol));
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
        Pageable pageable = rolUtils.createPageable(page, rows);
        Page<Rol> rolPage = rolRepository.findAll(pageable);
        return rolPage.map(rol -> rolUtils.mapToRolDTO(rol));
    }

    @Override
    public List<RolDTO> getActiveRoles() {
        List<Rol> rolPage = rolRepository.findByActiveState();
        return rolPage.stream().map(rol -> rolUtils.mapToRolDTO(rol)).collect(Collectors.toList());
    }

    @Override
    public RolDTO save(RolDTO rolDto) {
        Rol rol = rolUtils.mapToRol(rolDto);
        if (rol == null) {
            return null;
        }
        Rol savedRol = rolRepository.save(rol);
        return rolUtils.mapToRolDTO(savedRol);
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
