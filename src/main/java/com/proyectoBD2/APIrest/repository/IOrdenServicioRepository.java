package com.proyectoBD2.APIrest.repository;

import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.proyectoBD2.APIrest.model.OrdenServicioModel;

public interface IOrdenServicioRepository extends MongoRepository<OrdenServicioModel, ObjectId> {

    List<OrdenServicioModel> findByBicicletaIdAndEstadoIn(ObjectId bicicletaId, Collection<String> estados);

    List<OrdenServicioModel> findByBicicletaIdOrderByFechaRecepcionDesc(ObjectId bicicletaId);

    List<OrdenServicioModel> findByUsuarioIdOrderByFechaRecepcionDesc(ObjectId usuarioId);
}
