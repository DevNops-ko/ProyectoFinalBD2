package com.proyectoBD2.APIrest.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("usuarios") 
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class UsuarioModel {
    @Id 
    private ObjectId id;
    private String nombreCompleto;
    private String documentoIdentificacion;
    private String correo;
    private String numTelefono;
    private String rol;
    private String direccion;
}
