package com.proyectoBD2.APIrest.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proyectoBD2.APIrest.model.RepuestosPrevistosModel;
import com.proyectoBD2.APIrest.model.TrabajosModel;
import com.proyectoBD2.APIrest.repository.ITrabajosRepository;

@Service
public class TrabajosServiceImp implements ITrabajosService {
    @Autowired ITrabajosRepository trabajoRepository;
    @Autowired IOrdenServicioService ordenServicioService;
    @Autowired IUsuarioService usuarioService;
    @Autowired ICatalogoServiciosService catalogoServiciosService;
    @Autowired IRepuestosService repuestosService;

    @Override
    public TrabajosModel crearTrabajo(TrabajosModel trabajo) {
        ObjectId ordenServicioId = trabajo.getOrdenServicioId();
        if (ordenServicioService.buscarOrdenesServicioPorId(ordenServicioId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La orden de servicio con ID " + ordenServicioId + " no existe o es nulo.");
        }
        ObjectId tecnicoId = trabajo.getTecnicoId();
        if (usuarioService.buscarUsuariosPorId(tecnicoId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El tecnico con ID " + tecnicoId + " no existe o es nulo.");
        }
        if (trabajo.getRepuestosPrevistos() != null) {
            for (RepuestosPrevistosModel repuestoPrevisto : trabajo.getRepuestosPrevistos()) {
                ObjectId repuestoId = repuestoPrevisto.getRepuestoId();
                if (repuestosService.buscarRepuestosPorId(repuestoId) == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El repuesto con ID " + repuestoId + " no existe.");
                }
            }
        }
    
        return trabajoRepository.save(trabajo);
    }

    @Override
    public List<TrabajosModel> listarTrabajos() {
        return trabajoRepository.findAll();
    }

    @Override
    public TrabajosModel buscarTrabajosPorId(ObjectId idTrabajo) {
        if (idTrabajo == null) {
            return null;
        }
        Optional<TrabajosModel> existente = trabajoRepository.findById(idTrabajo);
        return existente.orElse(null);
    }
}
