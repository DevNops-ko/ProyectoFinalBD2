package com.proyectoBD2.APIrest.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.proyectoBD2.APIrest.model.CitasModel;

public interface ICitasService {
    CitasModel crearCita(CitasModel cita);
    List<CitasModel> listarCitas();
    CitasModel buscarCitasPorId(ObjectId idCita);
}
