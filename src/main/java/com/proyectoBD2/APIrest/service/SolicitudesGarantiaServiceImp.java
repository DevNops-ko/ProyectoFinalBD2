package com.proyectoBD2.APIrest.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.proyectoBD2.APIrest.model.SolicitudesGarantiaModel;
import com.proyectoBD2.APIrest.repository.ISolicitudesGarantiaRepository;

public class SolicitudesGarantiaServiceImp implements ISolicitudesGarantiaService {
    @Autowired ISolicitudesGarantiaRepository solicitudGarantiaRepository;
    @Autowired ITrabajosService trabajoService;

    @Override
    public SolicitudesGarantiaModel crearSolicitudGarantia(SolicitudesGarantiaModel solicitudGarantia) {
        ObjectId trabajoId = solicitudGarantia.getTrabajoId();
        if (trabajoService.buscarTrabajosPorId(trabajoId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El trabajo con ID " + trabajoId + " no existe o es nulo.");
        }
        return solicitudGarantiaRepository.save(solicitudGarantia);
    }

    @Override
    public List<SolicitudesGarantiaModel> listarSolicitudesGarantia() {
        return solicitudGarantiaRepository.findAll();
    }

    @Override
    public SolicitudesGarantiaModel buscarSolicitudesGarantiaPorId(ObjectId idSolicitudGarantia) {
        if (idSolicitudGarantia == null) {
            return null;
        }
        Optional<SolicitudesGarantiaModel> existente = solicitudGarantiaRepository.findById(idSolicitudGarantia);
        return existente.orElse(null);
    }
}
