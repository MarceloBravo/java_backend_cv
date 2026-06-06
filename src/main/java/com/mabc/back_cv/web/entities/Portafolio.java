package com.mabc.back_cv.web.entities;

import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "portafolio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Portafolio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "titulo", nullable = false, length = 100)
    private String title;

    @Column(name = "imagen", nullable = true, length = 500)
    private String image;

    @Column(name = "video", nullable = true, length = 500)
    private String video;
    
    @Column(name = "mouse_move_title", nullable = true, length = 255)
    private String mouseMoveTitle;

    @Column(name = "mouse_move_description", nullable = true, length = 255)
    private String mouseMoveDescription;

    @Column(name = "parrafo_inferior", nullable = true, length = 500)
    private String paragraph;
    
    @Column(name = "link", nullable = true, length = 255)
    private String link;

    @OneToMany(mappedBy = "portafolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DescripcionPortafolio> description = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Override
    public String toString() {
        return "Portafolio{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", video='" + video + '\'' +
                ", mouseMoveTitle='" + mouseMoveTitle + '\'' +
                ", mouseMoveDescription='" + mouseMoveDescription + '\'' +
                ", paragraph='" + paragraph + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Portafolio that = (Portafolio) o;
        return id != null && id.equals(that.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}