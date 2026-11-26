package com.proyectoBD2.APIrest.service;

import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

public interface IEstadisticasService {
    List<Document> serviciosMasSolicitados(ObjectId coordinadorId);
    List<Document> ordenesPorEstado(ObjectId coordinadorId);
    Document tiemposAtencion(ObjectId coordinadorId);
    Document valoracionesRecibidas(ObjectId coordinadorId);
}
