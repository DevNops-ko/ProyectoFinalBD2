package com.proyectoBD2.APIrest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor
public class ValoracionModel {
    private String comentarios;
    private Integer calificacion;
    private String sugerencias;

}
