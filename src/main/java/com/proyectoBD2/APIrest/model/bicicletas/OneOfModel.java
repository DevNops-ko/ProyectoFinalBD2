package com.proyectoBD2.APIrest.model.bicicletas;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document 
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class OneOfModel {
    private String suspension;
    private String recorrido;
    private String bateria;
    private String motor;
}
