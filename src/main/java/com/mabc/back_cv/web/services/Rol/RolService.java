package com.mabc.back_cv.web.services.Rol;

import java.util.List;

import org.springframework.data.domain.Page;

import com.mabc.back_cv.web.dto.RolDTO;

public interface RolService {

    RolDTO findById(Long id);

    Page<RolDTO> searchBy(String nombre, Boolean activo, int page, int rows);

    List<RolDTO> getAll();

    Page<RolDTO> getAll(int page, int rows);

    List<RolDTO> getActiveRoles();

    RolDTO save(RolDTO rol);

    void delete(Long id);
}
