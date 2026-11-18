package com.proyectoBD2.APIrest.model;

import java.util.Date;
import java.util.List;

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
public class HallazgosModel{
    @Id
    private String componente;
    private String descripcion;
    private String prioridadAtencion;
    private String observacionesHa;
    private String condicion;
    private String accionesRecomendadas;
    private ObjectId tecnicoId;
    private List<EvidenciasModel> evidencias;
}