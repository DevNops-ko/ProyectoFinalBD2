package com.proyectoBD2.APIrest.model;

import java.util.List;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class HallazgosModel{
    private String componente;
    private String descripcion;
    private String prioridadAtencion;
    private String observacionesHa;
    private String condicion;
    private String accionesRecomendadas;
    private ObjectId usuarioId;
    private List<EvidenciasModel> evidencias;
}