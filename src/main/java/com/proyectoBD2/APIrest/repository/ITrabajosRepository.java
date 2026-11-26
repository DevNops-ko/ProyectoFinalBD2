package com.proyectoBD2.APIrest.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.proyectoBD2.APIrest.model.TrabajosModel;

public interface ITrabajosRepository extends MongoRepository<TrabajosModel, ObjectId> {

    List<TrabajosModel> findByOrdenServicioId(ObjectId ordenServicioId);
}
