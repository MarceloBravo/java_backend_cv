package com.mabc.back_cv.web.entities;

import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "descripcion_portafolio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DescripcionPortafolio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "parrafo", nullable = false, columnDefinition = "TEXT")
    private String parrafo;

    @Column(name = "posicion", nullable = false)
    private Integer posicion;
    
    @ManyToOne
    @JoinColumn(name = "portafolio_id", nullable = false)
    private Portafolio portafolio;
    
    @Override
    public String toString() {
        return "DescripcionPortafolio{" +
                "id=" + id +
                ", parrafo='" + parrafo + '\'' +
                ", posicion=" + posicion +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DescripcionPortafolio that = (DescripcionPortafolio) o;
        return id != null && id.equals(that.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}