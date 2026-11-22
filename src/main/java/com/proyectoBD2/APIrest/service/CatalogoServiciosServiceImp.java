package com.proyectoBD2.APIrest.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyectoBD2.APIrest.model.CatalogoServiciosModel;
import com.proyectoBD2.APIrest.repository.ICatalogoServiciosRepository;

@Service
public class CatalogoServiciosServiceImp implements ICatalogoServiciosService {
    @Autowired ICatalogoServiciosRepository catalogoServiciosRepository;

    @Override
    public CatalogoServiciosModel crearServicio(CatalogoServiciosModel servicio) {
        return catalogoServiciosRepository.save(servicio);
    }

    @Override
    public List<CatalogoServiciosModel> listarServicios() {
        return catalogoServiciosRepository.findAll();
    }

    @Override
    public CatalogoServiciosModel buscarServiciosPorId(ObjectId idServicio) {
        if (idServicio == null) {
            return null;
        }
        Optional<CatalogoServiciosModel> existente = catalogoServiciosRepository.findById(idServicio);
        return existente.orElse(null);
    }
}