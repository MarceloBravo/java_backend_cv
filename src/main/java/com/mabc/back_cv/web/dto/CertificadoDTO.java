package com.mabc.back_cv.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CertificadoDTO {

    private Long id;

    private String name;

    private String image;

    private String url;

    private String mouse_move_title;

    private String mouse_move_description;

}
