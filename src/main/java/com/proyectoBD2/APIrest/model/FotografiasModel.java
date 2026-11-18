package com.proyectoBD2.APIrest.model;
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
public class FotografiasModel{
    private String nombreArchivo;
    private String descripcionFotografia;
    private String ubicacion;
}