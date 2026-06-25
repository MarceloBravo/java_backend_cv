package com.mabc.back_cv.portafolio;

import com.mabc.back_cv.web.dto.PortafolioDTO;
import com.mabc.back_cv.web.entities.Portafolio;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.services.portafolio.PortafolioMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas unitarias de PortafolioMapper")
class PortafolioMapperTest {

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
    // convertToDTO
    // =========================================================================
    @Nested
    @DisplayName("convertToDTO")
    class ConvertToDTOTests {

        @Test
        @DisplayName("Éxito: mapea correctamente todos los campos de Portafolio a PortafolioDTO")
        void exitoMapeoCompleto() {
            PortafolioDTO result = PortafolioMapper.convertToDTO(portafolioBase);

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

            PortafolioDTO result = PortafolioMapper.convertToDTO(portafolioMinimo);

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
            PortafolioDTO result = PortafolioMapper.convertToDTO(null);

            assertNull(result);
        }

        @Test
        @DisplayName("Éxito: portafolio sin ID produce DTO con ID null")
        void portafolioSinIdProduceDTOConIdNull() {
            portafolioBase.setId(null);

            PortafolioDTO result = PortafolioMapper.convertToDTO(portafolioBase);

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
            Portafolio result = PortafolioMapper.convertToEntity(dtoBases);

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

            Portafolio result = PortafolioMapper.convertToEntity(dtoBases);

            assertNotNull(result);
            assertNull(result.getId());
        }

        @Test
        @DisplayName("Éxito: mapea DTO con campos opcionales nulos sin lanzar excepción")
        void exitoConCamposOpcionalesNulos() {
            PortafolioDTO dtoMinimo = new PortafolioDTO();
            dtoMinimo.setTitle("Solo título");
            dtoMinimo.setUser(userBase);

            Portafolio result = PortafolioMapper.convertToEntity(dtoMinimo);

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
            Portafolio result = PortafolioMapper.convertToEntity(null);

            assertNull(result);
        }

        @Test
        @DisplayName("Parámetros inválidos: campos de texto vacíos se mapean tal cual a la entidad")
        void camposVaciosSeMapeam() {
            dtoBases.setTitle("");
            dtoBases.setImage("");
            dtoBases.setLink("");

            Portafolio result = PortafolioMapper.convertToEntity(dtoBases);

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
            PortafolioDTO dto = PortafolioMapper.convertToDTO(portafolioBase);
            Portafolio reconstruido = PortafolioMapper.convertToEntity(dto);

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
