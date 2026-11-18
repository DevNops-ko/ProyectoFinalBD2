package com.proyectoBD2.APIrest.model.repuestos;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document 
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class RespuestosModel {
    @Id
    private ObjectId id;
    private String nombre;
    private String especificaciones;
    private Integer precio;

}
