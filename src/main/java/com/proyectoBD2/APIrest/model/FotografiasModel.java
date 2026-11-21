package com.proyectoBD2.APIrest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class FotografiasModel{
    private String nombreArchivo;
    private String descripcionFotografia;
    private String ubicacion;
}