package com.mabc.back_cv.web.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.mabc.back_cv.web.dto.UserPresentationDTO;
import com.mabc.back_cv.web.services.userPresentation.UserPresentationService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/userPresentation")
public class UserPresentationController{

    @Autowired
    private UserPresentationService userPresentationService;

    @GetMapping("/all")
    public ResponseEntity<Page<UserPresentationDTO>> getAll(
        @RequestParam(required = false) String searchText,
        @RequestParam(required = false) Long userId,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size
    ){
        try{
            Page<UserPresentationDTO> resultado = userPresentationService.getAll(searchText, userId, page, size);
            return ResponseEntity.ok(resultado);
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserPresentationDTO> getById(@PathVariable Long id){
        try{
            UserPresentationDTO userPresentationDTO = userPresentationService.findById(id);
            if(userPresentationDTO == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(userPresentationDTO);
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<UserPresentationDTO> save(@Valid @RequestBody UserPresentationDTO userPresentationRequestDTO){
        try{
            UserPresentationDTO response = userPresentationService.save(userPresentationRequestDTO);
            return ResponseEntity.ok(response);
        }catch(Exception e){
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        try{
            userPresentationService.delete(id);
            return ResponseEntity.ok("UserPresentation eliminada correctamente");
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Error al eliminar la UserPresentation");
        }
    }

}