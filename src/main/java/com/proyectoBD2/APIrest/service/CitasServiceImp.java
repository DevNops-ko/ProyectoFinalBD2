package com.proyectoBD2.APIrest.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proyectoBD2.APIrest.model.CitasModel;
import com.proyectoBD2.APIrest.repository.ICitasRepository;

@Service
public class CitasServiceImp implements ICitasService {
    @Autowired ICitasRepository citaRepository;
    @Autowired IUsuarioService usuarioService;
    @Autowired IBicicletasService bicicletaService;

    @Override
    public CitasModel crearCita(CitasModel cita) {
        ObjectId usuarioId = cita.getUsuarioId();
        if (usuarioService.buscarUsuariosPorId(usuarioId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario con ID " + usuarioId + " no existe o es nulo.");
        }
        ObjectId bicicletaId = cita.getBicicletaId();
        if (bicicletaService.buscarBicicletasPorId(bicicletaId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La bicicleta con ID " + bicicletaId + " no existe o es nulo.");
        }
        return citaRepository.save(cita);
    }

    @Override
    public List<CitasModel> listarCitas() {
        return citaRepository.findAll();
    }

    @Override
    public CitasModel buscarCitasPorId(ObjectId idCita) {
        if (idCita == null) {
            return null;
        }
        Optional<CitasModel> existente = citaRepository.findById(idCita);
        return existente.orElse(null);
    }
}