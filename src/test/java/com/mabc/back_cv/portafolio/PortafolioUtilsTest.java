package com.mabc.back_cv.portafolio;

import com.mabc.back_cv.web.dto.PortafolioDTO;
import com.mabc.back_cv.web.entities.Portafolio;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.services.portafolio.PortafolioUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas unitarias de PortafolioUtils")
class PortafolioUtilsTest {

    private User userBase;
    private Portafolio portafolioBase;
    private PortafolioDTO dtoBases;

    @BeforeEach
    void setUp() {
        userBase = new User();
        userBase.setId(1L);
        userBase.setNombre("Juan");
        userBase.setApellido("Pérez");
        userBase.setEmail("juan@example.com");
        userBase.setPassword("pass123");
        userBase.setActivo(true);

        portafolioBase = new Portafolio();
        portafolioBase.setId(10L);
        portafolioBase.setTitle("Mi Portafolio");
        portafolioBase.setImage("imagen.png");
        portafolioBase.setVideo("video.mp4");
        portafolioBase.setMouseMoveTitle("Hover título");
        portafolioBase.setMouseMoveDescription("Hover descripción");
        portafolioBase.setParagraph("Párrafo inferior");
        portafolioBase.setLink("https://ejemplo.com");
        portafolioBase.setUser(userBase);
        portafolioBase.setDescription(new ArrayList<>());

        dtoBases = new PortafolioDTO();
        dtoBases.setId(10L);
        dtoBases.setTitle("Mi Portafolio");
        dtoBases.setImage("imagen.png");
        dtoBases.setVideo("video.mp4");
        dtoBases.setMouseMoveTitle("Hover título");
        dtoBases.setMouseMoveDescription("Hover descripción");
        dtoBases.setParagraph("Párrafo inferior");
        dtoBases.setLink("https://ejemplo.com");
        dtoBases.setUser(userBase);
    }

    // =========================================================================
    // createPageable
    // =========================================================================
    @Nested
    @DisplayName("createPageable")
    class CreatePageableTests {

        @Test
        @DisplayName("Éxito: crea Pageable con parámetros válidos")
        void exitoConParametrosValidos() {
            Pageable pageable = PortafolioUtils.createPageable(2, 5);

            assertNotNull(pageable);
            assertEquals(2, pageable.getPageNumber());
            assertEquals(5, pageable.getPageSize());
        }

        @Test
        @DisplayName("Éxito: crea Pageable con page=0 y size=1 (límites mínimos válidos)")
        void exitoConLimitesMinimosValidos() {
            Pageable pageable = PortafolioUtils.createPageable(0, 1);

            assertEquals(0, pageable.getPageNumber());
            assertEquals(1, pageable.getPageSize());
        }

        @Test
        @DisplayName("Parámetro nulo: page null usa valor por defecto 0")
        void pageNulaUsaDefecto() {
            Pageable pageable = PortafolioUtils.createPageable(null, 10);

            assertEquals(0, pageable.getPageNumber());
            assertEquals(10, pageable.getPageSize());
        }

        @Test
        @DisplayName("Parámetro nulo: size null usa valor por defecto 10")
        void sizeNuloUsaDefecto() {
            Pageable pageable = PortafolioUtils.createPageable(0, null);

            assertEquals(0, pageable.getPageNumber());
            assertEquals(10, pageable.getPageSize());
        }

        @Test
        @DisplayName("Parámetros nulos: ambos null usan valores por defecto (0 y 10)")
        void ambosNulosUsanDefecto() {
            Pageable pageable = PortafolioUtils.createPageable(null, null);

            assertEquals(0, pageable.getPageNumber());
            assertEquals(10, pageable.getPageSize());
        }

        @Test
        @DisplayName("Parámetro fuera de rango: page negativo se corrige a 0")
        void pageNegativaSeCorrigeACero() {
            Pageable pageable = PortafolioUtils.createPageable(-5, 10);

            assertEquals(0, pageable.getPageNumber());
        }

        @Test
        @DisplayName("Parámetro fuera de rango: size=0 se corrige a 10")
        void sizeCeroSeCorrigeADiez() {
            Pageable pageable = PortafolioUtils.createPageable(0, 0);

            assertEquals(10, pageable.getPageSize());
        }

        @Test
        @DisplayName("Parámetro fuera de rango: size negativo se corrige a 10")
        void sizeNegativoSeCorrigeADiez() {
            Pageable pageable = PortafolioUtils.createPageable(0, -1);

            assertEquals(10, pageable.getPageSize());
        }
    }

    // =========================================================================
    // convertToDTO
    // =========================================================================
    @Nested
    @DisplayName("convertToDTO")
    class ConvertToDTOTests {

        @Test
        @DisplayName("Éxito: mapea correctamente todos los campos de Portafolio a PortafolioDTO")
        void exitoMapeoCompleto() {
            PortafolioDTO result = PortafolioUtils.convertToDTO(portafolioBase);

            assertNotNull(result);
            assertEquals(10L, result.getId());
            assertEquals("Mi Portafolio", result.getTitle());
            assertEquals("imagen.png", result.getImage());
            assertEquals("video.mp4", result.getVideo());
            assertEquals("Hover título", result.getMouseMoveTitle());
            assertEquals("Hover descripción", result.getMouseMoveDescription());
            assertEquals("Párrafo inferior", result.getParagraph());
            assertEquals("https://ejemplo.com", result.getLink());
            assertEquals(userBase, result.getUser());
        }

