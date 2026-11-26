package com.proyectoBD2.APIrest.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.proyectoBD2.APIrest.model.CatalogoServiciosModel;
import com.proyectoBD2.APIrest.model.OrdenServicioModel;
import com.proyectoBD2.APIrest.model.TrabajosModel;
import com.proyectoBD2.APIrest.util.Constantes;

@Service
public class EstadisticasServiceImp implements IEstadisticasService {

    @Autowired MongoTemplate mongoTemplate;
    @Autowired IUsuarioService usuarioService;

    private List<Document> agregar(Class<?> modelo, List<Document> pipeline) {
        return mongoTemplate.getCollection(mongoTemplate.getCollectionName(modelo))
                .aggregate(pipeline).into(new ArrayList<>());
    }

    private void validarCoordinador(ObjectId coordinadorId) {
        usuarioService.validarRol(coordinadorId, Constantes.ROL_COORDINADOR);
    }

    @Override
    public List<Document> serviciosMasSolicitados(ObjectId coordinadorId) {
        validarCoordinador(coordinadorId);
        String catalogo = mongoTemplate.getCollectionName(CatalogoServiciosModel.class);
        return agregar(TrabajosModel.class, List.of(
            Document.parse("{ $match: { 'servicio.servicioId': { $exists: true } } }"),
            Document.parse("{ $group: { _id: '$servicio.servicioId', vecesSolicitado: { $sum: 1 },"
                + " nombreEnOrden: { $first: '$servicio.nombre' } } }"),
            Document.parse("{ $lookup: { from: '" + catalogo + "', localField: '_id', foreignField: '_id', as: 'catalogo' } }"),
            Document.parse("{ $project: { _id: 0, servicioId: { $toString: '$_id' },"
                + " nombre: { $ifNull: [ { $first: '$catalogo.nombre' }, '$nombreEnOrden' ] },"
                + " categoria: { $first: '$catalogo.categoria' }, vecesSolicitado: 1 } }"),
            Document.parse("{ $sort: { vecesSolicitado: -1 } }")));
    }

    @Override
    public List<Document> ordenesPorEstado(ObjectId coordinadorId) {
        validarCoordinador(coordinadorId);
        return agregar(OrdenServicioModel.class, List.of(
            Document.parse("{ $group: { _id: '$estado', total: { $sum: 1 } } }"),
            Document.parse("{ $project: { _id: 0, estado: '$_id', total: 1 } }"),
            Document.parse("{ $sort: { total: -1 } }")));
    }

    @Override
    public Document tiemposAtencion(ObjectId coordinadorId) {
        validarCoordinador(coordinadorId);
        List<Document> resultado = agregar(OrdenServicioModel.class, List.of(
            Document.parse("{ $match: { 'entrega.fecha': { $exists: true }, fechaRecepcion: { $exists: true } } }"),
            Document.parse("{ $project: {"
                + " dias: { $divide: [ { $subtract: ['$entrega.fecha', '$fechaRecepcion'] }, 86400000 ] },"
                + " aTiempo: { $cond: [ { $lte: ['$entrega.fecha', '$fechaEstimadaEntrega'] }, 1, 0 ] } } }"),
            Document.parse("{ $group: { _id: null, ordenesEntregadas: { $sum: 1 }, promedioDias: { $avg: '$dias' },"
                + " minimoDias: { $min: '$dias' }, maximoDias: { $max: '$dias' }, entregadasATiempo: { $sum: '$aTiempo' } } }"),
            Document.parse("{ $project: { _id: 0, ordenesEntregadas: 1, entregadasATiempo: 1,"
                + " promedioDias: { $round: ['$promedioDias', 2] }, minimoDias: { $round: ['$minimoDias', 2] },"
                + " maximoDias: { $round: ['$maximoDias', 2] } } }")));
        return resultado.isEmpty() ? new Document("ordenesEntregadas", 0) : resultado.get(0);
    }

    @Override
    public Document valoracionesRecibidas(ObjectId coordinadorId) {
        validarCoordinador(coordinadorId);
        List<Document> resultado = agregar(OrdenServicioModel.class, List.of(
            Document.parse("{ $match: { 'valoracion.calificacion': { $exists: true } } }"),
            Document.parse("{ $group: { _id: '$valoracion.calificacion', cantidad: { $sum: 1 } } }"),
            Document.parse("{ $sort: { _id: -1 } }"),
            Document.parse("{ $group: { _id: null, totalValoraciones: { $sum: '$cantidad' },"
                + " sumaCalificaciones: { $sum: { $multiply: ['$_id', '$cantidad'] } },"
                + " distribucion: { $push: { calificacion: '$_id', cantidad: '$cantidad' } } } }"),
            Document.parse("{ $project: { _id: 0, totalValoraciones: 1, distribucion: 1,"
                + " promedio: { $round: [ { $divide: ['$sumaCalificaciones', '$totalValoraciones'] }, 2 ] } } }")));
        return resultado.isEmpty() ? new Document("totalValoraciones", 0) : resultado.get(0);
    }
}
