package com.proyectoBD2.APIrest.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.proyectoBD2.APIrest.model.BicicletasModel;

public interface IBicicletasService {
    BicicletasModel crearBicicleta(BicicletasModel bicicleta);
    List<BicicletasModel> listarBicicletas();
    BicicletasModel buscarBicicletasPorId(ObjectId bicicletaId);
}
