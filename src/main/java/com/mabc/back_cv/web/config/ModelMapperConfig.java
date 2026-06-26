package com.mabc.back_cv.web.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Spring que define el bean de ModelMapper para la conversión
 * entre entidades y DTOs.
 */
@Configuration
public class ModelMapperConfig {

    /**
     * Crea y expone una instancia única de {@link ModelMapper} como bean de Spring.
     *
     * @return Instancia de ModelMapper.
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}