package com.proyectoBD2.APIrest.model;
import java.util.List;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class CitaClienteModel {
    private String problemasObservados;
    private List<ObjectId> serviciosInteres;
}
