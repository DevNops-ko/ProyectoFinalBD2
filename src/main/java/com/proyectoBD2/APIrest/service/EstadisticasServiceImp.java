package com.proyectoBD2.APIrest.service;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.proyectoBD2.APIrest.model.OrdenServicioModel;
import com.proyectoBD2.APIrest.model.TrabajosModel;
import com.proyectoBD2.APIrest.util.Constantes;

@Service
public class EstadisticasServiceImp implements IEstadisticasService {

    @Autowired MongoTemplate mongoTemplate;
    @Autowired IUsuarioService usuarioService;

    private void validarCoordinador(ObjectId coordinadorId) {
        usuarioService.validarRol(coordinadorId, Constantes.ROL_COORDINADOR);
    }

    private String coleccion(Class<?> modelo) {
        return mongoTemplate.getCollectionName(modelo);
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    @Override
    public List<Document> serviciosMasSolicitados(ObjectId coordinadorId) {
        if (coordinadorId != null) {
            validarCoordinador(coordinadorId);
        }

        Aggregation agg = newAggregation(
                match(Criteria.where("servicio.servicioId").ne(null)),
                group("servicio.servicioId")
                        .first("servicio.nombre").as("nombre")
                        .count().as("totalSolicitudes"),
                sort(Sort.Direction.DESC, "totalSolicitudes"));

        List<Document> docs = mongoTemplate
                .aggregate(agg, coleccion(TrabajosModel.class), Document.class)
                .getMappedResults();

        List<Document> resultado = new ArrayList<>();
        for (Document d : docs) {
            Document fila = new Document();
            fila.put("servicioId", String.valueOf(d.get("_id")));
            fila.put("nombre", d.get("nombre"));
            fila.put("totalSolicitudes", ((Number) d.get("totalSolicitudes")).intValue());
            resultado.add(fila);
        }
        return resultado;
    }

    // 2. Ordenes agrupadas por estado.
    @Override
    public List<Document> ordenesPorEstado(ObjectId coordinadorId) {
        if (coordinadorId != null) {
            validarCoordinador(coordinadorId);
        }

        Aggregation agg = newAggregation(
                group("estado").count().as("total"),
                sort(Sort.Direction.DESC, "total"));

        List<Document> docs = mongoTemplate
                .aggregate(agg, coleccion(OrdenServicioModel.class), Document.class)
                .getMappedResults();

        List<Document> resultado = new ArrayList<>();
        for (Document d : docs) {
            Document fila = new Document();
            fila.put("estado", d.get("_id"));
            fila.put("total", ((Number) d.get("total")).intValue());
            resultado.add(fila);
        }
        return resultado;
    }

    @Override
    public Document tiemposAtencion(ObjectId coordinadorId) {
        if (coordinadorId != null) {
            validarCoordinador(coordinadorId);
        }

        String ordenes = coleccion(OrdenServicioModel.class);

        long totalEntregadas = mongoTemplate.count(
                new Query(Criteria.where("estado").is(Constantes.ESTADO_ENTREGADO)), ordenes);

        Aggregation agg = newAggregation(
                match(new Criteria().andOperator(
                        Criteria.where("estado").is(Constantes.ESTADO_ENTREGADO),
                        Criteria.where("entrega.fecha").ne(null),
                        Criteria.where("fechaRecepcion").ne(null))),
                project("fechaRecepcion", "fechaEstimadaEntrega")
                        .and("entrega.fecha").as("fechaEntrega"),
                project()
                        .and(ArithmeticOperators.Divide.valueOf(
                                ArithmeticOperators.Subtract.valueOf("fechaEntrega").subtract("fechaRecepcion"))
                                .divideBy(3_600_000)).as("horas")
                        .and(ConditionalOperators.when(
                                ComparisonOperators.valueOf("fechaEntrega").lessThanEqualTo("fechaEstimadaEntrega"))
                                .then(1).otherwise(0)).as("aTiempo"),
                group()
                        .avg("horas").as("promedio")
                        .min("horas").as("minimo")
                        .max("horas").as("maximo")
                        .sum("aTiempo").as("entregadasATiempo")
                        .count().as("ordenesMedidas"));

        List<Document> r = mongoTemplate.aggregate(agg, ordenes, Document.class).getMappedResults();

        Document resultado = new Document();
        int medidas = r.isEmpty() ? 0 : ((Number) r.get(0).get("ordenesMedidas")).intValue();
        resultado.put("ordenesEntregadas", totalEntregadas);
        resultado.put("ordenesMedidas", medidas);
        resultado.put("ordenesSinDatos", totalEntregadas - medidas);

        if (r.isEmpty()) {
            resultado.put("entregadasATiempo", 0);
            resultado.put("promedioHoras", null);
            resultado.put("minimoHoras", null);
            resultado.put("maximoHoras", null);
            return resultado;
        }

        Document d = r.get(0);
        resultado.put("entregadasATiempo", ((Number) d.get("entregadasATiempo")).intValue());
        resultado.put("promedioHoras", redondear(((Number) d.get("promedio")).doubleValue()));
        resultado.put("minimoHoras", redondear(((Number) d.get("minimo")).doubleValue()));
        resultado.put("maximoHoras", redondear(((Number) d.get("maximo")).doubleValue()));
        return resultado;
    }

    @Override
    public Document valoracionesRecibidas(ObjectId coordinadorId) {
        if (coordinadorId != null) {
            validarCoordinador(coordinadorId);
        }

        String ordenes = coleccion(OrdenServicioModel.class);
        Criteria conValoracion = Criteria.where("valoracion.calificacion").ne(null);

        Aggregation resumen = newAggregation(
                match(conValoracion),
                group().avg("valoracion.calificacion").as("promedio")
                        .min("valoracion.calificacion").as("minima")
                        .max("valoracion.calificacion").as("maxima")
                        .count().as("total"));
        List<Document> r = mongoTemplate.aggregate(resumen, ordenes, Document.class).getMappedResults();

        Aggregation porNota = newAggregation(
                match(conValoracion),
                group("valoracion.calificacion").count().as("cantidad"));
        List<Document> n = mongoTemplate.aggregate(porNota, ordenes, Document.class).getMappedResults();

        Map<Integer, Integer> distribucion = new TreeMap<>(); // TreeMap las deja ordenadas por nota
        for (Document d : n) {
            distribucion.put(((Number) d.get("_id")).intValue(), ((Number) d.get("cantidad")).intValue());
        }

        Document resultado = new Document();
        if (r.isEmpty()) {
            resultado.put("totalValoraciones", 0);
            resultado.put("promedio", null);
        } else {
            Document d = r.get(0);
            resultado.put("totalValoraciones", ((Number) d.get("total")).intValue());
            resultado.put("promedio", redondear(((Number) d.get("promedio")).doubleValue()));
            resultado.put("minima", d.get("minima"));
            resultado.put("maxima", d.get("maxima"));
        }
        resultado.put("distribucionPorCalificacion", distribucion);
        return resultado;
    }
}