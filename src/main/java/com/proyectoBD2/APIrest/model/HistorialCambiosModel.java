package com.proyectoBD2.APIrest.model;

import java.util.Date;

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
public class HistorialCambiosModel{
    @Id
    private Date fecha;
    private ObjectId usuarioId;
    private String observacionesHC;
    private String estado;
    
} 