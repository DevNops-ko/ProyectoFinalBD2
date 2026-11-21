package com.proyectoBD2.APIrest.model;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("ordenServicio") 
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
    private List<ObjectId> tecnicosAsignados;
    private String estado;
    private String problemaReportado;
    private String condicionesObservadas;
    private List<String> accesoriosEntregados;
    private List<FotografiasModel> fotografias;
    private DiagnosticoModel diagnostico;
    private List<HistorialCambiosModel> historialCambios;
    private ValoracionModel valoracion;
    private RevisionEntregaModel revisionesEntrega;
    private EntregaModel entrega;
    
}
