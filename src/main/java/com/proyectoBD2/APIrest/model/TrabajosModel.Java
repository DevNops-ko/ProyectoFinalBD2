package com.proyectoBD2.APIrest.model;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("trabajos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrabajosModel {
    @Id
    private ObjectId id;
    private ObjectId ordenServicioId;
    private ObjectId usuarioId;
    private String descripcionIntervencion;
    private String componenteInvolucrado;
    private Double costoManoObra;
    private String observacionesPI;
    private ServicioModel servicio;
    private List<RepuestosPrevistosModel> repuestosPrevistos;
    private AutorizacionModel autorizacion;
    private EjecucionModel ejecucion;
    private GarantiaServicioModel garantiaServicio;
}