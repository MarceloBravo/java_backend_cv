package com.mabc.back_cv.web.entities;

import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.Table;
import com.mabc.back_cv.web.enums.TipoTecnologiaEnum;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tecnologias")
public class Tecnologia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nombre")
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoTecnologiaEnum type;
    
    @Column(name = "ruta_imagen", nullable = true, length = 500)
    private String pathImage;
    
    @Column(name = "logo_svg", nullable = true, columnDefinition = "TEXT")
    private String logoSvg;

    @Override
    public String toString() {
        return "Tecnologia{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", pathImage='" + pathImage + '\'' +
                ", logoSvg='" + logoSvg + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tecnologia that = (Tecnologia) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
