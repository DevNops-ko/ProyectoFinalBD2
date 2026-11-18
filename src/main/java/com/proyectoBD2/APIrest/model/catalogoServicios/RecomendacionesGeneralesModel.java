package com.proyectoBD2.APIrest.model.catalogoServicios;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document 
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class RecomendacionesGeneralesModel {
    private String recomendacion;
}
