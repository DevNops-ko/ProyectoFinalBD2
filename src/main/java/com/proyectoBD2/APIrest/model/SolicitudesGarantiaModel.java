package com.proyectoBD2.APIrest.model;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("solicitudesGarantia")
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class SolicitudesGarantiaModel {
    @Id 
    private ObjectId id;
    private ObjectId trabajoId;
    private String descripcionSV;
    private Date fechaSV;
    private String estadoSV;
    private Boolean procede;
    private String solucionAplicada;
    private List<EvidenciasModel> evidencias;
}
