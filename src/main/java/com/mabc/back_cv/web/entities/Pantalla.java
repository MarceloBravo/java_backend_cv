package com.mabc.back_cv.web.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "pantallas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pantalla {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_pantalla", nullable = false, length = 100)
    private String nombre_pantalla;

    @Column(name = "url_archivo", nullable = false, length = 255)
    private String url_archivo;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    /**
     * Determina si la pantalla tendrá la acción de crear registros
     */
    @Column(name = "accion_crear", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean accion_crear;
    
    /**
     * Determina si la pantalla tendrá la acción de editar registros
     */
    @Column(name = "accion_editar", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean accion_editar;
    
    /**
     * Determina si la pantalla tendrá la acción de eliminar registros
     */
    @Column(name = "accion_eliminar", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean accion_eliminar;
    
    /**
     * Determina si la pantalla tendrá la acción de consultar registros
     */
    @Column(name = "accion_consultar", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean accion_consultar;

    /**
     * Determina si la pantalla tendrá la acción de listar registros
     */
    @Column(name = "listar", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean listar;
    
    /**
     * Determina si la pantalla está activa
     */
    @Column(name = "activo", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean activo;

    @Override
    public String toString() {
        return "Pantalla{" +
                "id=" + id +
                ", nombre_pantalla='" + nombre_pantalla + '\'' +
                ", url_archivo='" + url_archivo + '\'' +
                ", accion_crear=" + accion_crear +
                ", accion_editar=" + accion_editar +
                ", accion_eliminar=" + accion_eliminar +
                ", accion_consultar=" + accion_consultar +
                ", listar=" + listar +
                ", activo=" + activo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pantalla that = (Pantalla) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
