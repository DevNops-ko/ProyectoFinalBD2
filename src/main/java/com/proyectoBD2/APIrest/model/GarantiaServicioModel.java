package com.proyectoBD2.APIrest.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GarantiaServicioModel {
    private Integer duracion;
    private Date fechaInicioGS;
    private String condiciones;
    private String exclusiones;
}
