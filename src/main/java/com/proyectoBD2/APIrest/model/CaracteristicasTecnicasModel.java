package com.proyectoBD2.APIrest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
