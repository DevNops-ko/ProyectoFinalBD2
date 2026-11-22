package com.proyectoBD2.APIrest.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.proyectoBD2.APIrest.model.SolicitudesGarantiaModel;

public interface ISolicitudesGarantiaService {
    SolicitudesGarantiaModel crearSolicitudGarantia(SolicitudesGarantiaModel solicitudGarantia);
    List<SolicitudesGarantiaModel> listarSolicitudesGarantia();
    SolicitudesGarantiaModel buscarSolicitudesGarantiaPorId(ObjectId idSolicitudGarantia);
}
