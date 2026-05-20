package com.mabc.back_cv.web.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "permisos_pantallas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PermisoPantalla {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;
    
    @ManyToOne
    @JoinColumn(name = "pantalla_id", nullable = false)
    private Pantalla pantalla;
    
    /**
     * Determina si el rol tiene permiso para utilizar la acción de crear asociada a la pantalla
     */
    @Column(name = "accion_crear", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean accion_crear;
    
    /**
     * Determina si el rol tiene permiso para utilizar la acción de editar asociada a la pantalla
     */
    @Column(name = "accion_editar", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean accion_editar;
    
    /**
     * Determina si el rol tiene permiso para utilizar la acción de eliminar asociada a la pantalla
     */
    @Column(name = "accion_eliminar", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean accion_eliminar;
    
    /**
     * Determina si el rol tiene permiso para utilizar la acción de consultar asociada a la pantalla
     */
    @Column(name = "accion_consultar", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean accion_consultar;

    /**
     * Determina si el rol tiene permiso para utilizar la acción de listar asociada a la pantalla
     */
    @Column(name = "listar", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean listar;

    /**
     * Determina si el permiso está activo
     */
    @Column(name = "activo", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean activo;
    
    @Override
    public String toString() {
        return "PermisoPantalla{" +
                "id=" + id +
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
        PermisoPantalla that = (PermisoPantalla) o;
        return id != null && id.equals(that.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}