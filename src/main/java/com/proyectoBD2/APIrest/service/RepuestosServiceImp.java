package com.proyectoBD2.APIrest.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyectoBD2.APIrest.model.RepuestosModel;
import com.proyectoBD2.APIrest.repository.IRepuestosRepository;

@Service
public class RepuestosServiceImp implements IRepuestosService {
    @Autowired IRepuestosRepository repuestoRepository;

    @Override
    public RepuestosModel crearRepuesto(RepuestosModel repuesto) {
        return repuestoRepository.save(repuesto);
    }

    @Override
    public List<RepuestosModel> listarRepuestos() {
        return repuestoRepository.findAll();
    }

    @Override
    public RepuestosModel buscarRepuestosPorId(ObjectId idRepuesto) {
        if (idRepuesto == null) {
            return null;
        }
        Optional<RepuestosModel> existente = repuestoRepository.findById(idRepuesto);
        return existente.orElse(null);
    }
}
