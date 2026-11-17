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
public class UsuarioModel {
    @Id 
    private ObjectId id;
    private String nombreCompleto;
    private Integer documentoIdentificacion;
    private String correo;
    private Integer numTelefono;
    private String rol;
    private String direccion;
}
