package com.proyectoBD2.APIrest.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proyectoBD2.APIrest.model.OrdenServicioModel;
import com.proyectoBD2.APIrest.repository.IOrdenServicioRepository;

@Service
public class OrdenServiciosServiceImp implements IOrdenServicioService {
    @Autowired IOrdenServicioRepository ordenServicioRepository;
    @Autowired IBicicletasService bicicletaService;
    @Autowired IUsuarioService usuarioService;
    @Autowired ICitasService citaService;

    @Override
    public OrdenServicioModel crearOrdenServicio(OrdenServicioModel ordenServicio) {
        ObjectId bicicletaId = ordenServicio.getBicicletaId();
        if (bicicletaService.buscarBicicletasPorId(bicicletaId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La bicicleta con ID " + bicicletaId + " no existe o es nulo.");
        }
        ObjectId usuarioId = ordenServicio.getUsuarioId();
        if (usuarioService.buscarUsuariosPorId(usuarioId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario con ID " + usuarioId + " no existe o es nulo.");
        }
        /* ObjectId coordinadorResponsable = ordenServicio.getCoordinadorResponsable();
        if (usuarioService.buscarUsuariosPorId(coordinadorResponsable) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario con ID " + coordinadorResponsable + " no existe o es nulo.");
        }*/
        if (ordenServicio.getTecnicosAsignados() != null) {
            for (ObjectId idItem : ordenServicio.getTecnicosAsignados()) {
                if (usuarioService.buscarUsuariosPorId(idItem) == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario con ID " + idItem + " no existe o es nulo.");
                }
            }
        }
        /*ObjectId citaId = ordenServicio.getCitaId();
        if (citaId != null && citaService.buscarCitasPorId(citaId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La cita con ID " + citaId + " no existe o es nulo.");
        }*/
        
        return ordenServicioRepository.save(ordenServicio);
    }

    @Override
    public List<OrdenServicioModel> listarOrdenesServicio() {
        return ordenServicioRepository.findAll();
    }

    @Override
    public OrdenServicioModel buscarOrdenesServicioPorId(ObjectId idOrdenServicio) {
        if (idOrdenServicio == null) {
            return null;
        }
        Optional<OrdenServicioModel> existente = ordenServicioRepository.findById(idOrdenServicio);
        return existente.orElse(null);
    }
}
