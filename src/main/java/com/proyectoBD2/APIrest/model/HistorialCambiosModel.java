package com.proyectoBD2.APIrest.model;

import java.util.Date;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class HistorialCambiosModel{
    private Date fecha;
    private ObjectId usuarioId;
    private String observacionesHC;
    private String estado;
    
} 