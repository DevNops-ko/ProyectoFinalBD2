package com.proyectoBD2.APIrest.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document 
@Data
@AllArgsConstructor 
@NoArgsConstructor 
public class CaracteristicasTecnicasModel {
    private Integer marco;
    private String material;
    private String rueda;
    private String tipoFrenos;
    private String transmision;
}
