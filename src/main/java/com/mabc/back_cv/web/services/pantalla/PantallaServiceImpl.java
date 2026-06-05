package com.mabc.back_cv.web.services.pantalla;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.mabc.back_cv.web.dto.PantallaDTO;
import com.mabc.back_cv.web.entities.Pantalla;
import com.mabc.back_cv.web.repositories.PantallaRepository;

@Service
public class PantallaServiceImpl implements PantallaService {

    private final PantallaRepository pantallaRepository;
    private final ModelMapper modelMapper;

    public PantallaServiceImpl(PantallaRepository pantallaRepository, ModelMapper modelMapper) {
        this.pantallaRepository = pantallaRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PantallaDTO> getAllPantallas() {
        List<Pantalla> pantallas = pantallaRepository.findAll();
        List<PantallaDTO> pantallaDTOs = pantallas
                .stream()
                .map(p -> modelMapper.map(p, PantallaDTO.class))
                .collect(Collectors.toList());
        return pantallaDTOs;
    }

    @Override
    public Page<PantallaDTO> searchPantallas(String terminoBuscado, Boolean estado, Integer page, Integer size,
            String sortBy) {
        if (terminoBuscado == null || terminoBuscado.trim().isEmpty())
            return Page.empty();

        page = (page == null || page < 0) ? 0 : page;
        size = (size == null || size <= 0) ? 10 : size;

        if (sortBy == null || sortBy.trim().isEmpty())
            sortBy = "id";

        Pageable pageRequest = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Pantalla> pantallaPage = pantallaRepository.searchByNombrePantallaUrlArchivoOrMenu(terminoBuscado, estado,
                pageRequest);
        Page<PantallaDTO> pantallaDTOPage = pantallaPage.map(p -> modelMapper.map(p, PantallaDTO.class));
        return pantallaDTOPage;
    }

    @Override
    public PantallaDTO getPantallaById(Long id) {
        Pantalla pantalla = pantallaRepository.findById(id).orElse(null);
        if (pantalla == null)
            return null;

        PantallaDTO pantallaDTO = modelMapper.map(pantalla, PantallaDTO.class);
        return pantallaDTO;
    }

    @Override
    public PantallaDTO savePantalla(PantallaDTO pantallaDTO) {
        Pantalla pantalla = modelMapper.map(pantallaDTO, Pantalla.class);

        Pantalla pantallaGuardada = pantallaRepository.save(pantalla);
        PantallaDTO pantallaDTOGuardada = modelMapper.map(pantallaGuardada, PantallaDTO.class);
        return pantallaDTOGuardada;
    }

    @Override
    public void deletePantalla(Long id) {
        if (id == null || !pantallaRepository.existsById(id)) {
            throw new IllegalArgumentException("No se encontró una pantalla a eliminar.");
        }
        pantallaRepository.deleteById(id);
    }

}