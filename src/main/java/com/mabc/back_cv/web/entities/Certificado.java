package com.mabc.back_cv.web.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "certificados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Certificado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String name;

    @Column(name = "url_imagen", nullable = false)
    private String image;

    @Column(name = "url_certificado", nullable = false)
    private String url;

    @Column(name = "texto_mouse", nullable = false)
    private String mouse_move_title;

    @Column(name = "texto_mouse_descripcion", nullable = false)
    private String mouse_move_description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "curso_id", nullable = true)
    private Curso curso;

    @Override
    public String toString() {
        return "Certificado{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", url='" + url + '\'' +
                ", mouse_move_title='" + mouse_move_title + '\'' +
                ", mouse_move_description='" + mouse_move_description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Certificado that = (Certificado) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}