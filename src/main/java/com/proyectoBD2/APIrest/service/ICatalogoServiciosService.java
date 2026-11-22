package com.proyectoBD2.APIrest.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.proyectoBD2.APIrest.model.CatalogoServiciosModel;

public interface ICatalogoServiciosService {
    CatalogoServiciosModel crearServicio(CatalogoServiciosModel servicio);
    List<CatalogoServiciosModel> listarServicios();
    CatalogoServiciosModel buscarServiciosPorId(ObjectId idServicio);
}
