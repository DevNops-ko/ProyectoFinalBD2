package com.proyectoBD2.APIrest.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.proyectoBD2.APIrest.model.BicicletasModel;

public interface IBicicletasRepository extends MongoRepository<BicicletasModel, ObjectId> {
    
}
