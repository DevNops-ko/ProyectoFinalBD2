package com.proyectoBD2.APIrest.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.proyectoBD2.APIrest.model.RepuestosModel;

public interface IRepuestosService {
    RepuestosModel crearRepuesto(RepuestosModel repuesto);
    List<RepuestosModel> listarRepuestos();
    RepuestosModel buscarRepuestosPorId(ObjectId idRepuesto);
}