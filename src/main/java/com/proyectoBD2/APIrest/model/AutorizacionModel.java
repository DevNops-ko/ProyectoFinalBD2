package com.proyectoBD2.APIrest.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutorizacionModel {
    private String decision; 
    private Date fecha;
    private String comentarios;
}
