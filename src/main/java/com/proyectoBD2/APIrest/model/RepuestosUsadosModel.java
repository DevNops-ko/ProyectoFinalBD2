package com.proyectoBD2.APIrest.model;

import org.bson.types.ObjectId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepuestosUsadosModel {
    private ObjectId repuestoId; 
    private Integer cantidadUsada;
    private Double precioAplicado;
}
