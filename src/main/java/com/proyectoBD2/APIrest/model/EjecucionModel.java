package com.proyectoBD2.APIrest.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EjecucionModel {
    private Date fechaInicio;
    private Date fechaFinalizacion;
    private String estado;
    private String descripcionRealizacion;
    private List<RepuestosUsadosModel> repuestosUsados;
}
