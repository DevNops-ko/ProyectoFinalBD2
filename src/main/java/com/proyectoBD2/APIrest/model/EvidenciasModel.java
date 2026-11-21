package com.proyectoBD2.APIrest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class EvidenciasModel{
    private String nombreArchivo;
    private String descripcion;
    private String ubicacion;
    
} 