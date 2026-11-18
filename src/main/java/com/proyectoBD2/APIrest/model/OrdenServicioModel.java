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
    private String estado;
    private String problemaReportado;
    private String condicionesObservadas;
    private List<AccesoriosEntregadosModel> accesoriosEntregados;
    private List<FotografiasModel> fotografias;
    private List<DiagnosticoModel> diagnósticos;
    private List<HistorialCambiosModel> historialCambios;
    private List<ValoracionModel> valoracion;
    private List<RevisionEntregaModel> revisionesEntrega;
    private List<EntregaModel> entrega;
    
}
