package com.proyectoBD2.APIrest.service;

import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.proyectoBD2.APIrest.model.OrdenServicioModel;

public interface IOrdenServicioService {
    OrdenServicioModel crearOrdenServicio(OrdenServicioModel ordenServicio);
    List<OrdenServicioModel> listarOrdenesServicio();
    OrdenServicioModel buscarOrdenesServicioPorId(ObjectId idOrdenServicio);
    OrdenServicioModel actualizarOrdenServicio(ObjectId idOrdenServicio, OrdenServicioModel ordenServicio);
    void eliminarOrdenServicio(ObjectId idOrdenServicio);
    List<OrdenServicioModel> listarOrdenesPorBicicleta(ObjectId bicicletaId);
    List<OrdenServicioModel> listarOrdenesPorCliente(ObjectId usuarioId);
    Document calcularValorEstimado(ObjectId ordenServicioId);
}
