package com.proyectoBD2.APIrest.model;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data 
@AllArgsConstructor 
@NoArgsConstructor
public class ValoracionModel {
    private String comentarios;
    private Integer calificacion;
    private String sugerencias;

}
