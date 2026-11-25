package com.proyectoBD2.APIrest.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proyectoBD2.APIrest.model.SolicitudesGarantiaModel;
import com.proyectoBD2.APIrest.repository.ISolicitudesGarantiaRepository;

@Service
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

    @Override
    public SolicitudesGarantiaModel actualizarSolicitudGarantia(ObjectId idSolicitudGarantia, SolicitudesGarantiaModel solicitudGarantia) {
        if (buscarSolicitudesGarantiaPorId(idSolicitudGarantia) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un registro con ID " + idSolicitudGarantia + " en solicitudesGarantia.");
        }
        ObjectId trabajoId = solicitudGarantia.getTrabajoId();
        if (trabajoService.buscarTrabajosPorId(trabajoId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El trabajo con ID " + trabajoId + " no existe o es nulo.");
        }
        solicitudGarantia.setId(idSolicitudGarantia);
        return solicitudGarantiaRepository.save(solicitudGarantia);
    }

    @Override
    public void eliminarSolicitudGarantia(ObjectId idSolicitudGarantia) {
        if (buscarSolicitudesGarantiaPorId(idSolicitudGarantia) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un registro con ID " + idSolicitudGarantia + " en solicitudesGarantia.");
        }
        solicitudGarantiaRepository.deleteById(idSolicitudGarantia);
    }

}
