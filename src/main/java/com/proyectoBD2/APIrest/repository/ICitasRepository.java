package com.proyectoBD2.APIrest.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.proyectoBD2.APIrest.model.CitasModel;

public interface ICitasRepository extends MongoRepository<CitasModel, ObjectId> {
    
    List<CitasModel> findByBicicletaIdAndFechaHoraAndEstadoIn(ObjectId bicicletaId, Date fechaHora, Collection<String> estados);
}
