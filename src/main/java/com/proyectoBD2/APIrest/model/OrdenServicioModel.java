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
public class OrdenServicioModel {
    @Id 
    private ObjectId id;
    private ObjectId usuarioId;
    private ObjectId citaId;
    private ObjectId bicicletaId;
    private Date fechaRecepcion;
    private Date fechaEstimadaEntrega;
    private List<TecnicosAsignadosModel> tecnicosAsignados;
    private List<AccesoriosEntregadosModel> accesoriosEntregados;
    
}