        @Test
        @DisplayName("Éxito: mapea portafolio con campos opcionales nulos sin lanzar excepción")
        void exitoConCamposOpcionalesNulos() {
            Portafolio portafolioMinimo = new Portafolio();
            portafolioMinimo.setId(1L);
            portafolioMinimo.setTitle("Solo título");
            portafolioMinimo.setUser(userBase);

            PortafolioDTO result = PortafolioUtils.convertToDTO(portafolioMinimo);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("Solo título", result.getTitle());
            assertNull(result.getImage());
            assertNull(result.getVideo());
            assertNull(result.getMouseMoveTitle());
            assertNull(result.getMouseMoveDescription());
            assertNull(result.getParagraph());
            assertNull(result.getLink());
        }

        @Test
        @DisplayName("Parámetro nulo: retorna null cuando la entidad es null")
        void entidadNulaRetornaNull() {
            PortafolioDTO result = PortafolioUtils.convertToDTO(null);

            assertNull(result);
        }

        @Test
        @DisplayName("Éxito: portafolio sin ID produce DTO con ID null")
        void portafolioSinIdProduceDTOConIdNull() {
            portafolioBase.setId(null);

            PortafolioDTO result = PortafolioUtils.convertToDTO(portafolioBase);

            assertNotNull(result);
            assertNull(result.getId());
        }
    }

    // =========================================================================
    // convertToEntity
    // =========================================================================
    @Nested
    @DisplayName("convertToEntity")
    class ConvertToEntityTests {

        @Test
        @DisplayName("Éxito: mapea correctamente todos los campos de PortafolioDTO a Portafolio")
        void exitoMapeoCompleto() {
            Portafolio result = PortafolioUtils.convertToEntity(dtoBases);

            assertNotNull(result);
            assertEquals(10L, result.getId());
            assertEquals("Mi Portafolio", result.getTitle());
            assertEquals("imagen.png", result.getImage());
            assertEquals("video.mp4", result.getVideo());
            assertEquals("Hover título", result.getMouseMoveTitle());
            assertEquals("Hover descripción", result.getMouseMoveDescription());
            assertEquals("Párrafo inferior", result.getParagraph());
            assertEquals("https://ejemplo.com", result.getLink());
            assertEquals(userBase, result.getUser());
        }

        @Test
        @DisplayName("Éxito: DTO sin ID produce entidad sin ID (para creación nueva)")
        void dtoSinIdProduceEntidadSinId() {
            dtoBases.setId(null);

            Portafolio result = PortafolioUtils.convertToEntity(dtoBases);

            assertNotNull(result);
            assertNull(result.getId());
        }

        @Test
        @DisplayName("Éxito: mapea DTO con campos opcionales nulos sin lanzar excepción")
        void exitoConCamposOpcionalesNulos() {
            PortafolioDTO dtoMinimo = new PortafolioDTO();
            dtoMinimo.setTitle("Solo título");
            dtoMinimo.setUser(userBase);

            Portafolio result = PortafolioUtils.convertToEntity(dtoMinimo);

            assertNotNull(result);
            assertEquals("Solo título", result.getTitle());
            assertNull(result.getImage());
            assertNull(result.getVideo());
            assertNull(result.getMouseMoveTitle());
            assertNull(result.getMouseMoveDescription());
            assertNull(result.getParagraph());
            assertNull(result.getLink());
        }

        @Test
        @DisplayName("Parámetro nulo: retorna null cuando el DTO es null")
        void dtoNuloRetornaNull() {
            Portafolio result = PortafolioUtils.convertToEntity(null);

            assertNull(result);
        }

        @Test
        @DisplayName("Parámetros inválidos: campos de texto vacíos se mapean tal cual a la entidad")
        void camposVaciosSeMapeam() {
            dtoBases.setTitle("");
            dtoBases.setImage("");
            dtoBases.setLink("");

            Portafolio result = PortafolioUtils.convertToEntity(dtoBases);

            assertEquals("", result.getTitle());
            assertEquals("", result.getImage());
            assertEquals("", result.getLink());
        }
    }

    // =========================================================================
    // Ciclo de ida y vuelta Portafolio → DTO → Portafolio
    // =========================================================================
    @Nested
    @DisplayName("Ciclo de conversión Portafolio ↔ DTO")
    class CicloConversionTests {

        @Test
        @DisplayName("Entidad convertida a DTO y de vuelta a Entidad mantiene los mismos campos")
        void cicloPortafolioDTOPortafolio() {
            PortafolioDTO dto = PortafolioUtils.convertToDTO(portafolioBase);
            Portafolio reconstruido = PortafolioUtils.convertToEntity(dto);

            assertEquals(portafolioBase.getId(), reconstruido.getId());
            assertEquals(portafolioBase.getTitle(), reconstruido.getTitle());
            assertEquals(portafolioBase.getImage(), reconstruido.getImage());
            assertEquals(portafolioBase.getVideo(), reconstruido.getVideo());
            assertEquals(portafolioBase.getMouseMoveTitle(), reconstruido.getMouseMoveTitle());
            assertEquals(portafolioBase.getMouseMoveDescription(), reconstruido.getMouseMoveDescription());
            assertEquals(portafolioBase.getParagraph(), reconstruido.getParagraph());
            assertEquals(portafolioBase.getLink(), reconstruido.getLink());
            assertEquals(portafolioBase.getUser(), reconstruido.getUser());
        }
    }
}
