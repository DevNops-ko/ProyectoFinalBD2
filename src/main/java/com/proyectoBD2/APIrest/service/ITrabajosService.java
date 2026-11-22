package com.proyectoBD2.APIrest.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.proyectoBD2.APIrest.model.TrabajosModel;

public interface ITrabajosService {
    TrabajosModel crearTrabajo(TrabajosModel trabajo);
    List<TrabajosModel> listarTrabajos();
    TrabajosModel buscarTrabajosPorId(ObjectId idTrabajo);
}
