package com.mabc.back_cv.web.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "educacion")
public class Educacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "institucion", nullable = false, length = 100)
    private String institution;
    
    @Column(name = "titulo", nullable = false, length = 100)
    private String title;

    @Column(name = "titulo_corto", nullable = false, length = 50)
    private String shortTitle;

    @Column(name = "nombre", nullable = false, length = 100)
    private String name;

    @Column(name = "descripcion", length = 500)
    private String description;
    
    @Column(name = "anio_desde", nullable = false)
    private Integer yearFrom;
    
    @Column(name = "anio_hasta", nullable = false)
    private Integer yearTo;

    @Column(name = "duracion_semestres", nullable = false)
    private Integer duration;
    
    @Column(name = "logo", length = 255)
    private String image;
    
    @Column(name = "url", length = 255)
    private String url;

    @Column(name = "estilos", length = 255)
    private String styles;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @Override
    public String toString() {
        return "Educacion{" +
                "id=" + id +
                ", institution='" + institution + '\'' +
                ", title='" + title + '\'' +
                ", shortTitle='" + shortTitle + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", yearFrom=" + yearFrom +
                ", yearTo=" + yearTo +
                ", duration=" + duration +
                ", image='" + image + '\'' +
                ", url='" + url + '\'' +
                ", styles='" + styles + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Educacion educacion = (Educacion) o;
        return id != null && id.equals(educacion.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
